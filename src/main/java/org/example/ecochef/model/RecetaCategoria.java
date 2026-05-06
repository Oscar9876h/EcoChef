package org.example.ecochef.model;

/**
 * Tabla intermedia N:M entre Recetas y CategoriaReceta.
 */
public class RecetaCategoria extends EntidadBase {

    private int idReceta;
    private int idCategoriaReceta;

    public RecetaCategoria() {
        super();
    }

    public RecetaCategoria(int id, int idReceta, int idCategoriaReceta) {
        this.id = id;
        this.idReceta = idReceta;
        this.idCategoriaReceta = idCategoriaReceta;
    }

    public int getIdReceta() { return idReceta; }
    public void setIdReceta(int idReceta) { this.idReceta = idReceta; }

    public int getIdCategoriaReceta() { return idCategoriaReceta; }
    public void setIdCategoriaReceta(int idCategoriaReceta) { this.idCategoriaReceta = idCategoriaReceta; }
}