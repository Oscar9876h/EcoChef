package org.example.ecochef.model;

/**
 * Clase Alimento optimizada con herencia.
 * Hereda de EntidadBase para centralizar la gestión de IDs.
 */
public class Alimento extends EntidadBase {

    private String nombre;
    private String tipo;
    private int calorias;
    private int idCategoriaAlimento;

    /**
     * Constructor vacío (Sobrecarga 1)
     * Necesario para frameworks de persistencia y JAXB.
     */
    public Alimento() {
        super();
    }

    /**
     * Constructor completo (Sobrecarga 2)
     * @param id El ID que se pasa a la clase madre EntidadBase
     */
    public Alimento(int id, String nombre, String tipo, int calorias, int idCategoriaAlimento) {
        this.id = id; // Atributo heredado de EntidadBase
        this.nombre = nombre;
        this.tipo = tipo;
        this.calorias = calorias;
        this.idCategoriaAlimento = idCategoriaAlimento;
    }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public int getCalorias() { return calorias; }
    public void setCalorias(int calorias) { this.calorias = calorias; }

    public int getIdCategoriaAlimento() { return idCategoriaAlimento; }
    public void setIdCategoriaAlimento(int idCategoriaAlimento) { this.idCategoriaAlimento = idCategoriaAlimento; }

    @Override
    public String toString() {
        return "Alimento{id=" + id + ", nombre='" + nombre + "', tipo='" + tipo + "'}";
    }
}