package com.biblioteca.editorial.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

import com.biblioteca.editorial.exception.ResourceNotFoundException;
import com.biblioteca.editorial.dto.EditorialDTO;
import com.biblioteca.editorial.model.Editorial;
import com.biblioteca.editorial.repository.EditorialRepository;

@Slf4j
@Service
@Transactional
public class EditorialService {

    @Autowired
    private EditorialRepository editorialRepository;

    //transformar comuna a dto response
    private EditorialDTO.Response mapToResponse(Editorial editorial){
        EditorialDTO.Response response = new EditorialDTO.Response();
        response.setId(editorial.getId());
        response.setNombre(editorial.getNombre());
        return response;
    }

    //ver todas las editoriales
    public List<EditorialDTO.Response> findAll(){
        return editorialRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    //buscar por id
    public EditorialDTO.Response findByIdOrThrow(Long id){
        return editorialRepository.findById(id).map(this::mapToResponse)
            .orElseThrow(() -> new ResourceNotFoundException("Comuna no encontrado con ID: " + id));
    }

    //crear
    public EditorialDTO.Response save(EditorialDTO.Request request) {
        //para q no se repita el nombre de comuna
    if (editorialRepository.existsByNombreIgnoreCase(request.getNombre())) {
            throw new IllegalArgumentException("La comuna con el nombre '" + request.getNombre() + "' ya existe.");
        }
    // Creamos un objeto de la entidad Comuna y le pasamos los datos del Request
    Editorial editorial = new Editorial();
    editorial.setNombre(request.getNombre());
    // El repository sigue trabajando con la Entidad, no con el DTO
    Editorial editorialGuardada = editorialRepository.save(editorial);    
    return mapToResponse(editorialGuardada);
    }

    //borrar
    public void delete(Long id) {
    // Verificamos si la comuna realmente existe antes de intentar borrarla
    if (!editorialRepository.existsById(id)) {
        // Si no existe, lanzamos un error 
        throw new ResourceNotFoundException("No se puede eliminar: La comuna con ID " + id + " no existe.");
    }
    //Si existe, procedemos a eliminarla
    editorialRepository.deleteById(id);
    }

    //buscar por nombre
    public Optional<EditorialDTO.Response> findByNombre(String comuna){
        return editorialRepository.findByNombreIgnoreCase(comuna)
                .map(this::mapToResponse);
    }

    //updatear 
    public EditorialDTO.Response update(Long id, EditorialDTO.Request request){
        Editorial editorialExistente= editorialRepository.findById(id)
            .orElseThrow(()-> new ResourceNotFoundException("Comuna no encontrada con id "+ id));
        editorialExistente.setNombre(request.getNombre());
        Editorial actualizada = editorialRepository.save(editorialExistente);
        return mapToResponse(actualizada);
    } 

}
