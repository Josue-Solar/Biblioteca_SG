package com.example.edicion.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.edicion.client.EditorialClient;
import com.example.edicion.client.EjemplarClient;
import com.example.edicion.dto.EdicionDTO;
import com.example.edicion.dto.EdicionEditorialDTO;
import com.example.edicion.dto.EjemplarDTO;
import com.example.edicion.dto.EjemplarEdicionDTO;
import com.example.edicion.dto.clientDTO.editorialclient.EditorialEdicionesDTO;
import com.example.edicion.model.Edicion;
import com.example.edicion.model.EdicionEditorial;
import com.example.edicion.repository.EdicionEditorialRepository;
import com.example.edicion.repository.EdicionRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class EdicionService {
    
    private final EdicionRepository edicionRepository;
    private final EdicionEditorialRepository edicionEditorialRepo;
    private final EditorialClient editorialClient;
    private final EjemplarClient ejemplarClient;

    public List<EdicionDTO.Response> obtenerTodos(){
        List<Edicion> ediciones = edicionRepository.findAll();
        List<EdicionDTO.Response> respuestas = new ArrayList<>();
        for(Edicion ed : ediciones) {
            respuestas.add(maptoResponseEdicionDTO(ed));
        }
        return respuestas;
    }

    public EdicionDTO.Response obtenerPorId(Long id) {
        Edicion edicion = edicionRepository.findById(id).orElseThrow(() -> new RuntimeException("Edición no encontrada con ID: " + id));
        return maptoResponseEdicionDTO(edicion);
    }

    public EdicionDTO.Response guardar(EdicionDTO.Request request) {
        Edicion edicion = new Edicion();
        edicion.getId();
        edicion.setNombre(request.getNombre());
        edicion.setAnnioPublicacion(request.getAnnioPublicacion());
        Edicion guardada = edicionRepository.save(edicion);
        return maptoResponseEdicionDTO(guardada);
    }

    public EdicionDTO.Response actualizar(Long id, EdicionDTO.Request request) {
        Edicion edicion = edicionRepository.findById(id).orElseThrow(() -> new RuntimeException("Edición no encontrada"));
        edicion.setNombre(request.getNombre());
        edicion.setAnnioPublicacion(request.getAnnioPublicacion());
        Edicion actualizada = edicionRepository.save(edicion);
        return maptoResponseEdicionDTO(actualizada);
    }

    // 👇 ESTE ES EL MÉTODO QUE TE FALTABA PARA QUE FUNCIONE EL TEST
    public void eliminar(Long id) {
        if (!edicionRepository.existsById(id)) {
            throw new RuntimeException("Edicion no encontrada con ID: " + id);
        }
        edicionRepository.deleteById(id);
    }

    public EjemplarEdicionDTO librosPorEdicion(Long edicionId){
        List<EjemplarDTO> ejemplares = ejemplarClient.getAllByEdicionId(edicionId);
        EjemplarEdicionDTO ejemplarEdicionDTO = new EjemplarEdicionDTO(edicionRepository.findById(edicionId).orElseThrow(() -> new RuntimeException()), ejemplares);
        return ejemplarEdicionDTO;
    }

    public EditorialEdicionesDTO listarEdiciones(Long editorialId){
        List<EdicionEditorial> registros = edicionEditorialRepo.findAllByEditorialId(editorialId);
        List<EdicionDTO.Response> ediciones = new ArrayList<>();

        registros.forEach(edics -> ediciones.add(maptoResponseEdicionDTO(
                                                    edicionRepository.findById(edics.getEdicionId()).orElseThrow()
                                                )
                                            )
                                        );

        EditorialEdicionesDTO editorialEdicionesDTO = new EditorialEdicionesDTO(editorialClient.buscarPorId(registros.get(0).getEditorialId()), ediciones);
        return editorialEdicionesDTO;
    }

    public EdicionEditorialDTO.Response mapToResponseEdicionEditorial(EdicionEditorial edicionEditorial){
        return new EdicionEditorialDTO.Response(edicionEditorial.getEditorialId(), edicionEditorial.getEdicionId());
    }

    // 👇 AQUÍ TAMBIÉN CORREGÍ QUE SE PASE EL ID COMO PRIMER ARGUMENTO
    public EdicionDTO.Response maptoResponseEdicionDTO(Edicion edicion){
        return new EdicionDTO.Response(edicion.getId(), edicion.getNombre(), edicion.getAnnioPublicacion());
    }
}