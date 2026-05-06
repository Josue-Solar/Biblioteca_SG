package com.example.libro.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.libro.model.LibroAutor;
import com.example.libro.model.LibroAutorID;
import com.example.libro.repository.LibroAutorRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class LibroAutorService {

    private final LibroAutorRepository libroAutorRepo;

    public List<LibroAutor> obtenerTodos(){
        return libroAutorRepo.findAll();
    }
    
    public Optional<LibroAutor> obtenerPorGeneroId(LibroAutorID autorId) {
        return libroAutorRepo.findById(autorId);
    }

    public Optional<LibroAutor> obtenerPorIsbn(LibroAutorID libroIsbn) {
        return libroAutorRepo.findById(libroIsbn);
    }

    public List<LibroAutor> obtenerPorAutorId(Long autorId){
        return libroAutorRepo.findAllByAutorId(autorId);
    }

    public LibroAutor guardar(LibroAutor libroAutor) {
        return libroAutorRepo.save(libroAutor);
    }

    public void eliminarPorIsbn(LibroAutorID libroIsbn) {
        libroAutorRepo.deleteById(libroIsbn);
    }

    public void eliminar(LibroAutorID autorId) {
        libroAutorRepo.deleteById(autorId);
    }

    
}
