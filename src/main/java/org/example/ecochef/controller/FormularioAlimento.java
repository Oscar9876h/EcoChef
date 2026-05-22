package org.example.ecochef.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.ecochef.dao.AlimentoDAOImpl;
import org.example.ecochef.model.Alimento;
import java.time.LocalDate;

/**
 * Controlador encargado de gestionar la ventana de creación de nuevos alimentos.
 * Captura los datos ingresados por el usuario y los envía a la capa de persistencia (DAO).
 */
public class FormularioAlimento {

    // Componentes de la interfaz definidos en el FXML
    @FXML private TextField txtNombre;
    @FXML private TextField txtTipo;
    @FXML private TextField txtCalorias;
    @FXML private DatePicker dateCaducidad;
    @FXML private TextField txtIdCategoria;

    // Objeto DAO para interactuar con la tabla de alimentos en la base de datos
    private final AlimentoDAOImpl alimentoDAO = new AlimentoDAOImpl();

    /**
     * Valida los campos, crea un objeto Alimento y lo guarda en la base de datos.
     * Si la validación falla (ej. calorías no numéricas), muestra un error.
     */
    @FXML
    private void guardar() {
        try {
            String nombre = txtNombre.getText();
            String tipo = txtTipo.getText();

            // Validamos que las calorías sean un entero válido
            int calorias = txtCalorias.getText().isEmpty() ? 0 : Integer.parseInt(txtCalorias.getText());
            LocalDate caducidad = dateCaducidad.getValue();

            // ID de categoría asignado por defecto al no existir selección en formulario
            int idCategoriaAutomatico = 1;

            // Verificamos que los campos obligatorios no estén vacíos
            if (!nombre.isEmpty() && caducidad != null) {
                // Creamos el nuevo objeto Alimento (id=0 indica que será autoincrementado por BD)
                Alimento nuevoAlimento = new Alimento(0, nombre, tipo, calorias, idCategoriaAutomatico, caducidad);
                alimentoDAO.guardar(nuevoAlimento);
                cerrarVentana();
            } else {
                mostrarAlerta("Campos incompletos", "Por favor, rellena al menos el nombre y la fecha de caducidad.");
            }
        } catch (NumberFormatException e) {
            mostrarAlerta("Error de formato", "Las calorías deben ser un número válido.");
        }
    }

    /**
     * Muestra una ventana de alerta al usuario en caso de error.
     * @param titulo Título de la ventana de alerta.
     * @param contenido Mensaje explicativo del error.
     */
    private void mostrarAlerta(String titulo, String contenido) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setContentText(contenido);
        alert.showAndWait();
    }

    /**
     * Acción vinculada al botón Cancelar: cierra la ventana sin guardar nada.
     */
    @FXML
    private void cancelar() {
        cerrarVentana();
    }

    /**
     * Método auxiliar para obtener el Stage actual y cerrar la ventana.
     */
    private void cerrarVentana() {
        Stage stage = (Stage) txtNombre.getScene().getWindow();
        stage.close();
    }
}