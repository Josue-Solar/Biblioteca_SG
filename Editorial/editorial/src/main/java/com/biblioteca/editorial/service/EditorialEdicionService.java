package com.biblioteca.editorial.service;

import org.springframework.stereotype.Service;

import com.biblioteca.editorial.model.EditorialEdicion;
import com.biblioteca.editorial.repository.EditorialEdicionRepository;

import jakarta.transaction.Transactional;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Service
@Transactional
@RequiredArgsConstructor
public class EditorialEdicionService {
    private final EditorialEdicionRepository editorialEdicionRepository;

    public EditorialEdicion agregarRegistro(EditorialEdicion editorialEdicion){
        return editorialEdicionRepository.save(editorialEdicion);
    }
}
