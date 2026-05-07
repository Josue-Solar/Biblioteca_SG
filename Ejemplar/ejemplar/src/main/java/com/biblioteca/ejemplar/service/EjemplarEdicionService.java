package com.biblioteca.ejemplar.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.biblioteca.ejemplar.model.EjemplarEdicion;
import com.biblioteca.ejemplar.model.EjemplarEdicionID;
import com.biblioteca.ejemplar.repository.EjemplarEdicionRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Transactional
@RequiredArgsConstructor
@Service
public class EjemplarEdicionService {

    @Autowired
    private EjemplarEdicionRepository EjemplarEdicionRepo;

    public List<EjemplarEdicion> obtenerTodos(){
        return EjemplarEdicionRepo.findAll();
    }
    
    public Optional<EjemplarEdicion> obtenerPorEjemplarId(EjemplarEdicionID ejemplarId) {
        return EjemplarEdicionRepo.findById(ejemplarId);
    }

    public Optional<EjemplarEdicion> obtenerPorEdicionId(EjemplarEdicionID edicionId) {
        return EjemplarEdicionRepo.findById(edicionId);
    }

    public EjemplarEdicion guardar(EjemplarEdicion ejemplarEdicion) {
        return EjemplarEdicionRepo.save(ejemplarEdicion);
    }

    public void eliminarPorEjemplarId(EjemplarEdicionID ejemplarId) {
        EjemplarEdicionRepo.deleteById(ejemplarId);
    }

    public void eliminarPorEdicionId(EjemplarEdicionID edicionId) {
        EjemplarEdicionRepo.deleteById(edicionId);
    }

}
