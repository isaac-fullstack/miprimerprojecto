
package com.example.demo.servicios;

import com.example.demo.entidades.Autor;
import com.example.demo.entidades.Editorial;
import com.example.demo.entidades.Libro;
import com.example.demo.errores.ErrorServicio;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.repositorios.LibroRepositorio;
import java.util.List;

@Service
public class LibroServicio {
    
    
    @Autowired
    private LibroRepositorio libroRepositorio; 
    
    @Transactional(rollbackOn = Exception.class)
    public void guardar(String id, Long isbn, String titulo, Integer anio, Integer ejemplares, Integer ejemplaresPrestados, Autor autor, Editorial editorial) throws ErrorServicio{
    
        validar(isbn, titulo, anio, ejemplares, ejemplaresPrestados, autor, editorial);
        
        Libro libro = new Libro();
        
        if (id != null && !id.isEmpty() && !id.equals(" ")) {
            libro = buscarPorId(id);
        }
        
        libro.setIsbn(isbn);
        libro.setTitulo(titulo);
        libro.setAnio(anio);
        libro.setEjemplares(ejemplares);
        libro.setEjemplaresPrestados(ejemplaresPrestados);
        libro.setEjemplaresRestantes(ejemplares-ejemplaresPrestados);
        libro.setAlta(true);
        libro.setAutor(autor);
        libro.setEditorial(editorial);
        
        libroRepositorio.save(libro);
        
    }
    
    @Transactional(rollbackOn = Exception.class)
    public Libro buscarPorId(String id) throws ErrorServicio{
        Optional<Libro> option = libroRepositorio.findById(id);
        if (option.isPresent()) {
            Libro libro = option.get();
            return libro;
        }else{
            throw new ErrorServicio("Libro no encontrado");
        }
    }
    
    @Transactional(rollbackOn = Exception.class)
    public void validar(Long isbn, String titulo, Integer anio, Integer ejemplares, Integer ejemplaresPrestados, Autor autor, Editorial editorial) throws ErrorServicio{
     
        if (isbn == null || isbn.equals("")) {
            throw new ErrorServicio("Isbn Vacio");
        }
        if (titulo == null || titulo.isEmpty()) {
            throw new ErrorServicio("Titulo vacio");
        }
        if (anio == null || anio.equals("")) {
            throw new ErrorServicio("Año Vacio");
        }
        if (ejemplares == null || ejemplares.equals("")) {
            throw new ErrorServicio("ejemplares Vacio");
        }
        if (ejemplaresPrestados == null || ejemplaresPrestados.equals("")) {
            throw new ErrorServicio("Ejemplares Prestados Vacio");
        }
    }
    
    @Transactional(rollbackOn = Exception.class)
    public void modificar(String id, Long isbn, String titulo, Integer anio, Integer ejemplares, Integer ejemplaresPrestados, Boolean alta, Autor autor, Editorial editorial) throws ErrorServicio{
        if (id == null || id.isEmpty()) {
            throw new ErrorServicio("El id no puede estar vacio");
        }
        validar(isbn, titulo, anio, ejemplares, ejemplaresPrestados, autor, editorial);
        
        Optional<Libro> respuesta = libroRepositorio.findById(id);
        if (respuesta.isPresent()) {
        Libro libro = respuesta.get();
        libro.setIsbn(isbn);
        libro.setTitulo(titulo);
        libro.setAnio(anio);
        libro.setEjemplares(ejemplares);
        libro.setEjemplaresPrestados(ejemplaresPrestados);
        libro.setEjemplaresRestantes(ejemplares-ejemplaresPrestados);
        libro.setAlta(alta);
        libro.setAutor(autor);
        libro.setEditorial(editorial);
        libroRepositorio.save(libro);
        } else{
            throw new ErrorServicio("No se encontro el libro");
        }
    }
    
    @Transactional(rollbackOn = Exception.class)
    public void deshabilitar(String id) throws ErrorServicio{
        if (id == null || id.isEmpty()) {
            throw new ErrorServicio("El id no puede estar vacio");
        }
        Optional<Libro> respuesta = libroRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Libro libro = respuesta.get();
            libro.setAlta(Boolean.FALSE);
            libroRepositorio.save(libro);
        } else{
            throw new ErrorServicio("No se encontro el libro");
        }
    }
     
    
    @Transactional(rollbackOn = Exception.class)
    public void habilitar(String id) throws ErrorServicio{
        if (id == null || id.isEmpty()) {
            throw new ErrorServicio("El id no puede estar vacio");
        }
        Optional<Libro> respuesta = libroRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Libro libro = respuesta.get();
            libro.setAlta(Boolean.TRUE);
            libroRepositorio.save(libro);
        } else{
            throw new ErrorServicio("No se encontro el libro");
        }
    }
    
     @Transactional(rollbackOn = Exception.class)
     public void eliminarLibro(String id) throws ErrorServicio{
         libroRepositorio.delete(buscarPorId(id));
     }
     
     @Transactional(rollbackOn = Exception.class)
     public List<Libro> listarTodos(){
         List<Libro> libros = libroRepositorio.findAll();
         return libros;
     }

     public List<Libro> buscarPorAutorId(String autorId){
         List<Libro> libros = libroRepositorio.buscarPorAutorId(autorId);
         return libros;
     }
     
     public List<Libro> buscarPorEditorialId(String editorialId){
         List<Libro> libros = libroRepositorio.buscarPorEditorialId(editorialId);
         return libros;
     }
     
     @Transactional(rollbackOn = Exception.class)
     public void prestar(Libro libro){
         libro.setEjemplaresPrestados(libro.getEjemplaresPrestados()+1);
         libro.setEjemplaresRestantes(libro.getEjemplaresRestantes()-1);
         libroRepositorio.save(libro);
     }
     
     @Transactional(rollbackOn = Exception.class)
     public void devolver(Libro libro){
         libro.setEjemplaresPrestados(libro.getEjemplaresPrestados()-1);
         libro.setEjemplaresRestantes(libro.getEjemplaresRestantes()+1);
         libroRepositorio.save(libro);
     }
     
     @Transactional(rollbackOn = Exception.class)
     public List<Libro> buscarPorPalabra(String palabra){
         return libroRepositorio.buscarPorPalabra(palabra);
     }

}
