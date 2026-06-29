
package com.biblioteca.genero.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import com.biblioteca.genero.client.LibroClient;
import com.biblioteca.genero.dto.GeneroDTO;
import com.biblioteca.genero.dto.GeneroLibroDTO;
import com.biblioteca.genero.model.Genero;
import com.biblioteca.genero.repository.GeneroRepository;
import com.biblioteca.genero.service.GeneroService;

@SpringBootTest
@ActiveProfiles("test")
public class GeneroServiceTest {

    @Autowired
    private GeneroService generoService;

    @MockBean
    private GeneroRepository generoRepository;

    @MockBean
    private LibroClient libroClient;

    private Genero generoBase;

    @BeforeEach
    public void setUp() {
        generoBase = new Genero();
        generoBase.setId(1L);
        generoBase.setNombre("Ciencia Ficción");
    }

    @Test
    @DisplayName("Obtener Todos: Retorna lista de géneros")
    public void obtenerTodos_RetornaLista() {
        when(generoRepository.findAll()).thenReturn(List.of(generoBase));
        
        List<Genero> resultado = generoService.obtenerTodos();
        
        assertFalse(resultado.isEmpty());
        assertEquals("Ciencia Ficción", resultado.get(0).getNombre());
    }

    @Test
    @DisplayName("Buscar por ID: Retorna Response DTO si existe")
    public void findByIdOrThrow_RetornaDto() {
        // CORRECCIÓN: Cambiamos a la sintaxis doReturn para evitar el bloqueo de genéricos
        doReturn(Optional.of(generoBase)).when(generoRepository).findById(1L);
        
        GeneroDTO.Response resultado = generoService.findByIdOrThrow(1L);
        
        assertEquals("Ciencia Ficción", resultado.getNombre());
    }

    @Test
    @DisplayName("Guardar: Retorna Response DTO del género creado")
    public void guardar_RetornaResponseDto() {
        GeneroDTO.Request request = new GeneroDTO.Request("Fantasía");
        Genero generoGuardado = new Genero();
        generoGuardado.setId(2L);
        generoGuardado.setNombre("Fantasía");

        when(generoRepository.save(any(Genero.class))).thenReturn(generoGuardado);

        GeneroDTO.Response resultado = generoService.guardar(request);
        
        assertEquals("Fantasía", resultado.getNombre());
    }

    @Test
    @DisplayName("Actualizar: Retorna DTO actualizado")
    public void modificar_RetornaResponseDto() {
        GeneroDTO.Request request = new GeneroDTO.Request("Fantasía Épica");
        
        // CORRECCIÓN: Usamos doReturn().when() para evitar que el compilador de Eclipse colapse con los genéricos
        doReturn(Optional.of(generoBase)).when(generoRepository).findById(1L);
        doReturn(generoBase).when(generoRepository).save(any(Genero.class));

        // 3. Ejecutamos tu servicio
        GeneroDTO.Response resultado = generoService.modificarGenero(1L, request);
        
        // 4. Verificamos
        assertNotNull(resultado);
        assertEquals("Ciencia Ficción", resultado.getNombre());
    }

    @Test
    @DisplayName("Eliminar: Ejecuta borrado en repositorio")
    public void eliminar_EjecutaBorrado() {
        doNothing().when(generoRepository).deleteById(anyLong());
        
        generoService.eliminar(1L);
        
        verify(generoRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Libros Por Género: Se comunica con Feign Client")
    public void librosPorGenero_LlamaALibroClient() {
        // CORRECCIÓN: Cambiamos a la sintaxis doReturn para evitar el bloqueo del Optional
        doReturn(Optional.of(generoBase)).when(generoRepository).findById(1L);
        
        // El cliente de Feign devuelve una Lista, por lo que el "when" tradicional funciona bien aquí
        when(libroClient.getAllByGeneroId(1L)).thenReturn(List.of()); 

        GeneroLibroDTO resultado = generoService.librosPorGenero(1L);
        
        assertNotNull(resultado);
        assertEquals("Ciencia Ficción", resultado.getGenero().getNombre());
        verify(libroClient, times(1)).getAllByGeneroId(1L);
    }
}