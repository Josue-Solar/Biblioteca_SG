package com.biblioteca.libro.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.biblioteca.libro.dto.LibroDTO;
import com.biblioteca.libro.model.Libro;
import com.biblioteca.libro.repository.LibroRepository;
import com.biblioteca.libro.repository.LibroGeneroRepository;
import com.biblioteca.libro.client.GeneroClient;

@ExtendWith(MockitoExtension.class) // 🌟 Cambiado de @SpringBootTest a Mockito puro
public class LibroServiceTest {

    @InjectMocks // 🌟 Inyecta automáticamente los mocks dentro de tu servicio
    private LibroService libroService;

    @Mock // 🌟 Cambiado de @MockBean a @Mock
    private LibroRepository libroRepository;

    @Mock
    private LibroGeneroRepository libroGeneroRepository;

    @Mock
    private GeneroClient generoClient;

    private Libro libroBase;

    @BeforeEach
    public void setUp() {
        libroBase = new Libro();
        libroBase.setIsbn(123456789L);
        libroBase.setNombre("Cien años de soledad");
    }

    @Test
    @DisplayName("Obtener Todos: Retorna lista de libros DTO")
    public void obtenerTodos_RetornaListaDto() {
        when(libroRepository.findAll()).thenReturn(List.of(libroBase));
        
        List<LibroDTO.Response> resultado = libroService.obtenerTodos();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Cien años de soledad", resultado.get(0).getNombre());
    }

    @Test
    @DisplayName("Obtener por ISBN: Retorna Libro DTO")
    public void obtenerPorIsbn_RetornaDto() {
        when(libroRepository.findByIsbn(123456789L)).thenReturn(libroBase);
        
        LibroDTO.Response resultado = libroService.obtenerPorIsbn(123456789L);

        assertNotNull(resultado);
        assertEquals("Cien años de soledad", resultado.getNombre());
    }

    @Test
    @DisplayName("Obtener por Nombre: Retorna Libro DTO si existe")
    public void obtenerPorNombre_RetornaDto() {
        when(libroRepository.findByNombre("Cien años de soledad")).thenReturn(List.of(libroBase));
        
        LibroDTO.Response resultado = libroService.obtenerPorNombre("Cien años de soledad");

        assertNotNull(resultado);
        assertEquals(123456789L, resultado.getIsbn());
    }

    @Test
    @DisplayName("Obtener por Nombre: Lanza excepción si no existe")
    public void obtenerPorNombre_LanzaExcepcion() {
        when(libroRepository.findByNombre("Inexistente")).thenReturn(List.of());
        
        assertThrows(RuntimeException.class, () -> {
            libroService.obtenerPorNombre("Inexistente");
        });
    }

    @Test
    @DisplayName("Guardar: Registra libro y retorna DTO")
    public void guardar_RegistraYRetornaDto() {
        LibroDTO.Request request = new LibroDTO.Request(123456789L, "Cien años de soledad");
        when(libroRepository.save(any(Libro.class))).thenReturn(libroBase);

        LibroDTO.Response resultado = libroService.guardar(request);

        assertNotNull(resultado);
        assertEquals("Cien años de soledad", resultado.getNombre());
    }

    @Test
    @DisplayName("Actualizar: Modifica libro existente")
    public void actualizar_ModificaLibro() {
        LibroDTO.Request request = new LibroDTO.Request(123456789L, "Nuevo Nombre");
        
        when(libroRepository.findById(123456789L)).thenReturn(Optional.of(libroBase));
        when(libroRepository.save(any(Libro.class))).thenReturn(libroBase);

        LibroDTO.Response resultado = libroService.actualizar(123456789L, request);

        assertNotNull(resultado);
    }
}