package com.example.libro.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.libro.client.AutorClient;
import com.example.libro.client.EjemplarClient;
import com.example.libro.dto.AutorDTO;
import com.example.libro.dto.EjemplarDTO;
import com.example.libro.dto.LibroAutorDTO;
import com.example.libro.dto.LibroEjemplarDTO;
import com.example.libro.model.Libro;
import com.example.libro.model.LibroAutor;
import com.example.libro.repository.LibroAutorRepository;
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
    
    public Libro obtenerPorIsbn(long isbn) {
        return libroRepository.findByIsbn(isbn);
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

    public Optional<Libro> actualizar(long isbn, Libro datos) {
        return libroRepository.findById(isbn).map(l -> {
            l.setNombre(datos.getNombre());
            return libroRepository.save(l);
        });
    }

    public Optional<Boolean> eliminar(long isbn) {
        return libroRepository.deleteLibroByIsbn(isbn);
    }

    private final AutorClient autorClient;
    private final LibroAutorRepository libroAutRepo;
    public LibroAutorDTO obtenerAutores(Long isbn){
        List<LibroAutor> registros = libroAutRepo.findByLibroIsbn(isbn);
        List<AutorDTO> autores = new ArrayList<>();

        for (LibroAutor libAut : registros) {
            autores.add(autorClient.buscarPorId(libAut.getAutorId()));
        }

        LibroAutorDTO libroPorAutores = new LibroAutorDTO(libroRepository.findByIsbn(isbn), autores);
        return libroPorAutores;
    }

    public List<Libro> listarLibros(Long autorId){
        List<LibroAutor> registros = libroAutRepo.findAllByAutorId(autorId);
        List<Libro> libros = new ArrayList<>();

        for (LibroAutor libs : registros) {
            libros.add(libroRepository.findByIsbn(libs.getLibroIsbn()));
        }

        return libros;
    }

    public final EjemplarClient ejemplarClient;
    public LibroEjemplarDTO listarEjemplares(Long isbn){
        List<EjemplarDTO> ejemplares = ejemplarClient.getAllByISBN(isbn);
        LibroEjemplarDTO libroEjemplares = new LibroEjemplarDTO(libroRepository.findByIsbn(isbn), ejemplares);

        return libroEjemplares;
    }
    
}