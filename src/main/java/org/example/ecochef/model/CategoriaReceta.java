package org.example.ecochef.model;

/**
 * Define las categorías de las recetas (ej: Saludable, Rápida).
 */
public class CategoriaReceta extends EntidadBase {

    private String nombreCategoria;

    public CategoriaReceta() {
        super();
    }

    public CategoriaReceta(int id, String nombreCategoria) {
        this.id = id;
        this.nombreCategoria = nombreCategoria;
    }

    public String getNombreCategoria() { return nombreCategoria; }
    public void setNombreCategoria(String nombreCategoria) { this.nombreCategoria = nombreCategoria; }

    @Override
    public String getTipoEntidad() {
        return "CATEGORIA_RECETA";
    }
}
