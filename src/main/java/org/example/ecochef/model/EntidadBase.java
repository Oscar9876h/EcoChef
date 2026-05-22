package org.example.ecochef.model;

/**
 * Clase abstracta para cumplir con el requisito de herencia del proyecto.
 * Todas las clases que se guardan en la DB heredan de aquí.
 */
public abstract class EntidadBase {
    protected int id;

    // Constructor vacío (necesario para JavaFX y DAOs)
    public EntidadBase() {
    }

    // NUEVO: Constructor con parámetros que invoca la hija
    public EntidadBase(int id) {
        this.id = id;
    }

    // ⭐ REQUISITO EXTRA: Método abstracto que obliga a las clases hijas a identificarse
    public abstract String getTipoEntidad();

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
}