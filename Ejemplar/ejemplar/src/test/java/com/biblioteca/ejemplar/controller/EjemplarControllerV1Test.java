package com.biblioteca.ejemplar.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
// Si usas Spring Boot 3.4+, recuerda usar:
// import org.springframework.test.context.bean.override.mockito.MockitoBean;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.biblioteca.ejemplar.model.Ejemplar;
import com.biblioteca.ejemplar.service.EjemplarService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(EjemplarController.class)
@AutoConfigureMockMvc(addFilters = false)
public class EjemplarControllerV1Test {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private EjemplarService ejemplarService;

    private Ejemplar ejemplarBase;

    @BeforeEach
    public void setUp() {
        ejemplarBase = new Ejemplar();
        ejemplarBase.setId(1L);
        ejemplarBase.setLibroIsbn(9781234567890L);
    }

    @Test
    @DisplayName("GET Listar: Retorna 200 OK")
    public void getAllEjemplares_Retorna200() throws Exception {
        when(ejemplarService.obtenerTodos()).thenReturn(List.of(ejemplarBase));

        mockMvc.perform(get("/api/v1/ejemplares"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET Buscar Por ID: Retorna 200 OK")
    public void getByID_CuandoExiste_Retorna200() throws Exception {
        // 1. Obligamos a Mockito a coincidir exactamente con el valor 1L
        when(ejemplarService.obtenerPorId(eq(1L))).thenReturn(Optional.of(ejemplarBase));

        // 2. Le pasamos el 1L explícitamente a la variable de la ruta de Spring
        mockMvc.perform(get("/api/v1/ejemplares/id/{id}", 1L))
                .andDo(print()) // <-- ESTO IMPRIMIRÁ EL REPORTE COMPLETO EN CONSOLA
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.libroIsbn").value(9781234567890L));
    }

    @Test
    @DisplayName("POST Crear: Retorna 201 Created")
    public void saveEntity_ConDatosValidos_Retorna201() throws Exception {
        when(ejemplarService.guardar(any(Ejemplar.class))).thenReturn(ejemplarBase);

        mockMvc.perform(post("/api/v1/ejemplares")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(ejemplarBase)))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @DisplayName("PUT Actualizar: Retorna código de éxito")
    public void updateEjemplar_RetornaOk() throws Exception {
        when(ejemplarService.modReserva(eq(1L), any(Ejemplar.class)))
                .thenReturn(Optional.of(ejemplarBase));

        mockMvc.perform(put("/api/v1/ejemplares/actualizar/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(ejemplarBase)))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @DisplayName("DELETE Eliminar: Retorna código de éxito")
    public void deleteEjemplar_RetornaExito() throws Exception {
        doNothing().when(ejemplarService).eliminar(eq(1L));

        mockMvc.perform(delete("/api/v1/ejemplares/{id}", 1L))
                .andExpect(status().is2xxSuccessful()); 
    }
}