package com.example.edicion.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class EdicionEditorialDTO {
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Request{
        
        private Long edicionId;
        private Long editorialId; 
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Response{
        private Long edicionId;
        private Long editorialId; 
    }
}
