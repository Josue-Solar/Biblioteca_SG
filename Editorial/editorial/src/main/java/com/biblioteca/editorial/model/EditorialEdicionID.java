package com.biblioteca.editorial.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EditorialEdicionID implements Serializable{
    private Long editorialId;
    private Long edicionId;
}
