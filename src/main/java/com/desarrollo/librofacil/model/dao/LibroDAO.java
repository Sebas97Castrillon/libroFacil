package com.desarrollo.librofacil.model.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.desarrollo.librofacil.model.entity.Libro;

@Repository
public interface LibroDAO extends JpaRepository<Libro, Long>{

    @Query("select distinct l from Libro l where l.titulo like ?1%")
	public List<Libro> buscarPorTitulo(String term);
    
}
