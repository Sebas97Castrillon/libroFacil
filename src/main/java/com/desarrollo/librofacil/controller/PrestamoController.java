package com.desarrollo.librofacil.controller;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.desarrollo.librofacil.model.entity.Ejemplar;
import com.desarrollo.librofacil.model.entity.Libro;
import com.desarrollo.librofacil.model.entity.Prestamo;
import com.desarrollo.librofacil.model.entity.Usuario;
import com.desarrollo.librofacil.service.LibreriaService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/libreria")
@SessionAttributes("prestamo")
public class PrestamoController {

    @Autowired
    private LibreriaService libreriaService;

    @GetMapping("/nuevoprestamo/{id}")
    public String nuevoPrestamo(@PathVariable(name = "id") Long id, Model model, RedirectAttributes flash) {
        Usuario usuario = libreriaService.buscarUsuarioPorId(id);

        if (usuario == null) {
            flash.addFlashAttribute("error", "El usuario no existe en la base de datos !!");
            return "redirect:/libreria/listarusuarios";
        }
        Prestamo prestamo = new Prestamo();
        prestamo.setUsuario(usuario);
        model.addAttribute("titulo",
                "Nuevo préstamo: " + prestamo.getUsuario().getNombres() + " " + prestamo.getUsuario().getApellidos());
        model.addAttribute("accion", "Guardar préstamo");
        model.addAttribute("prestamo", prestamo);
        return "prestamo/nuevo_prestamo";
    }

    @PostMapping("/agregarprestamo")
    public String agregarPrestamo(@Valid @ModelAttribute("prestamo") Prestamo prestamo, BindingResult result,
            Model model, @RequestParam(name = "detalle_id[]", required = false) Long[] detalleId,
            RedirectAttributes flash,
            SessionStatus status) {

        if (result.hasErrors()) {
            model.addAttribute("titulo", "Nuevo préstamo");
            model.addAttribute("accion", "Guardar préstamo");
            model.addAttribute("info", "Corregir los errores del formulario !!");
            return "prestamo/nuevo_prestamo";
        }

        if (detalleId == null || detalleId.length == 0) {
            model.addAttribute("titulo", "Nueva préstamo");
            model.addAttribute("accion", "Guardar préstamo");
            model.addAttribute("warning", "El prestamo debe tener al menos un item!");
            return "prestamo/nuevo_prestamo";
        } else {
            if (detalleId.length > 1) {
                model.addAttribute("titulo", "Nueva préstamo");
                model.addAttribute("accion", "Guardar préstamo");
                model.addAttribute("warning", "Solo se presta un ejemplar a la vez!");
                return "prestamo/nuevo_prestamo";
            }
        }

        List<Ejemplar> ejemplares = new ArrayList<Ejemplar>();
        Ejemplar ejemplar = null;

        if (detalleId != null) {
            for (int i = 0; i < detalleId.length; i++) {
                ejemplares = libreriaService.buscarEjemplar(detalleId[i]);
            }
        }

        for (Ejemplar item : ejemplares) {
            if (item.isDisponible() == true) {
                ejemplar = item;
                prestamo.setEjemplar(ejemplar);

            }
        }

        if (ejemplar != null) {
            ejemplar.setDisponible(false);
        }

        if (ejemplar == null || detalleId.length == 0) {
            model.addAttribute("titulo", "Nuevo préstamo");
            model.addAttribute("accion", "Guardar préstamo");
            model.addAttribute("info", "No hay ejemplares disponibles !!");
            return "prestamo/nuevo_prestamo";
        }

        libreriaService.actualizarPrestamo(prestamo);
        status.setComplete();
        flash.addFlashAttribute("success", "Préstamo Guardado !!");
        return "redirect:/libreria/consultarusuario/" + prestamo.getUsuario().getId();

    }

    @GetMapping("/consultarprestamo/{id}")
    public String consultarPrestamo(@PathVariable(value = "id") Long id, Model model,
            RedirectAttributes flash) {
        Prestamo prestamo = libreriaService.buscarPrestamoPorId(id);
        if (prestamo == null) {
            flash.addFlashAttribute("error", "El préstamo no existe en la base de datos !!");
            return "redirect:/libreria/listarusuarios";
        }

        // int devolucion =
        // prestamo.getFechaDevolucion().compareTo(prestamo.getFechaDevolucion());
        Date hoy = new Date();
        int dias = (int) ((prestamo.getFechaDevolucion().getTime() - hoy.getTime()) / 86400000);
        if (dias == 0){
            model.addAttribute("devolucion","Recuerda Hoy es el último día del prestamo");
        }
        

        if (dias < 0) {
            model.addAttribute("devolucion", "Atención el prestamo venció hace " +
                    (dias * -1) + " dias");
        }
        if (dias > 0) {
            model.addAttribute("devolucion",
                    "Faltan " + dias + " dias");
        }
        

        model.addAttribute("prestamo", prestamo);
        model.addAttribute("titulo", "Préstamo No. " + prestamo.getId());

        return "prestamo/consulta_prestamo";
    }

    @GetMapping("/eliminarprestamo/{id}")
    public String eliminarPrestamo(@PathVariable(value = "id") Long id, RedirectAttributes flash) {
        Prestamo prestamo = libreriaService.buscarPrestamoPorId(id);
        if (prestamo != null) {
            libreriaService.eliminarPrestamoPorId(id);
            prestamo.getEjemplar().setDisponible(true);
            libreriaService.actualizarEjemplar(prestamo.getEjemplar());
            flash.addFlashAttribute("success", "El préstamo " + prestamo.getId() + " fue eliminado !   ");
        } else {
            flash.addFlashAttribute("error", "La préstamo con id " + id + "no existe !!");
        }

        return "redirect:/libreria/consultarusuario/" + prestamo.getUsuario().getId();

    }

    @GetMapping(value = "/cargarlibros/{term}", produces = "application/json")
    public @ResponseBody List<Libro> buscarLibros(@PathVariable(name = "term") String term) {
        return libreriaService.buscarLibro(term);
    }
}
