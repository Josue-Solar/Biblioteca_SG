package com.example.libro.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.libro.dto.LibroGeneroDTO;
import com.example.libro.model.Libro;
import com.example.libro.model.LibroGenero;
import com.example.libro.model.LibroGeneroID;
import com.example.libro.repository.LibroGeneroRepository;
import com.example.libro.repository.LibroRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class LibroGeneroService {

    private final LibroGeneroRepository libroGeneroRepo;
    private final LibroRepository libroRepository;

    public List<LibroGenero> obtenerTodos(){
        return libroGeneroRepo.findAll();
    }
    
    public List<Libro> obtenerPorGeneroId(Long generoId) {
        List<LibroGenero> registros = libroGeneroRepo.findAllByGeneroId(generoId);
        List<Libro> libros = registros.stream().map(reg -> libroRepository.findByIsbn(reg.getLibroIsbn())).collect(Collectors.toList());
        
        return libros;
    }

    public Optional<LibroGenero> obtenerPorIsbn(LibroGeneroID libroIsbn) {
        return libroGeneroRepo.findById(libroIsbn);
    }

    public LibroGeneroDTO.Response guardar(LibroGeneroDTO.Request nLibroGenero) {
        if((existsByIsbn(nLibroGenero))){
            LibroGenero libroGenero = new LibroGenero();
            libroGenero.setGeneroId(nLibroGenero.getGeneroId());
            libroGenero.setLibroIsbn(nLibroGenero.getLibroIsbn());

            LibroGenero guardado = libroGeneroRepo.save(libroGenero);
            return mapToResponse(guardado);
        }else{
            throw new RuntimeException("Isbn no encontrado: " + nLibroGenero.getLibroIsbn());
        }
    }

    public void eliminarPorIsbn(LibroGeneroID libroIsbn) {
        libroGeneroRepo.deleteById(libroIsbn);
    }

    public void eliminar(Long generoId) {
        libroGeneroRepo.deleteByGeneroId(generoId);
    }

    public boolean existsByIsbn(LibroGeneroDTO.Request libroGenero){
        return libroRepository.existsById(libroGenero.getLibroIsbn());
    }

    public LibroGeneroDTO.Response actualizar(Long generoId, LibroGeneroDTO.Request nLibroGenero){
        if(existsByIsbn(nLibroGenero)){
            LibroGenero libroGenero = new LibroGenero();
            libroGenero.setGeneroId(nLibroGenero.getGeneroId());
            libroGenero.setLibroIsbn(nLibroGenero.getLibroIsbn());

            LibroGenero guardado = libroGeneroRepo.save(libroGenero);
            return mapToResponse(guardado);
        }else{
            throw new RuntimeException("Isbn no encontrado: " + nLibroGenero.getLibroIsbn());
        }
    }

    public LibroGeneroDTO.Response mapToResponse(LibroGenero libroGenero){
        return new LibroGeneroDTO.Response(
            libroGenero.getGeneroId(),
            libroGenero.getLibroIsbn()
        );
    }
    
}
