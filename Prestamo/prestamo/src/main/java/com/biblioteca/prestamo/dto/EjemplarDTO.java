package com.biblioteca.prestamo.dto;

import lombok.Data;


@Data
public class EjemplarDTO {

    private Long id;
    
    private Long libroIsbn;

    // Nota: Si en el futuro se agrega el título del libro 
    // solo tendrías que agregar 'private String titulo' 
}