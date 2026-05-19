package com.example.edicion.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Edicion")
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Edicion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "El nombre no puede estar vacío.")
    @Column(nullable = false, length = 50, name = "nombre")
    private String nombre;

    @NotBlank(message = "Debe ingresar un año de publicación.")
    @Column(nullable = false, name = "annio_publicacion")
    private int annioPublicacion;
}
