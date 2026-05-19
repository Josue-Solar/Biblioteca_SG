package com.example.edicion.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EdicionEditorialID implements Serializable{
    private long editorialId;
    private long edicionId;

}
