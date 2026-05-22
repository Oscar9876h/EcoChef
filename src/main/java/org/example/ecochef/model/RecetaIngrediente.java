package org.example.ecochef.model;

/**
 * Gestiona la relación N:M entre Recetas y Alimentos.
 * Incluye la cantidad necesaria de cada ingrediente.
 */
public class RecetaIngrediente extends EntidadBase {

    private int idReceta;
    private int idAlimento;
    private String cantidadNecesaria;

    public RecetaIngrediente() {
        super();
    }

    public RecetaIngrediente(int id, int idReceta, int idAlimento, String cantidadNecesaria) {
        this.id = id;
        this.idReceta = idReceta;
        this.idAlimento = idAlimento;
        this.cantidadNecesaria = cantidadNecesaria;
    }

    public int getIdReceta() { return idReceta; }
    public void setIdReceta(int idReceta) { this.idReceta = idReceta; }

    public int getIdAlimento() { return idAlimento; }
    public void setIdAlimento(int idAlimento) { this.idAlimento = idAlimento; }

    public String getCantidadNecesaria() { return cantidadNecesaria; }
    public void setCantidadNecesaria(String cantidadNecesaria) { this.cantidadNecesaria = cantidadNecesaria; }

    @Override
    public String getTipoEntidad() {
        return "RECETA_INGREDIENTE";
    }
}