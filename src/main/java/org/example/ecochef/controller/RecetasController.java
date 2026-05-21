package org.example.ecochef.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import org.example.ecochef.dao.RecetaDAOImpl;
import org.example.ecochef.model.Receta;
import org.example.ecochef.model.SesionActiva;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class RecetasController implements Initializable {

    // Cambiado a VBox para poder meter las secciones hacia abajo
    @FXML private VBox flowRecetasContenedor;


    @FXML private Button btnAnadirReceta;

    private final RecetaDAOImpl recetaDAO = new RecetaDAOImpl();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (!SesionActiva.esAdmin()) {
            btnAnadirReceta.setVisible(false);
            btnAnadirReceta.setManaged(false);
        }

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

    // 🌟 3. ACCIÓN DEL BOTÓN DE SCENE BUILDER
    // Este es el método que pusiste en la casilla "On Action" de Scene Builder.
    @FXML
    public void abrirFormularioAnadir() {
        Dialog<Receta> dialog = new Dialog<>();
        dialog.setTitle("Nueva Receta Oficial");
        dialog.setHeaderText("Introduce los datos de la receta para el catálogo global:");

        ButtonType guardarButtonType = new ButtonType("Guardar Receta", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(guardarButtonType, ButtonType.CANCEL);

        // Diseñamos el formulario usando un código básico de celdas (GridPane)
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField txtNombre = new TextField();
        TextField txtTiempo = new TextField();
        TextArea txtDescripcion = new TextArea();
        txtDescripcion.setPrefRowCount(3);

        grid.add(new Label("Nombre de la receta:"), 0, 0);
        grid.add(txtNombre, 1, 0);
        grid.add(new Label("Tiempo (minutos):"), 0, 1);
        grid.add(txtTiempo, 1, 1);
        grid.add(new Label("Instrucciones / Descripción:"), 0, 2);
        grid.add(txtDescripcion, 1, 2);

        dialog.getDialogPane().setContent(grid);

        // Convertimos la información de las cajas de texto en un Objeto Receta al pulsar Guardar
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == guardarButtonType) {
                Receta nuevaReceta = new Receta();
                nuevaReceta.setNombreReceta(txtNombre.getText());
                try {
                    nuevaReceta.setTiempoPreparacion(Integer.parseInt(txtTiempo.getText()));
                } catch (Exception e) {
                    nuevaReceta.setTiempoPreparacion(0);
                }
                nuevaReceta.setDescripcion(txtDescripcion.getText());
                return nuevaReceta;
            }
            return null;
        });

        // Esperamos a que el Admin pulse "Guardar" y procesamos el resultado
        Optional<Receta> result = dialog.showAndWait();
        result.ifPresent(receta -> {
            recetaDAO.guardar(receta); // Guarda la receta en tu base de datos MySQL
            cargarRecetas(); // Volvemos a leer de la base de datos para que aparezca clasificada al instante
        });
    }

    private void mostrarAlerta(String cabecera, String contenido) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("EcoChef - Verificación de Ingredientes");
        alert.setHeaderText(cabecera);
        alert.setContentText(contenido);
        alert.showAndWait();
    }
}