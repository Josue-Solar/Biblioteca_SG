package com.biblioteca.autor.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
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
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.biblioteca.autor.assemblers.AutorModelAssembler;
import com.biblioteca.autor.dto.AutorDTO;
import com.biblioteca.autor.service.AutorService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(AutorControllerV2.class)
@AutoConfigureMockMvc(addFilters = false)
public class AutorControllerV2Test {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AutorService autorService;

    @SpyBean
    private AutorModelAssembler assembler;

    private AutorDTO.Response responseDTO;

    @BeforeEach
    public void setUp() {
        responseDTO = new AutorDTO.Response(1L, "Julio", "Florencio", "Cortázar", "Descotte");
    }

    @Test
    @DisplayName("GET Buscar por Nombre V2: Retorna Colección HAL JSON con hipervínculos")
    public void buscarPorNombre_RetornaColeccionHateoas() throws Exception {
        when(autorService.findByPrimerNombre("Julio")).thenReturn(List.of(responseDTO));

        mockMvc.perform(get("/api/v2/autores/nombre/{nombre}", "Julio")
                .accept(MediaTypes.HAL_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", MediaTypes.HAL_JSON_VALUE))
                .andExpect(jsonPath("$._embedded.responseList[0].id").value(1L))
                .andExpect(jsonPath("$._embedded.responseList[0].primerNombre").value("Julio"))
                .andExpect(jsonPath("$._links.self.href").exists());
    }

    @Test
    @DisplayName("GET Buscar por Apellido V2: Retorna Colección HAL JSON válida")
    public void buscarPorApellido_RetornaColeccionHateoas() throws Exception {
        when(autorService.findByApPaterno("Cortázar")).thenReturn(List.of(responseDTO));

        mockMvc.perform(get("/api/v2/autores/apellido/{apellido}", "Cortázar")
                .accept(MediaTypes.HAL_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._links.self.href").exists());
    }

    @Test
    @DisplayName("PUT Actualizar V2: Retorna EntityModel HAL JSON individual")
    public void actualizar_RetornaModeloHateoas() throws Exception {
        AutorDTO.Request requestDTO = new AutorDTO.Request("Julio", "Florencio", "Cortázar", "Descotte");
        when(autorService.updateAutor(eq(1L), any(AutorDTO.Request.class))).thenReturn(responseDTO);

        mockMvc.perform(put("/api/v2/autores/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO))
                .accept(MediaTypes.HAL_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$._links.self.href").exists());
    }
}