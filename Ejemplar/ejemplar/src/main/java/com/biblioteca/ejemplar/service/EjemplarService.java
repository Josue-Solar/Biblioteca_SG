package com.biblioteca.ejemplar.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.biblioteca.ejemplar.client.EdicionClient;
import com.biblioteca.ejemplar.client.LibroClient;
import com.biblioteca.ejemplar.dto.EjemplarDTO;
import com.biblioteca.ejemplar.dto.LibroDTO;
import com.biblioteca.ejemplar.model.Ejemplar;
import com.biblioteca.ejemplar.repository.EjemplarRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class EjemplarService {

    private final EjemplarRepository ejemplarRepository;
    private final LibroClient libroClient;
    private final EdicionClient edicionClient;


    public List<Ejemplar> obtenerTodos(){
        return ejemplarRepository.findAll();
    }
    
    public Optional<Ejemplar> obtenerPorId(long id) {
        return ejemplarRepository.findById(id);
    }

    public Optional<Ejemplar> obtenerPorIsbn(long libroIsbn) {
        return ejemplarRepository.findById(libroIsbn);
    }

    public List<Ejemplar> obtenerTodosPorIsbn(long libroIsbn) {
        return ejemplarRepository.getAllByLibroIsbn(libroIsbn);
    }

    public Ejemplar guardar(Ejemplar ejemplar) {
        return ejemplarRepository.save(ejemplar);
    }

    public List<EjemplarDTO.Response> obtenerTodosPorEdicionId(Long id){
        List<Ejemplar> registros = ejemplarRepository.getAllByEdicionId(id);
        List<EjemplarDTO.Response> ejemplaresPorEdicion = new ArrayList<>();

        for (Ejemplar ej : registros) {
            ejemplaresPorEdicion.add(new EjemplarDTO.Response(ej.getLibroIsbn(), libroClient.getByID(ej.getLibroIsbn()).getNombre(), edicionClient.buscarPorId(id)));
        }

        return ejemplaresPorEdicion;

    }

    public Optional<Ejemplar> modReserva(long id, Ejemplar nEjemplar){
        return ejemplarRepository.findById(id).map(ej -> {
            ej.setId(nEjemplar.getId());
            ej.setLibroIsbn(nEjemplar.getLibroIsbn());
            return ejemplarRepository.save(ej);
        });
    }

    public void eliminar(long id) {
        ejemplarRepository.deleteById(id);
    }

    public void eliminarPorIsbn(long libroIsbn) {
        ejemplarRepository.deleteById(libroIsbn);
    }

    public LibroDTO getLibro(Long id){
        LibroDTO libro = libroClient.getByID(ejemplarRepository.getById(id).getLibroIsbn());
        return libro;
    }
}
