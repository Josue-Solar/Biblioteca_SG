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
@Table(name = "Libro_Genero")
public class LibroGenero {

    @Id
    @Column(nullable = false, name = "Genero_id")
    long generoId;
    
    @Id
    @Column(unique = true, name = "Libro_isbn")
    long libroIsbn;  

}
