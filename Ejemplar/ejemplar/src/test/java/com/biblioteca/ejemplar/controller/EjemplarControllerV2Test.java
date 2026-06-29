package com.biblioteca.ejemplar.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
// Si usas Spring Boot 3.4+:
// import org.springframework.test.context.bean.override.mockito.MockitoBean;
// import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.biblioteca.ejemplar.assemblers.EjemplarModelAssembler;
import com.biblioteca.ejemplar.model.Ejemplar;
import com.biblioteca.ejemplar.service.EjemplarService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(EjemplarControllerV2.class)
@AutoConfigureMockMvc(addFilters = false)
public class EjemplarControllerV2Test {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private EjemplarService ejemplarService;

    // SpyBean es necesario para generar los enlaces de HATEOAS
    @SpyBean
    private EjemplarModelAssembler assembler;

    private Ejemplar ejemplarBase;

    @BeforeEach
    public void setUp() {
        ejemplarBase = new Ejemplar();
        ejemplarBase.setId(1L);
        ejemplarBase.setLibroIsbn(9781234567890L);
    }

    @Test
    @DisplayName("GET Listar V2: Retorna Colección HAL JSON con hipervínculos")
    public void listar_RetornaColeccionHateoas() throws Exception {
        when(ejemplarService.obtenerTodos()).thenReturn(List.of(ejemplarBase));

        mockMvc.perform(get("/api/v2/ejemplares")
                .accept(MediaTypes.HAL_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", MediaTypes.HAL_JSON_VALUE))
                .andExpect(jsonPath("$._embedded.ejemplarList[0].id").value(1L))
                .andExpect(jsonPath("$._links.self.href").exists());
    }

    @Test
    @DisplayName("GET Buscar Por ISBN V2: Retorna Colección HAL JSON")
    public void buscarPorIsbn_RetornaColeccionHateoas() throws Exception {
        when(ejemplarService.obtenerTodosPorIsbn(9781234567890L)).thenReturn(List.of(ejemplarBase));

        mockMvc.perform(get("/api/v2/ejemplares/isbn/{isbn}", 9781234567890L)
                .accept(MediaTypes.HAL_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._links.self.href").exists());
    }

    @Test
    @DisplayName("POST Crear V2: Retorna EntityModel HAL JSON")
    public void guardar_RetornaModeloHateoas() throws Exception {
        when(ejemplarService.guardar(any(Ejemplar.class))).thenReturn(ejemplarBase);

        mockMvc.perform(post("/api/v2/ejemplares")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(ejemplarBase))
                .accept(MediaTypes.HAL_JSON_VALUE))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$._links.self.href").exists());
    }

    @Test
    @DisplayName("DELETE Eliminar V2: Retorna 204 No Content")
    public void eliminar_Retorna204() throws Exception {
        doNothing().when(ejemplarService).eliminar(1L);

        mockMvc.perform(delete("/api/v2/ejemplares/{id}", 1L))
                .andExpect(status().isNoContent());
    }
}
