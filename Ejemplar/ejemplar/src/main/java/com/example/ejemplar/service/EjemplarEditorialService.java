package com.example.ejemplar.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.ejemplar.model.EjemplarEditorial;
import com.example.ejemplar.model.EjemplarEditorialID;
import com.example.ejemplar.repository.EjemplarEditorialRepository;

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
