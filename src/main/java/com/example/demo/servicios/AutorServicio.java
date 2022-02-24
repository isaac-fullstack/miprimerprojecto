
package com.example.demo.servicios;

import com.example.demo.entidades.Autor;
import com.example.demo.entidades.Libro;
import com.example.demo.errores.ErrorServicio;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.repositorios.AutorRepositorio;
import java.util.List;

@Service
public class AutorServicio {
    
    private final AutorRepositorio autorRepositorio;
    private final LibroServicio libroServicio;
    
    @Autowired
    public AutorServicio(AutorRepositorio autorRepositorio, LibroServicio libroServicio) {
        this.autorRepositorio = autorRepositorio;
        this.libroServicio = libroServicio;
    }

    
    
     @Transactional(rollbackOn = Exception.class)
    public void guardar(String id, String nombre) throws ErrorServicio{
        
        
        validar(nombre);
                
        Autor autor = new Autor();
        
         if (id != null && !id.isEmpty() && !id.equals(" ")) {
             autor = buscarPorId(id);
         }
            autor.setNombre(nombre);
            autor.setAlta(Boolean.TRUE);
        
        autorRepositorio.save(autor);
    }

    
    
    /* no fue necesario este metodo.
    @Transactional(rollbackOn = Exception.class)
    
    public void modificar(String id, String nombre) throws ErrorServicio {

        if (id == null || id.isEmpty()) {
            throw new ErrorServicio("El id no puede estar vacio");
        }
        validar(nombre);

        Autor autor = buscarPorId(id);
        
        autor.setNombre(nombre);
        
        autorRepositorio.save(autor);

    }
    */
    
    @Transactional(rollbackOn = Exception.class)
    public void deshabilitar(String id) throws ErrorServicio {
        if (id == null || id.isEmpty()) {
            throw new ErrorServicio("El id no puede estar vacio");
        }
       
        Optional<Autor> respuesta = autorRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Autor editorial = respuesta.get();
            editorial.setAlta(Boolean.FALSE);
            autorRepositorio.save(editorial);
        } else {
            throw new ErrorServicio("No se encontro el Autor");
        }

    }
    
    
    @Transactional(rollbackOn = Exception.class)
    public void habilitar(String id) throws ErrorServicio {
        if (id == null || id.isEmpty()) {
            throw new ErrorServicio("El id no puede estar vacio");
        }
       
        Optional<Autor> respuesta = autorRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Autor editorial = respuesta.get();
            editorial.setAlta(Boolean.TRUE);
            autorRepositorio.save(editorial);
        } else {
            throw new ErrorServicio("No se encontro el Autor");
        }

    }
    
    
    @Transactional(rollbackOn = Exception.class)
    public void validar(String nombre) throws ErrorServicio{
        
        if (nombre == null || nombre.isEmpty()) {
            throw new ErrorServicio("El nombre no puede estar vacio");
        }
        
    }
    
    
    @Transactional(rollbackOn = Exception.class)
    public List<Autor> listarTodos(){
        return autorRepositorio.findAll();
    }
    
    @Transactional(rollbackOn = Exception.class)
    public Autor buscarPorId(String id) throws ErrorServicio{
        Optional<Autor> option = autorRepositorio.findById(id);
        if (option.isPresent()) {
            Autor autor = option.get();
            return autor;
        }else{
            throw new ErrorServicio("Autor no encontrado");
        }
    }
    
    @Transactional(rollbackOn = Exception.class)
    public void eliminarAutor(String id) throws ErrorServicio{
        
        List<Libro> libros = libroServicio.buscarPorAutorId(id);
        
        if (libros.isEmpty()) {
           Autor autor = buscarPorId(id);
           autorRepositorio.delete(autor); 
        }else{
            throw new ErrorServicio("El autor no pude ser borrado porque tiene un libro asignado");
        }
        
        
    }
    
    @Transactional(rollbackOn = Exception.class)
    public Autor buscarPorNombre(String nombre) throws ErrorServicio{
        
        validar(nombre);
        Autor autor = autorRepositorio.buscarPorNombre(nombre);
        
        return autor;
        
    }

    
}