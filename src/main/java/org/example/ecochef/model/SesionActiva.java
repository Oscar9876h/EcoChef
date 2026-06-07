package org.example.ecochef.model;

/**
 * Clase que gestiona la sesión del usuario actual.
 * Almacena el rol y el nombre del usuario logueado para controlar
 * permisos en toda la aplicación.
 */
public class SesionActiva {

    // Rol por defecto "USUARIO" para evitar accesos indebidos antes del login
    public static String rolUsuarioLogueado = "USUARIO";
    public static String nombreUsuarioLogueado = "";

    /**
     * Comprueba si el rol del usuario logueado es Administrador.
     * Utiliza equalsIgnoreCase para que funcione tanto con "ADMIN" como con "admin".
     */
    public static boolean esAdmin() {
        return "ADMIN".equalsIgnoreCase(rolUsuarioLogueado) ||
                "ADMINISTRADOR".equalsIgnoreCase(rolUsuarioLogueado);
    }
}
