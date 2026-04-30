package com.example.libro.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.libro.model.Libro;
import com.example.libro.repository.LibroRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class LibroService {
    
    @Autowired
    private LibroRepository libroRepository;

    public List<Libro> obtenerTodos(){
        return libroRepository.findAll();
    }
    
    public Optional<Libro> obtenerPorIsbn(Long isbn) {
        return libroRepository.findById(isbn);
    }

    public Libro obtenerPorNombre(String nombre) {
        List<Libro> libros = libroRepository.findByNombre(nombre);
        if (!libros.isEmpty()) {
            return libros.get(0);
        }
        return null;
    }

    public Libro guardar(Libro libro) {
        return libroRepository.save(libro);
    }

    public Optional<Libro> actualizar(Long isbn, Libro datos) {
        return libroRepository.findById(isbn).map(l -> {
            l.setNombre(datos.getNombre());
            return libroRepository.save(l);
        });
    }

    public void eliminar(Long isbn) {
        libroRepository.deleteById(isbn);
    }
}