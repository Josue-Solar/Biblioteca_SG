package com.example.libro.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Libro")
public class Libro {

    @Id
    @Column(unique = true, name = "isbn")
    Long isbn;

    @Column(nullable = false, length = 120, name = "nombre")
    String nombre;

}
