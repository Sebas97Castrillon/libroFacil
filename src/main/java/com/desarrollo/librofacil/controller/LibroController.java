package com.desarrollo.librofacil.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.desarrollo.librofacil.model.entity.Autor;
import com.desarrollo.librofacil.model.entity.Editorial;
import com.desarrollo.librofacil.model.entity.Libro;
import com.desarrollo.librofacil.service.LibreriaService;
import com.desarrollo.librofacil.utils.paginator.PageRender;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/libreria")
@SessionAttributes("libro")
public class LibroController {

    @Autowired
    private LibreriaService libreriaService;

    private Logger log = LoggerFactory.getLogger(getClass());

    @GetMapping("/listarlibros")
    public String listarLibros(@RequestParam(value = "pag", defaultValue = "0") int pag, Model model,
            Authentication authentication, HttpServletRequest request) {

        // Security check

        if (authentication != null) {
            log.info("Usuario autenticado '" + authentication.getName() + "'");
        }

        if (!roles().equals("")) {
            log.info("Los roles del usuario " + authentication.getName() + " son " + roles());
        }

        log.info((tieneRol("ROLE_ADMIN") ? authentication.getName() + " es administrador"
                : authentication.getName() + " no es administrador"));

        SecurityContextHolderAwareRequestWrapper securityContext = new SecurityContextHolderAwareRequestWrapper(request,
                "ROLE_");
        if (securityContext.isUserInRole("ADMIN"))
            log.info(authentication.getName() + " es administrador **");
        else
            log.info(authentication.getName() + " no es administrador **");

        // Paginator
        
        Pageable pagina = PageRequest.of(pag, 5);
        Page<Libro> libros = libreriaService.listarLibrosPag(pagina);
        Long cantidad = libreriaService.cantidadLibros();

        PageRender<Libro> pageRender = new PageRender<>("/libreria/listarlibros", libros);

        model.addAttribute("cantidad", cantidad);
        model.addAttribute("titulo", "Listado de libros");
        model.addAttribute("libros", libros);
        model.addAttribute("pageRender", pageRender);
        return "libro/listado_libros";
    }

    @GetMapping("/nuevolibro")
    public String nuevoLibro(Model model) {
        model.addAttribute("titulo", "Nuevo libro");
        model.addAttribute("accion", "Crear");
        model.addAttribute("libro", new Libro());
        return "libro/formulario_libro";
    }

    @PostMapping("/agregarlibro")
    public String agregarLibro(@Valid @ModelAttribute("libro") Libro libro, BindingResult result,
            Model model, @RequestParam("file") MultipartFile caratula, RedirectAttributes flash, SessionStatus status) {

        String accion = (libro.getId() == null) ? "Crear" : "Modificar";

        if (result.hasErrors()) {
            model.addAttribute("titulo", "Nuevo libro");
            model.addAttribute("accion", accion);
            model.addAttribute("info", "Corregir los errores del formulario !!");
            return "libro/formulario_libro";
        }
        if (libro.getAutor() == null) {
            model.addAttribute("titulo", "Nuevo libro");
            model.addAttribute("accion", accion);
            model.addAttribute("info", "Debe ingresar el autor del libro !!");
            return "libro/formulario_libro";
        }
        if (caratula.isEmpty()) {
            model.addAttribute("titulo", "Nuevo libro");
            model.addAttribute("accion", accion);
            model.addAttribute("libro", libro);
            model.addAttribute("info", "Debe ingresar una caratula del libro !!");
            return "libro/formulario_libro";
        } else {
            if (caratula.getSize() > 10485760) {
                model.addAttribute("titulo", "Nuevo libro");
                model.addAttribute("accion", accion);
                model.addAttribute("libro", libro);
                model.addAttribute("info", "El tamaño de la caratula no puede ser mayor a 10MB!!");
                return "libro/formulario_libro";
            } else {
                if (caratula.getContentType().equals("image/jpeg")
                        || caratula.getContentType().equals("image/png")) {
                    if (libro.getId() != null && libro.getId() > 0 && libro.getCaratula() != null
                            && libro.getCaratula().length() > 0) {
                        Path rutaAbsoluta = Paths.get("uploads").resolve(libro.getCaratula())
                                .toAbsolutePath();
                        File archivo = rutaAbsoluta.toFile();

                        if (archivo.exists() && archivo.canRead()) {
                            archivo.delete();
                        }
                    }
                    String nombreUnico = UUID.randomUUID().toString() + "_" + caratula.getOriginalFilename();

                    Path rutaUploads = Paths.get("uploads").resolve(nombreUnico);
                    Path rutaAbsoluta = rutaUploads.toAbsolutePath();
                    log.info("rutaUploads: " + rutaUploads);
                    log.info("rutaAbsoluta: " + rutaAbsoluta);

                    try {
                        Files.copy(caratula.getInputStream(), rutaAbsoluta);
                        libro.setCaratula(nombreUnico);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } else {
                    model.addAttribute("titulo", "Nuevo libro");
                    model.addAttribute("accion", accion);
                    model.addAttribute("libro", libro);
                    model.addAttribute("info", "Formato de archivo no permitido, solo jpg y png!!");
                    return "libro/formulario_libro";
                }

            }

        }

        if (libro.getId() != null && caratula.isEmpty()) {
            libro.setCaratula(libreriaService.buscarLibroPorId(libro.getId()).getCaratula());
        }

        if (libro.getId() == null && caratula.isEmpty()) {
            libro.setCaratula("");
        }

        libreriaService.actualizarLibro(libro);
        status.setComplete();
        flash.addFlashAttribute("success", "libro guardado con exito !!");
        return "redirect:/libreria/listarlibros";
    }

    @GetMapping("/consultarlibro/{id}")
    public String consultarLibro(@PathVariable(value = "id") Long id, Model model, RedirectAttributes flash) {
        Libro libro = libreriaService.buscarLibroPorId(id);
        if (libro == null) {
            flash.addFlashAttribute("error", "El libro no existe en la BD !!");
            return "redirect:/libreria/listarlibros";
        }
        model.addAttribute("titulo", "Consulta del libro: " + libro.getTitulo());
        model.addAttribute("libro", libro);

        return "libro/consulta_libro";
    }

    @GetMapping("/eliminarlibro/{id}")
    public String eliminarLibro(@PathVariable(value = "id") Long id, RedirectAttributes flash) {
        if (id > 0) {
            libreriaService.eliminarLibroPorId(id);
            flash.addFlashAttribute("success", "Libro eliminado!!");
        }
        return "redirect:/libreria/listarlibros";
    }

    @GetMapping("/modificarlibro/{id}")
    public String modificarLibro(@PathVariable(value = "id") Long id,
            Model model, RedirectAttributes flash) {
        Libro libro = null;
        if (id > 0) {
            libro = libreriaService.buscarLibroPorId(id);
            if (libro == null) {
                flash.addFlashAttribute("error", "El libro no existe en la BD !!");
                return "redirect:/libreria/listarlibros";
            }
        } else {
            flash.addFlashAttribute("error", "El ID del libro debe ser positivo !!");
            return "redirect:/libreria/listarlibros";
        }
        model.addAttribute("accion", "Modificar");
        model.addAttribute("titulo", "Modificar libro: " + libro.getTitulo());
        model.addAttribute("libro", libro);
        return "libro/formulario_libro";
    }

    @ModelAttribute("editoriales")
    public List<Editorial> obtenerEditoriales() {
        return libreriaService.buscarEditoriales();
    }

    @ModelAttribute("autores")
    public List<Autor> obtenerAutores() {
        return libreriaService.buscarAutores();
    }

    private String roles() {
        SecurityContext context = SecurityContextHolder.getContext();

        if (context == null || context.getAuthentication() == null) {
            return "";
        }

        Collection<? extends GrantedAuthority> authorities = context.getAuthentication().getAuthorities();

        String cad = "";

        for (GrantedAuthority auth : authorities) {
            if (cad.length() > 0)
                cad += ", ";
            cad += auth.getAuthority();
        }
        return cad;
    }

    private boolean tieneRol(String rol) {
        SecurityContext context = SecurityContextHolder.getContext();

        if (context == null || context.getAuthentication() == null) {
            return false;
        }

        Collection<? extends GrantedAuthority> authorities = context.getAuthentication().getAuthorities();

        return authorities.contains(new SimpleGrantedAuthority(rol));
    }
}