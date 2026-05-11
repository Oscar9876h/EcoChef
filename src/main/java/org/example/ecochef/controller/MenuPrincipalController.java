package org.example.ecochef.controller;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import org.example.ecochef.dao.AlimentoDAOImpl;
import org.example.ecochef.model.Alimento;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class MenuPrincipalController {

    @FXML private FlowPane flowAlimentos;
    @FXML private Label tituloSeccion;

    private AlimentoDAOImpl alimentoDAO = new AlimentoDAOImpl();

    @FXML
    public void initialize() {
        mostrarDespensa();
    }

    @FXML
    public void mostrarDespensa() {
        tituloSeccion.setText("Mi Despensa");
        cargarAlimentos();
    }

    @FXML
    public void mostrarCaducidad() {
        tituloSeccion.setText("Control de Caducidad");
        flowAlimentos.getChildren().clear();
        Label mensaje = new Label("Aquí aparecerán los alimentos próximos a caducar.");
        mensaje.setStyle("-fx-text-fill: #7f8c8d; -fx-font-style: italic;");
        flowAlimentos.getChildren().add(mensaje);
    }

    @FXML
    public void mostrarRecetas() {
        tituloSeccion.setText("Libro de Recetas");
        flowAlimentos.getChildren().clear();
        Label mensaje = new Label("Sección de recetas en construcción...");
        mensaje.setStyle("-fx-text-fill: #7f8c8d; -fx-font-style: italic;");
        flowAlimentos.getChildren().add(mensaje);
    }

    @FXML
    public void abrirFormularioAñadir() {
        // 1. Crear el Diálogo personalizado
        Dialog<Alimento> dialog = new Dialog<>();
        dialog.setTitle("Nuevo Alimento");
        dialog.setHeaderText("Añadir detalles del alimento");

        // 2. Botones de Guardar y Cancelar
        ButtonType guardarButtonType = new ButtonType("Guardar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(guardarButtonType, ButtonType.CANCEL);

        // 3. Crear el formulario (GridPane)
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField txtNombre = new TextField();
        txtNombre.setPromptText("Nombre del alimento");

        ComboBox<String> comboTipo = new ComboBox<>();
        comboTipo.getItems().addAll("Fruta", "Verdura", "Carne", "Pescado", "Lácteo", "Legumbre", "Otros");
        comboTipo.setValue("Fruta");

        TextField txtCalorias = new TextField();
        txtCalorias.setPromptText("Cantidad o Calorías");

        DatePicker pickerFecha = new DatePicker(LocalDate.now());

        grid.add(new Label("Nombre:"), 0, 0);
        grid.add(txtNombre, 1, 0);
        grid.add(new Label("Tipo:"), 0, 1);
        grid.add(comboTipo, 1, 1);
        grid.add(new Label("Cant/Calorías:"), 0, 2);
        grid.add(txtCalorias, 1, 2);
        grid.add(new Label("Caducidad:"), 0, 3);
        grid.add(pickerFecha, 1, 3);

        dialog.getDialogPane().setContent(grid);

        // 4. Convertir los datos al pulsar Guardar
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == guardarButtonType) {
                Alimento a = new Alimento();
                a.setNombre(txtNombre.getText());
                a.setTipo(comboTipo.getValue());
                try {
                    a.setCalorias(Integer.parseInt(txtCalorias.getText()));
                } catch (NumberFormatException e) { a.setCalorias(0); }
                a.setFechaCaducidad(pickerFecha.getValue());
                a.setIdCategoriaAlimento(1);
                return a;
            }
            return null;
        });

        // 5. Mostrar y procesar el resultado
        Optional<Alimento> result = dialog.showAndWait();
        result.ifPresent(alimento -> {
            alimentoDAO.guardar(alimento);
            cargarAlimentos();
        });
    }

    @FXML
    public void cargarAlimentos() {
        flowAlimentos.getChildren().clear();
        List<Alimento> lista = alimentoDAO.listarTodos();

        if (lista.isEmpty()) {
            flowAlimentos.getChildren().add(new Label("No hay alimentos en la despensa."));
            return;
        }

        for (Alimento alimento : lista) {
            VBox tarjeta = new VBox(5);
            tarjeta.setStyle("-fx-background-color: #ffffff; -fx-padding: 15; -fx-border-color: #27ae60; " +
                    "-fx-border-radius: 10; -fx-background-radius: 10; " +
                    "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 5, 0, 0, 0);");
            tarjeta.setPrefWidth(160);

            Label lbNombre = new Label(alimento.getNombre().toUpperCase());
            lbNombre.setStyle("-fx-font-weight: bold; -fx-text-fill: #2c3e50;");

            Label lbInfo = new Label(alimento.getTipo() + " | " + alimento.getCalorias());
            lbInfo.setStyle("-fx-font-size: 11px; -fx-text-fill: #7f8c8d;");

            // Añadimos la fecha de caducidad a la tarjeta si existe
            String fechaStr = (alimento.getFechaCaducidad() != null) ? alimento.getFechaCaducidad().toString() : "S/F";
            Label lbFecha = new Label("📅 " + fechaStr);
            lbFecha.setStyle("-fx-font-size: 10px; -fx-text-fill: #e67e22;");

            tarjeta.getChildren().addAll(lbNombre, lbInfo, lbFecha);
            flowAlimentos.getChildren().add(tarjeta);
        }
    }
}