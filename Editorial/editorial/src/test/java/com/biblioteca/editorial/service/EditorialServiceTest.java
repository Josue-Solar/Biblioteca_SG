package com.biblioteca.editorial.service;

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

import com.biblioteca.editorial.dto.EditorialDTO;
import com.biblioteca.editorial.exception.ResourceNotFoundException;
import com.biblioteca.editorial.model.Editorial;
import com.biblioteca.editorial.repository.EditorialRepository;

@SpringBootTest
@ActiveProfiles("test")
public class EditorialServiceTest {

    @Autowired
    private EditorialService editorialService;

    @MockBean
    private EditorialRepository editorialRepository;

    private Editorial editorialBase;

    @BeforeEach
    public void setUp() {
        editorialBase = new Editorial();
        editorialBase.setId(1L);
        editorialBase.setNombre("Planeta");
    }

    @Test
    @DisplayName("Listar Todas (Camino Feliz): Retorna lista de editoriales")
    public void findAll_ReturnsList() {
        // DADO
        when(editorialRepository.findAll()).thenReturn(List.of(editorialBase));

        // CUANDO
        List<EditorialDTO.Response> resultado = editorialService.findAll();

        // ENTONCES
        assertNotNull(resultado);
        assertFalse(resultado.isEmpty());
        assertEquals("Planeta", resultado.get(0).getNombre());
        verify(editorialRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Buscar por ID (Camino Feliz): Retorna la editorial encontrada")
    public void findByIdOrThrow_WhenIdExists_ReturnsEditorial() {
        // DADO
        when(editorialRepository.findById(1L)).thenReturn(Optional.of(editorialBase));

        // CUANDO
        EditorialDTO.Response resultado = editorialService.findByIdOrThrow(1L);

        // ENTONCES
        assertNotNull(resultado);
        assertEquals("Planeta", resultado.getNombre());
    }

    @Test
    @DisplayName("Guardar (Error): Lanza excepción si el nombre ya existe")
    public void save_WhenNameExists_ThrowsException() {
        // DADO
        EditorialDTO.Request request = new EditorialDTO.Request("Planeta");
        when(editorialRepository.existsByNombreIgnoreCase("Planeta")).thenReturn(true);

        // CUANDO y ENTONCES
        IllegalArgumentException excepcion = assertThrows(IllegalArgumentException.class, () -> {
            editorialService.save(request);
        });
        
        assertTrue(excepcion.getMessage().contains("ya existe"));
        verify(editorialRepository, never()).save(any(Editorial.class));
    }

    @Test
    @DisplayName("Eliminar (Camino Feliz): Borra exitosamente la editorial")
    public void delete_WhenIdExists_DeletesSuccessfully() {
        // DADO
        when(editorialRepository.existsById(1L)).thenReturn(true);
        doNothing().when(editorialRepository).deleteById(1L);

        // CUANDO
        editorialService.delete(1L);

        // ENTONCES
        verify(editorialRepository, times(1)).deleteById(1L);
    }
    
    @Test
    @DisplayName("Eliminar (Error): Lanza ResourceNotFoundException si no existe")
    public void delete_WhenIdDoesNotExist_ThrowsException() {
        // DADO
        when(editorialRepository.existsById(99L)).thenReturn(false);

        // CUANDO y ENTONCES
        assertThrows(ResourceNotFoundException.class, () -> {
            editorialService.delete(99L);
        });
    }
}