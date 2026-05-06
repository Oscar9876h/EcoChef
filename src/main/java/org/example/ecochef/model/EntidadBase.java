package org.example.ecochef.model;

/**
 * Clase abstracta para cumplir con el requisito de herencia del proyecto.
 * Todas las clases que se guardan en la DB heredan de aquí.
 */
public abstract class EntidadBase {
    protected int id; // Usamos protected para que las hijas lo vean

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
}