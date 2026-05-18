package com.biblioteca.genero.service;

import java.util.List;
import org.springframework.stereotype.Service;

import com.biblioteca.genero.client.LibroClient;
import com.biblioteca.genero.dto.GeneroDTO;
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
    private final LibroClient libroClient;

    public List<Genero> obtenerTodos(){
        return generoRepository.findAll();
    }
    
    public GeneroDTO.Response findByIdOrThrow(Long id){
        return mapToResponse(generoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Genero no encontrado con ID: " + id)));
    }

    public List<GeneroDTO.Response> obtenerPorNombre(String nombre){
        return generoRepository.findByNombre(nombre).stream().map(this::mapToResponse).toList();
    }

    public GeneroDTO.Response guardar(GeneroDTO.Request request) {
        Genero genero = new Genero();
        genero.setNombre(request.getNombre());
        return mapToResponse(generoRepository.save(genero));
    }

    public GeneroDTO.Response modificarGenero(long id, GeneroDTO.Request nGenero) {
        
        if(findByIdOrThrow(id) != null){
            Genero genero = new Genero();
            genero.setNombre(nGenero.getNombre());
            
            Genero guardado = generoRepository.save(genero);
            return mapToResponse(guardado);
        }
        return null;
    }

    public void eliminar(long id) {
        generoRepository.deleteById(id);
    }

    public GeneroLibroDTO librosPorGenero(Long generoId){
        Genero genero = generoRepository.findById(generoId).orElseThrow(() -> new RuntimeException());
        GeneroDTO.Response generoDTO = mapToResponse(genero);
        List<LibroDTO> libros = libroClient.getAllByGeneroId(generoId);

        GeneroLibroDTO librosPorGenero = new GeneroLibroDTO(generoDTO, libros);

        return librosPorGenero;
    }

    public GeneroDTO.Response mapToResponse(Genero genero){
        return new GeneroDTO.Response(genero.getNombre());
    }
}
