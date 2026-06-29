package com.biblioteca.ejemplar.service;

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

import com.biblioteca.ejemplar.client.EdicionClient;
import com.biblioteca.ejemplar.client.LibroClient;
import com.biblioteca.ejemplar.model.Ejemplar;
import com.biblioteca.ejemplar.repository.EjemplarRepository;
import com.biblioteca.ejemplar.service.EjemplarService;

@SpringBootTest
@ActiveProfiles("test")
public class EjemplarServiceTest {

    @Autowired
    private EjemplarService ejemplarService;

    @MockBean
    private EjemplarRepository ejemplarRepository;

    @MockBean
    private LibroClient libroClient;

    @MockBean
    private EdicionClient edicionClient;

    private Ejemplar ejemplarBase;

    @BeforeEach
    public void setUp() {
        ejemplarBase = new Ejemplar();
        ejemplarBase.setId(1L);
        ejemplarBase.setLibroIsbn(9781234567890L);
    }

    @Test
    @DisplayName("Obtener Todos: Retorna la lista de ejemplares")
    public void obtenerTodos_RetornaLista() {
        // DADO
        when(ejemplarRepository.findAll()).thenReturn(List.of(ejemplarBase));

        // CUANDO
        List<Ejemplar> resultado = ejemplarService.obtenerTodos();

        // ENTONCES
        assertNotNull(resultado);
        assertFalse(resultado.isEmpty());
        assertEquals(1L, resultado.get(0).getId());
        verify(ejemplarRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Obtener por ID: Retorna el ejemplar si existe")
    public void obtenerPorId_CuandoExiste_RetornaEjemplar() {
        // DADO
        when(ejemplarRepository.findById(1L)).thenReturn(Optional.of(ejemplarBase));

        // CUANDO
        Optional<Ejemplar> resultado = ejemplarService.obtenerPorId(1L);

        // ENTONCES
        assertTrue(resultado.isPresent());
        assertEquals(9781234567890L, resultado.get().getLibroIsbn());
    }

    @Test
    @DisplayName("Guardar: Retorna el ejemplar guardado")
    public void guardar_CaminoFeliz_RetornaEjemplar() {
        // DADO
        when(ejemplarRepository.save(any(Ejemplar.class))).thenReturn(ejemplarBase);

        // CUANDO
        Ejemplar resultado = ejemplarService.guardar(ejemplarBase);

        // ENTONCES
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
    }

    @Test
    @DisplayName("Eliminar: Llama al repositorio para borrar")
    public void eliminar_CaminoFeliz_EjecutaBorrado() {
        // DADO
        doNothing().when(ejemplarRepository).deleteById(1L);

        // CUANDO
        ejemplarService.eliminar(1L);

        // ENTONCES
        verify(ejemplarRepository, times(1)).deleteById(1L);
    }
}