package com.biblioteca.libro.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.biblioteca.libro.dto.LibroDTO;
import com.biblioteca.libro.service.LibroService;
import com.fasterxml.jackson.databind.ObjectMapper;

// 🌟 ESCUDO ACTIVO: Ignora la configuración global de BD
@WebMvcTest(controllers = LibroController.class, value = LibroController.class)
public class LibroControllerV1Test {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private LibroService libroService;

    @Test
    @DisplayName("Listar V1: Retorna 200 OK con lista de libros")
    public void listarLibros_Retorna200() throws Exception {
        LibroDTO.Response mockResponse = new LibroDTO.Response(123456789L, "Cien años de soledad");
        doReturn(List.of(mockResponse)).when(libroService).obtenerTodos();

        mockMvc.perform(get("/api/v1/libros")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Cien años de soledad"));
    }

    @Test
    @DisplayName("Listar V1: Retorna 204 No Content si está vacía")
    public void listarLibros_Retorna204() throws Exception {
        doReturn(List.of()).when(libroService).obtenerTodos();

        mockMvc.perform(get("/api/v1/libros")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Buscar por ISBN V1: Retorna 200 OK")
    public void buscarPorIsbn_Retorna200() throws Exception {
        LibroDTO.Response mockResponse = new LibroDTO.Response(123456789L, "Cien años de soledad");
        doReturn(mockResponse).when(libroService).obtenerPorIsbn(123456789L);

        mockMvc.perform(get("/api/v1/libros/123456789")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Cien años de soledad"));
    }

    @Test
    @DisplayName("Crear V1: Retorna 201 Created")
    public void crearLibro_Retorna201() throws Exception {
        LibroDTO.Request request = new LibroDTO.Request(123456789L, "Cien años de soledad");
        LibroDTO.Response mockResponse = new LibroDTO.Response(123456789L, "Cien años de soledad");
        
        doReturn(mockResponse).when(libroService).guardar(any(LibroDTO.Request.class));

        mockMvc.perform(post("/api/v1/libros")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Actualizar V1: Modifica exitosamente y retorna 200 OK")
    public void actualizarLibro_Retorna200() throws Exception {
        LibroDTO.Request request = new LibroDTO.Request(123456789L, "Cien años de soledad");
        LibroDTO.Response mockResponse = new LibroDTO.Response(123456789L, "Cien años de soledad");
        
        doReturn(mockResponse).when(libroService).actualizar(anyLong(), any(LibroDTO.Request.class));

        mockMvc.perform(put("/api/v1/libros/123456789")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Eliminar V1: Retorna 204 No Content")
    public void eliminarLibro_Retorna204() throws Exception {
        doReturn(Optional.of(true)).when(libroService).eliminar(123456789L);

        mockMvc.perform(delete("/api/v1/libros/123456789"))
                .andExpect(status().isNoContent());
    }
}