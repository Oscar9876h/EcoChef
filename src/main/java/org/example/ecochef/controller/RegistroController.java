package org.example.ecochef.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.ecochef.dao.UsuarioDAOImpl;
import org.example.ecochef.model.Usuario;

import java.io.IOException;

public class RegistroController {

    @FXML private ComboBox<String> comboTipoUsuario;
    @FXML private TextField txtNombre;
    @FXML private TextField txtEmail;
    @FXML private PasswordField txtPassword;
    @FXML private Button btnRegistrar;

    private UsuarioDAOImpl usuarioDAO = new UsuarioDAOImpl();

    /**
     * REQUISITO: Este método se ejecuta al cargar la vista.
     * Sirve para configurar validaciones iniciales.
     */
    @FXML
    public void initialize() {
        // Rellenar el ComboBox (opcional si ya lo haces en SceneBuilder)
        if (comboTipoUsuario.getItems().isEmpty()) {
            comboTipoUsuario.getItems().addAll("Chef", "Comensal", "Administrador");
        }

        // REQUISITO: Desactivar controles que no se pueden usar (el botón empieza apagado)
        btnRegistrar.setDisable(true);

        // REQUISITO: Uso de Expresiones Lambda para escuchar cambios en los campos
        txtNombre.textProperty().addListener((obs, old, newValue) -> validarCampos());
        txtEmail.textProperty().addListener((obs, old, newValue) -> validarCampos());
        txtPassword.textProperty().addListener((obs, old, newValue) -> validarCampos());
        comboTipoUsuario.valueProperty().addListener((obs, old, newValue) -> validarCampos());
    }

    /**
     * Lógica de validación para activar el botón.
     */
    private void validarCampos() {
        boolean camposVacios = txtNombre.getText().trim().isEmpty() ||
                txtEmail.getText().trim().isEmpty() ||
                txtPassword.getText().trim().isEmpty() ||
                comboTipoUsuario.getValue() == null;

        btnRegistrar.setDisable(camposVacios);
    }

    @FXML
    private void handleRegistro() {
        String nombre = txtNombre.getText();
        String email = txtEmail.getText();
        String pass = txtPassword.getText();
        String tipoSeleccionado = comboTipoUsuario.getValue();

        // Creamos el objeto Usuario
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

    private void limpiarCampos() {
        txtNombre.clear();
        txtEmail.clear();
        txtPassword.clear();
        comboTipoUsuario.getSelectionModel().clearSelection();
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}