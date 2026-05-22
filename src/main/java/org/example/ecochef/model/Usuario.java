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
        super(); // Llama al constructor vacío de EntidadBase
    }

    /**
     * Constructor completo (Sobrecarga 2)
     * Envía el ID correctamente a EntidadBase usando super(id)
     */
    public Usuario(int id, String nombre, String email, String contrasena, String rol) {
        super(id); // Pasa el id al constructor de EntidadBase
        this.nombre = nombre;
        this.email = email;
        this.contrasena = contrasena;
        this.rol = rol;
    }

    /**
     * IMPLEMENTACIÓN OBLIGATORIA DEL MÉTODO ABSTRACTO DE ENTIDADBASE
     * Esto es lo que soluciona el error y hace feliz a IntelliJ.
     */
    @Override
    public String getTipoEntidad() {
        return "USUARIO";
    }

    // --- GETTERS Y SETTERS ---


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
        return "Usuario{id=" + getId() + ", nombre='" + nombre + "', email='" + email + "', rol='" + rol + "'}";
    }
}