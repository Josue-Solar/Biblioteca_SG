package com.example.libro.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.libro.model.LibroGenero;
import com.example.libro.repository.LibroGeneroRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class LibroGeneroService {

    @Autowired
    private LibroGeneroRepository libroGeneroRepo;

    public List<LibroGenero> obtenerTodos(){
        return libroGeneroRepo.findAll();
    }
    
    public Optional<LibroGenero> obtenerPorGeneroId(Long generoId) {
        return libroGeneroRepo.findById(generoId);
    }

    public Optional<LibroGenero> obtenerPorIsbn(Long libroIsbn) {
        return libroGeneroRepo.findById(libroIsbn);
    }

    public LibroGenero guardar(LibroGenero libroGenero) {
        return libroGeneroRepo.save(libroGenero);
    }

    public void eliminarPorIsbn(Long libroIsbn) {
        libroGeneroRepo.deleteById(libroIsbn);
    }

    public void eliminar(Long generoId) {
        libroGeneroRepo.deleteById(generoId);
    }
}
