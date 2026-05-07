package com.biblioteca.ejemplar.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EjemplarEditorialID implements Serializable{
    private long ejemplarId;
    private long editorialId;
}
