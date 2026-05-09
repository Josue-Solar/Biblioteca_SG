package com.biblioteca.autor.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="autor")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Autor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El primer nombre es obligatorio")
    @Size(max = 50)
    @Column(name="pnombre",nullable = false, length = 50)
    private String pNombre;

    @Size(max = 50)
    @Column(name="snombre",length = 50)
    private String sNombre;

    @NotBlank(message = "El primero apellido es obligatorio")
    @Size(max = 50)
    @Column(name="apellido_paterno",nullable = false, length = 50)
    private String apPaterno;

    @Size(max = 50)
    @Column(name="apellido_materno",length = 50)
    private String apMaterno;

}
