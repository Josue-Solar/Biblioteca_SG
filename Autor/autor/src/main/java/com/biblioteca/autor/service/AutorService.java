package com.biblioteca.autor.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.biblioteca.autor.client.LibroClient;
import com.biblioteca.autor.dto.LibroAutorDTO;
import com.biblioteca.autor.dto.LibroDTO;
import com.biblioteca.autor.model.Autor;
import com.biblioteca.autor.repository.AutorRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AutorService {

    private final AutorRepository autorRepository;

    //ver todos los autores
    public List<Autor> findAll(){
        return autorRepository.findAll();
    }

    //buscar por id
    public Autor findByIdOrThrow(Long id){
        return autorRepository.findById(id).orElseThrow(() -> new RuntimeException("Autor no encontrado con ID: " + id));
    }

    private final LibroClient libroClient;
    public LibroAutorDTO listarLibros(Long autorId){
        List<LibroDTO> libros = libroClient.getAllByAuthId(autorId);
        Autor autor = autorRepository.findById(autorId).orElseThrow(() -> new RuntimeException("Autor no encontrado: " + autorId));

        LibroAutorDTO libroAutorDTO = new LibroAutorDTO(autor, libros);
        return libroAutorDTO;
    }   

    //crear
    public Autor save(Autor autor){
        return autorRepository.save(autor);
    }

    //updatear
    public Autor updateAutor(Long id, Autor nAutor){
        Autor autor= autorRepository.findById(id).orElse(null);
        if(autor!=null){
            autor.setPNombre(nAutor.getPNombre());
            autor.setSNombre(nAutor.getSNombre());
            autor.setApPaterno(nAutor.getApPaterno());
            autor.setApMaterno(nAutor.getApMaterno());
            return autorRepository.save(autor);
        }
        return null;
    }

    //borrar
    public void delete(Long id){
        autorRepository.deleteById(id);
    }

    //buscar por apellido
    public List<Autor> findByApPaterno(String apPaterno){
        return autorRepository.findByApPaterno(apPaterno);
    }

}
