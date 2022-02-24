package com.example.demo.servicios;

import com.example.demo.entidades.Editorial;
import com.example.demo.entidades.Libro;
import com.example.demo.errores.ErrorServicio;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.repositorios.EditorialRepositorio;

@Service
public class EditorialServicio {

    @Autowired
    private final EditorialRepositorio editorialRepositorio;
    private final LibroServicio libroServicio;

    @Autowired
    public EditorialServicio(EditorialRepositorio editorialRepositorio, LibroServicio libroServicio) {
        this.editorialRepositorio = editorialRepositorio;
        this.libroServicio = libroServicio;
    }

    @Transactional(rollbackOn = Exception.class)
    public void guardar(String id, String nombre) throws ErrorServicio {

        validar(nombre);

        Editorial editorial = new Editorial();

        if (id != null && !id.isEmpty() && !id.equals(" ")) {
            editorial = buscarPorId(id);
        }

        editorial.setNombre(nombre);
        editorial.setAlta(Boolean.TRUE);

        editorialRepositorio.save(editorial);
    }

    /*metodo no necesario
     @Transactional(rollbackOn = Exception.class)
     
    public void modificar(String id, String nombre) throws ErrorServicio {

        if (id == null || id.isEmpty()) {
            throw new ErrorServicio("El id no puede estar vacio");
        }
        validar(nombre);

        Optional<Editorial> respuesta = editorialRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Editorial editorial = respuesta.get();
            editorial.setNombre(nombre);
            editorialRepositorio.save(editorial);
        } else {
            throw new ErrorServicio("No se encontro la Editorial");
        }

    }
     */
    @Transactional(rollbackOn = Exception.class)
    public void deshabilitar(String id) throws ErrorServicio {
        if (id == null || id.isEmpty()) {
            throw new ErrorServicio("El id no puede estar vacio");
        }

        Optional<Editorial> respuesta = editorialRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Editorial editorial = respuesta.get();
            editorial.setAlta(Boolean.FALSE);
            editorialRepositorio.save(editorial);
        } else {
            throw new ErrorServicio("No se encontro la Editorial");
        }

    }

    @Transactional(rollbackOn = Exception.class)
    public void habilitar(String id) throws ErrorServicio {
        if (id == null || id.isEmpty()) {
            throw new ErrorServicio("El id no puede estar vacio");
        }

        Optional<Editorial> respuesta = editorialRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Editorial editorial = respuesta.get();
            editorial.setAlta(Boolean.TRUE);
            editorialRepositorio.save(editorial);
        } else {
            throw new ErrorServicio("No se encontro la Editorial");
        }

    }

    @Transactional(rollbackOn = Exception.class)
    public List<Editorial> listarEditoriales() {
        return editorialRepositorio.findAll();

    }

    @Transactional(rollbackOn = Exception.class)
    public void validar(String nombre) throws ErrorServicio {

        if (nombre == null || nombre.isEmpty()) {
            throw new ErrorServicio("El nombre no puede estar vacio");
        }

    }

    @Transactional(rollbackOn = Exception.class)
    public Editorial buscarPorId(String id) throws ErrorServicio {

        Optional<Editorial> option = editorialRepositorio.findById(id);

        if (option.isPresent()) {
            Editorial editorial = option.get();
            return editorial;
        } else {
            throw new ErrorServicio("Editorial no Encontrada");
        }
    }

    @Transactional(rollbackOn = Exception.class)
    public void eliminarPorId(String id) throws ErrorServicio {
        List<Libro> libros = libroServicio.buscarPorEditorialId(id);

        if (libros.isEmpty()) {
            editorialRepositorio.delete(buscarPorId(id));
        } else {
            throw new ErrorServicio("La editorial no pude ser borrada porque tiene un libro asignado");
        }
    }



@Transactional(rollbackOn = Exception.class)
        public Editorial buscarPorNombre(String nombre) throws ErrorServicio{
        
        validar(nombre);
        Editorial editorial =editorialRepositorio.buscarPorNombre(nombre);
        
        
        return editorial;
        
    }
    
}
