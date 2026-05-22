package org.example.ecochef.controller;

import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import org.example.ecochef.dao.RecetaDAOImpl;
import org.example.ecochef.model.Receta;
import org.example.ecochef.model.SesionActiva;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class RecetasController implements Initializable {

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

        // 🎯 --- PUNTO 1: USO DE STREAMS Y EXPRESIONES LAMBDA (REQUISITO COMPLETADO) ---
        // Clasificamos los cajones en memoria filtrando con la API de Streams de Java 8+
        List<Receta> saludables = lista.stream()
                .filter(r -> {
                    String n = r.getNombreReceta().toUpperCase();
                    return n.contains("GAZPACHO") || n.contains("ENSALADA") || n.contains("VERDURA") || n.contains("CREMA");
                })
                .collect(Collectors.toList());

        List<Receta> caprichos = lista.stream()
                .filter(r -> {
                    String n = r.getNombreReceta().toUpperCase();
                    return n.contains("CARBONARA") || n.contains("PASTA") || n.contains("HAMBURGUESA") || n.contains("PIZZA");
                })
                .collect(Collectors.toList());

        List<Receta> otras = lista.stream()
                .filter(r -> {
                    String n = r.getNombreReceta().toUpperCase();
                    boolean esSaludable = n.contains("GAZPACHO") || n.contains("ENSALADA") || n.contains("VERDURA") || n.contains("CREMA");
                    boolean esCapricho = n.contains("CARBONARA") || n.contains("PASTA") || n.contains("HAMBURGUESA") || n.contains("PIZZA");
                    return !esSaludable && !esCapricho;
                })
                .collect(Collectors.toList());

        // 3. PINTAMOS LAS TRES SECCIONES
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

    private void crearBloqueSeccion(String nombreSeccion, List<Receta> listaRecetas) {
        Label titulo = new Label(nombreSeccion + " (" + listaRecetas.size() + ")");
        flowRecetasContenedor.getChildren().add(titulo);

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

                    if (!SesionActiva.esAdmin()) {
                        btnEliminar.setVisible(false);
                        btnEliminar.setManaged(false);
                    }
                }

                flowSeccion.getChildren().add(tarjeta);

            } catch (IOException e) {
                System.err.println("Error cargando tarjeta en RecetasController: " + e.getMessage());
            }
        }

        flowRecetasContenedor.getChildren().add(flowSeccion);
    }

    @FXML
    public void abrirFormularioAnadir() {
        Dialog<Receta> dialog = new Dialog<>();
        dialog.setTitle("Nueva Receta Oficial");
        dialog.setHeaderText("Introduce los datos de la receta para el catálogo global:");

        ButtonType guardarButtonType = new ButtonType("Guardar Receta", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(guardarButtonType, ButtonType.CANCEL);

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

        // 🎯 --- PUNTO 3: CONTROL DE ESTADOS DE LA INTERFAZ (BOTÓN DESACTIVADO) ---
        Node botonGuardar = dialog.getDialogPane().lookupButton(guardarButtonType);
        botonGuardar.setDisable(true); // Nace desactivado de fábrica

        // Añadimos un listener para vigilar que el admin escriba cosas lógicas antes de pulsar
        txtNombre.textProperty().addListener((observable, oldValue, newValue) -> {
            boolean formularioInvalido = newValue.trim().isEmpty() || txtTiempo.getText().trim().isEmpty();
            botonGuardar.setDisable(formularioInvalido);
        });

        txtTiempo.textProperty().addListener((observable, oldValue, newValue) -> {
            boolean formularioInvalido = txtNombre.getText().trim().isEmpty() || newValue.trim().isEmpty();
            botonGuardar.setDisable(formularioInvalido);
        });

        // Hacemos que el cursor empiece directamente enfocado en el nombre por usabilidad
        Platform.runLater(txtNombre::requestFocus);

        // Convertimos la información al pulsar Guardar
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == guardarButtonType) {
                Receta nuevaReceta = new Receta();
                nuevaReceta.setNombreReceta(txtNombre.getText().trim());
                nuevaReceta.setDescripcion(txtDescripcion.getText().trim());

                // 🎯 --- PUNTO 2: VALIDACIÓN DE ENTRADA Y PROTECCIÓN CONTRA FALLOS ---
                try {
                    nuevaReceta.setTiempoPreparacion(Integer.parseInt(txtTiempo.getText().trim()));
                    return nuevaReceta; // Si pasa la conversión, todo correcto
                } catch (NumberFormatException e) {
                    // Detiene la conversión devolviendo null para evitar el crash mecánico de Java
                    return null;
                }
            }
            return null;
        });

        Optional<Receta> result = dialog.showAndWait();

        // Si la validación falló (el resultado es vacío), avisamos con un Alert en lugar de colgarse
        if (result.isPresent() && result.get().getNombreReceta() != null) {
            recetaDAO.guardar(result.get());
            cargarRecetas();
        } else if (result.isPresent()) {
            // El usuario pulsó guardar pero el try-catch de arriba atrapó letras en el número
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("EcoChef - Error de validación");
            errorAlert.setHeaderText("Formato numérico no válido");
            errorAlert.setContentText("El tiempo de preparación debe ser obligatoriamente un número entero.");
            errorAlert.showAndWait();

            // Volvemos a abrir el formulario para que el usuario corrija los datos sin perder el flujo
            abrirFormularioAnadir();
        }
    }

    private void mostrarAlerta(String cabecera, String contenido) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("EcoChef - Verificación de Ingredientes");
        alert.setHeaderText(cabecera);
        alert.setContentText(contenido);
        alert.showAndWait();
    }
}