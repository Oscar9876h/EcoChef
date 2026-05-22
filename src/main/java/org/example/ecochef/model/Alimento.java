package org.example.ecochef.model;

import java.time.LocalDate;

/**
 * Clase Alimento optimizada con herencia y gestión de fechas.
 */
public class Alimento extends EntidadBase {

    private String nombre;
    private String tipo;
    private int calorias;
    private int idCategoriaAlimento;
    private LocalDate fechaCaducidad;

    /**
     * Constructor vacío (Sobrecarga 1)
     */
    public Alimento() {
        super();
    }

    /**
     * Constructor completo (Sobrecarga 2)
     */
    public Alimento(int id, String nombre, String tipo, int calorias, int idCategoriaAlimento, LocalDate fechaCaducidad) {
        super(id); // 🧠 ¡CAMBIO AQUÍ! Invocamos al padre de forma óptima con super() en vez de usar this.id
        this.nombre = nombre;
        this.tipo = tipo;
        this.calorias = calorias;
        this.idCategoriaAlimento = idCategoriaAlimento;
        this.fechaCaducidad = fechaCaducidad;
    }

    // ⭐ --- IMPLEMENTACIÓN DEL MÉTODO ABSTRACTO (Esto elimina el error de compilación) ---
    @Override
    public String getTipoEntidad() {
        return "Alimento de la despensa";
    }

    // --- GETTERS Y SETTERS EXISTENTES ---
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public int getCalorias() { return calorias; }
    public void setCalorias(int calorias) { this.calorias = calorias; }

    public int getIdCategoriaAlimento() { return idCategoriaAlimento; }
    public void setIdCategoriaAlimento(int idCategoriaAlimento) { this.idCategoriaAlimento = idCategoriaAlimento; }

    // --- MÉTODOS PARA LA FECHA ---
    public LocalDate getFechaCaducidad() { return fechaCaducidad; }
    public void setFechaCaducidad(LocalDate fechaCaducidad) { this.fechaCaducidad = fechaCaducidad; }

    @Override
    public String toString() {
        return "Alimento{id=" + id + ", nombre='" + nombre + "', tipo='" + tipo + "', caducidad=" + fechaCaducidad + "}";
    }
}