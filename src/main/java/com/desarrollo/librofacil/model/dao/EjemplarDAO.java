package com.desarrollo.librofacil.model.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.desarrollo.librofacil.model.entity.Ejemplar;

@Repository
public interface EjemplarDAO extends JpaRepository<Ejemplar, Long> {

    @Query("select e from Ejemplar e join fetch e.libro l where l.id = ?1")
    public List<Ejemplar> buscarPorEjemplar(Long id);

    // @Query("select e from Ejemplar e join fetch e.libro l where l.titulo like
    // %?1%")
    // public List<Libro> buscarPorEjemplar(String term);

}
