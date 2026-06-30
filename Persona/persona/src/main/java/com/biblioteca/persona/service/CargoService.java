package com.biblioteca.persona.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.biblioteca.persona.model.Cargo;
import com.biblioteca.persona.repository.CargoRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class CargoService {

        @Autowired
    private CargoRepository cargoRepository;

    // Ver todos los cargos
    public List<Cargo> findAll() {
        return cargoRepository.findAll();
    }

    // Buscar por ID
    public Cargo findByIdOrThrow(Long id) {
        return cargoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Cargo no encontrado con ID: " + id));
    }

    // Crear o actualizar
    public Cargo save(Cargo cargo) {
        return cargoRepository.save(cargo);
    }

    // Buscar por nombre
    public Optional<Cargo> findByNombre(String nombre) {
        return cargoRepository.findByNombre(nombre);
    }

    //recordar actualizar esto en el github
    public Cargo updateCargo(Long id, Cargo nCargo){
        Cargo cargo= cargoRepository.findById(id).orElse(null);
        if(cargo!=null){
            cargo.setNombre(nCargo.getNombre());
            return cargoRepository.save(cargo);
        }
        return null;
    }

}
