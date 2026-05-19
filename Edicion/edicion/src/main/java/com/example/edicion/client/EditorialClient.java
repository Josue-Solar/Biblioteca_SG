package com.example.edicion.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.edicion.dto.clientDTO.editorialclient.EditorialDTO;

@FeignClient(name = "editorial", url = "${editorial.url}", configuration = FeignConfig.class)
public interface EditorialClient {
    @GetMapping("/api/v1/editoriales/{id}")
    EditorialDTO buscarPorId(@PathVariable("id") long id);
}
