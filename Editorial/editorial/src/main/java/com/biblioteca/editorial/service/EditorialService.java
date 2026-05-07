package com.biblioteca.editorial.service;

import java.util.List;
import org.springframework.stereotype.Service;

import com.biblioteca.editorial.model.Editorial;
import com.biblioteca.editorial.repository.EditorialRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class EditorialService {

    private final EditorialRepository editorialRepository;

    public List<Editorial> obtenerTodos(){
        return editorialRepository.findAll();
    }
    
    public Editorial findByIdOrThrow(Long id){
        return editorialRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Editorial no encontrada con ID: " + id));
    }

    public List<Editorial> obtenerPorNombre(String nombre){
        return editorialRepository.findByNombre(nombre);
    }

    public Editorial guardar(Editorial editorial) {
        return editorialRepository.save(editorial);
    }

    public Editorial modificarEditorial(long id, Editorial nEditorial) {
        Editorial editorial = findByIdOrThrow(id);
        if(editorial!=null){
            editorial.setNombre(nEditorial.getNombre());
            return editorialRepository.save(editorial);
        }
        return null;
    }

    public void eliminar(long id) {
        editorialRepository.deleteById(id);
    }
}
