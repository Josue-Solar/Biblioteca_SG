package com.example.libro.model;

import jakarta.persistence.Column;



public class Libro_autor {

    @Column(unique = true, name = "Libro_isbn")
    long libroIsbn;

    @Column(nullable = false, name = "Autor_id")
    long autorId;

}
