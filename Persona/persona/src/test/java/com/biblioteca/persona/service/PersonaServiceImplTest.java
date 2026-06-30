package com.biblioteca.persona.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.ActiveProfiles;

import com.biblioteca.persona.client.ComunaClient;
import com.biblioteca.persona.dto.ComunaDTO;
import com.biblioteca.persona.dto.PersonaDTO;
import com.biblioteca.persona.dto.RolDTO;
import com.biblioteca.persona.dto.SexoDTO;
import com.biblioteca.persona.model.Persona;
import com.biblioteca.persona.model.Rol;
import com.biblioteca.persona.model.Sexo;
import com.biblioteca.persona.repository.PersonaRepository;
import com.biblioteca.persona.repository.RolRepository;
import com.biblioteca.persona.repository.SexoRepository;
import com.biblioteca.persona.service.impl.PersonaServiceImpl;

@SpringBootTest
@ActiveProfiles("test")
public class PersonaServiceImplTest {

    // 1. Inyectamos el Servicio real
    @Autowired
    private PersonaServiceImpl personaService;

    // 2. Mockeamos TODOS los repositorios y servicios externos que usa PersonaServiceImpl
    @MockitoBean
    private PersonaRepository personaRepository;
    @MockitoBean
    private RolRepository rolRepository;
    @MockitoBean
    private SexoRepository sexoRepository;
    @MockitoBean
    private RolService rolService;
    @MockitoBean
    private SexoService sexoService;
    @MockitoBean
    private ComunaClient comunaClient;

    // 3. Variables globales de utilería
    private Persona personaPrueba;
    private PersonaDTO.Request requestPrueba;

    @BeforeEach
    void setUp() {
        // Creamos la comuna de respuesta falsa
        ComunaDTO comunaFalsa = new ComunaDTO(); // (Si tu client devuelve un ComunaDTO.Response, cámbialo aquí)
        comunaFalsa.setId(1L);
        comunaFalsa.setNombre("Puente Alto");
        when(comunaClient.buscarPorId(anyLong())).thenReturn(comunaFalsa);
        // Armamos Entidades básicas
        Sexo sexoPrueba = new Sexo();
        sexoPrueba.setId(1L);
        sexoPrueba.setNombre("FEMENINO");

        Rol rolPrueba = new Rol();
        rolPrueba.setId(1L);
        rolPrueba.setNombre("USUARIO");

        // Armamos la Persona de prueba (Entidad)
        personaPrueba = new Persona();
        personaPrueba.setId(1L);
        personaPrueba.setRun("11222333");
        personaPrueba.setDvRun("4");
        personaPrueba.setPNombre("Ada");
        personaPrueba.setApPaterno("Lovelace");
        personaPrueba.setCorreo("ada@biblioteca.com");
        personaPrueba.setComunaId(1L);
        personaPrueba.setSexo(sexoPrueba);
        personaPrueba.setRol(rolPrueba);

        // Armamos el Request de prueba (Lo que envía el usuario)
        requestPrueba = new PersonaDTO.Request();
        requestPrueba.setRun("11222333");
        requestPrueba.setDvRun("4");
        requestPrueba.setPNombre("Ada");
        requestPrueba.setApPaterno("Lovelace");
        requestPrueba.setCorreo("ada@biblioteca.com");
        requestPrueba.setComunaId(1L);
        requestPrueba.setSexoId(1L);
        requestPrueba.setIdRol(1L);

    }

// --- TESTS CRUD PRINCIPAL ---

    @Test
    @DisplayName("Buscar Todos: Retorna lista con datos")
    public void testFindAll() {
        when(personaRepository.findAll()).thenReturn(List.of(personaPrueba));
        List<PersonaDTO.Response> resultados = personaService.findAll();
        assertEquals(1, resultados.size());
    }

    @Test
    @DisplayName("Buscar Todos: Retorna lista vacía si no hay registros")
    public void testFindAll_Empty() {
        when(personaRepository.findAll()).thenReturn(new ArrayList<>());
        List<PersonaDTO.Response> resultados = personaService.findAll();
        assertTrue(resultados.isEmpty());
    }

    @Test
    @DisplayName("Buscar por ID: Retorna la persona si existe")
    public void testFindById_Success() {
        when(personaRepository.findById(1L)).thenReturn(Optional.of(personaPrueba));
        PersonaDTO.Response resultado = personaService.findById(1L);
        assertEquals("ada@biblioteca.com", resultado.getCorreo());
    }

    @Test
    @DisplayName("Buscar por ID: Lanza error si no existe")
    public void testFindById_NotFound() {
        when(personaRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> personaService.findById(99L));
    }

    @Test
    @DisplayName("Crear: Guarda si el RUN es nuevo")
    public void testSave_Success() {
        when(personaRepository.existsByRun("11222333")).thenReturn(false);
        when(sexoRepository.findById(1L)).thenReturn(Optional.of(new Sexo()));
        when(rolRepository.findById(1L)).thenReturn(Optional.of(new Rol()));
        when(personaRepository.save(any(Persona.class))).thenReturn(personaPrueba);

        PersonaDTO.Response resultado = personaService.save(requestPrueba);
        assertNotNull(resultado);
        verify(personaRepository, times(1)).save(any(Persona.class));
    }

    @Test
    @DisplayName("Crear: Lanza error si RUN ya existe")
    public void testSave_RunExists() {
        when(personaRepository.existsByRun("11222333")).thenReturn(true);
        assertThrows(RuntimeException.class, () -> personaService.save(requestPrueba));
        verify(personaRepository, never()).save(any(Persona.class));
    }

    @Test
    @DisplayName("Actualizar: Guarda si ID existe")
    public void testUpdatePersona_Success() {
        // DADO: Ahora simulamos que encontramos a la persona por su ID
        when(personaRepository.findById(1L)).thenReturn(Optional.of(personaPrueba));
        
        when(sexoRepository.findById(1L)).thenReturn(Optional.of(new Sexo()));
        when(rolRepository.findById(1L)).thenReturn(Optional.of(new Rol()));
        when(personaRepository.save(any(Persona.class))).thenReturn(personaPrueba);

        // CUANDO: Llamamos al método pasándole el ID 1L en vez del String
        PersonaDTO.Response resultado = personaService.updatePersona(1L, requestPrueba);
        
        // ENTONCES
        assertNotNull(resultado);
        verify(personaRepository, times(1)).save(any(Persona.class));
    }

    @Test
    @DisplayName("Actualizar: Lanza error si ID no existe")
    public void testUpdatePersona_NotFound() {
        // DADO: Simulamos que la base de datos devuelve vacío al buscar el ID 1L
        when(personaRepository.findById(1L)).thenReturn(Optional.empty());
        
        // CUANDO y ENTONCES: Esperamos que lance el error al pasarle 1L
        assertThrows(RuntimeException.class, () -> personaService.updatePersona(1L, requestPrueba));
    }

    //eliminar
    @Test
    @DisplayName("Borrar: Ejecuta eliminación por ID")
    public void testDelete() {
        doNothing().when(personaRepository).deleteById(1L);
        personaService.delete(1L);
        verify(personaRepository, times(1)).deleteById(1L);
    }

    // metodos especificos

    @Test
    @DisplayName("Buscar por RUN: Retorna DTO si existe")
    public void testFindByRun_Success() {
        when(personaRepository.findByRun("11222333")).thenReturn(Optional.of(personaPrueba));
        PersonaDTO.Response resultado = personaService.findByRun("11222333");
        assertNotNull(resultado);
    }

    @Test
    @DisplayName("Buscar por RUN: Retorna null si no existe")
    public void testFindByRun_NotFound() {
        when(personaRepository.findByRun("99999999")).thenReturn(Optional.empty());
        PersonaDTO.Response resultado = personaService.findByRun("99999999");
        assertNull(resultado);
    }

    @Test
    @DisplayName("Buscar por Apellido: Retorna lista")
    public void testFindByApPaterno() {
        when(personaRepository.findByApPaterno("Lovelace")).thenReturn(List.of(personaPrueba));
        List<PersonaDTO.Response> resultados = personaService.findByApPaterno("Lovelace");
        assertEquals(1, resultados.size());
    }

    @Test
    @DisplayName("Buscar por Rol: Retorna lista")
    public void testFindByRol() {
        RolDTO.Response rolDto = new RolDTO.Response();
        rolDto.setNombre("USUARIO");
        
        when(personaRepository.findByRolNombre("USUARIO")).thenReturn(List.of(personaPrueba));
        List<PersonaDTO.Response> resultados = personaService.findByRol(rolDto);
        assertEquals(1, resultados.size());
    }

    @Test
    @DisplayName("Buscar por Sexo: Retorna lista")
    public void testFindBySexo() {
        SexoDTO.Response sexoDto = new SexoDTO.Response();
        sexoDto.setNombre("FEMENINO");
        
        when(personaRepository.findBySexoNombre("FEMENINO")).thenReturn(List.of(personaPrueba));
        List<PersonaDTO.Response> resultados = personaService.findBySexo(sexoDto);
        assertEquals(1, resultados.size());
    }

    @Test
    @DisplayName("Buscar por Nombre Comuna: Retorna lista")
    public void testFindByComunaNombre() {
        ComunaDTO comunaFalsa = new ComunaDTO();
        comunaFalsa.setId(5L);
        comunaFalsa.setNombre("Santiago");
        
        when(comunaClient.buscarPorNombre("Santiago")).thenReturn(comunaFalsa);
        when(personaRepository.findByComunaId(5L)).thenReturn(List.of(personaPrueba));
        
        List<PersonaDTO.Response> resultados = personaService.findByComunaNombre("Santiago");
        assertEquals(1, resultados.size());
    }

    @Test
    @DisplayName("Buscar por ID de Comuna: Retorna lista")
    public void testFindByComunaID() {
        when(personaRepository.findByComunaId(1L)).thenReturn(List.of(personaPrueba));
        List<PersonaDTO.Response> resultados = personaService.findByComunaID(1L);
        assertEquals(1, resultados.size());
    }

}
