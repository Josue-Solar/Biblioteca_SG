package com.example.libro.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Libro")
public class Libro {

    @Id
    @Column(unique = true, name = "isbn")
    private long isbn;

    @NotBlank(message = "El nombre no puede estar vacío")
    @Column(nullable = false, length = 120, name = "nombre")
    private String nombre;

}
