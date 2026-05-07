package com.example.libro.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.libro.config.FeignConfig;
import com.example.libro.dto.EjemplarDTO;

@FeignClient(name = "ejemplar", url = "${ejemplar.url}", configuration = FeignConfig.class)
public interface EjemplarClient {
    @GetMapping("/api/v1/ejemplares/porISBN/{isbn}")
    List<EjemplarDTO> getAllByISBN(@PathVariable Long isbn);
}
