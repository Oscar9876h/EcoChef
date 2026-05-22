package org.example.ecochef.model;

public abstract class Persona {
    protected int id;
    protected String nombre;

    public Persona() {} // Constructor vacío de seguridad

    public Persona(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    // Método abstracto obligatorio para cumplir el requisito
    public abstract String getTipoUsuario();

    // Getters y Setters básicos
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
}