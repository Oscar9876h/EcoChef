package org.example.ecochef.model;

import java.time.LocalDate;

/**
 * Representa la despensa de un usuario.
 */
public class Inventario extends EntidadBase {

    private int idUsuario;
    private int idAlimento;
    private double cantidadDisponible;
    private LocalDate fechaCaducidad;

    public Inventario() {
        super();
    }

    public Inventario(int id, int idUsuario, int idAlimento, double cantidadDisponible, LocalDate fechaCaducidad) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.idAlimento = idAlimento;
        this.cantidadDisponible = cantidadDisponible;
        this.fechaCaducidad = fechaCaducidad;
    }

    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }

    public int getIdAlimento() { return idAlimento; }
    public void setIdAlimento(int idAlimento) { this.idAlimento = idAlimento; }

    public double getCantidadDisponible() { return cantidadDisponible; }
    public void setCantidadDisponible(double cantidadDisponible) { this.cantidadDisponible = cantidadDisponible; }

    public LocalDate getFechaCaducidad() { return fechaCaducidad; }
    public void setFechaCaducidad(LocalDate fechaCaducidad) { this.fechaCaducidad = fechaCaducidad; }
}