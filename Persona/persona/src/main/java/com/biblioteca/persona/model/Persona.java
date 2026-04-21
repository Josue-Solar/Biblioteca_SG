package com.biblioteca.persona.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="persona")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Persona {
    //comentario agregando los nombres de cada atributo para la BD(name="xxx"), creado atributo sexo, y hay q hacer las clases sexo y rol
    //con sus repo, serv y control

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name="num_run",unique = true, length = 9, nullable = false)
    private String run;

    @Column(name="dv_run",nullable = false, length = 1)
    private String dvRun;

    @Column(name="pnombre",nullable = false, length = 50)
    private String pNombre;

    @Column(name="snombre",length = 50)
    private String sNombre;

    @Column(name="apellido_paterno",nullable = false, length = 50)
    private String apPaterno;

    @Column(name="apellido_materno",length = 50)
    private String apMaterno;

    @Column(name="nombre_direccion",length = 100)
    private String direccion;

    @Column(name="correo",nullable = false, unique = true, length = 100)
    private String correo;

    @Column(name="sexo_id", nullable = false)
    private Long sexo;

    @Column(name="rol_id",nullable = false, length = 20)
    private String rol; //q cargo tiene como empleado o si es usuario

    //@Column(nullable = false)
    //private Comuna comuna; //foreing key de comuna

}
