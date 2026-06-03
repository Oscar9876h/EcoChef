package org.example.ecochef.model;

import javafx.beans.property.*; // IMPORTANTE: Necesitas esto para JavaFX

public class Receta extends EntidadBase {

    private String nombreReceta;
    private String descripcion;
    private int tiempoPreparacion;
    private Usuario usuario;

    public Receta() {
        super();
    }

    public Receta(int id, String nombreReceta, String descripcion, int tiempoPreparacion, Usuario usuario) {
        super(id);
        this.nombreReceta = nombreReceta;
        this.descripcion = descripcion;
        this.tiempoPreparacion = tiempoPreparacion;
        this.usuario = usuario;
    }

    @Override
    public String getTipoEntidad() { return "Receta de Cocina"; }

    // --- GETTERS Y SETTERS TRADICIONALES ---
    public String getNombreReceta() { return nombreReceta; }
    public void setNombreReceta(String nombreReceta) { this.nombreReceta = nombreReceta; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public int getTiempoPreparacion() { return tiempoPreparacion; }
    public void setTiempoPreparacion(int tiempoPreparacion) { this.tiempoPreparacion = tiempoPreparacion; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    // --- MÉTODOS PROPERTY PARA JAVAFX TABLEVIEW ---

    // Para el ID (que viene de EntidadBase, asumimos que tienes getId())
    public IntegerProperty idProperty() {
        return new SimpleIntegerProperty(this.getId());
    }

    // Para el nombre de la receta
    public StringProperty nombreRecetaProperty() {
        return new SimpleStringProperty(this.nombreReceta);
    }

    @Override
    public String toString() {
        return "Receta{id=" + id + ", nombre='" + nombreReceta + "'}";
    }
}