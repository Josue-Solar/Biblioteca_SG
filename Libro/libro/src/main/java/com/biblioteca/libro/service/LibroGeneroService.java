package com.biblioteca.libro.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.biblioteca.libro.dto.LibroDTO;
import com.biblioteca.libro.dto.LibroGeneroDTO;
import com.biblioteca.libro.model.Libro;
import com.biblioteca.libro.model.LibroGenero;
import com.biblioteca.libro.model.LibroGeneroID;
import com.biblioteca.libro.repository.LibroGeneroRepository;
import com.biblioteca.libro.repository.LibroRepository;

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

    public List<LibroGeneroDTO.Response> obtenerTodos(){
        return libroGeneroRepo.findAll().stream().map(l -> mapToResponse(l)).toList();
    }
    
    public List<LibroDTO.Response> obtenerPorGeneroId(Long generoId) {
        List<LibroGenero> registros = libroGeneroRepo.findAllByGeneroId(generoId);
        List<Libro> libros = registros.stream().map(reg -> libroRepository.findByIsbn(reg.getLibroIsbn())).collect(Collectors.toList());
        return libros.stream().map(l -> maptoResponseLibroDTO(l)).toList();
    }

    public LibroGeneroDTO.Response obtenerPorIsbn(LibroGeneroID libroIsbn) {
        return mapToResponse(libroGeneroRepo.findById(libroIsbn).orElseThrow());
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

    public LibroDTO.Response maptoResponseLibroDTO(Libro libro){
        return new LibroDTO.Response(libro.getIsbn(), libro.getNombre());
    }
}
