package com.example.libro.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

import com.example.libro.client.AutorClient;
import com.example.libro.client.EjemplarClient;
import com.example.libro.client.GeneroClient;
import com.example.libro.dto.LibroAutorDTO;
import com.example.libro.dto.LibroDTO;
import com.example.libro.dto.LibroGeneroDTO;
import com.example.libro.dto.clientDTO.autorClient.AutorDTO;
import com.example.libro.dto.clientDTO.autorClient.AutorLibrosDTO;
import com.example.libro.dto.clientDTO.autorClient.LibroAutoresDTO;
import com.example.libro.dto.clientDTO.ejemplarClient.EjemplarDTO;
import com.example.libro.dto.clientDTO.ejemplarClient.LibroEjemplaresDTO;
import com.example.libro.dto.clientDTO.generoClient.GeneroDTO;
import com.example.libro.dto.clientDTO.generoClient.GeneroLibrosDTO;
import com.example.libro.dto.clientDTO.generoClient.LibroGenerosDTO;
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

    public List<LibroDTO.Response> obtenerTodos(){
        return libroRepository.findAll().stream().map(l -> maptoResponseLibroDTO(l)).toList();
    }
    
    public LibroDTO.Response obtenerPorIsbn(long isbn) {
        return maptoResponseLibroDTO(libroRepository.findByIsbn(isbn));
    }

    public LibroDTO.Response obtenerPorNombre(String nombre) {
        List<Libro> libros = libroRepository.findByNombre(nombre);
        if (!libros.isEmpty()) {
            return maptoResponseLibroDTO(libros.get(0));
        }else{
            throw new RuntimeException("Libro no encontrado");
        }
        
    }

    public LibroDTO.Response guardar(LibroDTO.Request request) {
        Libro lib = new Libro();
        lib.setIsbn(request.getIsbn());
        lib.setNombre(request.getNombre());
        
        Libro guardado = libroRepository.save(lib);
        return maptoResponseLibroDTO(guardado);
    }

    public LibroDTO.Response actualizar(long isbn, LibroDTO.Request request) {
        return libroRepository.findById(isbn).map(l -> {
            l.setNombre(request.getNombre());
            return maptoResponseLibroDTO(libroRepository.save(l));
        }).orElseThrow();
    }

    public Optional<Boolean> eliminar(long isbn) {
        return libroRepository.deleteLibroByIsbn(isbn);
    }

    private final AutorClient autorClient;
    private final LibroAutorRepository libroAutRepo;
    public LibroAutoresDTO obtenerAutores(Long isbn){
        List<LibroAutor> registros = libroAutRepo.findByLibroIsbn(isbn);
        List<AutorDTO> autores = new ArrayList<>();

        for (LibroAutor libAut : registros) {
            autores.add(autorClient.buscarPorId(libAut.getAutorId()));
        }

        LibroAutoresDTO libroPorAutores = new LibroAutoresDTO(libroRepository.findByIsbn(isbn), autores);
        return libroPorAutores;
    }

    public AutorLibrosDTO listarLibros(Long autorId){
        List<LibroAutor> registros = libroAutRepo.findAllByAutorId(autorId);
        List<LibroDTO.Response> libros = new ArrayList<>();

        registros.forEach(libs -> libros.add(maptoResponseLibroDTO(
                                                    libroRepository.findByIsbn(libs.getLibroIsbn())
                                                )
                                            )
                                        );

        AutorLibrosDTO autorLibrosDTO = new AutorLibrosDTO(autorClient.buscarPorId(registros.get(0).getAutorId()), libros);
        return autorLibrosDTO;
    }

    public final EjemplarClient ejemplarClient;
    public LibroEjemplaresDTO listarEjemplares(Long isbn){
        List<EjemplarDTO> ejemplares = ejemplarClient.getAllByISBN(isbn);
        LibroEjemplaresDTO libroEjemplares = new LibroEjemplaresDTO(libroRepository.findByIsbn(isbn), ejemplares);

        return libroEjemplares;
    }

    public LibroGenerosDTO verGenero(Long isbn){
        List<LibroGenero> registros = libroGeneroRepository.findAllByLibroIsbn(isbn);
        List<GeneroDTO> generoDTOs = new ArrayList<>();

        for(LibroGenero libGen : registros){
            generoDTOs.add(generoClient.buscarPorId(libGen.getGeneroId()));
        }
        LibroGenerosDTO libroGeneroDTO = new LibroGenerosDTO(maptoResponseLibroDTO(libroRepository.findByIsbn(isbn)), generoDTOs);

        return libroGeneroDTO;
    }

    public GeneroLibrosDTO verLibrosPorGenero(Long idGenero){
        List<LibroGenero> registros = libroGeneroRepository.findAllByGeneroId(idGenero);
        List<LibroDTO.Response> libros = new ArrayList<>();

        registros.forEach(r -> libros.add(maptoResponseLibroDTO(libroRepository.findByIsbn(r.getLibroIsbn()))));

        GeneroLibrosDTO generoLibrosDTO = new GeneroLibrosDTO(generoClient.buscarPorId(idGenero), libros);
        return generoLibrosDTO;
    }

    public LibroGeneroDTO.Response mapToResponseLibroGeneroDTO(LibroGenero libroGenero){
        return new LibroGeneroDTO.Response(libroGenero.getGeneroId(), libroGenero.getLibroIsbn());
    }

    public LibroAutorDTO.Response mapToResponseLibroAutor(LibroAutor libroAutor){
        return new LibroAutorDTO.Response(libroAutor.getAutorId(), libroAutor.getLibroIsbn());
    }

    public LibroDTO.Response maptoResponseLibroDTO(Libro libro){
        return new LibroDTO.Response(libro.getIsbn(), libro.getNombre());
    }
}