package com.biblioteca.comuna.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.biblioteca.comuna.dto.ComunaDTO;
import com.biblioteca.comuna.service.ComunaService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(ComunaController.class) // Solo levanta la capa web de este controlador, indica que se está probando el controladorv1 de comuna
@AutoConfigureMockMvc(addFilters = false) // LA LLAVE QUE APAGA AL GUARDIA
public class ComunaControllerV1Test {

    @Autowired
    private MockMvc mockMvc; // Nuestro "Postman" virtual, proporciona una manera de realizar peticiones HTTP en las pruebas

    @Autowired
    private ObjectMapper objectMapper; // Traductor de Objetos Java a formato JSON y viceversa

    @MockitoBean
    private ComunaService comunaService; // Nuestro barman (Service) simulado, Crea un mock del servicio

    private ComunaDTO.Response comunaResponsePrueba;

    @BeforeEach
    void setUp() {
        // Preparamos nuestro DTO falso para todas las pruebas
        comunaResponsePrueba = new ComunaDTO.Response();
        comunaResponsePrueba.setId(1L);
        comunaResponsePrueba.setNombre("Puente Alto");
    }

    // ==========================================
    // TESTS DEL MÉTODO LISTAR (GET /api/v1/comunas)
    // ==========================================

    @Test
    @DisplayName("GET Listar (camino bueno): Retorna 200 OK y la lista de comunas")
    public void listar_CuandoHayComunas_Retorna200() throws Exception {
        // DADO: El Service devuelve una lista con 1 comuna
        when(comunaService.findAll()).thenReturn(List.of(comunaResponsePrueba));

        // CUANDO y ENTONCES: Hacemos la petición y esperamos los datos
        mockMvc.perform(get("/api/v1/comunas")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // Esperamos un 200 OK
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].nombre").value("Puente Alto"));
    }

    @Test
    @DisplayName("GET Listar (Vacio): Retorna 204 No Content si no hay datos")
    public void listar_CuandoNoHayComunas_Retorna204() throws Exception {
        // DADO: El Service devuelve una lista completamente vacía
        when(comunaService.findAll()).thenReturn(Collections.emptyList());

        // CUANDO y ENTONCES: Hacemos la petición y esperamos que la respuesta esté vacía
        mockMvc.perform(get("/api/v1/comunas")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent()); // Esperamos tu código 204 personalizado
    }

    // ==========================================
    // TESTS DEL MÉTODO CREAR (POST /api/v1/comunas)
    // ==========================================

    @Test
    @DisplayName("POST Crear (Camino Feliz): Retorna 201 Created si los datos son válidos")
    public void guardar_CuandoDatosSonValidos_Retorna201() throws Exception {
        // DADO: Creamos el cuerpo de la petición (lo que el usuario escribiría en Postman)
        ComunaDTO.Request requestBody = new ComunaDTO.Request();
        requestBody.setNombre("Puente Alto");

        // Le decimos al Mock: "Si recibes CUALQUIER objeto del tipo Request, devuelve comunaResponsePrueba"
        when(comunaService.save(any(ComunaDTO.Request.class))).thenReturn(comunaResponsePrueba);

        // CUANDO y ENTONCES: Hacemos la petición POST enviando el JSON
        mockMvc.perform(post("/api/v1/comunas")
                .contentType(MediaType.APPLICATION_JSON)
                // objectMapper traduce nuestro requestBody de Java a texto JSON: {"nombre": "Puente Alto"}
                .content(objectMapper.writeValueAsString(requestBody))) 
                .andExpect(status().isCreated()) // Esperamos el código 201 Created que pusiste en tu controller
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nombre").value("Puente Alto"));
    }

    @Test
    @DisplayName("POST Crear (Error de Validación): Retorna 400 Bad Request si el nombre tiene números")
    public void guardar_CuandoNombreEsInvalido_Retorna400() throws Exception {
        // DADO: Creamos una petición tramposa con números (violando tu @Pattern del DTO)
        ComunaDTO.Request requestBodyInvalido = new ComunaDTO.Request();
        requestBodyInvalido.setNombre("Puente Alto 123");

        // CUANDO y ENTONCES: Hacemos el POST y verificamos el bloqueo
        mockMvc.perform(post("/api/v1/comunas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBodyInvalido)))
                .andExpect(status().isBadRequest()); // Esperamos un 400 Bad Request automático de Spring
        
        // DOBLE VERIFICACIÓN: Confirmamos que el Service NUNCA fue llamado. 
        // El guardia (Controller) detuvo la petición en la puerta.
        verify(comunaService, never()).save(any(ComunaDTO.Request.class));
    }

    @Test
    @DisplayName("POST Crear (Error Servidor): Retorna 500 si el Service detecta nombre repetido")
    public void guardar_CuandoNombreYaExiste_Retorna500() throws Exception {
        ComunaDTO.Request requestBody = new ComunaDTO.Request();
        requestBody.setNombre("Santiago");

        // DADO: Le decimos al Mock que cuando intente guardar, lance una excepción (simulando nombre repetido)
        when(comunaService.save(any(ComunaDTO.Request.class)))
                .thenThrow(new RuntimeException("Violación de unicidad: El nombre ya existe"));

        // CUANDO y ENTONCES: Verificamos que el controlador escupa un error 500 Internal Server Error
        mockMvc.perform(post("/api/v1/comunas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isInternalServerError()); // Código 500
    }

    // ==========================================
    // TESTS DEL MÉTODO BUSCAR POR ID (GET /api/v1/comunas/id/{id})
    // ==========================================

    @Test
    @DisplayName("GET Buscar por ID (Camino Feliz): Retorna 200 OK si existe")
    public void buscarPorId_CuandoIdExiste_Retorna200() throws Exception {
        // DADO: El Service encuentra y devuelve nuestra comuna de prueba (DTO)
        when(comunaService.findByIdOrThrow(1L)).thenReturn(comunaResponsePrueba);

        // CUANDO y ENTONCES: Hacemos la petición a la URL y validamos el JSON
        mockMvc.perform(get("/api/v1/comunas/id/{id}", 1L) // Pasamos el 1L directo a la URL
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // Esperamos 200 OK
                .andExpect(jsonPath("$.id").value(1L)) // Aquí usamos "$" sin [0] porque NO es una lista, es 1 solo objeto
                .andExpect(jsonPath("$.nombre").value("Puente Alto"));
    }

    @Test
    @DisplayName("GET Buscar por ID (Error): Retorna 404 Not Found si no existe")
    public void buscarPorId_CuandoIdNoExiste_Retorna404() throws Exception {
        // DADO: Simulamos que el Service lanza tu excepción personalizada cuando le piden el ID 99
        when(comunaService.findByIdOrThrow(99L))
                .thenThrow(new com.biblioteca.comuna.exception.ResourceNotFoundException("Comuna no encontrada"));

        // CUANDO y ENTONCES: Verificamos que el controlador/excepcion global responda con 404
        mockMvc.perform(get("/api/v1/comunas/id/{id}", 99L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()); // Esperamos tu código 404
    }

    // ==========================================
    // TESTS DEL MÉTODO BUSCAR POR NOMBRE (GET /api/v1/comunas/nombre/{nombre})
    // ==========================================

    @Test
    @DisplayName("🔎 GET Buscar por Nombre (Camino Feliz): Retorna 200 OK si existe")
    public void buscarPorNombre_CuandoNombreExiste_Retorna200() throws Exception {
        // DADO: El Service encuentra la comuna y la devuelve dentro de un Optional
        // OJO: Cambia 'tuVariableAqui' por el nombre real de tu DTO (ej. comunaResponsePrueba)
        when(comunaService.findByNombre("Puente Alto")).thenReturn(Optional.of(comunaResponsePrueba));

        // CUANDO y ENTONCES: Hacemos la petición pasando el texto en la URL
        mockMvc.perform(get("/api/v1/comunas/nombre/{nombre}", "Puente Alto")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // Esperamos 200 OK
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nombre").value("Puente Alto"));
    }

    @Test
    @DisplayName("GET Buscar por Nombre (Error): Retorna 404 Not Found si no existe")
    public void buscarPorNombre_CuandoNombreNoExiste_Retorna404() throws Exception {
        // DADO: El Service busca pero no encuentra nada, devolviendo una "caja vacía" (Optional.empty)
        when(comunaService.findByNombre("Narnia")).thenReturn(Optional.empty());

        // CUANDO y ENTONCES: Verificamos que tu controlador lea el vacío y devuelva 404
        mockMvc.perform(get("/api/v1/comunas/nombre/{nombre}", "Narnia")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()); // Esperamos tu código 404 del ".orElse()"
    }

    // ==========================================
    // TESTS DEL MÉTODO ACTUALIZAR (PUT /api/v1/comunas/{id})
    // ==========================================

    @Test
    @DisplayName("✏️ PUT Actualizar (Camino Feliz): Retorna 200 OK al actualizar")
    public void actualizar_CuandoDatosSonValidos_Retorna200() throws Exception {
        ComunaDTO.Request requestBody = new ComunaDTO.Request();
        requestBody.setNombre("Santiago Nuevo");

        // Cambiamos temporalmente el nombre de nuestro mock para que coincida con la actualización
        comunaResponsePrueba.setNombre("Santiago Nuevo");

        // DADO: El Service actualiza y devuelve el DTO modificado
        when(comunaService.update(eq(1L), any(ComunaDTO.Request.class))).thenReturn(comunaResponsePrueba);

        // CUANDO y ENTONCES: Verificamos que responda 200 y el nombre nuevo
        mockMvc.perform(put("/api/v1/comunas/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nombre").value("Santiago Nuevo"));
    }

    @Test
    @DisplayName("❌ PUT Actualizar (Error Validación): Retorna 400 si el nuevo nombre es inválido")
    public void actualizar_CuandoNombreEsInvalido_Retorna400() throws Exception {
        ComunaDTO.Request requestBodyInvalido = new ComunaDTO.Request();
        requestBodyInvalido.setNombre("Santi@go!"); // Falla el @Pattern

        // CUANDO y ENTONCES: Verificamos el rechazo en la puerta
        mockMvc.perform(put("/api/v1/comunas/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBodyInvalido)))
                .andExpect(status().isBadRequest()); // Código 400
    }

    @Test
    @DisplayName("PUT Actualizar (Error Servidor): Retorna 500 si el nuevo nombre ya existe")
    public void actualizar_CuandoNombreYaExiste_Retorna500() throws Exception {
        ComunaDTO.Request requestBody = new ComunaDTO.Request();
        requestBody.setNombre("Santiago"); // Un nombre que "ya le pertenece a otra comuna"

        // DADO: Simulamos que el Service explota al intentar guardar este nombre repetido
        when(comunaService.update(eq(1L), any(ComunaDTO.Request.class)))
                .thenThrow(new RuntimeException("Violación de unicidad"));

        // CUANDO y ENTONCES: Verificamos la traducción a 500
        mockMvc.perform(put("/api/v1/comunas/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isInternalServerError());
    }

    // ==========================================
    // TESTS DEL MÉTODO ELIMINAR (DELETE /api/v1/comunas/{id})
    // ==========================================

    @Test
    @DisplayName("DELETE Eliminar (Camino Feliz): Retorna 204 No Content si se elimina")
    public void eliminar_CuandoIdExiste_Retorna204() throws Exception {
        // DADO: El service simplemente ejecuta su tarea (no devuelve nada porque es void)
        doNothing().when(comunaService).delete(1L);

        // CUANDO y ENTONCES: Hacemos el DELETE y esperamos el 204
        mockMvc.perform(delete("/api/v1/comunas/{id}", 1L))
                .andExpect(status().isNoContent());

        // Verificamos que el Service sí fue llamado exactamente 1 vez para ese ID
        verify(comunaService, times(1)).delete(1L);
    }

    @Test
    @DisplayName("DELETE Eliminar (Error): Retorna 404 si el ID no existe")
    public void eliminar_CuandoIdNoExiste_Retorna404() throws Exception {
        // DADO: Simulamos que el Service lanza tu excepción personalizada al intentar borrar el ID 99
        doThrow(new com.biblioteca.comuna.exception.ResourceNotFoundException("Comuna no encontrada"))
                .when(comunaService).delete(99L);

        // CUANDO y ENTONCES: Hacemos el DELETE y esperamos el 404 Not Found
        mockMvc.perform(delete("/api/v1/comunas/{id}", 99L))
                .andExpect(status().isNotFound());
    }

}
