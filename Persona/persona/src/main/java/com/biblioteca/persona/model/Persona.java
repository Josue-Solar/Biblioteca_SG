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

    @NotBlank(message = "El RUN no puede estar vacío")
    @Size(max = 9, message = "El RUN no puede tener más de 9 caracteres")
    @Column(name="num_run",unique = true, length = 9, nullable = false)
    private String run;

    @NotBlank(message = "El dígito verificador es obligatorio")
    @Size(max = 1)
    @Column(name="dv_run",nullable = false, length = 1)
    private String dvRun;

    @NotBlank(message = "El primer nombre es obligatorio")
    @Size(max = 50)
    @Column(name="pnombre",nullable = false, length = 50)
    private String pNombre;

    @Size(max = 50)
    @Column(name="snombre",length = 50)
    private String sNombre;

    @NotBlank(message = "El apellido paterno es obligatorio")
    @Size(max = 50)
    @Column(name="apellido_paterno",nullable = false, length = 50)
    private String apPaterno;

    @Size(max = 50)
    @Column(name="apellido_materno",length = 50)
    private String apMaterno;

    @Size(max = 100)
    @Column(name="nombre_direccion",length = 100)
    private String direccion;

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "Debe ser un formato de correo válido")
    @Size(max = 100)
    @Column(name="correo",nullable = false, unique = true, length = 100)
    private String correo;

    //foreign keys
    // diferente microservicio
    @NotNull(message = "Debe indicar la comuna")
    @Column(name="COMUNA_id",nullable = false)
    private Long comunaId;  
   
    //entidades dentro de este mismo microservicio
    @NotNull(message = "Debe indicar el sexo")
    @ManyToOne
    @JoinColumn(name="SEXO_id", nullable = false)
    private Sexo sexo;

    @NotNull(message = "Debe indicar el cargo")
    @ManyToOne
    @JoinColumn (name="CARGO_id", nullable= false)
    private Cargo cargo; //q cargo tiene como empleado o si es usuario

}
