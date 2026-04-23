package com.example.genero.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.genero.model.Genero;
import com.example.genero.repository.GeneroRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class GeneroService {

    @Autowired
    private GeneroRepository generoRepository;

    public List<Genero> obtenerTodos(){
        return generoRepository.findAll();
    }
    
    public Optional<Genero> obtenerPorId(Long id) {
        return generoRepository.findById(id);
    }

    public Genero obtenerPorNombre(String nombre) {
        List<Genero> generos = generoRepository.findByNombre(nombre);
        if (!generos.isEmpty()) {
            return generos.get(0);
        }
        return null;
    }

    public Genero guardar(Genero genero) {
        return generoRepository.save(genero);
    }

    public Optional<Genero> actualizar(Long id, Genero datos) {
        return generoRepository.findById(id).map(g -> {
            g.setNombre(datos.getNombre());
            return generoRepository.save(g);
        });
    }

    public void eliminar(Long id) {
        generoRepository.deleteById(id);
    }

}
