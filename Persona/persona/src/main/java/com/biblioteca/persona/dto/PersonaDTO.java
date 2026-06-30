package com.biblioteca.persona.dto;

import com.biblioteca.persona.model.Rol;
import com.biblioteca.persona.model.Sexo;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class PersonaDTO {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        @NotBlank(message = "El RUN no puede estar vacío")
        @Size(max = 9, message = "El RUN no puede tener más de 9 caracteres")
        private String run;

        @NotBlank(message = "El dígito verificador es obligatorio")
        @Size(max = 1)
        private String dvRun;

        @NotBlank(message = "El primer nombre es obligatorio")
        @Size(max = 50)
        @JsonProperty("pNombre")
        private String pNombre;

        @Size(max = 50)
        @JsonProperty("sNombre")
        private String sNombre;

        @NotBlank(message = "El apellido paterno es obligatorio")
        @Size(max = 50)
        private String apPaterno;

        @Size(max = 50)
        private String apMaterno;

        @Size(max = 100)
        private String direccion;

        @NotBlank(message = "El correo es obligatorio")
        @Email(message = "Debe ser un formato de correo válido")
        @Size(max = 100)
        private String correo;

        //foreign keys
        // diferente microservicio
        @NotNull(message = "Debe indicar la comuna")
        private Long comunaId;  
    
        //entidades dentro de este mismo microservicio
        @NotNull(message = "Debe indicar el sexo")
        private Long sexoId;

        @NotNull(message = "Debe indicar el rol")
        private Long idRol; //q rol tiene como empleado o si es usuario

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response{
        private Long id;
        private String nombreCompleto;
        private String rut;
        private String correo;
        private ComunaDTO comuna;
        private SexoDTO.Response sexo;
        private RolDTO.Response rol;
    }   
}
