package com.example.edicion.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.edicion.model.Edicion;
import com.example.edicion.repository.EdicionRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class EdicionService {
    @Autowired
    private EdicionRepository edicionRepository;

    public List<Edicion> obtenerTodos(){
        return edicionRepository.findAll();
    }
    
    public Optional<Edicion> obtenerPorId(Long id) {
        return edicionRepository.findById(id);
    }

    public Edicion obtenerPorNombre(String nombre) {
        List<Edicion> ediciones = edicionRepository.findByNombre(nombre);
        if (!ediciones.isEmpty()) {
            return ediciones.get(0);
        }
        return null;
    }

    public Edicion guardar(Edicion edicion) {
        return edicionRepository.save(edicion);
    }

    public Optional<Edicion> actualizar(Long id, Edicion datos) {
        return edicionRepository.findById(id).map(e -> {
            e.setNombre(datos.getNombre());
            return edicionRepository.save(e);
        });
    }

    public void eliminar(Long id) {
        edicionRepository.deleteById(id);
    }
    

}
