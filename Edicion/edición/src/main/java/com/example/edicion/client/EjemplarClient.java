package com.example.edicion.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.edicion.dto.EjemplarDTO;

@FeignClient(name = "ejemplar", url = "${ejemplar.url}", configuration = FeignConfig.class)
public interface EjemplarClient {
    @GetMapping("/api/v1/ejemplares/porEdicion/{edicionId}")
    List<EjemplarDTO> getAllByEdicionId(@PathVariable Long edicionId);
}
