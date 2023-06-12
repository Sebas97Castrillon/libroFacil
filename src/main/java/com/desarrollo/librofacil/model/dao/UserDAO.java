package com.desarrollo.librofacil.model.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.desarrollo.librofacil.model.entity.RolUsuario;

@Repository
public interface UserDAO extends JpaRepository<RolUsuario, Long>{

	@Query("select u from RolUsuario u where u.nombre = ?1")
	public RolUsuario buscarRolUsuarioPorNombre(String nombre);
	
	public RolUsuario findByNombre(String nombre);
}