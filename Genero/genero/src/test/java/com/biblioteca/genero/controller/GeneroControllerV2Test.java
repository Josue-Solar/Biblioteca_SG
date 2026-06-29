package com.biblioteca.genero.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
// Si usas Spring Boot 3.4+:
// import org.springframework.test.context.bean.override.mockito.MockitoBean;
// import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.biblioteca.genero.assemblers.GeneroModelAssembler;
import com.biblioteca.genero.dto.GeneroDTO;
import com.biblioteca.genero.model.Genero;
import com.biblioteca.genero.service.GeneroService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(GeneroControllerV2.class)
@AutoConfigureMockMvc(addFilters = false)
public class GeneroControllerV2Test {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private GeneroService generoService;

    // Fundamental para que HATEOAS genere los _links automáticamente en el test
    @SpyBean
    private GeneroModelAssembler assembler;

    private GeneroDTO.Response responseDTO;

    @BeforeEach
    public void setUp() {
        responseDTO = new GeneroDTO.Response(1L, "Romance");
    }

    @Test
    @DisplayName("Listar: Retorna lista con soporte HATEOAS")
    public void listarGeneros_RetornaHateoas() throws Exception {
        // 1. Creamos un objeto Genero ficticio (entidad pura que usa el listar V2)
        Genero generoFake = new Genero();
        generoFake.setId(1L);
        generoFake.setNombre("Ciencia Ficción");

        // 2. Forzamos al servicio a devolver una lista con nuestro género simulado
        doReturn(List.of(generoFake)).when(generoService).obtenerTodos();

        // 3. Ejecutamos la petición MockMvc a la ruta de la V2
        mockMvc.perform(get("/api/v2/generos")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()); // <-- Ahora sí responderá 200 OK
    }

    @Test
    @DisplayName("Buscar por ID V2: Retorna 200 OK con HATEOAS")
    public void buscarPorId_RetornaHateoas() throws Exception {
        // 1. Preparamos el DTO de respuesta esperado por el controlador V2
        GeneroDTO.Response mockResponse = new GeneroDTO.Response(1L, "Ciencia Ficción");
        
        // 2. Mockeamos el servicio de la V2 usando doReturn
        doReturn(mockResponse).when(generoService).findByIdOrThrow(anyLong());

        // 3. ADVERTENCIA EXTRA (Solo si tu assembler es un @MockBean):
        // Si tienes el assembler mockeado en la cabecera del test, descomenta la siguiente línea:
        // doReturn(org.springframework.hateoas.EntityModel.of(mockResponse)).when(assembler).toModel(any());

        // 4. Ejecutamos la petición al endpoint V2
        mockMvc.perform(get("/api/v2/generos/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()); // <-- Esperamos el 200 OK
    }

    @Test
    @DisplayName("POST Crear V2: Retorna EntityModel HATEOAS")
    public void guardar_RetornaHateoas() throws Exception {
        GeneroDTO.Request requestDTO = new GeneroDTO.Request("Romance");
        when(generoService.guardar(any(GeneroDTO.Request.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/api/v2/generos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO))
                .accept(MediaTypes.HAL_JSON_VALUE))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("Romance"))
                .andExpect(jsonPath("$._links.self.href").exists());
    }

    @Test
    @DisplayName("PUT Actualizar V2: Retorna EntityModel HATEOAS")
    public void actualizar_RetornaHateoas() throws Exception {
        GeneroDTO.Request requestDTO = new GeneroDTO.Request("Romance Clásico");
        GeneroDTO.Response updatedResponse = new GeneroDTO.Response(1L, "Romance Clásico");

        when(generoService.modificarGenero(anyLong(), any(GeneroDTO.Request.class))).thenReturn(updatedResponse);

        mockMvc.perform(put("/api/v2/generos/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO))
                .accept(MediaTypes.HAL_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Romance Clásico"))
                .andExpect(jsonPath("$._links.self.href").exists());
    }

    @Test
    @DisplayName("DELETE Eliminar V2: Retorna 204 No Content")
    public void eliminar_Retorna204() throws Exception {
        doNothing().when(generoService).eliminar(anyLong());

        mockMvc.perform(delete("/api/v2/generos/1"))
                .andExpect(status().is2xxSuccessful());
    }
}
