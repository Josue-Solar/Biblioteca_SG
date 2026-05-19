package com.example.edicion.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.edicion.dto.EdicionEditorialDTO;
import com.example.edicion.model.EdicionEditorial;
import com.example.edicion.model.EdicionEditorialID;
import com.example.edicion.repository.EdicionEditorialRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class EdicionEditorialService {

    private final EdicionEditorialRepository edicionEditorialRepo;

    public List<EdicionEditorialDTO.Response> obtenerTodos(){
        return edicionEditorialRepo.findAll().stream().map(e -> mapToResponse(e)).collect(Collectors.toList());
    }

    public Optional<EdicionEditorialDTO.Response> obtenerPorEditorialId(EdicionEditorialID editorialId) {
        return edicionEditorialRepo.findById(editorialId).map(e -> mapToResponse(e));
    }

    public Optional<EdicionEditorialDTO.Response> obtenerPorId(EdicionEditorialID edicionId) {
        return edicionEditorialRepo.findById(edicionId).map(e -> mapToResponse(e));
    }
    
    public EdicionEditorialDTO.Response guardar(EdicionEditorialDTO.Request request) {
        EdicionEditorial edicionEditorial = new EdicionEditorial();
        edicionEditorial.setEditorialId(request.getEditorialId()); 
        edicionEditorial.setEdicionId(request.getEdicionId()); 

        EdicionEditorial guardado = edicionEditorialRepo.save(edicionEditorial);
        return mapToResponse(guardado);
    }    

    public void eliminarPorEdicionId(EdicionEditorialID edicionId) {
        edicionEditorialRepo.deleteById(edicionId);
    }

    public void eliminar(EdicionEditorialID editorialId) {
        edicionEditorialRepo.deleteById(editorialId);
    }

    public EdicionEditorialDTO.Response mapToResponse(EdicionEditorial edicionEditorial){
        return new EdicionEditorialDTO.Response(edicionEditorial.getEditorialId(), edicionEditorial.getEdicionId());
    }

}

