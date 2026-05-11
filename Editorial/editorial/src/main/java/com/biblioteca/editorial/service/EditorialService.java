package com.biblioteca.editorial.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.biblioteca.editorial.client.EdicionClient;
import com.biblioteca.editorial.dto.EdicionDTO;
import com.biblioteca.editorial.dto.EditorialEdicionDTO;
import com.biblioteca.editorial.dto.EjemplarDTO;
import com.biblioteca.editorial.dto.EjemplarEdicionDTO;
import com.biblioteca.editorial.model.Editorial;
import com.biblioteca.editorial.model.EditorialEdicion;
import com.biblioteca.editorial.repository.EditorialEdicionRepository;
import com.biblioteca.editorial.repository.EditorialRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class EditorialService {

    private final EditorialRepository editorialRepository;
    private final EdicionClient edicionClient;
    private final EditorialEdicionRepository editorialEdicionRepository;

    public List<Editorial> obtenerTodos(){
        return editorialRepository.findAll();
    }
    
    public Editorial findByIdOrThrow(Long id){
        return editorialRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Editorial no encontrada con ID: " + id));
    }

    public List<Editorial> obtenerPorNombre(String nombre){
        return editorialRepository.findByNombre(nombre);
    }
    
    public EditorialEdicionDTO listarEdiciones(Long editorialId){
        List<EditorialEdicion> registros = editorialEdicionRepository.findAllByEditorialId(editorialId);
        List<EdicionDTO> ediciones = registros.stream().map(r -> edicionClient.buscarPorId(r.getEdicionId())).collect(Collectors.toList());
        List<EjemplarEdicionDTO> ejemplarEdicionDTO = new ArrayList<>();

        for (EdicionDTO ed : ediciones) {
            ejemplarEdicionDTO.add(edicionClient.librosPorEdicion(ed.getId()));
        }

        EditorialEdicionDTO editorialEdicionDTO = new EditorialEdicionDTO(editorialRepository.findById(editorialId).orElseThrow(), ejemplarEdicionDTO);
        return editorialEdicionDTO;
    }

    public Editorial guardar(Editorial editorial) {
        return editorialRepository.save(editorial);
    }

    public Editorial modificarEditorial(long id, Editorial nEditorial) {
        Editorial editorial = findByIdOrThrow(id);
        if(editorial!=null){
            editorial.setNombre(nEditorial.getNombre());
            return editorialRepository.save(editorial);
        }
        return null;
    }

    public void eliminar(long id) {
        editorialRepository.deleteById(id);
    }
}
