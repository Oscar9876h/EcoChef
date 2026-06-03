package org.example.ecochef.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.ecochef.dao.AlimentoDAOImpl;
import org.example.ecochef.dao.CategoriaDAOImpl;
import org.example.ecochef.model.Alimento;
import java.time.LocalDate;
import java.util.List;

public class FormularioAlimento {

    @FXML private TextField txtNombre;
    @FXML private TextField txtCalorias;
    @FXML private DatePicker dateCaducidad;
    @FXML private ComboBox<String> comboCategorias;

    private final AlimentoDAOImpl alimentoDAO = new AlimentoDAOImpl();
    private final CategoriaDAOImpl categoriaDAO = new CategoriaDAOImpl();

    @FXML
    public void initialize() {
        cargarCategorias();
    }

    private void cargarCategorias() {
        List<String> categorias = categoriaDAO.obtenerNombresCategorias();
        comboCategorias.getItems().setAll(categorias);
    }

    @FXML
    private void guardar() {
        try {
            String nombre = txtNombre.getText();
            int calorias = txtCalorias.getText().isEmpty() ? 0 : Integer.parseInt(txtCalorias.getText());
            LocalDate caducidad = dateCaducidad.getValue();
            String nombreCategoria = comboCategorias.getValue(); // Esto es el "tipo"

            if (!nombre.isEmpty() && caducidad != null && nombreCategoria != null) {
                int idCategoria = categoriaDAO.obtenerIdPorNombre(nombreCategoria);

                // Usamos nombreCategoria como el "tipo"
                Alimento nuevoAlimento = new Alimento(0, nombre, nombreCategoria, calorias, idCategoria, caducidad);
                alimentoDAO.guardar(nuevoAlimento);

                cerrarVentana();
            } else {
                mostrarAlerta("Campos incompletos", "Por favor, rellena todos los datos.");
            }
        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "Las calorías deben ser un número.");
        }
    }

    @FXML private void cancelar() { cerrarVentana(); }
    private void cerrarVentana() { ((Stage) txtNombre.getScene().getWindow()).close(); }
    private void mostrarAlerta(String t, String c) {
        Alert a = new Alert(Alert.AlertType.ERROR); a.setTitle(t); a.setContentText(c); a.showAndWait();
    }
}