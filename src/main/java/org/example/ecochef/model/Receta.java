package org.example.ecochef.model;

/**
 * Clase que representa una receta en el sistema.
 * Hereda de EntidadBase para una estructura limpia.
 */
public class Receta extends EntidadBase {

    private String nombreReceta;
    private String descripcion;
    private int tiempoPreparacion;

    public Receta() {
        super();
    }

    public Receta(int id, String nombreReceta, String descripcion, int tiempoPreparacion) {
        this.id = id;
        this.nombreReceta = nombreReceta;
        this.descripcion = descripcion;
        this.tiempoPreparacion = tiempoPreparacion;
    }

    public String getNombreReceta() { return nombreReceta; }
    public void setNombreReceta(String nombreReceta) { this.nombreReceta = nombreReceta; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public int getTiempoPreparacion() { return tiempoPreparacion; }
    public void setTiempoPreparacion(int tiempoPreparacion) { this.tiempoPreparacion = tiempoPreparacion; }

    @Override
    public String toString() {
        return "Receta{id=" + id + ", nombre='" + nombreReceta + "'}";
    }
}
