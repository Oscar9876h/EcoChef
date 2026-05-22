package org.example.ecochef.model;

/**
 * Gestiona la relación N:M (muchos a muchos) entre Recetas y Alimentos.
 * Almacena información adicional sobre la cantidad específica requerida de cada ingrediente para una receta.
 */
public class RecetaIngrediente extends EntidadBase {

    private int idReceta;
    private int idAlimento;
    private String cantidadNecesaria;

    /**
     * Constructor vacío.
     */
    public RecetaIngrediente() {
        super();
    }

    /**
     * Constructor con todos los campos.
     * @param id Identificador único de la relación.
     * @param idReceta ID de la receta a la que pertenece el ingrediente.
     * @param idAlimento ID del alimento o ingrediente.
     * @param cantidadNecesaria Descripción de la cantidad (ej. "200g", "1 unidad").
     */
    public RecetaIngrediente(int id, int idReceta, int idAlimento, String cantidadNecesaria) {
        this.id = id;
        this.idReceta = idReceta;
        this.idAlimento = idAlimento;
        this.cantidadNecesaria = cantidadNecesaria;
    }

    // --- GETTERS Y SETTERS ---


    public int getIdReceta() { return idReceta; }
    public void setIdReceta(int idReceta) { this.idReceta = idReceta; }
    public int getIdAlimento() { return idAlimento; }
    public void setIdAlimento(int idAlimento) { this.idAlimento = idAlimento; }
    public String getCantidadNecesaria() { return cantidadNecesaria; }
    public void setCantidadNecesaria(String cantidadNecesaria) { this.cantidadNecesaria = cantidadNecesaria; }

    /**
     * @return El tipo de entidad, utilizado para clasificar la tabla de relación.
     */
    @Override
    public String getTipoEntidad() {
        return "RECETA_INGREDIENTE";
    }
}