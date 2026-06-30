package com.biblioteca.persona.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.biblioteca.persona.dto.PersonaDTO;
import com.biblioteca.persona.dto.RolDTO;
import com.biblioteca.persona.service.RolService;
import com.biblioteca.persona.service.SexoService;
import com.biblioteca.persona.service.impl.PersonaServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;

// no pude poner esas anotaciones, me daban error
//@WebMvcTest(PersonaController.class) // Levanta solo este controlador
//@AutoConfigureMockMvc(addFilters = false) // Apaga los filtros de seguridad (si los hubiera)
public class PersonaControllerV1Test {
    //no se trabajo con spring y solo con mockito(de ahi el object mapper) q funciona solo
    //lo mas probable por alguna inconsistencia del pom

    // 1. Quitamos @Autowired porque lo configuraremos a mano en el setUp
    private MockMvc mockMvc; 

    // 2. Quitamos @Autowired, lo vamos a instanciar nosotros mismos
    private ObjectMapper objectMapper; 

    // 3. CAMBIO CRÍTICO: Usamos @Mock (de Mockito) en lugar de @MockitoBean (de Spring)
    @Mock
    private PersonaServiceImpl personaService;
    
    @Mock
    private SexoService sexoService;
    
    @Mock
    private RolService rolService;

    @InjectMocks
    private PersonaController personaController; 

    private PersonaDTO.Response personaResponsePrueba;

    @BeforeEach
    void setUp() {
        // Abre los mocks de Mockito estándar (ahora sí leerá los @Mock)
        MockitoAnnotations.openMocks(this);
        
        // Construimos nuestro Postman virtual
        this.mockMvc = MockMvcBuilders.standaloneSetup(personaController).build();
        
        // 4. NUEVO: Instanciamos el traductor JSON manualmente
        this.objectMapper = new ObjectMapper();
        
        // Preparamos el DTO de respuesta que usaremos en las pruebas
        personaResponsePrueba = new PersonaDTO.Response();
        personaResponsePrueba.setId(1L);
        personaResponsePrueba.setRut("11222333-4");
        personaResponsePrueba.setNombreCompleto("Ada Lovelace");
        personaResponsePrueba.setCorreo("ada@biblioteca.com");
    }

    // ==========================================
    // TESTS DEL MÉTODO LISTAR (GET /api/v1/personas)
    // ==========================================

    @Test
    @DisplayName("GET Listar (Camino Feliz): Retorna 200 OK y la lista de personas")
    public void listar_CuandoHayPersonas_Retorna200() throws Exception {
        // DADO: El Service devuelve una lista con 1 persona
        when(personaService.findAll()).thenReturn(List.of(personaResponsePrueba));

        // CUANDO y ENTONCES: Hacemos la petición y validamos la respuesta JSON
        mockMvc.perform(get("/api/v1/personas")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // Esperamos un HTTP 200
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].rut").value("11222333-4"))
                .andExpect(jsonPath("$[0].nombreCompleto").value("Ada Lovelace"))
                .andExpect(jsonPath("$[0].correo").value("ada@biblioteca.com"));
    }

    @Test
    @DisplayName("GET Listar (Camino Malo): Retorna 204 No Content si está vacío")
    public void listar_CuandoNoHayPersonas_Retorna204() throws Exception {
        // DADO: El Service devuelve una lista vacía
        when(personaService.findAll()).thenReturn(Collections.emptyList());

        // CUANDO y ENTONCES: Verificamos que el controlador retorne 204 sin cuerpo
        mockMvc.perform(get("/api/v1/personas")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent()); // Esperamos un HTTP 204
    }

    // ==========================================
    // TESTS DEL MÉTODO CREAR (POST /api/v1/personas)
    // ==========================================

    @Test
    @DisplayName("POST Crear (Camino Feliz): Retorna 201 Created con la persona guardada")
    public void guardar_CuandoDatosSonValidos_Retorna201() throws Exception {
        // DADO: Construimos el Request con todos los campos obligatorios correctos
        PersonaDTO.Request requestBody = new PersonaDTO.Request();
        requestBody.setRun("11222333");
        requestBody.setDvRun("4");
        requestBody.setPNombre("Ada");
        requestBody.setApPaterno("Lovelace");
        requestBody.setCorreo("ada@biblioteca.com");
        requestBody.setComunaId(1L);
        requestBody.setSexoId(1L);
        requestBody.setIdRol(1L);

        // Simulamos que el Service procesa el Request y retorna exitosamente el ResponsePrueba
        when(personaService.save(any(PersonaDTO.Request.class))).thenReturn(personaResponsePrueba);

        // CUANDO y ENTONCES: Ejecutamos el POST enviando el objeto convertido a JSON
        mockMvc.perform(post("/api/v1/personas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isCreated()) // Valida el estado 201
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.rut").value("11222333-4"))
                .andExpect(jsonPath("$.nombreCompleto").value("Ada Lovelace"))
                .andExpect(jsonPath("$.correo").value("ada@biblioteca.com"));
    }

    @Test
    @DisplayName("POST Crear (Camino Malo): Retorna 400 Bad Request si fallan las validaciones")
    public void guardar_CuandoDatosSonInvalidos_Retorna400() throws Exception {
        // DADO: Forzamos errores dejando el RUN vacío (@NotBlank) y el correo mal escrito (@Email)
        PersonaDTO.Request requestInvalido = new PersonaDTO.Request();
        requestInvalido.setRun(""); // Inválido: Vacío
        requestInvalido.setDvRun("4");
        requestInvalido.setPNombre("Ada");
        requestInvalido.setApPaterno("Lovelace");
        requestInvalido.setCorreo("correoInvalidoSinArroba"); // Inválido: No es formato email
        requestInvalido.setComunaId(1L);
        requestInvalido.setSexoId(1L);
        requestInvalido.setIdRol(1L);

        // CUANDO y ENTONCES: El interceptor @Valid de Spring debería rebotar esto antes de tocar el Service
        mockMvc.perform(post("/api/v1/personas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestInvalido)))
                .andExpect(status().isBadRequest()); // Valida el estado 400

        // Verificación de seguridad: El servicio NUNCA debió ser invocado
        verify(personaService, never()).save(any(PersonaDTO.Request.class));
    }

    // ==========================================
    // TESTS DEL MÉTODO BUSCAR POR ID (GET /api/v1/personas/{id})
    // ==========================================

    @Test
    @DisplayName("GET Buscar por ID (Camino Feliz): Retorna 200 OK si el ID existe")
    public void buscarPorId_CuandoIdExiste_Retorna200() throws Exception {
        // DADO: El servicio encuentra la persona con ID 1 y la retorna
        when(personaService.findById(1L)).thenReturn(personaResponsePrueba);

        // CUANDO y ENTONCES: Hacemos la petición GET pasando el ID en la URL
        mockMvc.perform(get("/api/v1/personas/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // Valida HTTP 200
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.rut").value("11222333-4"))
                .andExpect(jsonPath("$.nombreCompleto").value("Ada Lovelace"));
        
        // Verificamos que el servicio fue consultado con el ID correcto
        verify(personaService, times(1)).findById(1L);
    }

    @Test
    @DisplayName("GET Buscar por ID (Camino Malo): Retorna 404 Not Found si el ID no existe")
    public void buscarPorId_CuandoIdNoExiste_Retorna404() throws Exception {
        // DADO: El servicio lanza una excepción al buscar un ID inexistente (ej: 99)
        when(personaService.findById(99L)).thenThrow(new RuntimeException("Persona no encontrada"));

        // CUANDO y ENTONCES: Tu catch en el controlador debería transformar el error en un 404
        mockMvc.perform(get("/api/v1/personas/{id}", 99L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()); // Valida HTTP 404
        
        verify(personaService, times(1)).findById(99L);
    }

    // TESTS DEL MÉTODO CREAR (POST /api/v1/personas)
    // ==========================================

    @Test
    @DisplayName("POST Crear (Camino Feliz): Retorna 201 Created con la persona guardada")
    public void guardar_DatosSonValidos_Retorna201() throws Exception {
        // DADO: Construimos el Request con todos los campos obligatorios correctos
        PersonaDTO.Request requestBody = new PersonaDTO.Request();
        requestBody.setRun("11222333");
        requestBody.setDvRun("4");
        requestBody.setPNombre("Ada");
        requestBody.setApPaterno("Lovelace");
        requestBody.setCorreo("ada@biblioteca.com");
        requestBody.setComunaId(1L);
        requestBody.setSexoId(1L);
        requestBody.setIdRol(1L);

        // Simulamos que el Service procesa el Request y retorna exitosamente el ResponsePrueba
        when(personaService.save(any(PersonaDTO.Request.class))).thenReturn(personaResponsePrueba);

        // CUANDO y ENTONCES: Ejecutamos el POST enviando el objeto convertido a JSON
        mockMvc.perform(post("/api/v1/personas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isCreated()) // Valida el estado 201
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.rut").value("11222333-4"))
                .andExpect(jsonPath("$.nombreCompleto").value("Ada Lovelace"))
                .andExpect(jsonPath("$.correo").value("ada@biblioteca.com"));
    }

    @Test
    @DisplayName("POST Crear (Camino Malo): Retorna 400 Bad Request si fallan las validaciones")
    public void guardar_DatosSonInvalidos_Retorna400() throws Exception {
        // DADO: Forzamos errores dejando el RUN vacío (@NotBlank) y el correo mal escrito (@Email)
        PersonaDTO.Request requestInvalido = new PersonaDTO.Request();
        requestInvalido.setRun(""); // Inválido: Vacío
        requestInvalido.setDvRun("4");
        requestInvalido.setPNombre("Ada");
        requestInvalido.setApPaterno("Lovelace");
        requestInvalido.setCorreo("correoInvalidoSinArroba"); // Inválido: No es formato email
        requestInvalido.setComunaId(1L);
        requestInvalido.setSexoId(1L);
        requestInvalido.setIdRol(1L);

        // CUANDO y ENTONCES: El interceptor @Valid de Spring debería rebotar esto antes de tocar el Service
        mockMvc.perform(post("/api/v1/personas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestInvalido)))
                .andExpect(status().isBadRequest()); // Valida el estado 400

        // Verificación de seguridad: El servicio NUNCA debió ser invocado
        verify(personaService, never()).save(any(PersonaDTO.Request.class));
    }

    // ==========================================
    // TESTS DEL MÉTODO BUSCAR POR ID (GET /api/v1/personas/{id})
    // ==========================================

    @Test
    @DisplayName("GET Buscar por ID (Camino Feliz): Retorna 200 OK si el ID existe")
    public void buscarPorId_Existe_Retorna200() throws Exception {
        // DADO: El servicio encuentra la persona con ID 1 y la retorna
        when(personaService.findById(1L)).thenReturn(personaResponsePrueba);

        // CUANDO y ENTONCES: Hacemos la petición GET pasando el ID en la URL
        mockMvc.perform(get("/api/v1/personas/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // Valida HTTP 200
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.rut").value("11222333-4"))
                .andExpect(jsonPath("$.nombreCompleto").value("Ada Lovelace"));
        
        // Verificamos que el servicio fue consultado con el ID correcto
        verify(personaService, times(1)).findById(1L);
    }

    @Test
    @DisplayName("GET Buscar por ID (Camino Malo): Retorna 404 Not Found si el ID no existe")
    public void buscarPorId_NoExiste_Retorna404() throws Exception {
        // DADO: El servicio lanza una excepción al buscar un ID inexistente (ej: 99)
        when(personaService.findById(99L)).thenThrow(new RuntimeException("Persona no encontrada"));

        // CUANDO y ENTONCES: Tu catch en el controlador debería transformar el error en un 404
        mockMvc.perform(get("/api/v1/personas/{id}", 99L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()); // Valida HTTP 404
        
        verify(personaService, times(1)).findById(99L);
    }

    // ==========================================
    // TESTS DEL MÉTODO ACTUALIZAR (PUT /api/v1/personas/{id})
    // ==========================================

    @Test
    @DisplayName("PUT Actualizar (Camino Feliz): Retorna 200 OK con los datos modificados")
    public void actualizar_IdExisteYDatosSonValidos_Retorna200() throws Exception {
        // DADO
        PersonaDTO.Request requestBody = new PersonaDTO.Request();
        requestBody.setRun("11222333");
        requestBody.setDvRun("4");
        requestBody.setPNombre("Ada Modificado"); 
        requestBody.setApPaterno("Lovelace");
        requestBody.setCorreo("ada.nueva@biblioteca.com"); 
        requestBody.setComunaId(1L);
        requestBody.setSexoId(1L);
        requestBody.setIdRol(1L);

        PersonaDTO.Response respuestaModificada = new PersonaDTO.Response();
        respuestaModificada.setId(1L);
        respuestaModificada.setRut("11222333-4");
        respuestaModificada.setNombreCompleto("Ada Modificado Lovelace");
        respuestaModificada.setCorreo("ada.nueva@biblioteca.com");

        // ATENCIÓN AQUÍ: Usamos updatePersona
        when(personaService.updatePersona(eq(1L), any(PersonaDTO.Request.class))).thenReturn(respuestaModificada);

        // CUANDO y ENTONCES
        mockMvc.perform(put("/api/v1/personas/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isOk()) 
                .andExpect(jsonPath("$.nombreCompleto").value("Ada Modificado Lovelace"))
                .andExpect(jsonPath("$.correo").value("ada.nueva@biblioteca.com"));

        // ATENCIÓN AQUÍ: Usamos updatePersona
        verify(personaService, times(1)).updatePersona(eq(1L), any(PersonaDTO.Request.class));
    }

    @Test
    @DisplayName("PUT Actualizar (Camino Malo - Validación): Retorna 400 Bad Request si los datos fallan validación")
    public void actualizar_DatosSonInvalidos_Retorna400() throws Exception {
        // DADO
        PersonaDTO.Request requestInvalido = new PersonaDTO.Request();
        requestInvalido.setRun("11222333");
        requestInvalido.setCorreo("correo-invalido"); 

        // CUANDO y ENTONCES
        mockMvc.perform(put("/api/v1/personas/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestInvalido)))
                .andExpect(status().isBadRequest());

        // ATENCIÓN AQUÍ: Usamos updatePersona
        verify(personaService, never()).updatePersona(anyLong(), any(PersonaDTO.Request.class));
    }

    @Test
    @DisplayName("PUT Actualizar (Camino Malo - Inexistente): Retorna 404 Not Found si el ID a actualizar no existe")
    public void actualizar_IdNoExiste_Retorna404() throws Exception {
        // DADO: Preparamos un Request con datos totalmente válidos
        PersonaDTO.Request requestValido = new PersonaDTO.Request();
        requestValido.setRun("11222333");
        requestValido.setDvRun("4");
        requestValido.setPNombre("Ada");
        requestValido.setApPaterno("Lovelace");
        requestValido.setCorreo("ada@biblioteca.com");
        requestValido.setComunaId(1L);
        requestValido.setSexoId(1L);
        requestValido.setIdRol(1L);

        // Simulamos que el Service intentará buscar el ID 99L y lanzará la excepción que programamos
        when(personaService.updatePersona(eq(99L), any(PersonaDTO.Request.class)))
                .thenThrow(new RuntimeException("No existe una persona con el ID: 99"));

        // CUANDO y ENTONCES: Se espera que el controlador maneje el error y retorne 404 Not Found
        mockMvc.perform(put("/api/v1/personas/{id}", 99L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestValido)))
                .andExpect(status().isNotFound()); // Valida HTTP 404

        // Verificamos que el servicio sí intentó hacer la actualización antes de fallar
        verify(personaService, times(1)).updatePersona(eq(99L), any(PersonaDTO.Request.class));
    }

    // ==========================================
    // TESTS DEL MÉTODO ELIMINAR (DELETE /api/v1/personas/{id})
    // ==========================================

    @Test
    @DisplayName("DELETE Eliminar (Camino Feliz): Retorna 204 No Content si se elimina con éxito")
    public void eliminar_IdExiste_Retorna204() throws Exception {
        // DADO: El método delete en un Service suele ser 'void'. 
        // Le indicamos a Mockito que haga "nada" (doNothing) cuando se llame con el ID 1.
        doNothing().when(personaService).delete(1L);

        // CUANDO y ENTONCES: Las buenas prácticas dictan que un DELETE exitoso responde 204 (Sin Cuerpo)
        mockMvc.perform(delete("/api/v1/personas/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent()); // Valida HTTP 204

        verify(personaService, times(1)).delete(1L);
    }

    @Test
    @DisplayName("DELETE Eliminar (Camino Malo): Retorna 404 Not Found si el ID no existe")
    public void eliminar_IdNoExiste_Retorna404() throws Exception {
        // DADO: El servicio lanza una excepción si intentas borrar un ID inexistente (ej: 99)
        doThrow(new RuntimeException("No se puede eliminar: ID no encontrado"))
                .when(personaService).delete(99L);

        // CUANDO y ENTONCES
        mockMvc.perform(delete("/api/v1/personas/{id}", 99L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()); // Valida HTTP 404

        verify(personaService, times(1)).delete(99L);
    }

    // ==========================================
    // TESTS DEL MÉTODO BUSCAR POR RUN (GET /api/v1/personas/run/{run})
    // ==========================================

    @Test
    @DisplayName("GET Buscar por RUN (Camino Feliz): Retorna 200 OK si el RUN existe")
    public void buscarPorRun_CuandoRunExiste_Retorna200() throws Exception {
        // DADO: El servicio encuentra a la persona por su RUN y retorna el DTO Response
        when(personaService.findByRun("11222333")).thenReturn(personaResponsePrueba);

        // CUANDO y ENTONCES: Hacemos la petición GET pasando el RUN en la URL
        mockMvc.perform(get("/api/v1/personas/run/{run}", "11222333")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // Valida HTTP 200
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.rut").value("11222333-4"))
                .andExpect(jsonPath("$.nombreCompleto").value("Ada Lovelace"));
        
        // Verificamos que el servicio fue consultado con el RUN exacto
        verify(personaService, times(1)).findByRun("11222333");
    }

    @Test
    @DisplayName("GET Buscar por RUN (Camino Malo): Retorna 404 Not Found si el RUN no existe")
    public void buscarPorRun_CuandoRunNoExiste_Retorna404() throws Exception {
        // DADO: El servicio lanza una excepción al buscar un RUN que no está registrado
        when(personaService.findByRun("99999999")).thenThrow(new RuntimeException("Persona no encontrada con RUN: 99999999"));

        // CUANDO y ENTONCES: El bloque catch o manejador de errores del controlador debe devolver 404
        mockMvc.perform(get("/api/v1/personas/run/{run}", "99999999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()); // Valida HTTP 404
        
        verify(personaService, times(1)).findByRun("99999999");
    }

    // ==========================================
    // 1. TESTS: BUSCAR POR APELLIDO (GET /api/v1/personas/apellido/{apellido})
    // ==========================================

    @Test
    @DisplayName("GET Buscar por Apellido (Camino Feliz): Retorna 200 OK con la lista de personas")
    public void buscarPorApellido_CuandoExistenCoincidencias_Retorna200YLista() throws Exception {
        // DADO: El servicio retorna una lista con nuestra persona de prueba al buscar "Lovelace"
        when(personaService.findByApPaterno("Lovelace")).thenReturn(List.of(personaResponsePrueba));

        // CUANDO y ENTONCES
        mockMvc.perform(get("/api/v1/personas/apellido/{apellido}", "Lovelace")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1))) // Verifica que venga 1 elemento en el array JSON
                .andExpect(jsonPath("$[0].nombreCompleto").value("Ada Lovelace"));

        verify(personaService, times(1)).findByApPaterno("Lovelace");
    }

    @Test
    @DisplayName("GET Buscar por Apellido (Camino Alternativo): Retorna 200 OK con lista vacía si nadie tiene ese apellido")
    public void buscarPorApellido_CuandoNoHayCoincidencias_Retorna200YListaVacia() throws Exception {
        // DADO: El servicio retorna una lista vacía para un apellido inexistente
        when(personaService.findByApPaterno("Stark")).thenReturn(List.of());

        // CUANDO y ENTONCES
        mockMvc.perform(get("/api/v1/personas/apellido/{apellido}", "Stark")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent()); // Y borra la validación del jsonPath del tamaño

        verify(personaService, times(1)).findByApPaterno("Stark");
    }

    // ==========================================
    // 2. TESTS: BUSCAR POR NOMBRE DE COMUNA (GET /api/v1/personas/comuna/{nombreComuna})
    // ==========================================

    @Test
    @DisplayName("GET Buscar por Nombre Comuna (Camino Feliz): Retorna 200 OK con las personas de esa comuna")
    public void buscarPorNombreComuna_CuandoComunaExisteYTienePersonas_Retorna200YLista() throws Exception {
        // DADO
        when(personaService.findByComunaNombre("Santiago")).thenReturn(List.of(personaResponsePrueba));

        // CUANDO y ENTONCES
        mockMvc.perform(get("/api/v1/personas/comuna")
                .param("nombre", "Santiago") 
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // ¡No olvides tus validaciones!
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].nombreCompleto").value("Ada Lovelace"));

        verify(personaService, times(1)).findByComunaNombre("Santiago");
    }

    @Test
    @DisplayName("GET Buscar por Nombre Comuna (Camino Alternativo): Retorna 200 OK con lista vacía si no hay personas")
    public void buscarPorNombreComuna_CuandoComunaNoTienePersonas_Retorna200YListaVacia() throws Exception {
        // DADO
        when(personaService.findByComunaNombre("Macul")).thenReturn(List.of());

        // CUANDO y ENTONCES
        mockMvc.perform(get("/api/v1/personas/comuna")
                .param("nombre", "Macul") 
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // Esperamos 200 OK
                .andExpect(jsonPath("$", hasSize(0))); // Y una lista de tamaño 0

        verify(personaService, times(1)).findByComunaNombre("Macul");
    }

    // ==========================================
    // 3. TESTS: BUSCAR POR ROL (GET /api/v1/personas/rol/{rolId})
    // ==========================================

    @Test
    @DisplayName("GET Buscar por Rol (Camino Feliz): Retorna 200 OK con las personas asignadas a ese Rol")
    public void buscarPorRol_CuandoRolExisteYTienePersonas_Retorna200YLista() throws Exception {
        when(rolService.findByIdOrThrow(anyLong())).thenReturn(new RolDTO.Response());
        // Usamos any(RolDTO.Response.class) en lugar de 1L
        when(personaService.findByRol(any(RolDTO.Response.class))).thenReturn(List.of(personaResponsePrueba));

        // CUANDO y ENTONCES (La URL sigue recibiendo el ID numérico 1L)
        mockMvc.perform(get("/api/v1/personas/rol/{rolId}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].nombreCompleto").value("Ada Lovelace"));

        // Verificamos con el matcher del objeto correspondiente
        verify(personaService, times(1)).findByRol(any(RolDTO.Response.class));
    }

    @Test
    @DisplayName("GET Buscar por Rol (Camino Alternativo): Retorna 200 OK con lista vacía si ningún usuario tiene el rol")
    public void buscarPorRol_CuandoRolNoTienePersonas_Retorna200YListaVacia() throws Exception {
        when(rolService.findByIdOrThrow(anyLong())).thenReturn(new RolDTO.Response());
        // Usamos any(RolDTO.Response.class) en lugar de 3L
        when(personaService.findByRol(any(RolDTO.Response.class))).thenReturn(List.of());

        // CUANDO y ENTONCES
        mockMvc.perform(get("/api/v1/personas/rol/{rolId}", 3L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

        // Verificamos con el matcher del objeto correspondiente
        verify(personaService, times(1)).findByRol(any(RolDTO.Response.class));
    }
}
