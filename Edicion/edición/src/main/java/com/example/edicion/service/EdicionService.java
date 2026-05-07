package com.example.edicion.service;

import java.util.List;
import org.springframework.stereotype.Service;

import com.example.edicion.model.Edicion;
import com.example.edicion.repository.EdicionRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class EdicionService {
    
    private final EdicionRepository edicionRepository;

    public List<Edicion> obtenerTodos(){
        return edicionRepository.findAll();
    }
    
    public Edicion findByIdOrThrow(Long id){
        return edicionRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Edicion no encontrada con ID: " + id));
    }

    public List<Edicion> obtenerPorNombre(String nombre){
        return edicionRepository.findByNombre(nombre);
    }

    public Edicion guardar(Edicion edicion) {
        return edicionRepository.save(edicion);
    }

    public Edicion modificarEdicion(long id, Edicion nEdicion) {
        Edicion edicion = findByIdOrThrow(id);
        if(edicion!=null){
            edicion.setNombre(nEdicion.getNombre());
            edicion.setFechaPublicacion(nEdicion.getFechaPublicacion());
            return edicionRepository.save(edicion);
        }
        return null;
    }

    public void eliminar(long id) {
        edicionRepository.deleteById(id);
    }


}
