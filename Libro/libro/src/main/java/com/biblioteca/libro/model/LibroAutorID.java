package com.biblioteca.libro.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LibroAutorID implements Serializable{
    private Long autorId;
    private Long libroIsbn;
}
