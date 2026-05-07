package com.biblioteca.reserva.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.biblioteca.reserva.config.FeignConfig;
import com.biblioteca.reserva.dto.EjemplarDTO;

@FeignClient(name = "ejemplar", url = "http://localhost:8083", configuration = FeignConfig.class)
public interface EjemplarClient {
    @GetMapping("/api/v1/ejemplares/traeNombre/{isbn}")
    EjemplarDTO getLibro(@PathVariable Long isbn);
}
