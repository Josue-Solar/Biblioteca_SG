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
    //comentario

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, length = 9, nullable = false)
    private String run;

    @Column(nullable = false, length = 1)
    private String dvRun;

    @Column(nullable = false, length = 50)
    private String pNombre;

    @Column(length = 50)
    private String sNombre;

    @Column(nullable = false, length = 50)
    private String apPaterno;

    @Column(length = 50)
    private String apMaterno;

    @Column(length = 100)
    private String direccion;

    @Column(nullable = false, unique = true, length = 100)
    private String correo;

    @Column(nullable = false, length = 20)
    private String rol; //q cargo tiene como empleado o si es usuario

    //@Column(nullable = false)
    //private Comuna comuna; //foreing key de comuna

}
