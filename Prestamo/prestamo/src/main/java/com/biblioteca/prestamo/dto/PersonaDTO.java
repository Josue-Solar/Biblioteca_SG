package com.biblioteca.prestamo.dto;

import lombok.Data;

@Data
public class PersonaDTO {

    private Long id;
    private String nombreCompleto; //nombre y apellido
    private String rut;  //run (-) dv
    private String correo;

}
