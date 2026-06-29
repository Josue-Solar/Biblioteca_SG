package com.biblioteca.autor.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import com.biblioteca.autor.dto.AutorDTO;
import com.biblioteca.autor.exception.ResourceNotFoundException;
import com.biblioteca.autor.model.Autor;
import com.biblioteca.autor.repository.AutorRepository;

@SpringBootTest
@ActiveProfiles("test")
public class AutorServiceTest {

    @Autowired
    private AutorService autorService;

    @MockBean
    private AutorRepository autorRepository;

    private Autor autorBase;

    @BeforeEach
    public void setUp() {
        autorBase = new Autor();
        autorBase.setId(1L);
        autorBase.setPrimerNombre("Gabriel");
        autorBase.setSegundoNombre("José");
        autorBase.setApPaterno("García");
        autorBase.setApMaterno("Márquez");
    }

    @Test
    @DisplayName("Listar Todos (Camino Feliz): Retorna lista de autores DTO")
    public void findAll_ReturnsList() {
        when(autorRepository.findAll()).thenReturn(List.of(autorBase));

        List<AutorDTO.Response> resultado = autorService.findAll();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Gabriel", resultado.get(0).getPrimerNombre());
        verify(autorRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Buscar por ID (Camino Feliz): Retorna el autor encontrado")
    public void findByIdOrThrow_WhenIdExists_ReturnsAutor() {
        when(autorRepository.findById(1L)).thenReturn(Optional.of(autorBase));

        AutorDTO.Response resultado = autorService.findByIdOrThrow(1L);

        assertNotNull(resultado);
        assertEquals("Gabriel", resultado.getPrimerNombre());
        assertEquals("García", resultado.getApPaterno());
    }

    @Test
    @DisplayName("Buscar por ID (Error): Lanza ResourceNotFoundException si no existe")
    public void findByIdOrThrow_WhenIdDoesNotExist_ThrowsException() {
        when(autorRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            autorService.findByIdOrThrow(99L);
        });
    }

    @Test
    @DisplayName("Guardar (Camino Feliz): Registra un nuevo autor con éxito")
    public void save_ValidRequest_ReturnsResponseDTO() {
        AutorDTO.Request request = new AutorDTO.Request("Gabriel", "José", "García", "Márquez");
        when(autorRepository.save(any(Autor.class))).thenReturn(autorBase);

        AutorDTO.Response resultado = autorService.save(request);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Gabriel", resultado.getPrimerNombre());
    }

    @Test
    @DisplayName("Borrar (Camino Feliz): Elimina el autor de forma exitosa")
    public void delete_WhenIdExists_DeletesSuccessfully() {
        when(autorRepository.existsById(1L)).thenReturn(true);
        doNothing().when(autorRepository).deleteById(1L);

        autorService.delete(1L);

        verify(autorRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Borrar (Error): Lanza excepción si el autor no existe")
    public void delete_WhenIdDoesNotExist_ThrowsException() {
        when(autorRepository.existsById(99L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> {
            autorService.delete(99L);
        });
        verify(autorRepository, never()).deleteById(anyLong());
    }

    @Test
    @DisplayName("Buscar por Apellido: Retorna lista de autores coincidentes")
    public void findByApPaterno_ReturnsList() {
        when(autorRepository.findByApPaternoIgnoreCase("García")).thenReturn(List.of(autorBase));

        List<AutorDTO.Response> resultado = autorService.findByApPaterno("García");

        assertFalse(resultado.isEmpty());
        assertEquals("García", resultado.get(0).getApPaterno());
    }
}