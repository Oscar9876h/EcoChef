package org.example.ecochef.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import org.example.ecochef.dao.RecetaDAOImpl;
import org.example.ecochef.model.Receta;
import org.example.ecochef.model.SesionActiva;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class RecetasController implements Initializable {

    // Cambiado a VBox para poder meter las secciones hacia abajo
    @FXML private VBox flowRecetasContenedor;

    private final RecetaDAOImpl recetaDAO = new RecetaDAOImpl();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cargarRecetas();
    }

    public void cargarRecetas() {
        List<Receta> lista = recetaDAO.listarTodos();
        flowRecetasContenedor.getChildren().clear();

        if (lista.isEmpty()) {
            flowRecetasContenedor.getChildren().add(new Label("No hay recetas disponibles en el libro."));
            return;
        }

        // 1. CREAMOS TRES LISTAS NORMALES (Nuestros cajones en memoria)
        List<Receta> saludables = new ArrayList<>();
        List<Receta> caprichos = new ArrayList<>();
        List<Receta> otras = new ArrayList<>();

        // 2. CLASIFICAMOS CON UN BUCLE FOR E IF TRADICIONALES
        for (Receta receta : lista) {
            String nombre = receta.getNombreReceta().toUpperCase();

            if (nombre.contains("GAZPACHO") || nombre.contains("ENSALADA") || nombre.contains("VERDURA") || nombre.contains("CREMA")) {
                saludables.add(receta);
            } else if (nombre.contains("CARBONARA") || nombre.contains("PASTA") || nombre.contains("HAMBURGUESA") || nombre.contains("PIZZA")) {
                caprichos.add(receta);
            } else {
                otras.add(receta);
            }
        }

        // 3. PINTAMOS LAS TRES SECCIONES (Solo si tienen alguna receta dentro)
        if (!saludables.isEmpty()) {
            crearBloqueSeccion("• RECETAS MÁS SALUDABLES", saludables);
        }
        if (!caprichos.isEmpty()) {
            crearBloqueSeccion("• OPCIONES MENOS SALUDABLES", caprichos);
        }
        if (!otras.isEmpty()) {
            crearBloqueSeccion("• OTRAS PROPUESTAS", otras);
        }
    }

    /**
     * Método auxiliar para no repetir código. Crea el título y añade las tarjetas.
     * CERO DISEÑO AQUÍ: Se limita a instanciar los objetos básicos.
     */
    private void crearBloqueSeccion(String nombreSeccion, List<Receta> listaRecetas) {
        // Creamos el texto de la sección con el total: "• RECETAS MÁS SALUDABLES (3)"
        Label titulo = new Label(nombreSeccion + " (" + listaRecetas.size() + ")");
        flowRecetasContenedor.getChildren().add(titulo);

        // Creamos el FlowPane donde se colocarán las tarjetas de esta sección de lado a lado
        FlowPane flowSeccion = new FlowPane();

        for (Receta receta : listaRecetas) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/ecochef/tarjeta-receta.fxml"));
                VBox tarjeta = loader.load();

                Label lbNombre = (Label) tarjeta.lookup("#lbNombre");
                Label lbTiempo = (Label) tarjeta.lookup("#lbTiempo");
                Label lbDesc = (Label) tarjeta.lookup("#lbDesc");
                Button btnCheck = (Button) tarjeta.lookup("#btnCheck");
                Button btnEliminar = (Button) tarjeta.lookup("#btnEliminar");

                if (lbNombre != null) lbNombre.setText(receta.getNombreReceta().toUpperCase());
                if (lbTiempo != null) lbTiempo.setText("⏱ " + receta.getTiempoPreparacion() + " min");
                if (lbDesc != null) lbDesc.setText(receta.getDescripcion());

                if (btnCheck != null) {
                    btnCheck.setVisible(true);
                    btnCheck.setManaged(true);
                    btnCheck.setOnAction(event -> {
                        String disponibilidad = recetaDAO.comprobarDisponibilidad(receta.getId());
                        mostrarAlerta("Ingredientes para " + receta.getNombreReceta(), disponibilidad);
                    });
                }

                if (btnEliminar != null) {
                    btnEliminar.setText("Eliminar Receta");
                    btnEliminar.setOnAction(event -> {
                        recetaDAO.eliminar(receta.getId());
                        cargarRecetas();
                    });

                    // 🌟 COMPROBACIÓN DE SEGURIDAD SEGÚN EL ROL
                    // Si NO es administrador, el botón de eliminar la receta se destruye visualmente de la tarjeta
                    if (!SesionActiva.esAdmin()) {
                        btnEliminar.setVisible(false);
                        btnEliminar.setManaged(false);
                    }
                }

                // Añadimos la tarjeta al contenedor horizontal de esta sección
                flowSeccion.getChildren().add(tarjeta);

            } catch (IOException e) {
                System.err.println("Error cargando tarjeta en RecetasController: " + e.getMessage());
            }
        }

        // Añadimos el contenedor de tarjetas al VBox principal de la pantalla
        flowRecetasContenedor.getChildren().add(flowSeccion);
    }

    private void mostrarAlerta(String cabecera, String contenido) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("EcoChef - Verificación de Ingredientes");
        alert.setHeaderText(cabecera);
        alert.setContentText(contenido);
        alert.showAndWait();
    }
}