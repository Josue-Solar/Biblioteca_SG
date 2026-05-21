package com.biblioteca.reserva.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="reserva")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    //FKs
    
    @NotNull(message = "El id de la persona es obligatorio")
    @Positive(message = "El ID de la persona debe ser un numero positivo")
    @Column(name="PERSONA_id", nullable = false)
    private Long personaId;

    @NotNull(message = "El id del ejemplar es obligatorio")
    @Positive(message = "El ID del ejemplar debe ser un numero positivo")
    @Column(name="EJEMPLAR_id", nullable = false)
    private Long ejemplarId;

    //atributos extras
    @Column(name= "fecha_reserva", nullable = false)
    private LocalDate fechaReserva;

    @Column(name="fecha_expiracion",nullable = false)
    private LocalDate fechaExpiracion;

    @Enumerated(EnumType.STRING)
    @Column(name="estado",nullable = false, length = 20)
    private EstadoReserva estado;  // "ACTIVA", "COMPLETADA", "CANCELADA", "EXPIRADA"

    // cuando se completo la reserva (retiro el libro)
    @Column(name="fecha_retiro")
    private LocalDate fechaRetiro;

}
