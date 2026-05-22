package org.example.ecochef.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.ecochef.dao.UsuarioDAOImpl;
import org.example.ecochef.model.Usuario;

import java.io.IOException;

/**
 * Controlador de la vista de registro.
 * Gestiona la creación de nuevos usuarios, valida los campos en tiempo real
 * y maneja la navegación hacia la pantalla de inicio de sesión.
 */
public class RegistroController {

    // Componentes de la interfaz definidos en el FXML
    @FXML private ComboBox<String> comboTipoUsuario;
    @FXML private TextField txtNombre;
    @FXML private TextField txtEmail;
    @FXML private PasswordField txtPassword;
    @FXML private Button btnRegistrar;

    // Objeto para interactuar con la tabla de usuarios en la base de datos
    private final UsuarioDAOImpl usuarioDAO = new UsuarioDAOImpl();

    /**
     * Configuración inicial de la vista: llena el desplegable de roles y
     * añade escuchadores (listeners) a los campos para habilitar/deshabilitar el botón de registro.
     */
    @FXML
    public void initialize() {
        // Inicialización de valores del ComboBox
        if (comboTipoUsuario.getItems().isEmpty()) {
            comboTipoUsuario.getItems().addAll("Chef", "Comensal", "Administrador");
        }

        // El botón comienza deshabilitado hasta que todos los campos sean válidos
        btnRegistrar.setDisable(true);

        // Añadimos escuchadores para validar cada vez que el usuario escribe un carácter
        txtNombre.textProperty().addListener((obs, old, newValue) -> validarCampos());
        txtEmail.textProperty().addListener((obs, old, newValue) -> validarCampos());
        txtPassword.textProperty().addListener((obs, old, newValue) -> validarCampos());
        comboTipoUsuario.valueProperty().addListener((obs, old, newValue) -> validarCampos());
    }

    /**
     * Valida que ninguno de los campos necesarios esté vacío.
     * Si todo es correcto, habilita el botón de registro.
     */
    private void validarCampos() {
        boolean camposVacios = txtNombre.getText().trim().isEmpty() ||
                txtEmail.getText().trim().isEmpty() ||
                txtPassword.getText().trim().isEmpty() ||
                comboTipoUsuario.getValue() == null;

        btnRegistrar.setDisable(camposVacios);
    }

    /**
     * Captura la información ingresada, crea un objeto Usuario y solicita al DAO guardarlo.
     */
    @FXML
    private void handleRegistro() {
        String nombre = txtNombre.getText();
        String email = txtEmail.getText();
        String pass = txtPassword.getText();
        String tipoSeleccionado = comboTipoUsuario.getValue();

        // Mapeo de datos del formulario al modelo Usuario
        Usuario nuevo = new Usuario();
        nuevo.setNombre(nombre);
        nuevo.setEmail(email);
        nuevo.setContrasena(pass);
        nuevo.setRol(tipoSeleccionado);

        try {
            usuarioDAO.guardar(nuevo);
            mostrarAlerta("Éxito", "¡Usuario " + nombre + " registrado correctamente!");
            limpiarCampos();
            irALogin();
        } catch (Exception e) {
            mostrarAlerta("Error", "No se pudo guardar en la base de datos: " + e.getMessage());
        }
    }

    /**
     * Cambia la escena actual a la vista de inicio de sesión (Login).
     */
    @FXML
    private void irALogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/ecochef/login-view.fxml"));
            Scene loginScene = new Scene(loader.load());
            Stage stage = (Stage) txtNombre.getScene().getWindow();
            stage.setScene(loginScene);
            stage.setTitle("EcoChef - Iniciar Sesión");
            stage.show();
        } catch (IOException e) {
            System.err.println("Error al cargar la pantalla de login: " + e.getMessage());
        }
    }

    /**
     * Restablece los valores de los campos del formulario.
     */
    private void limpiarCampos() {
        txtNombre.clear();
        txtEmail.clear();
        txtPassword.clear();
        comboTipoUsuario.getSelectionModel().clearSelection();
    }

    /**
     * Muestra una ventana emergente de información al usuario.
     */
    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}