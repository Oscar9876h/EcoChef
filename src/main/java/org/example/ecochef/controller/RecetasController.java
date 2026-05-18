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

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class RecetasController implements Initializable {

    // Este ID coincide con el FlowPane que pusimos dentro de recetas-view.fxml
    @FXML private FlowPane flowRecetasContenedor;

    private final RecetaDAOImpl recetaDAO = new RecetaDAOImpl();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // En cuanto se abre la pestaña de recetas, se listan solas automáticamente
        cargarRecetas();
    }

    public void cargarRecetas() {
        List<Receta> lista = recetaDAO.listarTodos();
        flowRecetasContenedor.getChildren().clear();

        if (lista.isEmpty()) {
            flowRecetasContenedor.getChildren().add(new Label("No hay recetas disponibles en el libro."));
            return;
        }

        for (Receta receta : lista) {
            try {
                // Reutilizamos tu tarjeta-receta.fxml para el diseño visual
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/ecochef/tarjeta-receta.fxml"));
                VBox tarjeta = loader.load();

                // Buscamos los componentes dentro de la tarjeta
                Label lbNombre = (Label) tarjeta.lookup("#lbNombre");
                Label lbTiempo = (Label) tarjeta.lookup("#lbTiempo");
                Label lbDesc = (Label) tarjeta.lookup("#lbDesc");
                Button btnCheck = (Button) tarjeta.lookup("#btnCheck");
                Button btnEliminar = (Button) tarjeta.lookup("#btnEliminar");

                // Asignamos los datos reales que vienen de MySQL
                if (lbNombre != null) lbNombre.setText(receta.getNombreReceta().toUpperCase());
                if (lbTiempo != null) lbTiempo.setText("⏱ " + receta.getTiempoPreparacion() + " min");
                if (lbDesc != null) lbDesc.setText(receta.getDescripcion());

                // Lógica del BOTÓN AMARILLO (Comprobar disponibilidad de ingredientes)
                if (btnCheck != null) {
                    btnCheck.setVisible(true);
                    btnCheck.setManaged(true);
                    btnCheck.setOnAction(event -> {
                        // Llama a tu método en el DAO que cruza los datos con la despensa
                        String disponibilidad = recetaDAO.comprobarDisponibilidad(receta.getId());
                        mostrarAlerta("Ingredientes para " + receta.getNombreReceta(), disponibilidad);
                    });
                }

                // Botón para eliminar la receta de la base de datos
                if (btnEliminar != null) {
                    btnEliminar.setText("Eliminar Receta");
                    btnEliminar.setOnAction(event -> {
                        recetaDAO.eliminar(receta.getId());
                        cargarRecetas(); // Volvemos a refrescar la pantalla de recetas
                    });
                }

                // Añadimos la tarjeta completada al contenedor de la pantalla
                flowRecetasContenedor.getChildren().add(tarjeta);

            } catch (IOException e) {
                System.err.println("Error cargando tarjeta en RecetasController: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    /**
     * Muestra la ventana modal informativa con los ingredientes que faltan o se tienen
     */
    private void mostrarAlerta(String cabecera, String contenido) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("EcoChef - Verificación de Ingredientes");
        alert.setHeaderText(cabecera);
        alert.setContentText(contenido);
        alert.showAndWait();
    }
}