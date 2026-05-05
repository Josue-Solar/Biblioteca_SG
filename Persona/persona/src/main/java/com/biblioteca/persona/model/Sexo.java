package com.biblioteca.persona.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "sexo")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Sexo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 20)
    @Column(nullable = false, unique = true, length = 20)
    private String nombre; // "Masculino", "Femenino"

}
