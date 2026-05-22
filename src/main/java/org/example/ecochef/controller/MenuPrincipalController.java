package org.example.ecochef.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;
import java.io.IOException;

/**
 * Controlador principal encargado de la navegación global de la aplicación.
 * Actúa como un "contenedor dinámico" que carga diferentes vistas FXML
 * según la opción seleccionada por el usuario.
 */
public class MenuPrincipalController {

    /** * Contenedor donde se inyectan las sub-vistas (Despensa, Caducidad, Recetas).
     * Definido en Scene Builder.
     */
    @FXML private VBox contenedorPrincipal;

    /**
     * Inicializa el menú mostrando la vista por defecto al arrancar.
     */
    @FXML
    public void initialize() {
        mostrarDespensa();
    }

    /**
     * Carga la vista de Despensa en el área principal.
     */
    @FXML
    public void mostrarDespensa() {
        cambiarPantalla("/org/example/ecochef/despensa.fxml");
    }

    /**
     * Carga la vista de alertas de caducidad en el área principal.
     */
    @FXML
    public void mostrarCaducidad() {
        cambiarPantalla("/org/example/ecochef/caducidad.fxml");
    }

    /**
     * Carga la vista de gestión de recetas en el área principal.
     */
    @FXML
    public void mostrarRecetas() {
        cambiarPantalla("/org/example/ecochef/recetas.fxml");
    }

    /**
     * Método central de navegación. Reemplaza el contenido del contenedor
     * principal con la nueva vista cargada desde su archivo FXML.
     * * @param rutaFxml La ruta del archivo FXML a cargar.
     */
    private void cambiarPantalla(String rutaFxml) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(rutaFxml));
            Parent nuevaVista = loader.load();

            // setAll reemplaza todos los elementos actuales del VBox por la nueva vista
            contenedorPrincipal.getChildren().setAll(nuevaVista);
        } catch (IOException e) {
            System.err.println("Error al cambiar de pantalla: " + e.getMessage());
            e.printStackTrace();
        }
    }
}