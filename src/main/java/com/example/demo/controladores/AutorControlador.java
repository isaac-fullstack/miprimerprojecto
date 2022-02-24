package com.example.demo.controladores;

import com.example.demo.entidades.Autor;
import com.example.demo.entidades.Libro;
import com.example.demo.errores.ErrorServicio;
import com.example.demo.servicios.AutorServicio;
import com.example.demo.servicios.LibroServicio;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/autor")
public class AutorControlador {

    private final AutorServicio autorServicio;
    private final LibroServicio libroServicio;

    @Autowired
    public AutorControlador(AutorServicio autorServicio, LibroServicio libroServicio) {
        this.autorServicio = autorServicio;
        this.libroServicio = libroServicio;
    }

    @GetMapping("/form")
    public String crearAutor(
            @RequestParam(required = false) String id,
            ModelMap model) {

       
            try {
                if (id != null && !id.isEmpty() && !id.equals(" ")) {
                model.addAttribute("id", id);
                Autor autor = autorServicio.buscarPorId(id);
                model.addAttribute("nombre", autor.getNombre());
                }
            } catch (ErrorServicio e) {
                model.addAttribute("error", e.getMessage());
            }

        
        return "/autor/crearAutor.html";

    }

    @PostMapping("/form")
    public String guardarAutor(
            @RequestParam(required = false) String id,
            @RequestParam String nombre,
            ModelMap model) {

        try {

            autorServicio.guardar(id, nombre);
        } catch (ErrorServicio e) {
            model.addAttribute("error", e.getMessage());
            return "/autor/crearAutor.html";
        }

        return "redirect:/autor/listado";
    }

    @GetMapping("/listado")
    public String mostrarListado(ModelMap model) {
        List<Autor> autores = autorServicio.listarTodos();
        model.addAttribute("autores", autores);
        return "autor/lista-autores.html";
    }

    @GetMapping("/baja/{id}")
    public String estadoAutor(@PathVariable("id") String id,
            ModelMap model) {

        System.out.println("Este sout funciona");
        try {
            Autor autor = autorServicio.buscarPorId(id);

            if (autor.getAlta()) {
                autorServicio.deshabilitar(id);
            } else {
                autorServicio.habilitar(id);
            }

        } catch (ErrorServicio e) {
            model.addAttribute("error", e.getMessage());
        }

        return "redirect:/autor/listado";
    }

    // este metodo esta en construccion
    @GetMapping("/eliminar")
    public String eliminar(
            @RequestParam String id,
            RedirectAttributes attr) {

        try {

            autorServicio.eliminarAutor(id);

        } catch (ErrorServicio e) {
            attr.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/autor/listado";
    }
}
