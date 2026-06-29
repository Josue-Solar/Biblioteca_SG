package com.example.edicion.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import com.example.edicion.dto.EdicionDTO;
import com.example.edicion.model.Edicion;
import com.example.edicion.repository.EdicionRepository;
import com.example.edicion.repository.EdicionEditorialRepository;
import com.example.edicion.client.EditorialClient;
import com.example.edicion.client.EjemplarClient;

@SpringBootTest
@ActiveProfiles("test")
public class EdicionServiceTest {

    @Autowired
    private EdicionService edicionService;

    @MockBean
    private EdicionRepository edicionRepository;

    @MockBean
    private EdicionEditorialRepository edicionEditorialRepo;

    @MockBean
    private EditorialClient editorialClient;

    @MockBean
    private EjemplarClient ejemplarClient;

    private Edicion edicionBase;

    @BeforeEach
    public void setUp() {
        edicionBase = new Edicion();
        edicionBase.setId(1L);
        edicionBase.setNombre("Primera Edición");
        edicionBase.setAnnioPublicacion(2020);
    }

    @Test
    @DisplayName("Obtener Todos (Camino Feliz): Retorna lista de ediciones")
    public void obtenerTodos_CaminoFeliz_RetornaLista() {
        // DADO
        when(edicionRepository.findAll()).thenReturn(List.of(edicionBase));

        // CUANDO
        List<EdicionDTO.Response> resultado = edicionService.obtenerTodos();

        // ENTONCES
        assertNotNull(resultado);
        assertFalse(resultado.isEmpty());
        verify(edicionRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Obtener por ID (Camino Feliz): Retorna la edición encontrada")
    public void obtenerPorId_CuandoExiste_RetornaEdicion() {
        // DADO
        when(edicionRepository.findById(1L)).thenReturn(Optional.of(edicionBase));

        // CUANDO
        EdicionDTO.Response resultado = edicionService.obtenerPorId(1L);

        // ENTONCES
        assertNotNull(resultado);
        assertEquals("Primera Edición", resultado.getNombre());
    }

    @Test
    @DisplayName("Guardar (Camino Feliz): Registra una edición con éxito")
    public void guardar_CaminoFeliz_RetornaResponseDTO() {
        // DADO
        EdicionDTO.Request request = new EdicionDTO.Request();
        request.setNombre("Primera Edición");
        request.setAnnioPublicacion(2020);
        
        when(edicionRepository.save(any(Edicion.class))).thenReturn(edicionBase);

        // CUANDO
        EdicionDTO.Response resultado = edicionService.guardar(request);

        // ENTONCES
        assertNotNull(resultado);
        assertEquals("Primera Edición", resultado.getNombre());
    }

    @Test
    @DisplayName("Eliminar (Camino Feliz): Elimina la edición de forma exitosa")
    public void eliminar_CuandoIdExiste_EliminaExitosamente() {
        // DADO
        when(edicionRepository.existsById(1L)).thenReturn(true);
        doNothing().when(edicionRepository).deleteById(1L);

        // CUANDO
        edicionService.eliminar(1L);

        // ENTONCES
        verify(edicionRepository, times(1)).deleteById(1L);
    }
}