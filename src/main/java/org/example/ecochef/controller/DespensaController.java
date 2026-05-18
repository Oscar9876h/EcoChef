package org.example.ecochef.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import org.example.ecochef.dao.AlimentoDAOImpl;
import org.example.ecochef.model.Alimento;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class DespensaController implements Initializable {

    @FXML private FlowPane flowAlimentos;

    private final AlimentoDAOImpl alimentoDAO = new AlimentoDAOImpl();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Carga los alimentos automáticamente al abrir la pantalla
        cargarDespensa();
    }

    public void cargarDespensa() {
        actualizarVista(alimentoDAO.listarTodos());
    }

    private void actualizarVista(List<Alimento> lista) {
        flowAlimentos.getChildren().clear();

        if (lista.isEmpty()) {
            flowAlimentos.getChildren().add(new Label("Despensa vacía."));
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

                if (btnCheck != null) {
                    btnCheck.setVisible(false);
                    btnCheck.setManaged(false);
                }

                if (btnEliminar != null) {
                    btnEliminar.setText("Eliminar");
                    btnEliminar.setOnAction(event -> {
                        alimentoDAO.eliminar(alimento.getId());
                        cargarDespensa();
                    });
                }

                flowAlimentos.getChildren().add(tarjeta);

            } catch (IOException e) {
                System.err.println("Error cargando tarjeta en DespensaController: " + e.getMessage());
            }
        }
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
            cargarDespensa();
        });
    }
}
