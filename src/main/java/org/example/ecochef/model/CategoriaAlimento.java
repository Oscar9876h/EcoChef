package org.example.ecochef.model;

/**
 * Clase que representa una categoría de alimento en el sistema EcoChef.
 * Hereda de EntidadBase para una gestión uniforme de identificadores.
 * Ejemplo: lácteos, frutas, verduras, carnes...
 */
public class CategoriaAlimento extends EntidadBase {

    private String nombreCategoria;

    /**
     * Constructor vacío necesario para la arquitectura del sistema.
     */
    public CategoriaAlimento() {
        super();
    }

    /**
     * Constructor completo para crear una categoría de alimento.
     *
     * @param id              Identificador único (gestionado por la clase base)
     * @param nombreCategoria Nombre descriptivo de la categoría
     */
    public CategoriaAlimento(int id, String nombreCategoria) {
        this.id = id;
        this.nombreCategoria = nombreCategoria;
    }

    // --- GETTERS Y SETTERS ---

    public String getNombreCategoria() {
        return nombreCategoria;
    }

    public void setNombreCategoria(String nombreCategoria) {
        this.nombreCategoria = nombreCategoria;
    }

    /**
     * Representación en texto de la categoría de alimento.
     *
     * @return String con los datos de la categoría siguiendo el estándar del proyecto
     */
    @Override
    public String toString() {
        return "CategoriaAlimento{id=" + id + ", nombre='" + nombreCategoria + "'}";
    }

    @Override
    public String getTipoEntidad() {
        return "CATEGORIA_ALIMENTO";
    }
}