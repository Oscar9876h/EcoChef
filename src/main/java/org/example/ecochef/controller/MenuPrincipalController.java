package org.example.ecochef.controller;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import org.example.ecochef.dao.AlimentoDAOImpl;
import org.example.ecochef.dao.RecetaDAOImpl;
import org.example.ecochef.model.Alimento;
import org.example.ecochef.model.Receta;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class MenuPrincipalController {

    @FXML private FlowPane flowAlimentos;
    @FXML private Label tituloSeccion;
    @FXML private Button btnAñadirAlimento;

    private AlimentoDAOImpl alimentoDAO = new AlimentoDAOImpl();
    private RecetaDAOImpl recetaDAO = new RecetaDAOImpl();

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
     * Carga las recetas e incluye el botón de verificación de ingredientes
     */
    private void actualizarVistaRecetas(List<Receta> lista) {
        flowAlimentos.getChildren().clear();

        if (lista.isEmpty()) {
            Label mensaje = new Label("No hay recetas disponibles en la base de datos.");
            mensaje.setStyle("-fx-text-fill: #7f8c8d; -fx-padding: 20;");
            flowAlimentos.getChildren().add(mensaje);
            return;
        }

        for (Receta receta : lista) {
            VBox tarjeta = new VBox(8);
            tarjeta.setStyle("-fx-background-color: #ffffff; -fx-padding: 15; -fx-border-color: #3498db; " +
                    "-fx-border-radius: 10; -fx-background-radius: 10; " +
                    "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 5, 0, 0, 0);");
            tarjeta.setPrefWidth(200);

            Label lbNombre = new Label(receta.getNombreReceta().toUpperCase());
            lbNombre.setStyle("-fx-font-weight: bold; -fx-text-fill: #2980b9;");

            Label lbTiempo = new Label("⏱ " + receta.getTiempoPreparacion() + " min");
            lbTiempo.setStyle("-fx-font-size: 11px; -fx-text-fill: #7f8c8d;");

            Label lbDesc = new Label(receta.getDescripcion());
            lbDesc.setStyle("-fx-font-size: 10px;");
            lbDesc.setWrapText(true);
            lbDesc.setMaxHeight(50);

            // --- NUEVO: Botón de Comprobación de Ingredientes ---
            Button btnCheck = new Button("¿Tengo los ingredientes?");
            btnCheck.setMaxWidth(Double.MAX_VALUE);
            btnCheck.setStyle("-fx-background-color: #f1c40f; -fx-text-fill: #2c3e50; -fx-font-weight: bold; -fx-cursor: hand; -fx-font-size: 10px;");

            btnCheck.setOnAction(event -> {
                // Llama al método que añadimos al RecetaDAOImpl
                String disponibilidad = recetaDAO.comprobarDisponibilidad(receta.getId());

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("EcoChef - Verificador");
                alert.setHeaderText("Ingredientes para " + receta.getNombreReceta());
                alert.setContentText(disponibilidad);
                alert.showAndWait();
            });

            Button btnEliminar = new Button("Borrar Receta");
            btnEliminar.setMaxWidth(Double.MAX_VALUE);
            btnEliminar.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-size: 10px; -fx-cursor: hand;");

            btnEliminar.setOnAction(event -> {
                recetaDAO.eliminar(receta.getId());
                mostrarRecetas();
            });

            // Añadimos todos los elementos a la tarjeta
            tarjeta.getChildren().addAll(lbNombre, lbTiempo, lbDesc, btnCheck, btnEliminar);
            flowAlimentos.getChildren().add(tarjeta);
        }
    }

    private void actualizarVista(List<Alimento> lista, boolean esVistaCaducidad) {
        flowAlimentos.getChildren().clear();

        if (lista.isEmpty()) {
            String msg = esVistaCaducidad ? "✅ No hay alimentos que caduquen pronto." : "Tu despensa está vacía.";
            Label lbVacío = new Label(msg);
            lbVacío.setStyle("-fx-text-fill: #7f8c8d; -fx-padding: 20;");
            flowAlimentos.getChildren().add(lbVacío);
            return;
        }

        for (Alimento alimento : lista) {
            VBox tarjeta = crearTarjeta(alimento, esVistaCaducidad);
            flowAlimentos.getChildren().add(tarjeta);
        }
    }

    private VBox crearTarjeta(Alimento alimento, boolean esVistaCaducidad) {
        VBox tarjeta = new VBox(8);
        String colorBorde = esVistaCaducidad ? "#e67e22" : "#27ae60";
        tarjeta.setStyle("-fx-background-color: #ffffff; -fx-padding: 15; -fx-border-color: " + colorBorde + "; " +
                "-fx-border-radius: 10; -fx-background-radius: 10; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 5, 0, 0, 0);");
        tarjeta.setPrefWidth(160);

        Label lbNombre = new Label(alimento.getNombre().toUpperCase());
        lbNombre.setStyle("-fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        Label lbInfo = new Label(alimento.getTipo() + " | " + alimento.getCalorias() + " cal");
        lbInfo.setStyle("-fx-font-size: 11px; -fx-text-fill: #7f8c8d;");

        String fechaStr = (alimento.getFechaCaducidad() != null) ? alimento.getFechaCaducidad().toString() : "S/F";
        Label lbFecha = new Label("📅 " + fechaStr);
        lbFecha.setStyle("-fx-font-size: 10px; -fx-text-fill: " + (esVistaCaducidad ? "#c0392b" : "#2c3e50") + ";");

        Button btnEliminar = new Button("Eliminar");
        btnEliminar.setMaxWidth(Double.MAX_VALUE);
        btnEliminar.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-size: 10px; -fx-cursor: hand; -fx-font-weight: bold;");

        btnEliminar.setOnAction(event -> {
            alimentoDAO.eliminar(alimento.getId());
            if (esVistaCaducidad) mostrarCaducidad(); else mostrarDespensa();
        });

        tarjeta.getChildren().addAll(lbNombre, lbInfo, lbFecha, btnEliminar);
        return tarjeta;
    }

    @FXML
    public void abrirFormularioAñadir() {
        Dialog<Alimento> dialog = new Dialog<>();
        dialog.setTitle("Nuevo Alimento");
        dialog.setHeaderText("Añadir detalles del alimento");

        ButtonType guardarButtonType = new ButtonType("Guardar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(guardarButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10); grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

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