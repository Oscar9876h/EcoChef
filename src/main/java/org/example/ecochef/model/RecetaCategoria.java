package org.example.ecochef.model;

/**
 * Representa la tabla intermedia que gestiona la relación N:M (muchos a muchos)
 * entre la entidad Receta y la entidad CategoriaReceta.
 */
public class RecetaCategoria extends EntidadBase {

    private int idReceta;
    private int idCategoriaReceta;

    /**
     * Constructor vacío.
     */
    public RecetaCategoria() {
        super();
    }

    /**
     * Constructor con todos los campos.
     * @param id Identificador único de la relación.
     * @param idReceta ID de la receta involucrada.
     * @param idCategoriaReceta ID de la categoría asignada a dicha receta.
     */
    public RecetaCategoria(int id, int idReceta, int idCategoriaReceta) {
        this.id = id;
        this.idReceta = idReceta;
        this.idCategoriaReceta = idCategoriaReceta;
    }

    // --- GETTERS Y SETTERS ---

    public int getIdReceta() { return idReceta; }
    public void setIdReceta(int idReceta) { this.idReceta = idReceta; }
    public int getIdCategoriaReceta() { return idCategoriaReceta; }
    public void setIdCategoriaReceta(int idCategoriaReceta) { this.idCategoriaReceta = idCategoriaReceta; }

    /**
     * @return El tipo de entidad, útil para identificar tablas de unión.
     */
    @Override
    public String getTipoEntidad() {
        return "RECETA_CATEGORIA";
    }
}