package com.biblioteca.genero.service;

import java.util.List;
import org.springframework.stereotype.Service;

import com.biblioteca.genero.model.Genero;
import com.biblioteca.genero.repository.GeneroRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class GeneroService {
    
    private GeneroRepository generoRepository;

    public List<Genero> obtenerTodos(){
        return generoRepository.findAll();
    }
    
    public Genero findByIdOrThrow(Long id){
        return generoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Genero no encontrado con ID: " + id));
    }

    public List<Genero> obtenerPorNombre(String nombre){
        return generoRepository.findByNombre(nombre);
    }

    public Genero guardar(Genero genero) {
        return generoRepository.save(genero);
    }

    public Genero modificarGenero(long id, Genero nGenero) {
        Genero genero = findByIdOrThrow(id);
        if(genero!=null){
            genero.setNombre(nGenero.getNombre());
            return generoRepository.save(genero);
        }
        return null;
    }

    public void eliminar(long id) {
        generoRepository.deleteById(id);
    }

}
