package com.biblioteca.comuna.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.biblioteca.comuna.model.Comuna;
import com.biblioteca.comuna.repository.ComunaRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ComunaService {

    @Autowired
    private ComunaRepository comunaRepository;

    //ver todas las comunas
    public List<Comuna> findAll(){
        return comunaRepository.findAll();
    }

    //crear y updatear
    public Comuna save(Comuna comuna){
        return comunaRepository.save(comuna);
    }

    //borrar
    public void delete(Long id){
        comunaRepository.deleteById(id);
    }

    // buscar por id
    public Comuna findByIdOrThrow(Long id){
        return comunaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Comuna no encontrada con ID: " + id));
    }

    public boolean existsByNombre(String nombre){
        return comunaRepository.existsByNombre(nombre);
    }

    //buscar por nombre
    public Optional<Comuna> findByNombre(String comuna){
        return comunaRepository.findByNombre(comuna);
    }

    //updatear version nueva
    public Comuna updateComuna(Long id, Comuna nComuna){
        Comuna comuna= findByIdOrThrow(id);
        comuna.setNombre(nComuna.getNombre());
            return comunaRepository.save(comuna);
    }

}
