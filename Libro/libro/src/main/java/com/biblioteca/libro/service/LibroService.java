package com.biblioteca.libro.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

import com.biblioteca.libro.client.AutorClient;
import com.biblioteca.libro.client.EjemplarClient;
import com.biblioteca.libro.client.GeneroClient;
import com.biblioteca.libro.dto.LibroAutorDTO;
import com.biblioteca.libro.dto.LibroDTO;
import com.biblioteca.libro.dto.LibroGeneroDTO;
import com.biblioteca.libro.dto.clientDTO.autorClient.AutorDTO;
import com.biblioteca.libro.dto.clientDTO.autorClient.AutorLibrosDTO;
import com.biblioteca.libro.dto.clientDTO.autorClient.LibroAutoresDTO;
import com.biblioteca.libro.dto.clientDTO.ejemplarClient.EjemplarDTO;
import com.biblioteca.libro.dto.clientDTO.ejemplarClient.LibroEjemplaresDTO;
import com.biblioteca.libro.dto.clientDTO.generoClient.GeneroDTO;
import com.biblioteca.libro.dto.clientDTO.generoClient.GeneroLibrosDTO;
import com.biblioteca.libro.dto.clientDTO.generoClient.LibroGenerosDTO;
import com.biblioteca.libro.model.Libro;
import com.biblioteca.libro.model.LibroAutor;
import com.biblioteca.libro.model.LibroGenero;
import com.biblioteca.libro.repository.LibroAutorRepository;
import com.biblioteca.libro.repository.LibroGeneroRepository;
import com.biblioteca.libro.repository.LibroRepository;

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