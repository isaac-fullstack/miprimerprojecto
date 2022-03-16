package com.example.demo.controladores;

import com.example.demo.entidades.Autor;
import com.example.demo.entidades.Editorial;
import com.example.demo.entidades.Libro;
import com.example.demo.errores.ErrorServicio;
import com.example.demo.servicios.AutorServicio;
import com.example.demo.servicios.EditorialServicio;
import com.example.demo.servicios.LibroServicio;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/libro")
@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
public class LibroControlador {

    private final LibroServicio libroServicio;
    private final AutorServicio autorServicio;
    private final EditorialServicio editorialServicio;

    @Autowired
    public LibroControlador(LibroServicio libroServicio, AutorServicio autorServicio, EditorialServicio editorialServicio) {
        this.libroServicio = libroServicio;
        this.autorServicio = autorServicio;
        this.editorialServicio = editorialServicio;
    }

    @GetMapping("/form")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public String crearLibro(
            @RequestParam(required = false) String id,
            ModelMap model) {

        try {
            if (id != null) {

                Libro libro = libroServicio.buscarPorId(id);
                model.addAttribute("id", id);
                model.addAttribute("titulo", libro.getTitulo());
                model.addAttribute("isbn", libro.getIsbn());
                model.addAttribute("anio", libro.getAnio());
                model.addAttribute("ejemplares", libro.getEjemplares());
                model.addAttribute("ejemplaresPrestados", libro.getEjemplaresPrestados());
                model.addAttribute("ejemplaresRestantes", libro.getEjemplaresRestantes());
                model.addAttribute("autorId", libro.getAutor().getId());
                model.addAttribute("editorialId", libro.getEditorial().getId());

            }
        } catch (ErrorServicio e) {
            model.addAttribute("error", e.getMessage());
        }

        List<Autor> autores = autorServicio.listarTodos();
        model.addAttribute("autores", autores);

        List<Editorial> editoriales = editorialServicio.listarEditoriales();
        model.addAttribute("editoriales", editoriales);

        return "/libro/crearLibro.html";
    }

    @PostMapping("/form")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public String guardarLibro(
            @RequestParam(required = false) String id,
            @RequestParam(required = false) Long isbn,
            @RequestParam(required = false) String titulo,
            @RequestParam(required = false) Integer anio,
            @RequestParam(required = false) Integer ejemplares,
            @RequestParam(required = false) Integer ejemplaresPrestados,
            @RequestParam(required = false) String autorId,
            @RequestParam(required = false) String editorialId,
            ModelMap model,
            RedirectAttributes attr) {

        try {
            Autor autor = autorServicio.buscarPorId(autorId);
            Editorial editorial = editorialServicio.buscarPorId(editorialId);
            libroServicio.guardar(id, isbn, titulo, anio, ejemplares, ejemplaresPrestados, autor, editorial);

        } catch (ErrorServicio e) {
            attr.addFlashAttribute("error", e.getMessage());
            attr.addFlashAttribute("isbn", isbn);
            attr.addFlashAttribute("titulo", titulo);
            attr.addFlashAttribute("anio", anio);
            attr.addFlashAttribute("ejemplares", ejemplares);
            attr.addFlashAttribute("ejemplaresPrestados", ejemplaresPrestados);
            attr.addFlashAttribute("autorId", autorId);
            attr.addFlashAttribute("editorialId", editorialId);
            return "redirect:/libro/form";
        }

        return "redirect:/libro/listado";
    }

    @GetMapping("/listado")
    public String mostrarListado(
            @RequestParam(required = false) String palabra,
            ModelMap model) {

        List<Libro> libros = new ArrayList<>();

        if (palabra == null || palabra.isEmpty() || palabra.equalsIgnoreCase(" ")) {

            libros.addAll(libroServicio.listarTodos());

        } else {
            libros.addAll(libroServicio.buscarPorPalabra(palabra));
        }

        libros.sort(Comparator.comparing(Libro::getTitulo));
        model.addAttribute("libros", libros);

        List<Autor> autores = autorServicio.listarTodos();
        autores.sort(Comparator.comparing(Autor::getNombre));
        model.addAttribute("autores", autores);

        List<Editorial> editoriales = editorialServicio.listarEditoriales();
        editoriales.sort(Comparator.comparing(Editorial::getNombre));
        model.addAttribute("editoriales", editoriales);

        return "/libro/lista-libros.html";
    }

    @GetMapping("/baja/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public String cambiarEstado(
            @PathVariable("id") String id,
            RedirectAttributes attr) {

        try {
            Libro libro = libroServicio.buscarPorId(id);

            if (libro.getAlta()) {
                libroServicio.deshabilitar(id);
            } else {
                libroServicio.habilitar(id);
            }

        } catch (ErrorServicio e) {
            attr.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/libro/listado";
    }

    @GetMapping("/eliminar")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public String eliminar(
            @RequestParam String id,
            RedirectAttributes attr) {
        try {
            libroServicio.eliminarLibro(id);
        } catch (ErrorServicio e) {
            attr.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/libro/listado";
    }
}
