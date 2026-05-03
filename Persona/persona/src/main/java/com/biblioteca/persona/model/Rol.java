package com.biblioteca.persona.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "rol")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Rol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "El nombre del rol es obligatorio")
    @Size(max = 30)
    @Column(nullable = false, unique = true, length = 30)
    private String nombre; // "ADMIN", "EMPLEADO", "USUARIO"

}
