package com.biblioteca.persona.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.biblioteca.persona.dto.RolDTO;
import com.biblioteca.persona.model.Rol;
import com.biblioteca.persona.repository.RolRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class RolService {

    @Autowired
    private RolRepository rolRepository;

    // Ver todos los roles
    public List<RolDTO.Response> findAll() {
        return rolRepository.findAll().stream().map(this::mapToResponse).toList();
    }

    // Buscar por ID
    public RolDTO.Response findByIdOrThrow(Long id) {
        return mapToResponse(rolRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Rol no encontrado con ID: " + id)));
    }

    // Crear o actualizar
    public RolDTO.Response save(RolDTO.Request request) {
        if(rolRepository.findByNombre(request.getNombre()) == null){
            Rol rol = new Rol();
            rol.setNombre(request.getNombre());

            Rol guardado = rolRepository.save(rol);

            return mapToResponse(guardado);
        }

        return null;
        
    }

    // Buscar por nombre
    public RolDTO.Response findByNombre(String nombre) {
        return mapToResponse(rolRepository.findByNombre(nombre).orElseThrow(() -> new RuntimeException("Nombre no encontrado")));
    }

    //recordar actualizar esto en el github
    public RolDTO.Response updateRol(Long id, RolDTO.Request nRol){
        if(rolRepository.findById(id)!=null){
            Rol rol = rolRepository.findById(id).orElse(null);
            rol.setNombre(nRol.getNombre());
            Rol actualizado = rolRepository.save(rol);
            return mapToResponse(actualizado);
        }
        return null;
    }

    public RolDTO.Response mapToResponse(Rol rol){
        return new RolDTO.Response(rol.getNombre());
    }
}
