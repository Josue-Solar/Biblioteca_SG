package com.biblioteca.persona.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.biblioteca.persona.model.Persona;
import com.biblioteca.persona.model.Sexo;
import com.biblioteca.persona.repository.SexoRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class SexoService {

    @Autowired
    private SexoRepository sexoRepository;

    // Ver todos los sexos
    public List<Sexo> findAll() {
        return sexoRepository.findAll();
    }

    // Buscar por ID
    public Sexo findByIdOrThrow(Long id) {
        return sexoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Sexo no encontrado con ID: " + id));
    }

    // Crear o actualizar
    public Sexo save(Sexo sexo) {
        return sexoRepository.save(sexo);
    }

    // Eliminar por ID
    public void delete(Long id) {
        sexoRepository.deleteById(id);
    }

    // Buscar por nombre
    public Optional<Sexo> findByNombre(String nombre) {
        return sexoRepository.findByNombre(nombre);
    }

    //recordar actualizar esto en github, update 
    public Sexo updateSexo(Long id, Sexo nSexo){
        Sexo sexo= sexoRepository.findById(id).orElse(null);
        if(sexo!=null){
            sexo.setNombre(nSexo.getNombre());
            return sexoRepository.save(sexo);
        }
        return null;
    }

}
