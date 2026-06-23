package com.biblioteca.comuna.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
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

import com.biblioteca.comuna.dto.ComunaDTO;
import com.biblioteca.comuna.exception.ResourceNotFoundException;
import com.biblioteca.comuna.model.Comuna;
import com.biblioteca.comuna.repository.ComunaRepository;

@SpringBootTest
@ActiveProfiles("test") // Le decimos que use el properties de prueba sin puerto
public class ComunaServiceTest {

    // 1. Inyectamos el Servicio real que vamos a examinar
    @Autowired
    private ComunaService comunaService;

    // 2. Creamos el Mock para la base de datos, para simular su comportamiento.
    @MockBean
    private ComunaRepository comunaRepository;

    // 3. Variable global para no escribir los mismos datos en cada prueba
    private Comuna comunaPrueba;

    @BeforeEach
    void setUp() {
        // Antes de cada @Test, armamos nuestra comuna de utilería
        comunaPrueba = new Comuna();
        comunaPrueba.setId(1L);
        comunaPrueba.setNombre("Puente Alto");
    }

    @Test
    @DisplayName("Buscar Todos: Debe retornar la lista completa de comunas")
    public void testFindAll() {
        // DADO (Arrange): Preparamos la trampa del Mock. 
        // Cuando el Service real llame al Repository, el Mock le entregará una lista con nuestra comunaPrueba
        when(comunaRepository.findAll()).thenReturn(List.of(comunaPrueba));

        // CUANDO (Act): Ejecutamos el método real de tu Service.
        // Fíjate que el método devuelve una lista de DTOs, no de Entidades.
        List<ComunaDTO.Response> resultados = comunaService.findAll();

        // ENTONCES (Assert): Verificamos que el Service hizo bien su trabajo.
        assertNotNull(resultados); // La lista no debe ser nula
        assertEquals(1, resultados.size()); // Debe haber 1 elemento
        assertEquals("Puente Alto", resultados.get(0).getNombre()); // El DTO debe tener el nombre correcto
        assertEquals(1L, resultados.get(0).getId()); // El DTO debe tener la ID correcta
    }

    @Test
    @DisplayName("Crear (camino bueno): Debe guardar la comuna si el nombre es único")
    public void testSave_WhenNombreIsUnique_ReturnsComunaDTO() {
        // DADO (Arrange)
        // 1. Preparamos el Request (lo que envía el usuario)
        ComunaDTO.Request request = new ComunaDTO.Request();
        request.setNombre("Puente Alto");

        // 2. Le decimos al Mock: "Cuando te pregunten si el nombre existe, di que NO (false)"
        when(comunaRepository.existsByNombreIgnoreCase("Puente Alto")).thenReturn(false);
        
        // 3. Le decimos al Mock: "Cuando el Service intente guardar CUALQUIER comuna (any), 
        // devuélvele nuestra comunaPrueba que ya tiene ID asignada"
        when(comunaRepository.save(any(Comuna.class))).thenReturn(comunaPrueba);

        // CUANDO (Act)
        ComunaDTO.Response resultado = comunaService.save(request);

        // ENTONCES (Assert)
        assertNotNull(resultado);
        assertEquals("Puente Alto", resultado.getNombre());
        assertEquals(1L, resultado.getId());
    }

    @Test
    @DisplayName("Crear (Error): Falla al guardar si el nombre ya existe")
    public void testSave_LanzaExcepcionCuandoNombreYaExiste() {
        // DADO (Arrange)
        ComunaDTO.Request request = new ComunaDTO.Request();
        request.setNombre("Puente Alto");

        // Le decimos al Mock: "Cuando te pregunten si el nombre existe, di que SÍ (true)"
        when(comunaRepository.existsByNombreIgnoreCase("Puente Alto")).thenReturn(true);

        // CUANDO y ENTONCES (Act & Assert)
        // Usamos assertThrows para verificar que el Service REALMENTE lance la excepción esperada
        IllegalArgumentException excepcion = assertThrows(IllegalArgumentException.class, () -> {
            comunaService.save(request); // Al intentar guardar, debe explotar
        });

        // Opcional: Verificamos que el mensaje de la excepción sea exactamente el que escribiste
        assertEquals("La comuna con el nombre 'Puente Alto' ya existe.", excepcion.getMessage());
        
        // Verificamos que el repository NUNCA haya llamado al método save() 
        // (porque el código debió detenerse antes en el if)
        verify(comunaRepository, never()).save(any(Comuna.class));
    }

    // --- TESTS PARA BUSCAR POR ID ---

    @Test
    @DisplayName("Buscar por ID: Debe retornar la comuna si el ID existe")
    public void findByIdOrThrow_WhenIdExists_ReturnsComunaDTO() {
        // DADO: El Mock dice que SÍ encontró la comuna con ID 1
        when(comunaRepository.findById(1L)).thenReturn(Optional.of(comunaPrueba));

        // CUANDO: Ejecutamos el método
        ComunaDTO.Response resultado = comunaService.findByIdOrThrow(1L);

        // ENTONCES: Verificamos que devuelve la correcta
        assertNotNull(resultado);
        assertEquals("Puente Alto", resultado.getNombre());
    }

    @Test
    @DisplayName("Buscar por ID (Error): Lanza excepción si el ID no existe")
    public void findByIdOrThrow_WhenIdDoesNotExist_ThrowsResourceNotFoundException() {
        // DADO: El Mock dice que la base de datos está vacía (Optional.empty)
        when(comunaRepository.findById(99L)).thenReturn(Optional.empty());

        // CUANDO & ENTONCES: Verificamos que explote con nuestra excepción personalizada
        ResourceNotFoundException excepcion = assertThrows(ResourceNotFoundException.class, () -> {
            comunaService.findByIdOrThrow(99L);
        });
        assertEquals("Comuna no encontrada", excepcion.getMessage());
    }

    // --- TESTS PARA BUSCAR POR NOMBRE ---

    @Test
    @DisplayName("Buscar por Nombre: Retorna la comuna si el nombre coincide")
    public void findByNombre_WhenNombreExists_ReturnsOptionalWithComunaDTO() {
        // DADO: El Mock encuentra la comuna
        when(comunaRepository.findByNombreIgnoreCase("Puente Alto")).thenReturn(Optional.of(comunaPrueba));

        // CUANDO: Ejecutamos el método
        Optional<ComunaDTO.Response> resultado = comunaService.findByNombre("Puente Alto");

        // ENTONCES: Verificamos que la "caja" (Optional) no esté vacía y tenga el nombre correcto
        assertTrue(resultado.isPresent()); // isPresent() revisa que sí haya un objeto dentro
        assertEquals("Puente Alto", resultado.get().getNombre()); // .get() saca el objeto de la caja
    }

    @Test
    @DisplayName("Buscar por Nombre (Vacío): Retorna Optional vacío si no existe")
    public void findByNombre_WhenNombreDoesNotExist_ReturnsEmptyOptional() {
        // DADO: El Mock no encuentra nada y devuelve una caja vacía
        when(comunaRepository.findByNombreIgnoreCase("Narnia")).thenReturn(Optional.empty());

        // CUANDO: Ejecutamos el método
        Optional<ComunaDTO.Response> resultado = comunaService.findByNombre("Narnia");

        // ENTONCES: Verificamos que la caja (Optional) venga efectivamente vacía
        assertTrue(resultado.isEmpty());
    }

    // --- TESTS PARA ACTUALIZAR ---

    @Test
    @DisplayName("Actualizar (camino bueno): Cambia el nombre si es válido y único")
    public void update_WhenValidData_ReturnsUpdatedComunaDTO() {
        // DADO (Arrange)
        // 1. Lo que envía el usuario (quiere cambiar a "Santiago")
        ComunaDTO.Request request = new ComunaDTO.Request();
        request.setNombre("Santiago");

        // 2. La comuna que ya existe en la base de datos (Puente Alto)
        when(comunaRepository.findById(1L)).thenReturn(Optional.of(comunaPrueba));
        
        // 3. El Mock revisa si "Santiago" ya está tomado por OTRA comuna (dice que no)
        when(comunaRepository.existsByNombreIgnoreCase("Santiago")).thenReturn(false);
        
        // 4. El Mock devuelve la comuna guardada
        when(comunaRepository.save(any(Comuna.class))).thenReturn(comunaPrueba);

        // CUANDO (Act)
        ComunaDTO.Response resultado = comunaService.update(1L, request);

        // ENTONCES (Assert)
        assertNotNull(resultado);
        // Verificamos que guardó el cambio (nuestra comunaPrueba cambió su nombre por el request)
        verify(comunaRepository, times(1)).save(comunaPrueba); 
    }

    @Test
    @DisplayName("Actualizar (Error): Falla si el nuevo nombre ya lo usa otra comuna")
    public void update_WhenNameAlreadyExists_ThrowsIllegalArgumentException() {
        // DADO: El usuario quiere cambiar el nombre a "Santiago"
        ComunaDTO.Request request = new ComunaDTO.Request();
        request.setNombre("Santiago");

        // El Mock encuentra la comuna actual (Puente Alto)
        when(comunaRepository.findById(1L)).thenReturn(Optional.of(comunaPrueba));
        // Pero el Mock detecta que "Santiago" YA lo está usando otra comuna diferente
        when(comunaRepository.existsByNombreIgnoreCase("Santiago")).thenReturn(true);

        // CUANDO & ENTONCES: Verificamos que explote y aborte la misión
        IllegalArgumentException excepcion = assertThrows(IllegalArgumentException.class, () -> {
            comunaService.update(1L, request);
        });
        assertEquals("La comuna con el nombre 'Santiago' ya existe.", excepcion.getMessage());
        
        // Verificamos que el Service se detuvo y NUNCA guardó nada en la BD
        verify(comunaRepository, never()).save(any(Comuna.class));
    }

    // --- TESTS PARA BORRAR ---

    @Test
    @DisplayName("Borrar (camino bueno): Elimina la comuna exitosamente si existe")
    public void delete_WhenIdExists_DeletesSuccessfully() {
        // DADO: El Mock confirma que el ID 1 sí existe
        when(comunaRepository.existsById(1L)).thenReturn(true);
        // doNothing() se usa para métodos "void" (que no devuelven nada, como el deleteById)
        doNothing().when(comunaRepository).deleteById(1L);

        // CUANDO: Ejecutamos el borrado
        comunaService.delete(1L);

        // ENTONCES: Verificamos que el repositorio realmente llamó a la instrucción de borrar 1 sola vez
        verify(comunaRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Borrar (Error): Lanza excepción si se intenta borrar un ID inexistente")
    public void delete_WhenIdDoesNotExist_ThrowsResourceNotFoundException() {
        // DADO: El Mock dice que la comuna con ID 99 NO existe
        when(comunaRepository.existsById(99L)).thenReturn(false);

        // CUANDO & ENTONCES: Verificamos que lance la excepción correcta
        ResourceNotFoundException excepcion = assertThrows(ResourceNotFoundException.class, () -> {
            comunaService.delete(99L);
        });
        assertEquals("No se puede eliminar: La comuna con ID 99 no existe.", excepcion.getMessage());
        
        // Verificamos que NUNCA se intentó borrar nada en la base de datos
        verify(comunaRepository, never()).deleteById(anyLong());
    }

    

}
