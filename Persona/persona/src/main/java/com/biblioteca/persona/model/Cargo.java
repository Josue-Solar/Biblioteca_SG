package com.biblioteca.persona.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "cargo")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cargo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre del cargo es obligatorio")
    @Size(max = 30)
    @Column(nullable = false, unique = true, length = 30)
    private String nombre; // "ADMIN", "EMPLEADO", "USUARIO"

}
