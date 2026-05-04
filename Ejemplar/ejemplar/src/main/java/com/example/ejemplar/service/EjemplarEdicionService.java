package com.example.ejemplar.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.ejemplar.model.EjemplarEdicion;
import com.example.ejemplar.repository.EjemplarEdicionRepository;

public class EjemplarEdicionService {

    @Autowired
    private EjemplarEdicionRepository EjemplarEdicionRepo;

    public List<EjemplarEdicion> obtenerTodos(){
        return EjemplarEdicionRepo.findAll();
    }
    
    public Optional<EjemplarEdicion> obtenerPorEjemplarId(Long ejemplarId) {
        return EjemplarEdicionRepo.findById(ejemplarId);
    }

    public Optional<EjemplarEdicion> obtenerPorEdicionId(Long edicionId) {
        return EjemplarEdicionRepo.findById(edicionId);
    }

    public EjemplarEdicion guardar(EjemplarEdicion ejemplarEdicion) {
        return EjemplarEdicionRepo.save(ejemplarEdicion);
    }

    public void eliminarPorEjemplarId(Long ejemplarId) {
        EjemplarEdicionRepo.deleteById(ejemplarId);
    }

    public void eliminarPorEdicionId(Long edicionId) {
        EjemplarEdicionRepo.deleteById(edicionId);
    }

}
