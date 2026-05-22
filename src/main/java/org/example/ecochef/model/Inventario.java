package org.example.ecochef.model;

import java.time.LocalDate;

/**
 * Representa el inventario personal de un usuario, vinculando alimentos específicos
 * a sus cantidades y fechas de caducidad correspondientes.
 */
public class Inventario extends EntidadBase {

    private int idUsuario;
    private int idAlimento;
    private double cantidadDisponible;
    private LocalDate fechaCaducidad;

    /**
     * Constructor vacío.
     */
    public Inventario() {
        super();
    }

    /**
     * Constructor completo para crear un registro de inventario.
     * @param id Identificador único del registro en el inventario.
     * @param idUsuario ID del usuario propietario de este inventario.
     * @param idAlimento ID del alimento contenido en el inventario.
     * @param cantidadDisponible Cantidad actual del alimento disponible.
     * @param fechaCaducidad Fecha de caducidad específica de este lote.
     */
    public Inventario(int id, int idUsuario, int idAlimento, double cantidadDisponible, LocalDate fechaCaducidad) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.idAlimento = idAlimento;
        this.cantidadDisponible = cantidadDisponible;
        this.fechaCaducidad = fechaCaducidad;
    }

    // --- GETTERS Y SETTERS ---


    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }
    public int getIdAlimento() { return idAlimento; }
    public void setIdAlimento(int idAlimento) { this.idAlimento = idAlimento; }
    public double getCantidadDisponible() { return cantidadDisponible; }
    public void setCantidadDisponible(double cantidadDisponible) { this.cantidadDisponible = cantidadDisponible; }
    public LocalDate getFechaCaducidad() { return fechaCaducidad; }
    public void setFechaCaducidad(LocalDate fechaCaducidad) { this.fechaCaducidad = fechaCaducidad; }

    /**
     * @return El tipo de entidad, utilizado para clasificar objetos en la capa de datos.
     */
    @Override
    public String getTipoEntidad() {
        return "INVENTARIO";
    }
}