package com.biblioteca.libro.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LibroGeneroID implements Serializable{
    private Long generoId;
    private Long libroIsbn;
}
