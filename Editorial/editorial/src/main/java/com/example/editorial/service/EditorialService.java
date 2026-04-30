package com.example.editorial.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.editorial.model.Editorial;
import com.example.editorial.repository.EditorialRepository;

import jakarta.transaction.Transactional;


@Service
@Transactional
public class EditorialService {

    @Autowired
    private EditorialRepository editorialRepository;

    public List<Editorial> obtenerTodos(){
        return editorialRepository.findAll();
    }
    
    public Optional<Editorial> obtenerPorId(Long id) {
        return editorialRepository.findById(id);
    }

    public Editorial obtenerPorNombre(String nombre) {
        List<Editorial> editoriales = editorialRepository.findByNombre(nombre);
        if (!editoriales.isEmpty()) {
            return editoriales.get(0);
        }
        return null;
    }

    public Editorial guardar(Editorial editorial) {
        return editorialRepository.save(editorial);
    }

    public Optional<Editorial> actualizar(Long id, Editorial datos) {
        return editorialRepository.findById(id).map(g -> {
            g.setNombre(datos.getNombre());
            return editorialRepository.save(g);
        });
    }

    public void eliminar(Long id) {
        editorialRepository.deleteById(id);
    }
}
