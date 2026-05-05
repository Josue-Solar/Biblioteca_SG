package com.example.edicion.model;

import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Edición")
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Edicion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @NotNull(message = "Nombre no puede ser vacio")
    @NotEmpty(message = "Nombre no puede ser vacio")
    @Column(nullable = false, length = 50, name = "nombre")
    String nombre;

    @Column(nullable = false, name = "fecha_publicacion")
    Date fechaPublicacion;
}
