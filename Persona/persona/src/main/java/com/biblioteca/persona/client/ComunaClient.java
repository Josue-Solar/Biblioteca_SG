package com.biblioteca.persona.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.biblioteca.persona.config.FeignConfig;
import com.biblioteca.persona.dto.ComunaDTO;

@FeignClient(name = "comuna", url = "${comuna.url}", configuration = FeignConfig.class)
public interface ComunaClient {
    @GetMapping("/api/v1/comunas/nombre/{nombre}")
    ComunaDTO buscarPorNombre(@PathVariable String nombre);

    @GetMapping("/api/v1/comunas/id/{id}")
    ComunaDTO buscarPorId(@PathVariable("id") Long id);
}