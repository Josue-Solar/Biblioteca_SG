package com.example.edicion.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.edicion.client.EditorialClient;
import com.example.edicion.client.EjemplarClient;
import com.example.edicion.dto.EdicionDTO;
import com.example.edicion.dto.EdicionEditorialDTO;
import com.example.edicion.dto.EjemplarDTO;
import com.example.edicion.dto.EjemplarEdicionDTO;
import com.example.edicion.dto.clientDTO.editorialclient.EdicionEditorialesDTO;
import com.example.edicion.dto.clientDTO.editorialclient.EditorialDTO;
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
        return edicionRepository.findAll().stream().map(e -> maptoResponseEdicionDTO(e)).toList();
    }    

    public EdicionDTO.Response obtenerPorId(long id) {
        return maptoResponseEdicionDTO(edicionRepository.findById(id).orElseThrow());
    }

    public EdicionDTO.Response obtenerPorNombre(String nombre) {
        List<Edicion> ediciones = edicionRepository.findByNombre(nombre);
        if (!ediciones.isEmpty()) {
            return maptoResponseEdicionDTO(ediciones.get(0));
        }else{
            throw new RuntimeException("Edicion no encontrada");
        }
        
    }

    public EdicionDTO.Response guardar(EdicionDTO.Request request) {
        Edicion edic = new Edicion();
        edic.setNombre(request.getNombre());
        edic.setAnnioPublicacion(request.getAnnio_publicacion());
        
        Edicion guardado = edicionRepository.save(edic);
        return maptoResponseEdicionDTO(guardado);
    }

    public EdicionDTO.Response actualizar(long id, EdicionDTO.Request request) {
        return edicionRepository.findById(id).map(e -> {
            e.setNombre(request.getNombre());
            e.setAnnioPublicacion(request.getAnnio_publicacion());
            return maptoResponseEdicionDTO(edicionRepository.save(e));
        }).orElseThrow();
    }

    public Optional<Boolean> eliminar(long id) {
        return edicionRepository.deleteEdicionById(id);
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

    public EdicionDTO.Response maptoResponseEdicionDTO(Edicion edicion){
        return new EdicionDTO.Response( edicion.getNombre(), edicion.getAnnioPublicacion());
    }
}
