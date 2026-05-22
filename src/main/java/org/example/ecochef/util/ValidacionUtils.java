package org.example.ecochef.util;

public class ValidacionUtils {

    /**
     * Método que valida que el email tenga un formato correcto.
     * Usa una expresión regular (regex) para comprobar que el email
     * Tiene que tener los caracteres permitidos por el regex.
     * @param email el email a validar
     * @return true si el email es válido, false si no
     */
    public static boolean emailValido(String email) {
        String regex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return email.matches(regex);
    }
}