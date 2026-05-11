package com.example.libro.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.libro.client.AutorClient;
import com.example.libro.client.EjemplarClient;
import com.example.libro.client.GeneroClient;
import com.example.libro.dto.LibroDTO;
import com.example.libro.dto.LibroGeneroDTO;
import com.example.libro.dto.clientDTO.AutorDTO;
import com.example.libro.dto.clientDTO.EjemplarDTO;
import com.example.libro.dto.clientDTO.GeneroDTO;
import com.example.libro.dto.clientDTO.LibroAutorDTO;
import com.example.libro.dto.clientDTO.LibroEjemplarDTO;
import com.example.libro.dto.clientDTO.LibroGeneroDTOcli;
import com.example.libro.model.Libro;
import com.example.libro.model.LibroAutor;
import com.example.libro.model.LibroGenero;
import com.example.libro.repository.LibroAutorRepository;
import com.example.libro.repository.LibroGeneroRepository;
import com.example.libro.repository.LibroRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class LibroService {
    
    private final LibroRepository libroRepository;
    private final LibroGeneroRepository libroGeneroRepository;
    private final GeneroClient generoClient;

    public List<Libro> obtenerTodos(){
        return libroRepository.findAll();
    }
    
    public Libro obtenerPorIsbn(long isbn) {
        return libroRepository.findByIsbn(isbn);
    }

    public Libro obtenerPorNombre(String nombre) {
        List<Libro> libros = libroRepository.findByNombre(nombre);
        if (!libros.isEmpty()) {
            return libros.get(0);
        }
        return null;
    }

    public Libro guardar(Libro libro) {
        return libroRepository.save(libro);
    }

    public Optional<Libro> actualizar(long isbn, Libro datos) {
        return libroRepository.findById(isbn).map(l -> {
            l.setNombre(datos.getNombre());
            return libroRepository.save(l);
        });
    }

    public Optional<Boolean> eliminar(long isbn) {
        return libroRepository.deleteLibroByIsbn(isbn);
    }

    private final AutorClient autorClient;
    private final LibroAutorRepository libroAutRepo;
    public LibroAutorDTO obtenerAutores(Long isbn){
        List<LibroAutor> registros = libroAutRepo.findByLibroIsbn(isbn);
        List<AutorDTO> autores = new ArrayList<>();

        for (LibroAutor libAut : registros) {
            autores.add(autorClient.buscarPorId(libAut.getAutorId()));
        }

        LibroAutorDTO libroPorAutores = new LibroAutorDTO(libroRepository.findByIsbn(isbn), autores);
        return libroPorAutores;
    }

    public List<Libro> listarLibros(Long autorId){
        List<LibroAutor> registros = libroAutRepo.findAllByAutorId(autorId);
        List<Libro> libros = new ArrayList<>();

        for (LibroAutor libs : registros) {
            libros.add(libroRepository.findByIsbn(libs.getLibroIsbn()));
        }

        return libros;
    }

    public final EjemplarClient ejemplarClient;
    public LibroEjemplarDTO listarEjemplares(Long isbn){
        List<EjemplarDTO> ejemplares = ejemplarClient.getAllByISBN(isbn);
        LibroEjemplarDTO libroEjemplares = new LibroEjemplarDTO(libroRepository.findByIsbn(isbn), ejemplares);

        return libroEjemplares;
    }

    public LibroGeneroDTOcli verGenero(Long isbn){
        List<LibroGenero> registros = libroGeneroRepository.findAllByLibroIsbn(isbn);
        List<GeneroDTO> generoDTOs = new ArrayList<>();

        for(LibroGenero libGen : registros){
            generoDTOs.add(generoClient.buscarPorId(libGen.getGeneroId()));
        }
        LibroGeneroDTOcli libroGeneroDTOcli = new LibroGeneroDTOcli(maptoResponseLibroDTO(libroRepository.findByIsbn(isbn)), generoDTOs);

        return libroGeneroDTOcli;
    }
    public LibroGeneroDTO.Response mapToResponseLibroGeneroDTO(LibroGenero libroGenero){
        return new LibroGeneroDTO.Response(libroGenero.getGeneroId(), libroGenero.getLibroIsbn());
    }

    public LibroDTO.Response maptoResponseLibroDTO(Libro libro){
        return new LibroDTO.Response(libro.getIsbn(), libro.getNombre());
    }
}