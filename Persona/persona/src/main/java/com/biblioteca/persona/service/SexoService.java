package com.biblioteca.persona.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.biblioteca.persona.dto.SexoDTO;
import com.biblioteca.persona.model.Sexo;
import com.biblioteca.persona.repository.SexoRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class SexoService {

    @Autowired
    private SexoRepository sexoRepository;

    // Ver todos los sexos
    public List<SexoDTO.Response> findAll() {
        return sexoRepository.findAll().stream().map(this::mapToResponse).toList();
    }

    // Buscar por ID
    public SexoDTO.Response findByIdOrThrow(Long id) {
        return mapToResponse(sexoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Sexo no encontrado con ID: " + id)));
    }

    // Crear 
    public SexoDTO.Response save(SexoDTO.Request request) {
        Sexo sexo = new Sexo();
        sexo.setNombre(request.getNombre());
        Sexo guardado = sexoRepository.save(sexo);
        return mapToResponse(guardado);
    }

    // Eliminar por ID
    public void delete(Long id) {
        sexoRepository.deleteById(id);
    }

    // Buscar por nombre
    public SexoDTO.Response findByNombre(String nombre) {
        return mapToResponse(sexoRepository.findByNombre(nombre).orElseThrow());
    }

    //recordar actualizar esto en github, update 
    public SexoDTO.Response updateSexo(Long id, SexoDTO.Request nSexo){
        if(sexoRepository.findById(id)!=null){
            Sexo sexo = sexoRepository.findById(id).orElse(null);
            sexo.setNombre(nSexo.getNombre());
            Sexo guardado = sexoRepository.save(sexo);
            return mapToResponse(guardado);
        }
        return null;
    }

    public SexoDTO.Response mapToResponse(Sexo sexo){
        return new SexoDTO.Response(sexo.getNombre());
    }

}
