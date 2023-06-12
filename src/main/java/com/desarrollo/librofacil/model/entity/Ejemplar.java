package com.desarrollo.librofacil.model.entity;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "ejemplares")
public class Ejemplar implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "estado", length = 50)
    private String estado;

    @Column(name = "fecha_ingreso")
    @Temporal(TemporalType.DATE)
    private Date fechaIngreso;

    @Column(name = "numero_ejemplar")
    private Long numeroEjemplar;

    @Column(name = "tipo_cubierta", length = 60)
    private String tipoCubierta;

    @Column(name = "disponible", length = 1, nullable = false)
    private boolean disponible;

    // Relaciones

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Libro libro;
    
    public Ejemplar() {
    }

    public Ejemplar(Long id, String estado, Date fechaIngreso, Long numeroEjemplar, String tipoCubierta,
            boolean disponible) {
        this.id = id;
        this.estado = estado;
        this.fechaIngreso = fechaIngreso;
        this.numeroEjemplar = numeroEjemplar;
        this.tipoCubierta = tipoCubierta;
        this.disponible = disponible;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public Long getNumeroEjemplar() {
        return numeroEjemplar;
    }

    public void setNumeroEjemplar(Long numeroEjemplar) {
        this.numeroEjemplar = numeroEjemplar;
    }

    public String getTipoCubierta() {
        return tipoCubierta;
    }

    public void setTipoCubierta(String tipoCubierta) {
        this.tipoCubierta = tipoCubierta;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    // Relaciones

    public Libro getLibro() {
        return libro;
    }

    public void setLibro(Libro libro) {
        this.libro = libro;
    }

    @Override
    public String toString() {
        return "Ejemplar [id=" + id + ", estado=" + estado + ", fechaIngreso=" + fechaIngreso + ", numeroEjemplar="
                + numeroEjemplar + ", tipoCubierta=" + tipoCubierta + ", disponible=" + disponible + ", libro=" + libro
                + "]";
    }

   

}
