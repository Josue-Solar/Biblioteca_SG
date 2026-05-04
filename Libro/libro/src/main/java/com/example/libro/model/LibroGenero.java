package com.example.libro.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Libro_Genero")
@IdClass(LibroGeneroID.class)
public class LibroGenero {

    @Id
    @Column(nullable = false, name = "GENERO_id")
    private Long generoId;
    
    @Id
    @Column(unique = true, name = "LIBRO_isbn")
    private Long libroIsbn;  

}
