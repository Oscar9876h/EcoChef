package org.example.ecochef.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;
import java.io.IOException;

public class MenuPrincipalController {

    @FXML private VBox contenedorPrincipal;

    @FXML
    public void initialize() {
        // Al arrancar la app, mostramos la despensa por defecto
        mostrarDespensa();
    }

    @FXML
    public void mostrarDespensa() {
        // Quitamos el "-view", porque tu archivo real se llama "despensa.fxml"
        cambiarPantalla("/org/example/ecochef/despensa.fxml");
    }

    @FXML
    public void mostrarCaducidad() {
        // Quitamos el "-view", porque tu archivo real se llama "caducidad.fxml"
        cambiarPantalla("/org/example/ecochef/caducidad.fxml");
    }

    @FXML
    public void mostrarRecetas() {
        // Quitamos el "-view", porque tu archivo real se llama "recetas.fxml"
        cambiarPantalla("/org/example/ecochef/recetas.fxml");
    }

    private void cambiarPantalla(String rutaFxml) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(rutaFxml));
            Parent nuevaVista = loader.load();

            // Metemos la nueva pantalla dentro del contenedor vacío debajo de la barra verde
            contenedorPrincipal.getChildren().setAll(nuevaVista);
        } catch (IOException e) {
            System.err.println("Error al cambiar de pantalla: " + e.getMessage());
            e.printStackTrace();
        }
    }
}