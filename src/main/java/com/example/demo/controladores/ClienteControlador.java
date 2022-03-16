package com.example.demo.controladores;

import com.example.demo.entidades.Cliente;
import com.example.demo.entidades.Libro;
import com.example.demo.entidades.Prestamo;
import com.example.demo.errores.ErrorServicio;
import com.example.demo.servicios.ClienteServicio;
import com.example.demo.servicios.LibroServicio;
import com.example.demo.servicios.PrestamoServicio;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/cliente")
public class ClienteControlador {

    private final ClienteServicio clienteServicio;
    private final PrestamoServicio prestamoServicio;
    private final LibroServicio libroServicio;

    @Autowired
    public ClienteControlador(ClienteServicio clienteServicio, PrestamoServicio prestamoServicio, LibroServicio libroServicio) {
        this.clienteServicio = clienteServicio;
        this.prestamoServicio = prestamoServicio;
        this.libroServicio = libroServicio;
    }

    @GetMapping("/registro")
    public String registro(ModelMap model) {
        model.addAttribute("cliente", new Cliente());
        return ("registro.html");
    }

    @PostMapping("/registro")
    public String guardarCliente(
            ModelMap model,
            @RequestParam(required = false) String id,
            @RequestParam String nombre,
            @RequestParam String apellido,
            @RequestParam String telefono,
            @RequestParam String email,
            @RequestParam String clave) {

        try {
            clienteServicio.guardar(id, nombre, apellido, telefono, email, clave);
        } catch (ErrorServicio e) {
            model.addAttribute("error", e.getMessage());
            return "registro.html";
        }
        return "redirect:/cliente/listado";
    }

    @GetMapping("/listado")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public String mostrarListado(ModelMap model) {
        List<Cliente> clientes = clienteServicio.listarTodos();
        clientes.sort(Comparator.comparing(Cliente::getNombre));
        model.addAttribute("clientes", clientes);
        return "cliente/lista-clientes.html";
    }

    @GetMapping("/perfil")
    public String mostrarDatos(
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

        return "/cliente/perfil";
    }
}
