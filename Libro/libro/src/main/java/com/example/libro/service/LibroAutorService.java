package com.example.libro.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.libro.model.LibroAutor;
import com.example.libro.repository.LibroAutorRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class LibroAutorService {

    @Autowired
    private LibroAutorRepository libroAutorRepo;

    public List<LibroAutor> obtenerTodos(){
        return libroAutorRepo.findAll();
    }
    
    public Optional<LibroAutor> obtenerPorGeneroId(Long autorId) {
        return libroAutorRepo.findById(autorId);
    }

    public Optional<LibroAutor> obtenerPorIsbn(Long libroIsbn) {
        return libroAutorRepo.findById(libroIsbn);
    }

    public LibroAutor guardar(LibroAutor libroAutor) {
        return libroAutorRepo.save(libroAutor);
    }

    public void eliminarPorIsbn(Long libroIsbn) {
        libroAutorRepo.deleteById(libroIsbn);
    }

    public void eliminar(Long autorId) {
        libroAutorRepo.deleteById(autorId);
    }
}
