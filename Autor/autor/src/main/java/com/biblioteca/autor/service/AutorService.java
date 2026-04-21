package com.biblioteca.autor.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.biblioteca.autor.model.Autor;
import com.biblioteca.autor.repository.AutorRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class AutorService {

    @Autowired
    private AutorRepository autorRepository;

    //ver todos los autores
    public List<Autor> findAll(){
        return autorRepository.findAll();
    }

    public Autor findByIdOrThrow(Long id){
        return autorRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Autor no encontrado con ID: " + id));
    }

    //crear
    public Autor save(Autor autor){
        return autorRepository.save(autor);
    }

    //updatear
    public Autor updateAutor(Long id, Autor nAutor){
        Autor autor= autorRepository.findById(id)
            .orElse(null);
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
