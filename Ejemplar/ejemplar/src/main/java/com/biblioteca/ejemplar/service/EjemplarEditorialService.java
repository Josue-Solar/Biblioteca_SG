package com.biblioteca.ejemplar.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.biblioteca.ejemplar.model.EjemplarEditorial;
import com.biblioteca.ejemplar.model.EjemplarEditorialID;
import com.biblioteca.ejemplar.repository.EjemplarEditorialRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Transactional
@RequiredArgsConstructor
@Service
public class EjemplarEditorialService {

    @Autowired
    private EjemplarEditorialRepository EjemplarEditorialRepo;

    public List<EjemplarEditorial> obtenerTodos(){
        return EjemplarEditorialRepo.findAll();
    }
    
    public Optional<EjemplarEditorial> obtenerPorEjemplarId(EjemplarEditorialID ejemplarId) {
        return EjemplarEditorialRepo.findById(ejemplarId);
    }

    public Optional<EjemplarEditorial> obtenerPorEditorialId(EjemplarEditorialID editorialId) {
        return EjemplarEditorialRepo.findById(editorialId);
    }

    public EjemplarEditorial guardar(EjemplarEditorial ejemplarEditorial) {
        return EjemplarEditorialRepo.save(ejemplarEditorial);
    }

    public void eliminarPorEjemplarId(EjemplarEditorialID ejemplarId) {
        EjemplarEditorialRepo.deleteById(ejemplarId);
    }

    public void eliminarPorEditorialId(EjemplarEditorialID editorialId) {
        EjemplarEditorialRepo.deleteById(editorialId);
    }


}
