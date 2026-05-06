package org.example.ecochef.model;

import java.time.LocalDateTime;

/**
 * Relaciona usuarios con sus recetas preferidas.
 */
public class Favorito extends EntidadBase {

    private int idUsuario;
    private int idReceta;
    private LocalDateTime fechaGuardado;

    public Favorito() {
        super();
    }

    public Favorito(int id, int idUsuario, int idReceta, LocalDateTime fechaGuardado) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.idReceta = idReceta;
        this.fechaGuardado = fechaGuardado;
    }

    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }

    public int getIdReceta() { return idReceta; }
    public void setIdReceta(int idReceta) { this.idReceta = idReceta; }

    public LocalDateTime getFechaGuardado() { return fechaGuardado; }
    public void setFechaGuardado(LocalDateTime fechaGuardado) { this.fechaGuardado = fechaGuardado; }
}