package org.example.ecochef.model;

/**
 * Clase Usuario optimizada con herencia.
 * Hereda de EntidadBase para cumplir con los requisitos del proyecto.
 */
public class Usuario extends EntidadBase {

    private String nombre;
    private String email;
    private String contrasena;
    private String rol;

    /**
     * Constructor vacío (Sobrecarga 1)
     */
    public Usuario() {
        super();
    }

    /**
     * Constructor completo (Sobrecarga 2)
     * @param id El ID se pasa a EntidadBase usando super o asignación directa a 'id'
     */
    public Usuario(int id, String nombre, String email, String contrasena, String rol) {
        this.id = id; // Atributo heredado de EntidadBase
        this.nombre = nombre;
        this.email = email;
        this.contrasena = contrasena;
        this.rol = rol;
    }

    // --- GETTERS Y SETTERS ---
    // Nota: getId() y setId() ya no se escriben aquí, ¡se heredan!

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }

    @Override
    public String toString() {
        return "Usuario{id=" + id + ", nombre='" + nombre + "', email='" + email + "', rol='" + rol + "'}";
    }
}