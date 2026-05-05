package com.example.ejemplar.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ejemplar.model.Ejemplar;
import com.example.ejemplar.repository.EjemplarRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class EjemplarService {

    @Autowired
    private EjemplarRepository ejemplarRepository;

    public List<Ejemplar> obtenerTodos(){
        return ejemplarRepository.findAll();
    }
    
    public Optional<Ejemplar> obtenerPorId(long id) {
        return ejemplarRepository.findById(id);
    }

    public Optional<Ejemplar> obtenerPorIsbn(long libroIsbn) {
        return ejemplarRepository.findById(libroIsbn);
    }

    public Ejemplar guardar(Ejemplar ejemplar) {
        return ejemplarRepository.save(ejemplar);
    }

    public void eliminar(long id) {
        ejemplarRepository.deleteById(id);
    }

    public void eliminarPorIsbn(long libroIsbn) {
        ejemplarRepository.deleteById(libroIsbn);
    }
}
