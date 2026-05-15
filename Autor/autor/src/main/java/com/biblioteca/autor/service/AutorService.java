package com.biblioteca.autor.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.biblioteca.autor.dto.AutorDTO;
import com.biblioteca.autor.exception.ResourceNotFoundException;
import com.biblioteca.autor.model.Autor;
import com.biblioteca.autor.repository.AutorRepository;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class AutorService {

    @Autowired
    private AutorRepository autorRepository;

    //ver todos los autores
    public List<AutorDTO.Response> findAll(){
        return autorRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    //buscar por id
    public AutorDTO.Response findByIdOrThrow(Long id){
        return autorRepository.findById(id).map(this::mapToResponse)
            .orElseThrow(() -> new ResourceNotFoundException("Autor no encontrado con ID: " + id));
    }

    //crear
    public AutorDTO.Response save(AutorDTO.Request request){
        Autor autor = new Autor();
        autor.setPrimerNombre(request.getPrimerNombre());
        autor.setSegundoNombre(request.getSegundoNombre());
        autor.setApPaterno(request.getApPaterno());
        autor.setApMaterno(request.getApMaterno());
        Autor nAutor = autorRepository.save(autor);
        return mapToResponse(nAutor);
    }

    //updatear
    public AutorDTO.Response updateAutor(Long id, AutorDTO.Request request){
        Autor autorExistente= autorRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Autor no encontrado con id =" + id));
            autorExistente.setPrimerNombre(request.getPrimerNombre());
            autorExistente.setSegundoNombre(request.getSegundoNombre());
            autorExistente.setApPaterno(request.getApPaterno());
            autorExistente.setApMaterno(request.getApMaterno());
        Autor actualizado = autorRepository.save(autorExistente);
        return mapToResponse(actualizado);
    }

    

    //borrar
    public void delete(Long id){
        if(!autorRepository.existsById(id)){
            throw new ResourceNotFoundException("No se puede eliminar, autor no encontrado con id =" + id);
            }
        autorRepository.deleteById(id);
    }

    //buscar por apellido
    public List<AutorDTO.Response> findByApPaterno(String apPaterno){
        return autorRepository.findByApPaternoIgnoreCase(apPaterno)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    //buscar por nombre
    public List<AutorDTO.Response> findByPrimerNombre(String primerNombre){
        return autorRepository.findByPrimerNombreIgnoreCase(primerNombre)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    //transformar autor a dto response
    private AutorDTO.Response mapToResponse(Autor autor){
        AutorDTO.Response response = new AutorDTO.Response();
        response.setId(autor.getId());
        response.setPrimerNombre(autor.getPrimerNombre());
        response.setSegundoNombre(autor.getSegundoNombre());
        response.setApPaterno(autor.getApPaterno());
        response.setApMaterno(autor.getApMaterno());
        return response;
    }

}