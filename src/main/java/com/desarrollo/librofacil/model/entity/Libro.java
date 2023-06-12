package com.desarrollo.librofacil.model.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "libros")
public class Libro implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "titulo", length = 100, nullable = false)
    @NotEmpty
    private String titulo;

    @Column(name = "nro_paginas", length = 5, nullable = false)
    @Min(value = 1)
    @Max(value = 5000)
    @NotNull
    private int nroPaginas;

    @Column(name = "anio", length = 10)
    private String anio;

    @Column(name = "isbn", length = 60, nullable = false)
    @NotEmpty
    private String isbn;

    @Column(name = "edicion", length = 100, nullable = false)
    @NotEmpty
    private String edicion;

    @Column(name = "tema", length = 100, nullable = false)
    @NotEmpty
    private String tema;

    @Column(name = "caratula", length = 255, nullable = false)
    public String caratula;

    // Relaciones

    @OneToMany(mappedBy = "libro", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Ejemplar> ejemplares;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @NotNull
    private Editorial editorial;

    // ManytoMany

    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })

    @JoinTable(name = "libros_autores", joinColumns = { @JoinColumn(name = "libro_id") }, inverseJoinColumns = {
            @JoinColumn(name = "autor_id") })
            @JsonIgnore
    private List<Autor> autor = new ArrayList<>();

    public Libro() {
        ejemplares = new ArrayList<>();
        autor = new ArrayList<>();
    }

    public Libro(Long id, String titulo, int nroPaginas, String anio, String isbn, String edicion, String tema,
            String caratula) {
        this.id = id;
        this.titulo = titulo;
        this.nroPaginas = nroPaginas;
        this.anio = anio;
        this.isbn = isbn;
        this.edicion = edicion;
        this.tema = tema;
        ejemplares = new ArrayList<>();
        autor = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public int getNroPaginas() {
        return nroPaginas;
    }

    public void setNroPaginas(int nroPaginas) {
        this.nroPaginas = nroPaginas;
    }

    public String getAnio() {
        return anio;
    }

    public void setAnio(String anio) {
        this.anio = anio;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getEdicion() {
        return edicion;
    }

    public void setEdicion(String edicion) {
        this.edicion = edicion;
    }

    public String getTema() {
        return tema;
    }

    public void setTema(String tema) {
        this.tema = tema;
    }

    public String getCaratula() {
        return caratula;
    }

    public void setCaratula(String caratula) {
        this.caratula = caratula;
    }

    public Editorial getEditorial() {
        return editorial;
    }

    public void setEditorial(Editorial editorial) {
        this.editorial = editorial;
    }

    // Relaciones

    public List<Ejemplar> getEjemplares() {
        return ejemplares;
    }

    public void setEjemplares(List<Ejemplar> ejemplares) {
        this.ejemplares = ejemplares;
    }

    public List<Autor> getAutor() {
        return autor;
    }

    public void setAutor(List<Autor> autor) {
        this.autor = autor;
    }

    @Override
    public String toString() {
        return "Libro [id=" + id + ", titulo=" + titulo + ", nroPaginas=" + nroPaginas + ", anio=" + anio + ", isbn="
                + isbn + ", edicion=" + edicion + ", tema=" + tema + ", editorial=" + editorial + "]";
    }

    

}
