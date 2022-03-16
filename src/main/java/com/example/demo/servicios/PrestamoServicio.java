
package com.example.demo.servicios;

import com.example.demo.entidades.Cliente;
import com.example.demo.entidades.Libro;
import com.example.demo.entidades.Prestamo;
import com.example.demo.errores.ErrorServicio;
import com.example.demo.repositorios.PrestamoRepositorio;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

@Service
public class PrestamoServicio {
    
    private final PrestamoRepositorio prestamoRepositorio;
    private final LibroServicio libroServicio;

    @Autowired
    public PrestamoServicio(PrestamoRepositorio prestamoRepositorio, LibroServicio libroServicio) {
        this.prestamoRepositorio = prestamoRepositorio;
        this.libroServicio = libroServicio;
    }
    
    @Transactional(rollbackOn = Exception.class)
    public void guardar(Libro libro, Cliente cliente) throws ErrorServicio{
        
        if (!libro.getAlta()) {
            throw new ErrorServicio("Libro no disponible");
        }
        if (!cliente.getAlta()) {
            throw new ErrorServicio("No esta autorizado a pedir Prestamos, comuniquese con el Bibliotecario");
        }
        if (prestado(libro, cliente)) {
            throw new ErrorServicio("Ya tiene este libro: " + libro.getTitulo());
        }
        if (libro.getEjemplaresRestantes() <= 0) {
            throw new ErrorServicio("No quedan Ejemplares disponibles");
        }
        libroServicio.prestar(libro);
                
        
        Prestamo prestamo = new Prestamo();
        prestamo.setFechaPrestamo(LocalDateTime.now());
        prestamo.setAlta(Boolean.TRUE);
        prestamo.setCliente(cliente);
        prestamo.setLibro(libro);
        
        prestamoRepositorio.save(prestamo);
        
    }
    
    @Transactional(rollbackOn = Exception.class)
    public void devolver(String id){
        Prestamo prestamo = prestamoRepositorio.getById(id);
        prestamo.setAlta(Boolean.FALSE);
        prestamo.setFechaDevolucion(LocalDateTime.now());
        libroServicio.devolver(prestamo.getLibro());
        prestamoRepositorio.save(prestamo);
    }
    
    public List<Prestamo> buscarPorCliente(String id){
        return prestamoRepositorio.buscarPorCliente(id);
    }
    
    public Boolean prestado(Libro libro, Cliente cliente){
        
        Boolean prestado = false;
        
        List<Prestamo> prestamos = prestamoRepositorio.buscarPorLibro(libro.getId());
        
        for (Prestamo prestamo : prestamos) {
            System.out.print("el libro es: " + prestamo.getLibro().getTitulo());
            System.out.println(" y el alta es: " + prestamo.getAlta());
            if (prestamo.getAlta() && cliente.getId().equals(prestamo.getCliente().getId()) && libro.getId().equals(prestamo.getLibro().getId())) {
                prestado = true;
                System.out.println("prestado es " + prestado.toString());
            }
        }
       
    
        
        return prestado;
    }
    
    public List<Prestamo> buscarTodos(){
        return prestamoRepositorio.findAll();
    }
    
    public List<Prestamo> buscarActivos(List<Prestamo> prestamos){
        
        
        List<Prestamo> prestamosActivos = new ArrayList<>();
        
        for (Prestamo prestamo : prestamos) {
            if (prestamo.getAlta()) {
                prestamosActivos.add(prestamo);
            }
        }

        return prestamosActivos;
    }
    
    public List<Prestamo> listarOrdenadosPorFecha(){
        
        return prestamoRepositorio.buscarTodosOrdenadosPorFecha();
        
    }
    public List<Prestamo> listarActivosOrdenadosPorFecha(){
        
        return prestamoRepositorio.buscarTodosActivosOrdenadosPorFecha();
        
    }
    
    
    public List<Prestamo> ordenarPorNombreCliente(List<Prestamo> prestamos){
        
        //prestamos.sort();
        
        
        return prestamos;
    }
    
}
