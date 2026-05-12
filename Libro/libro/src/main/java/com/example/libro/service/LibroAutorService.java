package com.example.libro.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.libro.dto.LibroAutorDTO;
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

    public List<LibroAutorDTO.Response> obtenerTodos(){
        return libroAutorRepo.findAll().stream().map(l -> mapToResponse(l)).collect(Collectors.toList());
    }
    
    public Optional<LibroAutorDTO.Response> obtenerPorGeneroId(LibroAutorID autorId) {
        return libroAutorRepo.findById(autorId).map(l -> mapToResponse(l));
    }

    public Optional<LibroAutorDTO.Response> obtenerPorIsbn(LibroAutorID libroIsbn) {
        return libroAutorRepo.findById(libroIsbn).map(l -> mapToResponse(l));
    }

    public List<LibroAutorDTO.Response> obtenerPorAutorId(Long autorId){
        return libroAutorRepo.findAllByAutorId(autorId).stream().map(l -> mapToResponse(l)).collect(Collectors.toList());
    }

    public LibroAutorDTO.Response guardar(LibroAutorDTO.Request request) {
        LibroAutor libroAutor = new LibroAutor();
        libroAutor.setAutorId(request.getAutorId()); 
        libroAutor.setLibroIsbn(request.getLibroIsbn()); 

        LibroAutor guardado = libroAutorRepo.save(libroAutor);
        return mapToResponse(guardado);
    }

    public void eliminarPorIsbn(LibroAutorID libroIsbn) {
        libroAutorRepo.deleteById(libroIsbn);
    }

    public void eliminar(LibroAutorID autorId) {
        libroAutorRepo.deleteById(autorId);
    }

    public LibroAutorDTO.Response mapToResponse(LibroAutor libroAutor){
        return new LibroAutorDTO.Response(libroAutor.getAutorId(), libroAutor.getLibroIsbn());
    }
}
