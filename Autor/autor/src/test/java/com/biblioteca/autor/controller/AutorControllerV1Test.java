package com.biblioteca.autor.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.biblioteca.autor.dto.AutorDTO;
import com.biblioteca.autor.service.AutorService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(AutorController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AutorControllerV1Test {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AutorService autorService;

    private AutorDTO.Response responseDTO;

    @BeforeEach
    public void setUp() {
        responseDTO = new AutorDTO.Response(1L, "Isabel", "Angelica", "Allende", "Llona");
    }

    @Test
    @DisplayName("GET Listar: Retorna 200 OK con la lista de autores")
    public void listar_Retorna200YLista() throws Exception {
        when(autorService.findAll()).thenReturn(List.of(responseDTO));

        mockMvc.perform(get("/api/v1/autores"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].primerNombre").value("Isabel"));
    }

    @Test
    @DisplayName("POST Crear: Retorna 21 Created al registrar un autor válido")
    public void crear_ConDatosValidos_Retorna201() throws Exception {
        AutorDTO.Request requestDTO = new AutorDTO.Request("Isabel", "Angelica", "Allende", "Llona");
        when(autorService.save(any(AutorDTO.Request.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/api/v1/autores")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.primerNombre").value("Isabel"));
    }

    @Test
    @DisplayName("GET Buscar Por Nombre: Retorna 204 si la lista está vacía")
    public void buscarPorNombre_CuandoNoExiste_Retorna204() throws Exception {
        when(autorService.findByPrimerNombre("Inexistente")).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/autores/nombre/{nombre}", "Inexistente"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("PUT Actualizar: Retorna 200 OK con el autor modificado")
    public void actualizar_CuandoExiste_Retorna200() throws Exception {
        AutorDTO.Request requestDTO = new AutorDTO.Request("Isabel", "Modificado", "Allende", "Llona");
        responseDTO.setSegundoNombre("Modificado");
        
        when(autorService.updateAutor(eq(1L), any(AutorDTO.Request.class))).thenReturn(responseDTO);

        mockMvc.perform(put("/api/v1/autores/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.segundoNombre").value("Modificado"));
    }

    @Test
    @DisplayName("DELETE Eliminar: Retorna 204 No Content ante eliminación exitosa")
    public void eliminar_CuandoIdExiste_Retorna204() throws Exception {
        doNothing().when(autorService).delete(1L);

        mockMvc.perform(delete("/api/v1/autores/{id}", 1L))
                .andExpect(status().isNoContent());
    }
}
