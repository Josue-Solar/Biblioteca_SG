package com.example.ejemplar.model;

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
@Table(name = "Ejemplar_editorial")
public class EjemplarEditorial {

    @Id
    @Column(nullable = false, name = "Ejemplar_id")
    long ejemplarId;

    @Id
    @Column(nullable = false, name = "Editorial_id")
    long editorialId;
}
