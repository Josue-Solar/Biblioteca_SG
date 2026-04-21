package com.example.libro.model;

import jakarta.persistence.Column;

public class Libro_genero {

    @Column(nullable = false, name = "Genero_id")
    long generoId;

    @Column(unique = true, name = "Libro_isbn")
    long libroIsbn;  

}
