package com.biblioteca.prestamo.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="prestamo")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Prestamo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message=   "La fecha de inicio es obligatoria")
    @PastOrPresent(message="La fecha de inicio no puede ser futura")
    @Column(name="fecha_inicio",nullable = false)
    private LocalDate fechaInicio;

    @NotNull(message = "La fecha limite es obligatoria")
    @Future(message = "La fecha limite debe ser futura")
    @Column(name="fecha_fin",nullable = false)
    private LocalDate fechaFin; //fecha limite de devolucion


    @Future(message = "La fecha de devolucion debe ser futura")
    @Column(name="fecha_devolucion")
    private LocalDate fechaDevolucion; //cuando lo devolvio

    @Transient
    public boolean isAtrasado() {   //no se guarda en bd, se calcula 
        return fechaDevolucion == null && LocalDate.now().isAfter(fechaFin);
    }
    
    // claves externas
    
    //nombres persona: String run, String dvRun; String pNombre; String apPaterno; String correo;

    @NotNull(message = "El id de la persona es obligatorio")
    @Positive(message = "El ID de la persona debe ser un numero positivo")
    @Column(name="PERSONA_id",nullable = false)
    private Long personaId;

    //("/api/v1/ejemplares") @GetMapping("/id:{id}")
    // nombres ejemplar: long libroIsbn;

    @NotNull(message = "El id del ejemplar del libro es obligatorio")
    @Positive(message = "El ID del ejemplar debe ser un numero positivo")
    @Column(name="EJEMPLAR_id",nullable = false)
    private Long ejemplarId;

}
