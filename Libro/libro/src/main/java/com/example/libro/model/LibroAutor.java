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
@Table(name = "Autor_Libro")
@IdClass(LibroAutorID.class)
public class LibroAutor {

    @Id
    @Column(nullable = false, name = "AUTOR_id")
    private Long autorId;

    @Id
    @Column(unique = true, name = "LIBRO_isbn")
    private Long libroIsbn;
}
