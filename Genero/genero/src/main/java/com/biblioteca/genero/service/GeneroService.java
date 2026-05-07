package com.biblioteca.genero.service;

import java.util.List;
import org.springframework.stereotype.Service;

import com.biblioteca.genero.client.LibroClient;
import com.biblioteca.genero.dto.GeneroLibroDTO;
import com.biblioteca.genero.dto.LibroDTO;
import com.biblioteca.genero.model.Genero;
import com.biblioteca.genero.repository.GeneroRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class GeneroService {
    
    private final GeneroRepository generoRepository;

    public List<Genero> obtenerTodos(){
        return generoRepository.findAll();
    }
    
    public Genero findByIdOrThrow(Long id){
        return generoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Genero no encontrado con ID: " + id));
    }

    public List<Genero> obtenerPorNombre(String nombre){
        return generoRepository.findByNombre(nombre);
    }

    public Genero guardar(Genero genero) {
        return generoRepository.save(genero);
    }

    public Genero modificarGenero(long id, Genero nGenero) {
        Genero genero = findByIdOrThrow(id);
        if(genero!=null){
            genero.setNombre(nGenero.getNombre());
            return generoRepository.save(genero);
        }
        return null;
    }

    public void eliminar(long id) {
        generoRepository.deleteById(id);
    }

    private final LibroClient libroClient;
    public GeneroLibroDTO findByLibroNombre(String nombreLibro){
        LibroDTO libro = libroClient.buscarPorNombre(nombreLibro);
        GeneroLibroDTO generoLibroDTO = new GeneroLibroDTO(libro, generoRepository.findByLibroIsbn(libro.getIsbn()));
        return generoLibroDTO;
    }

    public GeneroLibroDTO findByLibroISBN(Long isbn){
        LibroDTO libro = libroClient.buscarPorIsbn(isbn);
        GeneroLibroDTO generoLibroDTO = new GeneroLibroDTO(libro, generoRepository.findByLibroIsbn(isbn));
        return generoLibroDTO;
    }   
}
