package org.example.ecochef.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import org.example.ecochef.dao.AlimentoDAOImpl;
import org.example.ecochef.dao.RecetaDAOImpl;
import org.example.ecochef.model.Alimento;
import org.example.ecochef.model.Receta;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class MenuPrincipalController {

    @FXML private FlowPane flowAlimentos;
    @FXML private Label tituloSeccion;
    @FXML private Button btnAñadirAlimento;

    private final AlimentoDAOImpl alimentoDAO = new AlimentoDAOImpl();
    private final RecetaDAOImpl recetaDAO = new RecetaDAOImpl();

    @FXML
    public void initialize() {
        mostrarDespensa();
    }

    @FXML
    public void mostrarDespensa() {
        tituloSeccion.setText("Mi Despensa");
        btnAñadirAlimento.setVisible(true);
        btnAñadirAlimento.setManaged(true);
        actualizarVista(alimentoDAO.listarTodos(), false);
    }

    @FXML
    public void mostrarCaducidad() {
        tituloSeccion.setText("Control de Caducidad");
        btnAñadirAlimento.setVisible(false);
        btnAñadirAlimento.setManaged(false);
        actualizarVista(alimentoDAO.listarProximosACaducar(), true);
    }

    @FXML
    public void mostrarRecetas() {
        tituloSeccion.setText("Libro de Recetas");
        btnAñadirAlimento.setVisible(false);
        btnAñadirAlimento.setManaged(false);
        actualizarVistaRecetas(recetaDAO.listarTodos());
    }

    /**
     * Llena el panel con tarjetas de recetas usando el FXML de Scene Builder
     */
    private void actualizarVistaRecetas(List<Receta> lista) {
        flowAlimentos.getChildren().clear();

        if (lista.isEmpty()) {
            flowAlimentos.getChildren().add(new Label("No hay recetas disponibles."));
            return;
        }

        for (Receta receta : lista) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/ecochef/tarjeta-receta.fxml"));
                VBox tarjeta = loader.load();

                // Enlace con los IDs del Scene Builder
                Label lbNombre = (Label) tarjeta.lookup("#lbNombre");
                Label lbTiempo = (Label) tarjeta.lookup("#lbTiempo");
                Label lbDesc = (Label) tarjeta.lookup("#lbDesc");
                Button btnCheck = (Button) tarjeta.lookup("#btnCheck");
                Button btnEliminar = (Button) tarjeta.lookup("#btnEliminar");

                if (lbNombre != null) lbNombre.setText(receta.getNombreReceta().toUpperCase());
                if (lbTiempo != null) lbTiempo.setText("⏱ " + receta.getTiempoPreparacion() + " min");
                if (lbDesc != null) lbDesc.setText(receta.getDescripcion());

                if (btnCheck != null) {
                    btnCheck.setOnAction(event -> {
                        String disponibilidad = recetaDAO.comprobarDisponibilidad(receta.getId());
                        mostrarAlerta("Ingredientes para " + receta.getNombreReceta(), disponibilidad);
                    });
                }

                if (btnEliminar != null) {
                    btnEliminar.setOnAction(event -> {
                        recetaDAO.eliminar(receta.getId());
                        mostrarRecetas();
                    });
                }

                flowAlimentos.getChildren().add(tarjeta);

            } catch (IOException e) {
                System.err.println("Error cargando tarjeta-receta en recetas: " + e.getMessage());
            }
        }
    }

    /**
     * Llena el panel con tarjetas de alimentos reutilizando el FXML
     */
    private void actualizarVista(List<Alimento> lista, boolean esVistaCaducidad) {
        flowAlimentos.getChildren().clear();

        if (lista.isEmpty()) {
            String msg = esVistaCaducidad ? "✅ Todo al día." : "Despensa vacía.";
            flowAlimentos.getChildren().add(new Label(msg));
            return;
        }

        for (Alimento alimento : lista) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/ecochef/tarjeta-receta.fxml"));
                VBox tarjeta = loader.load();

                Label lbNombre = (Label) tarjeta.lookup("#lbNombre");
                Label lbInfo = (Label) tarjeta.lookup("#lbTiempo");
                Label lbFecha = (Label) tarjeta.lookup("#lbDesc");
                Button btnCheck = (Button) tarjeta.lookup("#btnCheck");
                Button btnEliminar = (Button) tarjeta.lookup("#btnEliminar");

                if (lbNombre != null) lbNombre.setText(alimento.getNombre().toUpperCase());
                if (lbInfo != null) lbInfo.setText(alimento.getTipo() + " | " + alimento.getCalorias() + " cal");

                String fechaStr = (alimento.getFechaCaducidad() != null) ? alimento.getFechaCaducidad().toString() : "S/F";
                if (lbFecha != null) lbFecha.setText("📅 Caduca: " + fechaStr);

                // Configuramos visibilidad (La estética se queda en el FXML)
                if (btnCheck != null) {
                    btnCheck.setVisible(false);
                    btnCheck.setManaged(false);
                }

                if (btnEliminar != null) {
                    btnEliminar.setText("Eliminar");
                    btnEliminar.setOnAction(event -> {
                        alimentoDAO.eliminar(alimento.getId());
                        if (esVistaCaducidad) mostrarCaducidad(); else mostrarDespensa();
                    });
                }

                flowAlimentos.getChildren().add(tarjeta);

            } catch (IOException e) {
                System.err.println("Error cargando tarjeta en alimentos: " + e.getMessage());
            }
        }
    }

    /**
     * Método auxiliar para alertas
     */
    private void mostrarAlerta(String cabecera, String contenido) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("EcoChef");
        alert.setHeaderText(cabecera);
        alert.setContentText(contenido);
        alert.showAndWait();
    }

    @FXML
    public void abrirFormularioAñadir() {
        Dialog<Alimento> dialog = new Dialog<>();
        dialog.setTitle("Nuevo Alimento");

        ButtonType guardarButtonType = new ButtonType("Guardar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(guardarButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10); grid.setVgap(10);
        grid.setPadding(new Insets(20, 20, 10, 10));

        TextField txtNombre = new TextField();
        ComboBox<String> comboTipo = new ComboBox<>();
        comboTipo.getItems().addAll("Fruta", "Verdura", "Carne", "Pescado", "Lácteo", "Legumbre", "Otros");
        comboTipo.setValue("Fruta");
        TextField txtCalorias = new TextField();
        DatePicker pickerFecha = new DatePicker(LocalDate.now());

        grid.add(new Label("Nombre:"), 0, 0); grid.add(txtNombre, 1, 0);
        grid.add(new Label("Tipo:"), 0, 1); grid.add(comboTipo, 1, 1);
        grid.add(new Label("Calorías:"), 0, 2); grid.add(txtCalorias, 1, 2);
        grid.add(new Label("Caducidad:"), 0, 3); grid.add(pickerFecha, 1, 3);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == guardarButtonType) {
                Alimento a = new Alimento();
                a.setNombre(txtNombre.getText());
                a.setTipo(comboTipo.getValue());
                try { a.setCalorias(Integer.parseInt(txtCalorias.getText())); } catch (Exception e) { a.setCalorias(0); }
                a.setFechaCaducidad(pickerFecha.getValue());
                a.setIdCategoriaAlimento(1);
                return a;
            }
            return null;
        });

        Optional<Alimento> result = dialog.showAndWait();
        result.ifPresent(alimento -> {
            alimentoDAO.guardar(alimento);
            mostrarDespensa();
        });
    }
}