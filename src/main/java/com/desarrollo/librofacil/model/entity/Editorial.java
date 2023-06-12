package com.desarrollo.librofacil.model.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "editoriales")
public class Editorial implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "direccion", length = 255)
    private String direccion;

    @Column(name = "nombre_completo", length = 60, nullable = false)
    private String nombreCompleto;

    @Column(name = "pais", length = 255)
    private String pais;

    @Column(name = "ciudad", length = 100)
    private String ciudad;

    @Column(name = "url", length = 255, nullable = false)
    private String url;

    @Column(name = "telefono", length = 20)
    private String telefono;

    // Relaciones

    @OneToMany(mappedBy = "editorial", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Libro> libros;;

    public Editorial() {
        libros = new ArrayList<>();
    }

    public Editorial(Long id, String direccion, String nombreCompleto, String pais, String ciudad, String url,
            String telefono) {
        this.id = id;
        this.direccion = direccion;
        this.nombreCompleto = nombreCompleto;
        this.pais = pais;
        this.ciudad = ciudad;
        this.url = url;
        this.telefono = telefono;
        libros = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    // Relaciones

    public List<Libro> getLibros() {
        return libros;
    }

    public void setLibros(List<Libro> libros) {
        this.libros = libros;
    }

}
