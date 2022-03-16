package com.example.demo.controladores;

import com.example.demo.entidades.Autor;
import com.example.demo.entidades.Cliente;
import com.example.demo.entidades.Editorial;
import com.example.demo.entidades.Libro;
import com.example.demo.entidades.Prestamo;
import com.example.demo.errores.ErrorServicio;
import com.example.demo.servicios.AutorServicio;
import com.example.demo.servicios.EditorialServicio;
import com.example.demo.servicios.LibroServicio;
import com.example.demo.servicios.PrestamoServicio;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
@RequestMapping("/prestamo")
public class PrestamoControlador {

    private final PrestamoServicio prestamoServicio;
    private final LibroServicio libroServicio;
    private final AutorServicio autorServicio;
    private final EditorialServicio editorialServicio;

    
    @Autowired
    public PrestamoControlador(PrestamoServicio prestamoServicio, LibroServicio libroServicio, AutorServicio autorServicio, EditorialServicio editorialServicio) {
        this.prestamoServicio = prestamoServicio;
        this.libroServicio = libroServicio;
        this.autorServicio = autorServicio;
        this.editorialServicio = editorialServicio;
    }

    

    @GetMapping()
    public String solicitud(
            ModelMap model,
            @RequestParam() String id,
            HttpSession sesion) {

        Cliente cliente = (Cliente) sesion.getAttribute("clienteSesion");

        try {
            Libro libro = libroServicio.buscarPorId(id);
            prestamoServicio.guardar(libro, cliente);
        } catch (ErrorServicio e) {
            List<Libro> libros = libroServicio.listarTodos();
            model.addAttribute("libros", libros);
            model.addAttribute("error", e.getMessage());
            return "/libro/lista-libros.html";
        }

        return "redirect:/cliente/perfil";
    }

    @GetMapping("/devolver")
    public String devolver(@RequestParam String id) {

        prestamoServicio.devolver(id);

        return "redirect:/cliente/perfil";
    }
    
    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/listado")
    public String mostrarPrestamos(ModelMap model){
        
        List<Prestamo> prestamos = new ArrayList<>();
        List<Prestamo> prestamosActivos = new ArrayList<>();
        
        
        prestamos.addAll(prestamoServicio.listarActivosOrdenadosPorFecha());
        
        model.addAttribute("prestamos", prestamos);
        
        return "/prestamo/lista-prestamos.html";
    }
    
    @GetMapping("/listado/cliente")
    public String prestamosPorClientes(
            HttpSession session,
            ModelMap model) {

        Cliente cliente = (Cliente) session.getAttribute("clienteSesion");

        List<Prestamo> prestamos = prestamoServicio.buscarPorCliente(cliente.getId());
        //prestamos.sort(Comparator.comparing(Prestamo::getFechaPrestamo));
        //si lo ordeno me genera errores, caracteristica a evaluar.
        //List<Prestamo> prestados = new ArrayList();
        //List<Prestamo> devueltos = new ArrayList();
        List<Libro> librosPrestados = new ArrayList();

        for (Prestamo prestamo : prestamos) {
            librosPrestados.add(prestamo.getLibro());
        }
        
        model.addAttribute("librosPrestados", librosPrestados);
        model.addAttribute("prestamos", prestamos);

        return "/prestamo/lista-prestamos-por-cliente.html";
    }

}
