package com.biblioteca.reserva.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Reserva")
public class Reserva {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(precision = 10)
    private Long id;

    @Column(name = "PERSONA_id", unique = true, nullable = false, precision = 10)
    private Long personaID;

    @Column(name = "EJEMPLAR_id", unique = true, nullable = false, precision = 10)
    private Long ejemplarID;
}
