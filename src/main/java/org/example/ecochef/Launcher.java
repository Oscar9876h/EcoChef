package org.example.ecochef;

import javafx.application.Application;

public class Launcher {
    public static void main(String[] args) {
        // La prueba del DAO ha sido un éxito, así que borramos el bloque temporal.
        // Ahora la App arrancará directamente a la interfaz gráfica.
        Application.launch(EcoChefApp.class, args);
    }
}