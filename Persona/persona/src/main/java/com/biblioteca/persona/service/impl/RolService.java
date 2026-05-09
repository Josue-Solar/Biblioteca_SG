package com.biblioteca.persona.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.biblioteca.persona.model.Rol;
import com.biblioteca.persona.repository.RolRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class RolService {

        @Autowired
    private RolRepository rolRepository;

    // Ver todos los roles
    public List<Rol> findAll() {
        return rolRepository.findAll();
    }

    // Buscar por ID
    public Rol findByIdOrThrow(Long id) {
        return rolRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Rol no encontrado con ID: " + id));
    }

    // Crear o actualizar
    public Rol save(Rol rol) {
        return rolRepository.save(rol);
    }

    // Buscar por nombre
    public Optional<Rol> findByNombre(String nombre) {
        return rolRepository.findByNombre(nombre);
    }

    //recordar actualizar esto en el github
    public Rol updateRol(Long id, Rol nRol){
        Rol rol = rolRepository.findById(id).orElse(null);
        if(rol!=null){
            rol.setNombre(nRol.getNombre());
            return rolRepository.save(rol);
        }
        return null;
    }

}
