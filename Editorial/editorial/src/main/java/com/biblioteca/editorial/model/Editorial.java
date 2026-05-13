package com.biblioteca.editorial.model;

import jakarta.persistence.*;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Editorial")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Editorial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "El nombre no puede estar vacío")
    @Column(nullable = false, length = 40, name = "nombre")
    private String nombre;

}
