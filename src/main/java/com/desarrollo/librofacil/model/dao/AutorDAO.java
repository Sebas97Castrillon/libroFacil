package com.desarrollo.librofacil.model.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.desarrollo.librofacil.model.entity.Autor;

@Repository
public interface AutorDAO extends JpaRepository<Autor, Long>{
    
}
