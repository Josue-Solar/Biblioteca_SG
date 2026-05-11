package com.biblioteca.prestamo.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.biblioteca.prestamo.dto.EjemplarDTO;

@FeignClient(name = "ejemplares", url = "${ms.ejemplares.url}", configuration = FeignClientConfig.class)
public interface EjemplarClient {

    @GetMapping("/api/v1/ejemplares/id:{id}")       // ("/api/v1/ejemplares") ("/id:{id}")
    EjemplarDTO obtenerPorId(@PathVariable Long id);


}
