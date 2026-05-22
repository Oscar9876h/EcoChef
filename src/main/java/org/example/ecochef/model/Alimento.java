package org.example.ecochef.model;

import java.time.LocalDate;

/**
 * Representa un producto alimenticio dentro de la aplicación EcoChef.
 * Esta clase hereda de {@link EntidadBase} para mantener una estructura de datos consistente.
 */
public class Alimento extends EntidadBase {

    private String nombre;
    private String tipo;
    private int calorias;
    private int idCategoriaAlimento;
    private LocalDate fechaCaducidad;

    /**
     * Constructor por defecto. Inicializa un nuevo Alimento sin valores predefinidos.
     */
    public Alimento() {
        super();
    }

    /**
     * Constructor completo para crear un Alimento con todos sus atributos.
     * * @param id Identificador único del alimento.
     * @param nombre Nombre descriptivo del alimento.
     * @param tipo Categoría o grupo alimenticio (ej. Lácteo, Fruta).
     * @param calorias Valor energético en kcal.
     * @param idCategoriaAlimento ID asociado a la categoría de alimento en la base de datos.
     * @param fechaCaducidad Fecha límite de consumo.
     */
    public Alimento(int id, String nombre, String tipo, int calorias, int idCategoriaAlimento, LocalDate fechaCaducidad) {
        super(id);
        this.nombre = nombre;
        this.tipo = tipo;
        this.calorias = calorias;
        this.idCategoriaAlimento = idCategoriaAlimento;
        this.fechaCaducidad = fechaCaducidad;
    }

    /**
     * Define el tipo de esta entidad para efectos de visualización o lógica de negocio.
     * @return Una cadena descriptiva de la entidad.
     */
    @Override
    public String getTipoEntidad() {
        return "Alimento de la despensa";
    }

    // --- GETTERS Y SETTERS ---

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public int getCalorias() { return calorias; }
    public void setCalorias(int calorias) { this.calorias = calorias; }

    public int getIdCategoriaAlimento() { return idCategoriaAlimento; }
    public void setIdCategoriaAlimento(int idCategoriaAlimento) { this.idCategoriaAlimento = idCategoriaAlimento; }

    public LocalDate getFechaCaducidad() { return fechaCaducidad; }
    public void setFechaCaducidad(LocalDate fechaCaducidad) { this.fechaCaducidad = fechaCaducidad; }

    /**
     * Representación textual del objeto, útil para depuración y logs.
     */
    @Override
    public String toString() {
        return "Alimento{id=" + id + ", nombre='" + nombre + "', tipo='" + tipo + "', caducidad=" + fechaCaducidad + "}";
    }
}