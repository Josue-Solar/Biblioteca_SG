package com.example.libro.model;

import jakarta.persistence.Column;
import jakarta.persistence.Id;

public class Libro {

    @Id
    @Column(unique = true, name = "isbn")
    long isbn;

    @Column(nullable = false, length = 120, name = "nombre")
    String nombre;

}
