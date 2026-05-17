package com.biblioteca.comuna.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.biblioteca.comuna.dto.ComunaDTO;
import com.biblioteca.comuna.exception.ResourceNotFoundException;
import com.biblioteca.comuna.model.Comuna;
import com.biblioteca.comuna.repository.ComunaRepository;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class ComunaService {

    @Autowired
    private ComunaRepository comunaRepository;

    //ver todas las comunas
    public List<ComunaDTO.Response> findAll(){
        return comunaRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    //crear
    public ComunaDTO.Response save(ComunaDTO.Request request) {
        //para q no se repita el nombre de comuna
    if (comunaRepository.existsByNombreIgnoreCase(request.getNombre())) {
            throw new IllegalArgumentException("La comuna con el nombre '" + request.getNombre() + "' ya existe.");
        }
    // Creamos un objeto de la entidad Comuna y le pasamos los datos del Request
    Comuna comuna = new Comuna();
    comuna.setNombre(request.getNombre());
    // 2. GUARDAR EN BD
    // El repository sigue trabajando con la Entidad, no con el DTO
    Comuna comunaGuardada = comunaRepository.save(comuna);    
    return mapToResponse(comunaGuardada);
    }

    //borrar
    public void delete(Long id) {
    // 1. Verificamos si la comuna realmente existe antes de intentar borrarla
    if (!comunaRepository.existsById(id)) {
        // Si no existe, lanzamos un error para que el sistema no explote en silencio
        throw new ResourceNotFoundException("No se puede eliminar: La comuna con ID " + id + " no existe.");
    }
    // 2. Si existe, procedemos a eliminarla de la base de datos
    comunaRepository.deleteById(id);
    }

    // buscar por id
    public ComunaDTO.Response findByIdOrThrow(Long id){
        Comuna comuna = comunaRepository.findById(id)
            .orElseThrow(()-> new RuntimeException("Comuna no encontrada"));
        return mapToResponse(comuna);
    }

    //buscar por nombre
    public Optional<ComunaDTO.Response> findByNombre(String comuna){
        return comunaRepository.findByNombreIgnoreCase(comuna)
                .map(this::mapToResponse);
    }

    //updatear version nueva por id
    public ComunaDTO.Response update(Long id, ComunaDTO.Request request){
        Comuna comunaExistente= comunaRepository.findById(id)
            .orElseThrow(()-> new ResourceNotFoundException("Comuna no encontrada con id "+ id));
        comunaExistente.setNombre(request.getNombre());
        Comuna actualizada = comunaRepository.save(comunaExistente);
        return mapToResponse(actualizada);
    }

    //transformar comuna a dto response
    private ComunaDTO.Response mapToResponse(Comuna comuna){
        ComunaDTO.Response response = new ComunaDTO.Response();
        response.setId(comuna.getId());
        response.setNombre(comuna.getNombre());
        return response;
    }
}
