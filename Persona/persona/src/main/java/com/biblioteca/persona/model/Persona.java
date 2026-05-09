package com.biblioteca.persona.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="persona")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Persona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 9)
    @Column(name="num_run",unique = true, length = 9, nullable = false)
    private String run;

    @NotBlank
    @Size(max = 1)
    @Column(name="dv_run",nullable = false, length = 1)
    private String dvRun;

    @NotBlank
    @Size(max = 50)
    @Column(name="pnombre",nullable = false, length = 50)
    private String pNombre;

    @Size(max = 50)
    @Column(name="snombre",length = 50)
    private String sNombre;

    @NotBlank
    @Size(max = 50)
    @Column(name="apellido_paterno",nullable = false, length = 50)
    private String apPaterno;

    @Size(max = 50)
    @Column(name="apellido_materno",length = 50)
    private String apMaterno;

    @Size(max = 100)
    @Column(name="nombre_direccion",length = 100)
    private String direccion;

    @NotBlank
    @Email
    @Size(max = 100)
    @Column(name="correo",nullable = false, unique = true, length = 100)
    private String correo;

    //foreign keys
    // diferente microservicio
    @NotNull
    @Column(name="COMUNA_id",nullable = false)
    private Long comunaId;  
   
    //entidades dentro de este mismo microservicio
    @NotNull
    @ManyToOne
    @JoinColumn(name="SEXO_id", nullable = false)
    private Sexo sexo;

    @NotNull
    @ManyToOne
    @JoinColumn (name="ROL_id", nullable= false)
    private Rol rol; //q rol tiene como empleado o si es usuario

    public String getNombreYApellido(){
        return this.getPNombre() + " " + this.getApPaterno();
    }

    public String getRut(){
        return this.getRun() + this.getDvRun();
    }

}
