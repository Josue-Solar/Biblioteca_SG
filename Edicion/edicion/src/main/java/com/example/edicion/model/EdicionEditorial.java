package com.example.edicion.model;

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
@Table(name = "Editorial_Edicion")
@IdClass(EdicionEditorialID.class)
public class EdicionEditorial {
    @Id
    @Column(nullable = false, name = "EDITORIAL_id")
    private long editorialId;
    
    @Id
    @Column(nullable = false, name = "EDICION_id")
    private long edicionId;  
}
