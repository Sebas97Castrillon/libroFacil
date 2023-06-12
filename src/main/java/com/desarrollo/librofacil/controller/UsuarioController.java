package com.desarrollo.librofacil.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
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

import com.desarrollo.librofacil.model.entity.Usuario;
import com.desarrollo.librofacil.service.LibreriaService;
import com.desarrollo.librofacil.utils.paginator.PageRender;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/libreria")
@SessionAttributes("usuario")
public class UsuarioController {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private LibreriaService libreriaService;

    @GetMapping("/listarusuarios")
    public String listarUsuarios(@RequestParam(value = "pag", defaultValue = "0") int pag, Model model,
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
        Page<Usuario> usuarios = libreriaService.listarUsuariosPag(pagina);
        Long cantidad = libreriaService.cantidadUsuarios();

        PageRender<Usuario> pageRender = new PageRender<>("/libreria/listarusuarios", usuarios);

        model.addAttribute("cantidad", cantidad);
        model.addAttribute("titulo", "Listado de usuarios");
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("pageRender", pageRender);
        return "usuario/listado_usuarios";
    }

    @GetMapping("/nuevousuario")
    public String nuevoUsuario(Model model) {
        model.addAttribute("titulo", "Nuevo usuario");
        model.addAttribute("accion", "Crear");
        model.addAttribute("usuario", new Usuario());
        return "usuario/formulario_usuario";
    }

    @PostMapping("/agregarusuario")
    public String agregarUsuario(@Valid @ModelAttribute("usuario") Usuario usuario, BindingResult result,
            Model model, @RequestParam("file") MultipartFile foto, RedirectAttributes flash, SessionStatus status) {

        String accion = (usuario.getId() == null) ? "Crear" : "Modificar";

        if (result.hasErrors()) {
            model.addAttribute("titulo", "Nuevo usuario");
            model.addAttribute("accion", accion);
            model.addAttribute("info", "Corregir los errores del formulario !!");
            return "usuario/formulario_usuario";
        }

        if (foto.isEmpty()) {
            model.addAttribute("titulo", "Nuevo usuario");
            model.addAttribute("accion", accion);
            model.addAttribute("usuario", usuario);
            model.addAttribute("info", "Debe ingresar una foto del usuario !!");
            return "usuario/formulario_usuario";
        } else {
            if (foto.getSize() > 10485760) {
                model.addAttribute("titulo", "Nuevo usuario");
                model.addAttribute("accion", accion);
                model.addAttribute("usuario", usuario);
                model.addAttribute("info", "El tamaño de la foto no puede ser mayor a 10MB!!");
                return "usuario/formulario_usuario";
            } else {
                if (foto.getContentType().equals("image/jpeg")
                        || foto.getContentType().equals("image/png")) {
                    if (usuario.getId() != null && usuario.getId() > 0 && usuario.getFoto() != null
                            && usuario.getFoto().length() > 0) {
                        Path rutaAbsoluta = Paths.get("uploads").resolve(usuario.getFoto())
                                .toAbsolutePath();
                        File archivo = rutaAbsoluta.toFile();

                        if (archivo.exists() && archivo.canRead()) {
                            archivo.delete();
                        }
                    }
                    String nombreUnico = UUID.randomUUID().toString() + "_" + foto.getOriginalFilename();

                    Path rutaUploads = Paths.get("uploads").resolve(nombreUnico);
                    Path rutaAbsoluta = rutaUploads.toAbsolutePath();
                    log.info("rutaUploads: " + rutaUploads);
                    log.info("rutaAbsoluta: " + rutaAbsoluta);

                    try {
                        Files.copy(foto.getInputStream(), rutaAbsoluta);
                        usuario.setFoto(nombreUnico);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } else {
                    model.addAttribute("titulo", "Nuevo usuario");
                    model.addAttribute("accion", accion);
                    model.addAttribute("usuario", usuario);
                    model.addAttribute("info", "Formato de archivo no permitido, solo jpg y png!!");
                    return "usuario/formulario_usuario";
                }

            }

        }

        if (usuario.getId() != null && foto.isEmpty()) {
            usuario.setFoto(libreriaService.buscarUsuarioPorId(usuario.getId()).getFoto());
        }

        if (usuario.getId() == null && foto.isEmpty()) {
            usuario.setFoto("");
        }

        libreriaService.actualizarUsuario(usuario);
        status.setComplete();
        flash.addFlashAttribute("success", accion + " usuario, proceso exitoso !!");
        return "redirect:/libreria/listarusuarios";
    }

    @GetMapping("/consultarusuario/{id}")
    public String consultarUsuario(@PathVariable(value = "id") Long id, Model model, RedirectAttributes flash) {
        Usuario usuario = libreriaService.buscarUsuarioPorId(id);
        if (usuario == null) {
            flash.addFlashAttribute("error", "El cliente no existe en la BD !!");
            return "redirect:/libreria/listarusuarios";
        }

        model.addAttribute("titulo", "Consulta del usuario: " + usuario.getNombres() + " " + usuario.getApellidos());
        model.addAttribute("usuario", usuario);

        return "usuario/consulta_usuario";
    }

    @GetMapping("/eliminarusuario/{id}")
    public String eliminarUsuario(@PathVariable(value = "id") Long id, RedirectAttributes flash) {
        if (id > 0) {
            libreriaService.eliminarUsuarioPorId(id);
            flash.addFlashAttribute("success", "Usuario eliminado!!");
        }
        return "redirect:/libreria/listarusuarios";
    }

    @GetMapping("/modificarusuario/{id}")
    public String modificarUsuario(@PathVariable(value = "id") Long id,
            Model model, RedirectAttributes flash) {
        Usuario usuario = null;
        if (id > 0) {
            usuario = libreriaService.buscarUsuarioPorId(id);
            if (usuario == null) {
                flash.addFlashAttribute("error", "El usuario no existe en la BD !!");
                return "redirect:/libreria/listarusuarios";
            }
        } else {
            flash.addFlashAttribute("error", "El ID del usuario debe ser positivo !!");
            return "redirect:/libreria/listarusuarios";
        }

        model.addAttribute("accion", "Modificar");
        model.addAttribute("titulo", "Modificar usuario");
        model.addAttribute("usuario", usuario);
        return "usuario/formulario_usuario";
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