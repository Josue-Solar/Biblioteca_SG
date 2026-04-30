package com.example.ejemplar.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.ejemplar.model.Ejemplar;
import com.example.ejemplar.repository.EjemplarRepository;

public class EjemplarService {

    @Autowired
    private EjemplarRepository ejemplarRepository;

    public List<Ejemplar> obtenerTodos(){
        return ejemplarRepository.findAll();
    }
    
    public Optional<Ejemplar> obtenerPorId(Long id) {
        return ejemplarRepository.findById(id);
    }

    public Optional<Ejemplar> obtenerPorIsbn(Long libroIsbn) {
        return ejemplarRepository.findById(libroIsbn);
    }

    public Ejemplar guardar(Ejemplar ejemplar) {
        return ejemplarRepository.save(ejemplar);
    }

    public void eliminarPorId(Long id) {
        ejemplarRepository.deleteById(id);
    }

    public void eliminarPorIsbn(Long libroIsbn) {
        ejemplarRepository.deleteById(libroIsbn);
    }
}
