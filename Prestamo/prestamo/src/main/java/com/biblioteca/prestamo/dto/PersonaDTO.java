package com.biblioteca.prestamo.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class PersonaDTO {

    private Long id;
    
    // @JsonIgnore hace que Feign pueda atrapar el dato, pero evita que 
    // salga en la respuesta final de tu API como un campo suelto.
    @JsonIgnore
    private String run;
    
    @JsonIgnore
    private String dvRun;
    
    @JsonIgnore
    private String pNombre;
    
    @JsonIgnore
    private String apPaterno;
    
    private String correo;

    // DATOS PROCESADOS (Lo que realmente verá el usuario)   
    /**
     * Automáticamente creará el campo "rutCompleto" en el JSON final.
     * Ejemplo: "12345678-9"
     */
    public String getRutCompleto() {
        if (run != null && dvRun != null) {
            return run + "-" + dvRun;
        }
        return null;
    }

    public String getNombreCompleto() {
        if (pNombre != null && apPaterno != null) {
            return pNombre + " " + apPaterno;
        }
        return null;
    }

}
