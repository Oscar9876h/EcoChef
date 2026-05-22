package org.example.ecochef.model;

import java.time.LocalDateTime;

/**
 * Representa la relación de favoritos entre un usuario y una receta.
 * Permite almacenar qué recetas han sido marcadas como favoritas y cuándo.
 */
public class Favorito extends EntidadBase {

    private int idUsuario;
    private int idReceta;
    private LocalDateTime fechaGuardado;

    /**
     * Constructor vacío.
     */
    public Favorito() {
        super();
    }

    /**
     * Constructor con todos los campos.
     * @param id Identificador único del registro favorito.
     * @param idUsuario El ID del usuario que marca la receta.
     * @param idReceta El ID de la receta marcada como favorita.
     * @param fechaGuardado Fecha y hora exacta en la que se guardó el favorito.
     */
    public Favorito(int id, int idUsuario, int idReceta, LocalDateTime fechaGuardado) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.idReceta = idReceta;
        this.fechaGuardado = fechaGuardado;
    }

    // --- GETTERS Y SETTERS ---


    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }
    public int getIdReceta() { return idReceta; }
    public void setIdReceta(int idReceta) { this.idReceta = idReceta; }
    public LocalDateTime getFechaGuardado() { return fechaGuardado; }
    public void setFechaGuardado(LocalDateTime fechaGuardado) { this.fechaGuardado = fechaGuardado; }

    /**
     * @return El tipo de entidad, útil para identificación en colecciones genéricas.
     */
    @Override
    public String getTipoEntidad() {
        return "FAVORITO";
    }
}