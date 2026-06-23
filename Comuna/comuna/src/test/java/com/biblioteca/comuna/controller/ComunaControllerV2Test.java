package com.biblioteca.comuna.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.biblioteca.comuna.assemblers.ComunaModelAssembler;
import com.biblioteca.comuna.dto.ComunaDTO;
import com.biblioteca.comuna.service.ComunaService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(ComunaControllerV2.class)
@AutoConfigureMockMvc(addFilters = false) // Apagamos el guardia de seguridad
public class ComunaControllerV2Test {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ComunaService comunaService;

    //IMPORTANTE: Agregamos el Assembler al Mock porque la V2 lo necesita

    @SpyBean 
    private ComunaModelAssembler assembler;

    @Autowired
    private ObjectMapper objectMapper;

    private ComunaDTO.Response comunaResponsePrueba;

    @BeforeEach
    void setUp() {
        comunaResponsePrueba = new ComunaDTO.Response();
        comunaResponsePrueba.setId(1L);
        comunaResponsePrueba.setNombre("Puente Alto");
    }

    // ==========================================
    // TESTS DEL MÉTODO LISTAR (GET /api/v2/comunas)
    // ==========================================

    @Test
    @DisplayName("GET Listar (Camino Feliz): Retorna 200 OK y la lista envuelta en HATEOAS")
    public void listarTodos_CuandoHayComunas_Retorna200ConLinks() throws Exception {
        when(comunaService.findAll()).thenReturn(List.of(comunaResponsePrueba));

        mockMvc.perform(get("/api/v2/comunas")
                .accept(MediaTypes.HAL_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.responseList[0].id").value(1L))
                .andExpect(jsonPath("$._embedded.responseList[0].nombre").value("Puente Alto"))
                .andExpect(jsonPath("$._embedded.responseList[0]._links.self.href").exists())
                .andExpect(jsonPath("$._links.self.href").exists());
    }

    @Test
    @DisplayName("GET Listar (Vacío): Retorna 200 OK con links, pero sin bloque _embedded")
    public void listarTodos_CuandoNoHayComunas_Retorna200Vacio() throws Exception {
        when(comunaService.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v2/comunas")
                .accept(MediaTypes.HAL_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded").doesNotExist())
                .andExpect(jsonPath("$._links.self.href").exists());
    }

    // ==========================================
    // TESTS DEL MÉTODO GUARDAR (POST /api/v2/comunas)
    // ==========================================

    @Test
    @DisplayName("POST Crear (camino bueno): Retorna 201 Created, Header Location y HATEOAS")
    public void guardar_CuandoDatosSonValidos_Retorna201() throws Exception {
        // DADO: Preparamos el DTO de envío (Request)
        ComunaDTO.Request requestValido = new ComunaDTO.Request();
        requestValido.setNombre("La Florida");

        // Preparamos lo que nos va a devolver el Service (Response)
        ComunaDTO.Response responseSimulada = new ComunaDTO.Response();
        responseSimulada.setId(2L);
        responseSimulada.setNombre("La Florida");

        // Le decimos a Mockito que cuando el Service reciba CUALQUIER Request, devuelva nuestra Respuesta
        when(comunaService.save(any(ComunaDTO.Request.class))).thenReturn(responseSimulada);

        // CUANDO y ENTONCES
        mockMvc.perform(post("/api/v2/comunas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestValido)) // 👈 Aquí entra en acción tu ObjectMapper
                .accept(MediaTypes.HAL_JSON_VALUE))
                .andExpect(status().isCreated()) // 201 Created
                // Tu controller tiene .created(linkTo(...).toUri()), eso genera un header "Location" en la respuesta HTTP
                .andExpect(header().exists("Location")) 
                // Validamos los datos y los links del body
                .andExpect(jsonPath("$.id").value(2L))
                .andExpect(jsonPath("$.nombre").value("La Florida"))
                .andExpect(jsonPath("$._links.self.href").exists());
    }

    @Test
    @DisplayName("POST Crear (Error Validación): Retorna 400 si falla el @Valid")
    public void guardar_CuandoNombreEsInvalido_Retorna400() throws Exception {
        // DADO: Un Request con datos inválidos (por ejemplo, nombre vacío o con números, según tu DTO)
        ComunaDTO.Request requestInvalido = new ComunaDTO.Request();
        requestInvalido.setNombre(""); // O "123" si esa era tu regla de validación

        // CUANDO y ENTONCES: No necesitamos mockear el Service, porque el @Valid bloquea la petición antes de llegar a él
        mockMvc.perform(post("/api/v2/comunas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestInvalido)))
                .andExpect(status().isBadRequest()); // 400 Bad Request
    }

    @Test
    @DisplayName("POST Crear (Error Servidor): Retorna 500 si el nombre ya existe")
    public void guardar_CuandoNombreYaExiste_Retorna500() throws Exception {
        // DADO: Un Request válido en formato, pero que choca con la base de datos
        ComunaDTO.Request requestRepetido = new ComunaDTO.Request();
        requestRepetido.setNombre("Puente Alto");

        // Simulamos que el Service se da cuenta de que ya existe y lanza una excepción
        when(comunaService.save(any(ComunaDTO.Request.class)))
                .thenThrow(new RuntimeException("El nombre de la comuna ya existe"));

        // CUANDO y ENTONCES
        mockMvc.perform(post("/api/v2/comunas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestRepetido)))
                .andExpect(status().isInternalServerError()); // 500 Internal Server Error
    }

    // ==========================================
    // TESTS DEL MÉTODO BUSCAR POR ID (GET /api/v2/comunas/{id})
    // ==========================================

    @Test
    @DisplayName("GET ID (Camino Feliz): Retorna 200 OK y la comuna con links HATEOAS")
    public void buscarPorId_CuandoExiste_Retorna200() throws Exception {
        // DADO: El service encuentra la comuna y nos devuelve nuestro objeto de prueba
        when(comunaService.findByIdOrThrow(1L)).thenReturn(comunaResponsePrueba);

        // CUANDO y ENTONCES
        mockMvc.perform(get("/api/v2/comunas/{id}", 1L)
                .accept(MediaTypes.HAL_JSON_VALUE))
                .andExpect(status().isOk()) // 200 OK
                // Verificamos los datos de la comuna
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nombre").value("Puente Alto"))
                // Verificamos que el ensamblador le inyectó los links HATEOAS
                .andExpect(jsonPath("$._links.self.href").exists())
                .andExpect(jsonPath("$._links.comunas.href").exists()); // Asumiendo que tu ensamblador crea un link global a "comunas"
    }

    @Test
    @DisplayName("GET ID (Error): Retorna 404 si la comuna no existe")
    public void buscarPorId_CuandoNoExiste_Retorna404() throws Exception {
        // DADO: El service no encuentra la comuna y lanza tu excepción personalizada
        when(comunaService.findByIdOrThrow(99L))
                .thenThrow(new com.biblioteca.comuna.exception.ResourceNotFoundException("Comuna no encontrada"));

        // CUANDO y ENTONCES
        mockMvc.perform(get("/api/v2/comunas/{id}", 99L)
                .accept(MediaTypes.HAL_JSON_VALUE))
                .andExpect(status().isNotFound()); // 404 Not Found
    }

    // ==========================================
    // TESTS DEL MÉTODO ACTUALIZAR (PUT /api/v2/comunas/{id})
    // ==========================================

    @Test
    @DisplayName("PUT Actualizar (camino bueno): Retorna 200 OK y la comuna actualizada con links")
    public void actualizar_CuandoDatosSonValidos_Retorna200() throws Exception {
        // DADO: El Request con el nuevo nombre
        ComunaDTO.Request requestValido = new ComunaDTO.Request();
        requestValido.setNombre("Maipu");
        // Reutilizamos el objeto del setUp y lo actualizamos
        comunaResponsePrueba.setNombre("Maipu");
        // usamos eq(1L) para asegurar que el Service reciba la ID correcta
        when(comunaService.update(eq(1L), any(ComunaDTO.Request.class))).thenReturn(comunaResponsePrueba);

        // CUANDO y ENTONCES
        mockMvc.perform(put("/api/v2/comunas/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestValido))
                .accept(MediaTypes.HAL_JSON_VALUE))
                .andExpect(status().isOk()) // 200 OK
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nombre").value("Maipu"))
                .andExpect(jsonPath("$._links.self.href").exists()); // Validamos HATEOAS
    }

    @Test
    @DisplayName(" PUT Actualizar (Error Validación): Retorna 400 si el nombre tiene números o símbolos")
    public void actualizar_CuandoNombreEsInvalido_Retorna400() throws Exception {
        // DADO: Un nombre que no cumple la regla de "solo letras y espacios" (según tu validación)
        ComunaDTO.Request requestInvalido = new ComunaDTO.Request();
        requestInvalido.setNombre("M@ipu 123");

        // CUANDO y ENTONCES: El @Valid frena esto, no llega al Service
        mockMvc.perform(put("/api/v2/comunas/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestInvalido)))
                .andExpect(status().isBadRequest()); // 400 Bad Request
    }

    @Test
    @DisplayName("PUT Actualizar (No Encontrado): Retorna 404 si la ID no existe")
    public void actualizar_CuandoIdNoExiste_Retorna404() throws Exception {
        // DADO: Un request válido, pero apuntando a una ID fantasma
        ComunaDTO.Request requestValido = new ComunaDTO.Request();
        requestValido.setNombre("Maipu");

        when(comunaService.update(eq(99L), any(ComunaDTO.Request.class)))
                .thenThrow(new com.biblioteca.comuna.exception.ResourceNotFoundException("Comuna no encontrada"));

        // CUANDO y ENTONCES
        mockMvc.perform(put("/api/v2/comunas/{id}", 99L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestValido)))
                .andExpect(status().isNotFound()); // 404 Not Found
    }

    @Test
    @DisplayName("PUT Actualizar (Conflicto): Retorna 500 si el nuevo nombre ya está ocupado")
    public void actualizar_CuandoNombreYaExiste_Retorna500() throws Exception {
        // DADO: Queremos cambiar el nombre a uno que ya tiene otra comuna
        ComunaDTO.Request requestRepetido = new ComunaDTO.Request();
        requestRepetido.setNombre("Puente Alto");

        when(comunaService.update(eq(1L), any(ComunaDTO.Request.class)))
                .thenThrow(new RuntimeException("El nombre ya está en uso"));

        // CUANDO y ENTONCES
        mockMvc.perform(put("/api/v2/comunas/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestRepetido)))
                .andExpect(status().isInternalServerError()); // 500 Internal Server Error
    }

    // ==========================================
    // TESTS DEL MÉTODO ELIMINAR (DELETE /api/v2/comunas/{id})
    // ==========================================

    @Test
    @DisplayName("DELETE Eliminar (camino bueno): Retorna 204 No Content al borrar con éxito")
    public void eliminar_CuandoIdExiste_Retorna204() throws Exception {
        // DADO: No necesitamos decirle a Mockito que devuelva nada con 'when' 
        // porque el método comunaService.delete(id) es un método 'void'.
        // Por defecto, Mockito no hace nada cuando se llama a un método void, lo cual simula un borrado exitoso.

        // CUANDO y ENTONCES
        mockMvc.perform(delete("/api/v2/comunas/{id}", 1L))
                .andExpect(status().isNoContent()); // 204 No Content
    }

    @Test
    @DisplayName("DELETE Eliminar (No Encontrado): Retorna 404 si la ID no existe")
    public void eliminar_CuandoIdNoExiste_Retorna404() throws Exception {
        // DADO: Aquí sí necesitamos forzar al método 'void' a lanzar una excepción si le pasan un ID falso (99L)
        // Usamos doThrow en lugar de 'when' porque el método del service es de tipo void
        org.mockito.Mockito.doThrow(new com.biblioteca.comuna.exception.ResourceNotFoundException("Comuna no encontrada"))
                .when(comunaService).delete(99L);

        // CUANDO y ENTONCES
        mockMvc.perform(delete("/api/v2/comunas/{id}", 99L))
                .andExpect(status().isNotFound()); // 404 Not Found
    }

}
