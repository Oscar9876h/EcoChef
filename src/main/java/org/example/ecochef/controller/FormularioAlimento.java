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
    @FXML private TextField txtTipo;
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
            String tipo = txtTipo.getText();
            int calorias = txtCalorias.getText().isEmpty() ? 0 : Integer.parseInt(txtCalorias.getText());
            LocalDate caducidad = dateCaducidad.getValue();
            String nombreCategoria = comboCategorias.getValue();

            if (!nombre.isEmpty() && caducidad != null && nombreCategoria != null) {
                // Buscamos el ID real de la categoría seleccionada
                int idCategoria = categoriaDAO.obtenerIdPorNombre(nombreCategoria);

                // Creamos y guardamos el alimento con el ID correcto
                Alimento nuevoAlimento = new Alimento(0, nombre, tipo, calorias, idCategoria, caducidad);
                alimentoDAO.guardar(nuevoAlimento);

                cerrarVentana();
            } else {
                mostrarAlerta("Campos incompletos", "Por favor, selecciona una categoría y rellena los datos obligatorios.");
            }
        } catch (NumberFormatException e) {
            mostrarAlerta("Error de formato", "Las calorías deben ser un número válido.");
        }
    }

    @FXML
    private void cancelar() {
        cerrarVentana();
    }

    private void cerrarVentana() {
        Stage stage = (Stage) txtNombre.getScene().getWindow();
        stage.close();
    }

    private void mostrarAlerta(String titulo, String contenido) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setContentText(contenido);
        alert.showAndWait();
    }
}