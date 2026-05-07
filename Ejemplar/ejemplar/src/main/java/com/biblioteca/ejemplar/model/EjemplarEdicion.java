package com.biblioteca.ejemplar.model;

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
@Table(name = "Ejemplar_edicion")
@IdClass(EjemplarEdicionID.class)
public class EjemplarEdicion {

    @Id
    @Column(nullable = false, name = "Ejemplar_id")
    long ejemplarId;

    @Id
    @Column(nullable = false, name = "Edicion_id")
    long edicionId;
}
