package com.example.demo.controladores;

import com.example.demo.entidades.Editorial;
import com.example.demo.errores.ErrorServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.example.demo.servicios.EditorialServicio;
import java.util.Comparator;
import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/editorial")
@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
public class EditorialControlador {

    private final EditorialServicio editorialServicio;

    @Autowired
    public EditorialControlador(EditorialServicio editorialServicio) {
        this.editorialServicio = editorialServicio;
    }

    @GetMapping("/form")
    public String crearEditorial(
            @RequestParam(required = false) String id,
            ModelMap model) {

        try {
            if (id != null) {
                model.addAttribute("id", id);
                Editorial editorial = editorialServicio.buscarPorId(id);
                model.addAttribute("nombre", editorial.getNombre());
            }
        } catch (ErrorServicio e) {
            model.addAttribute("error", e.getMessage());
        }

        return ("editorial/crearEditorial.html");
    }

    @PostMapping("/form")
    public String guardarEditorial(
            @RequestParam(required = false) String id,
            @RequestParam String nombre,
            ModelMap model) {
        try {
            editorialServicio.guardar(id, nombre);
        } catch (ErrorServicio e) {
            List<Editorial> editoriales = editorialServicio.listarEditoriales();
            editoriales.sort(Comparator.comparing(Editorial::getNombre));
            model.addAttribute("editoriales", editoriales);
            model.addAttribute("error", e.getMessage());
            return ("editorial/lista-editoriales.html");
        }

        return "redirect:/editorial/listado";
    }

    @GetMapping("/listado")
    public String listarEditoriales(ModelMap model) {
        List<Editorial> editoriales = editorialServicio.listarEditoriales();
        editoriales.sort(Comparator.comparing(Editorial::getNombre));
        model.addAttribute("editoriales", editoriales);
        return ("editorial/lista-editoriales.html");
    }

    @GetMapping("/baja/{id}")
    public String estadoEditorial(
            @PathVariable("id") String id,
            ModelMap model) {

        try {
            Editorial editorial = editorialServicio.buscarPorId(id);
            if (editorial.getAlta()) {
                editorialServicio.deshabilitar(id);
            } else {
                editorialServicio.habilitar(id);
            }
        } catch (ErrorServicio e) {
            model.addAttribute("error", e.getMessage());
        }

        return "redirect:/editorial/listado";
    }

    @GetMapping("/eliminar")
    public String eliminar(
            @RequestParam String id,
            RedirectAttributes attr) {

        try {
            editorialServicio.eliminarPorId(id);
        } catch (ErrorServicio e) {
            attr.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/editorial/listado";
    }

}
