package com.desarrollo.librofacil.model.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.desarrollo.librofacil.model.entity.Prestamo;

@Repository
public interface PrestamoDAO extends JpaRepository<Prestamo, Long>{
}
