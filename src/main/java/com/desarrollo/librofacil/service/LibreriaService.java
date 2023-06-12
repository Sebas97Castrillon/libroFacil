package com.desarrollo.librofacil.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.desarrollo.librofacil.model.entity.Autor;
import com.desarrollo.librofacil.model.entity.Editorial;
import com.desarrollo.librofacil.model.entity.Ejemplar;
import com.desarrollo.librofacil.model.entity.Libro;
import com.desarrollo.librofacil.model.entity.Prestamo;
import com.desarrollo.librofacil.model.entity.Usuario;

public interface LibreriaService {
	// Usuario

	public List<Usuario> listarUsuarios();

	public Page<Usuario> listarUsuariosPag(Pageable pageable);

	public void actualizarUsuario(Usuario usuario);

	public Usuario buscarUsuarioPorId(Long id);

	public void eliminarUsuarioPorId(Long id);

	public Long cantidadUsuarios();

	// Libro

	public List<Libro> listarLibros();

	public Page<Libro> listarLibrosPag(Pageable pageable);

	public void actualizarLibro(Libro libro);

	public Libro buscarLibroPorId(Long id);

	public void eliminarLibroPorId(Long id);

	public Long cantidadLibros();

	public List<Libro> buscarLibro(String term);

	// Editorial

	public List<Editorial> buscarEditoriales();

	// Editorial

	public List<Autor> buscarAutores();

	// Prestamo

    public void actualizarPrestamo(Prestamo prestamo);

    public Prestamo buscarPrestamoPorId(Long id);

    public void eliminarPrestamoPorId(Long id);

	// Ejemplar

	public Ejemplar buscarEjemplarPorId(Long id);

	public void actualizarEjemplar(Ejemplar ejemplar);

	public List<Ejemplar> buscarEjemplar(long id);

}
