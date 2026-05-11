package com.biblioteca.editorial.model;

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
@Table(name = "editorial_edicion")
@IdClass(EditorialEdicionID.class)
public class EditorialEdicion {
    @Id
    @Column(nullable = false, name = "editorial_id")
    private Long editorialId;

    @Id
    @Column(nullable = false, name = "edicion_id")
    private Long edicionId;
}
