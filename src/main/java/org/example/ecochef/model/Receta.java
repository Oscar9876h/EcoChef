package org.example.ecochef.model;

/**
 * Clase que representa una receta en el sistema.
 * CUMPLE REQUISITO: Herencia de EntidadBase y Relación 1:N (un Usuario crea la receta).
 */
public class Receta extends EntidadBase {

    private String nombreReceta;
    private String descripcion;
    private int tiempoPreparacion;

    // REQUISITO: Relación entre objetos (Asociación 1:N)
    // Una receta tiene un autor (Usuario)
    private Usuario usuario;

    /**
     * Constructor vacío (Sobrecarga 1)
     * Llama al constructor de EntidadBase.
     */
    public Receta() {
        super();
    }

    /**
     * Constructor completo (Sobrecarga 2)
     * @param id Atributo heredado
     * @param usuario Objeto relacionado
     */
    public Receta(int id, String nombreReceta, String descripcion, int tiempoPreparacion, Usuario usuario) {
        super(id); // REQUISITO: Llamada al constructor padre
        this.nombreReceta = nombreReceta;
        this.descripcion = descripcion;
        this.tiempoPreparacion = tiempoPreparacion;
        this.usuario = usuario;
    }

    @Override
    public String getTipoEntidad() {
        return "Receta de Cocina";
    }

    // --- GETTERS Y SETTERS ---

    public String getNombreReceta() { return nombreReceta; }
    public void setNombreReceta(String nombreReceta) { this.nombreReceta = nombreReceta; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public int getTiempoPreparacion() { return tiempoPreparacion; }
    public void setTiempoPreparacion(int tiempoPreparacion) { this.tiempoPreparacion = tiempoPreparacion; }

    // Getter y Setter para el objeto relacionado
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    @Override
    public String toString() {
        return "Receta{id=" + id + ", nombre='" + nombreReceta + "', autor=" +
                (usuario != null ? usuario.getNombre() : "Sin autor") + "}";
    }
}