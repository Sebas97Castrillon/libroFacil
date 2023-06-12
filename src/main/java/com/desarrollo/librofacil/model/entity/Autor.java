package com.desarrollo.librofacil.model.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "autores")
public class Autor implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", length = 100, nullable = false)
    private String nombre;

    @Column(name = "apellido", length = 100, nullable = false)
    private String apellido;

    @Column(name = "pais", length = 100, nullable = false)
    private String pais;

    @Column(name = "ciudad", length = 100)
    private String ciudad;

    @Column(name = "fecha_nacimiento", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date fechaNacimiento;

    @Column(name = "fecha_fallecimiento")
    @Temporal(TemporalType.DATE)
    private Date fechaFallecimineto;

    @Column(name = "estudios", length = 100)
    private String estudios;

    // Relaciones

    @ManyToMany(mappedBy = "autor", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    public List<Libro> libro;

    public Autor() {
        libro = new ArrayList<>();
    }

    public Autor(Long id, String nombre, String apellido, String pais, String ciudad, Date fechaNacimiento,
            Date fechaFallecimineto, String estudios) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.pais = pais;
        this.ciudad = ciudad;
        this.fechaNacimiento = fechaNacimiento;
        this.fechaFallecimineto = fechaFallecimineto;
        this.estudios = estudios;
        libro = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public Date getFechaFallecimineto() {
        return fechaFallecimineto;
    }

    public void setFechaFallecimineto(Date fechaFallecimineto) {
        this.fechaFallecimineto = fechaFallecimineto;
    }

    public String getEstudios() {
        return estudios;
    }

    public void setEstudios(String estudios) {
        this.estudios = estudios;
    }

    // Relaciones

    public List<Libro> getLibro() {
        return libro;
    }

    public void setLibro(List<Libro> libro) {
        this.libro = libro;
    }

}
