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
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.biblioteca.libro.assemblers.LibroModelAssembler;
import com.biblioteca.libro.dto.LibroDTO;
import com.biblioteca.libro.service.LibroService;
import com.fasterxml.jackson.databind.ObjectMapper;

// 🌟 ESCUDO ACTIVO: Ignora la configuración global de BD
@WebMvcTest(controllers = LibroControllerV2.class, excludeAutoConfiguration = LibroControllerV2.class)
public class LibroControllerV2Test {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private LibroService libroService;

    @MockBean
    private LibroModelAssembler assembler;

    @Test
    @DisplayName("Listar V2 HATEOAS: Retorna 200 OK con formato HAL+JSON")
    public void listarLibrosV2_RetornaHateoas() throws Exception {
        LibroDTO.Response mockResponse = new LibroDTO.Response(123456789L, "Cien años de soledad");
        
        doReturn(List.of(mockResponse)).when(libroService).obtenerTodos();
        doReturn(EntityModel.of(mockResponse)).when(assembler).toModel(any());

        mockMvc.perform(get("/api/v2/libros")
                .accept(MediaTypes.HAL_JSON_VALUE))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Buscar por ISBN V2: Retorna 200 OK hipermedia")
    public void buscarPorIsbnV2_RetornaHateoas() throws Exception {
        LibroDTO.Response mockResponse = new LibroDTO.Response(123456789L, "Cien años de soledad");
        
        doReturn(mockResponse).when(libroService).obtenerPorIsbn(123456789L);
        doReturn(EntityModel.of(mockResponse)).when(assembler).toModel(any());

        mockMvc.perform(get("/api/v2/libros/123456789")
                .accept(MediaTypes.HAL_JSON_VALUE))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Registrar V2: Crea el libro y responde 201 Created")
    public void addLibroV2_Retorna201() throws Exception {
        LibroDTO.Request request = new LibroDTO.Request(123456789L, "Cien años de soledad");
        LibroDTO.Response mockResponse = new LibroDTO.Response(123456789L, "Cien años de soledad");
        
        doReturn(mockResponse).when(libroService).guardar(any(LibroDTO.Request.class));
        doReturn(EntityModel.of(mockResponse)).when(assembler).toModel(any());

        mockMvc.perform(post("/api/v2/libros")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON_VALUE)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Actualizar V2: Modifica y responde 200 OK HATEOAS")
    public void putLibroV2_Retorna200() throws Exception {
        LibroDTO.Request request = new LibroDTO.Request(123456789L, "Cien años de soledad");
        LibroDTO.Response mockResponse = new LibroDTO.Response(123456789L, "Cien años de soledad");
        
        doReturn(mockResponse).when(libroService).actualizar(anyLong(), any(LibroDTO.Request.class));
        doReturn(EntityModel.of(mockResponse)).when(assembler).toModel(any());

        mockMvc.perform(put("/api/v2/libros/123456789")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON_VALUE)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Eliminar V2: Borra el recurso y responde 204 No Content")
    public void deleteLibroV2_Retorna204() throws Exception {
        doReturn(Optional.of(true)).when(libroService).eliminar(123456789L);

        mockMvc.perform(delete("/api/v2/libros/123456789"))
                .andExpect(status().isNoContent());
    }
}