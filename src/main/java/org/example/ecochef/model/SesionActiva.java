package org.example.ecochef.model;

public class SesionActiva {
    // Por defecto lo dejamos en "usuario". Si fuera "admin", se activarían los botones.
    public static String rolUsuarioLogueado = "usuario";
    public static String nombreUsuarioLogueado = "";

    // Método rápido que usan los controladores para comprobar el rol
    public static boolean esAdmin() {
        return "admin".equalsIgnoreCase(rolUsuarioLogueado);
    }
}
