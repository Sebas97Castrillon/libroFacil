package com.desarrollo.librofacil.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.desarrollo.librofacil.model.dao.AutorDAO;
import com.desarrollo.librofacil.model.dao.EditorialDAO;
import com.desarrollo.librofacil.model.dao.EjemplarDAO;
import com.desarrollo.librofacil.model.dao.LibroDAO;
import com.desarrollo.librofacil.model.dao.PrestamoDAO;
import com.desarrollo.librofacil.model.dao.UsuarioDAO;
import com.desarrollo.librofacil.model.entity.Autor;
import com.desarrollo.librofacil.model.entity.Editorial;
import com.desarrollo.librofacil.model.entity.Ejemplar;
import com.desarrollo.librofacil.model.entity.Libro;
import com.desarrollo.librofacil.model.entity.Prestamo;
import com.desarrollo.librofacil.model.entity.Usuario;

@Service
public class LibreriaServiceImpl implements LibreriaService {

	@Autowired
	private UsuarioDAO usuarioDAO;

	@Autowired
	private LibroDAO libroDAO;

	@Autowired
	private PrestamoDAO prestamoDAO;

	@Autowired
	private EditorialDAO editorialDAO;

	@Autowired
	private EjemplarDAO ejemplarDAO;

	@Autowired
	private AutorDAO autorDAO;

	// Usuario
	@Override
	@Transactional(readOnly = true)
	public List<Usuario> listarUsuarios() {
		return (List<Usuario>) usuarioDAO.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Usuario> listarUsuariosPag(Pageable pageable) {
		return usuarioDAO.findAll(pageable);
	}

	@Override
	@Transactional
	public void actualizarUsuario(Usuario usuario) {
		usuarioDAO.save(usuario);
	}

	@Override
	@Transactional(readOnly = true)
	public Usuario buscarUsuarioPorId(Long id) {
		return usuarioDAO.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public void eliminarUsuarioPorId(Long id) {
		usuarioDAO.deleteById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public Long cantidadUsuarios() {
		return usuarioDAO.count();

	}

	// Libro

	@Override
	@Transactional(readOnly = true)
	public List<Libro> listarLibros() {
		return (List<Libro>) libroDAO.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Libro> listarLibrosPag(Pageable pageable) {
		return libroDAO.findAll(pageable);
	}

	@Override
	@Transactional
	public void actualizarLibro(Libro libro) {
		libroDAO.save(libro);
	}

	@Override
	@Transactional(readOnly = true)
	public Libro buscarLibroPorId(Long id) {
		return libroDAO.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public void eliminarLibroPorId(Long id) {
		libroDAO.deleteById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public Long cantidadLibros() {
		return libroDAO.count();

	}

	@Override
	@Transactional(readOnly = true)
	public List<Libro> buscarLibro(String term) {
		return libroDAO.buscarPorTitulo(term);
	}

	// Editorial

	@Override
	@Transactional(readOnly = true)
	public List<Editorial> buscarEditoriales() {
		return (List<Editorial>) editorialDAO.findAll();
	}

	// Autor

	@Override
	@Transactional(readOnly = true)
	public List<Autor> buscarAutores() {
		return (List<Autor>) autorDAO.findAll();
	}

	// Prestamo

	@Override
	public void actualizarPrestamo(Prestamo prestamo) {
		prestamoDAO.save(prestamo);
	}

	@Override
	@Transactional(readOnly = true)
	public Prestamo buscarPrestamoPorId(Long id) {
		return prestamoDAO.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public void eliminarPrestamoPorId(Long id) {
		prestamoDAO.deleteById(id);
	}

	// Ejemplar

	@Override
	@Transactional(readOnly = true)
	public Ejemplar buscarEjemplarPorId(Long id) {
		return ejemplarDAO.findById(id).orElse(null);
	}

	@Override
	public List<Ejemplar> buscarEjemplar(long id) {
		return ejemplarDAO.buscarPorEjemplar(id);
	}

	@Override
	public void actualizarEjemplar(Ejemplar ejemplar) {
		ejemplarDAO.save(ejemplar);
	}
}
