package com.biblioteca.reserva.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.biblioteca.reserva.client.EjemplarClient;
import com.biblioteca.reserva.client.PersonaClient;
import com.biblioteca.reserva.dto.EjemplarDTO;
import com.biblioteca.reserva.dto.PersonaDTO;
import com.biblioteca.reserva.dto.ReservaDTO;
import com.biblioteca.reserva.exception.ResourceNotFoundException;
import com.biblioteca.reserva.model.EstadoReserva;
import com.biblioteca.reserva.model.Reserva;
import com.biblioteca.reserva.repository.ReservaRepository;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Transactional
@Service
public class ReservaService {

    @Autowired
    private ReservaRepository reservaRepository;

    //INYECTAMOS LOS FEIGN CLIENTS
    @Autowired
    private PersonaClient personaClient;

    @Autowired
    private EjemplarClient ejemplarClient;

    //de entidad a dto
    private ReservaDTO.Response mapToResponse(Reserva reserva) {
        ReservaDTO.Response response = new ReservaDTO.Response();
        response.setId(reserva.getId());
        response.setFechaReserva(reserva.getFechaReserva());
        response.setFechaExpiracion(reserva.getFechaExpiracion());
        response.setEstado(reserva.getEstado().name());
        response.setFechaRetiro(reserva.getFechaRetiro());
        // Traemos la información desde los otros microservicios
        try {
            PersonaDTO persona = personaClient.obtenerPorId(reserva.getPersonaId());
            response.setPersona(persona);
            EjemplarDTO ejemplar = ejemplarClient.obtenerPorId(reserva.getEjemplarId());
            response.setEjemplar(ejemplar);
        } catch (Exception e) {
            log.warn("No se pudo obtener el detalle de Persona/Ejemplar para la reserva ID: {}", 
                            reserva.getId());
            // Si el otro MS está caído, devolvemos la reserva igual pero sin los datos enriquecidos.
        }

        return response;
    }

    //ver reservas
    public List<ReservaDTO.Response> findAll(){
        return reservaRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // buscar por id 
    public ReservaDTO.Response findByIdOrThrow(Long id) {
    return reservaRepository.findById(id)
        .map(this::mapToResponse)
        .orElseThrow(() -> new ResourceNotFoundException("Reserva no encontrada con ID: " + id));
    }

    //crear
    public ReservaDTO.Response crear(ReservaDTO.Request request) {
    //Validar que la persona no tenga ya una reserva ACTIVA para este mismo ejemplar
        if (reservaRepository.existsByPersonaIdAndEjemplarIdAndEstado(
            request.getPersonaId(), request.getEjemplarId(), EstadoReserva.ACTIVA)) {
        throw new IllegalArgumentException("La persona ya tiene una reserva activa para este ejemplar.");
        }
    // Validar si el ejemplar ya está reservado por alguien más
        if (reservaRepository.existsByEjemplarIdAndEstado(request.getEjemplarId(), EstadoReserva.ACTIVA)) {
        throw new IllegalArgumentException("Este ejemplar ya se encuentra reservado por otro usuario.");
        }
    //Validar que la persona y el ejemplar existan en los otros microservicios (Feign)
    validarExistencia(request.getPersonaId(), request.getEjemplarId());//esto se comenta para probarlo solo
    //Convertir el Request (DTO) a Entidad para guardarlo en la BD
    Reserva reserva = new Reserva();
    reserva.setPersonaId(request.getPersonaId());
    reserva.setEjemplarId(request.getEjemplarId());
    // Reglas automáticas de fechas al crear una reserva
    reserva.setFechaReserva(LocalDate.now()); 
    reserva.setFechaExpiracion(LocalDate.now().plusDays(3)); // 3 días para retirar el libro
    reserva.setEstado(EstadoReserva.ACTIVA); // Toda reserva nace ACTIVA
    reserva.setFechaRetiro(null); // Aún no se ha retirado
    Reserva guardada = reservaRepository.save(reserva);
    // 5. Devolver la respuesta enriquecida (con datos de Persona y Ejemplar)
    return mapToResponse(guardada);
    }

    // Método auxiliar de apoyo para validar via Feign 
    private void validarExistencia(Long personaId, Long ejemplarId) {
    try {
        personaClient.obtenerPorId(personaId);
        ejemplarClient.obtenerPorId(ejemplarId);
    } catch (Exception e) {
        throw new ResourceNotFoundException
            ("No se pudo verificar la existencia de la Persona o el Ejemplar. Verifique los IDs.");
    }
    }

    //updatear por id
    public ReservaDTO.Response updateReserva(Long id, ReservaDTO.Request request) {
    //Buscar la reserva o lanzar error
    Reserva reserva = reservaRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Reserva no encontrada con ID: " + id));
    //Solo validamos en otros microservicios si cambiaron los IDs (para no llamar a Feign en vano)
    if (!reserva.getPersonaId().equals(request.getPersonaId()) || 
        !reserva.getEjemplarId().equals(request.getEjemplarId())) {
        validarExistencia(request.getPersonaId(), request.getEjemplarId()); //esto se comenta para probarlo solo
        }
    //Actualizamos los campos que vienen en el Request
    reserva.setPersonaId(request.getPersonaId());
    reserva.setEjemplarId(request.getEjemplarId());
    // Guardar y mapear a Response
    Reserva actualizada = reservaRepository.save(reserva);
    return mapToResponse(actualizada);
    }

    //borrar
    public void delete(Long id){
        reservaRepository.deleteById(id);
    }

    // metodos no basicos
    //buscar reservas por persona id
    public List<ReservaDTO.Response> findByPersonaId(Long personaId) {
        return reservaRepository.findByPersonaId(personaId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    //buscar reservas por ejemplar id
    public List<ReservaDTO.Response> findByEjemplarId(Long ejemplarId){
        return reservaRepository.findByEjemplarId(ejemplarId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // Buscar reservas activas (no retirados aún)
    public List<ReservaDTO.Response> findReservasActivas() {
        return reservaRepository.findByEstado(EstadoReserva.ACTIVA)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    //registrar retiro libro
    public ReservaDTO.Response registrarRetiro(Long id) {
    //Buscar la reserva existente o lanzar 404 si no existe
    Reserva reserva = reservaRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Reserva no encontrada con ID: " + id));
    //Validar que la reserva realmente esté ACTIVA antes de poder retirarla
    if (reserva.getEstado() != EstadoReserva.ACTIVA) {
        throw new IllegalArgumentException("No se puede registrar el retiro. La reserva no está ACTIVA.");
        }
    //Modificar los atributos de la entidad (Reglas de negocio automáticas)
    reserva.setEstado(EstadoReserva.COMPLETADA);
    reserva.setFechaRetiro(LocalDate.now()); // Registramos el día de hoy como fecha de retiro
    //Guardar los cambios en la base de datos
    Reserva actualizada = reservaRepository.save(reserva);
    // Retornar la respuesta enriquecida
    return mapToResponse(actualizada);
    }

    //cancelar reserva
    public ReservaDTO.Response cancelarReserva(Long id) {
    //Buscar la reserva existente o lanzar 404 si no existe
    Reserva reserva = reservaRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Reserva no encontrada con ID: " + id));
    //Validar que la reserva esté ACTIVA (no se puede cancelar algo ya retirado o ya cancelado)
    if (reserva.getEstado() != EstadoReserva.ACTIVA) {
        throw new IllegalArgumentException("No se puede cancelar. La reserva ya no está ACTIVA.");
        }
    //Cambiar el estado del sistema
    reserva.setEstado(EstadoReserva.CANCELADA); 
    //La fechaRetiro se mantiene en null porque el libro nunca salio
    Reserva actualizada = reservaRepository.save(reserva);
    // Retornar la respuesta enriquecida
    return mapToResponse(actualizada);
    }

    // limpiar las reservas expiradas
    public void procesarReservasExpiradas() {
    List<Reserva> expiradas = reservaRepository.findReservasExpiradas(EstadoReserva.ACTIVA);
    expiradas.forEach(reserva -> {
        reserva.setEstado(EstadoReserva.CANCELADA);
        reservaRepository.save(reserva);
    });
    log.info("Se han cancelado {} reservas expiradas.", expiradas.size());
}
    
    

}
