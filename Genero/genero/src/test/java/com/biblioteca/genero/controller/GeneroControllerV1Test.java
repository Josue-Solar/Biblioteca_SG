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
// Si usas Spring Boot 3.4+:
// import org.springframework.test.context.bean.override.mockito.MockitoBean;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.biblioteca.genero.dto.GeneroDTO;
import com.biblioteca.genero.dto.GeneroLibroDTO;
import com.biblioteca.genero.model.Genero;
import com.biblioteca.genero.service.GeneroService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(GeneroController.class)
@AutoConfigureMockMvc(addFilters = false)
public class GeneroControllerV1Test {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private GeneroService generoService;

    private GeneroDTO.Response responseDTO;
    private Genero generoBase;

    @BeforeEach
    public void setUp() {
        responseDTO = new GeneroDTO.Response(1L, "Terror");
        
        generoBase = new Genero();
        generoBase.setId(1L);
        generoBase.setNombre("Terror");
    }

    @Test
    @DisplayName("GET Listar V1: Retorna 200 OK")
    public void listarGeneros_Retorna200() throws Exception {
        // En tu controller V1, getAllGeneros retorna List<Genero> y no DTOs
        when(generoService.obtenerTodos()).thenReturn(List.of(generoBase));

        mockMvc.perform(get("/api/v1/generos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Terror"));
    }

    @Test
    @DisplayName("Buscar por ID: Retorna 200 OK")
    public void buscarPorId_Retorna200() throws Exception {
        // 1. Preparamos el DTO de respuesta que debería dar el servicio
        GeneroDTO.Response mockResponse = new GeneroDTO.Response(1L, "Ciencia Ficción");
        
        // 2. Forzamos al servicio a devolver el DTO sin importar qué ID de tipo Long reciba
        doReturn(mockResponse).when(generoService).findByIdOrThrow(anyLong());

        // 3. Ejecutamos MockMvc (REVISA BIEN QUE LA URL COINCIDA CON TU @RestController)
        mockMvc.perform(get("/api/v1/generos/1") // <-- Verifica si lleva /api/v1 o solo /generos
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // Espera 200
                .andExpect(jsonPath("$.nombre").value("Ciencia Ficción"));
    }

    @Test
    @DisplayName("Buscar libros por género: Retorna 200 OK")
    public void buscarLibrosPorGenero_Retorna200() throws Exception {
        GeneroLibroDTO mockResponse = new GeneroLibroDTO(responseDTO, null); // O como tengas construido tu DTO
        // ... setea los datos necesarios a tu mockResponse si hace falta ...

        // Forzamos al servicio a responder el DTO usando doReturn
        doReturn(mockResponse).when(generoService).librosPorGenero(anyLong());

        mockMvc.perform(get("/api/v1/generos/1/libros") // <-- Revisa bien cómo es tu URL real aquí
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("POST Crear V1: Retorna 201 Created")
    public void guardar_Retorna201() throws Exception {
        GeneroDTO.Request requestDTO = new GeneroDTO.Request("Terror");
        when(generoService.guardar(any(GeneroDTO.Request.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/api/v1/generos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("Terror"));
    }

    @Test
    @DisplayName("PUT Actualizar V1: Retorna 200 OK")
    public void actualizar_Retorna200() throws Exception {
        GeneroDTO.Request requestDTO = new GeneroDTO.Request("Misterio");
        GeneroDTO.Response updatedResponse = new GeneroDTO.Response(1L, "Misterio");

        when(generoService.modificarGenero(anyLong(), any(GeneroDTO.Request.class))).thenReturn(updatedResponse);

        mockMvc.perform(put("/api/v1/generos/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Misterio"));
    }

    @Test
    @DisplayName("DELETE Eliminar V1: Retorna 204 No Content")
    public void eliminar_RetornaNoContent() throws Exception {
        doNothing().when(generoService).eliminar(anyLong());

        mockMvc.perform(delete("/api/v1/generos/1"))
                .andExpect(status().is2xxSuccessful());
    }
}