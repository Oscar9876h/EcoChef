package org.example.ecochef.controller;

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
 * Gestiona la creación de nuevos usuarios de forma simplificada.
 */
public class RegistroController {

    @FXML private TextField txtNombre;
    @FXML private TextField txtEmail;
    @FXML private PasswordField txtPassword;
    @FXML private Button btnRegistrar;

    private final UsuarioDAOImpl usuarioDAO = new UsuarioDAOImpl();

    @FXML
    public void initialize() {
        btnRegistrar.setDisable(true);

        // Validamos campos mientras el usuario escribe
        txtNombre.textProperty().addListener((obs, old, newValue) -> validarCampos());
        txtEmail.textProperty().addListener((obs, old, newValue) -> validarCampos());
        txtPassword.textProperty().addListener((obs, old, newValue) -> validarCampos());
    }

    private void validarCampos() {
        boolean camposVacios = txtNombre.getText().trim().isEmpty() ||
                txtEmail.getText().trim().isEmpty() ||
                txtPassword.getText().trim().isEmpty();

        btnRegistrar.setDisable(camposVacios);
    }

    @FXML
    private void handleRegistro() {
        Usuario nuevo = new Usuario();
        nuevo.setNombre(txtNombre.getText());
        nuevo.setEmail(txtEmail.getText());
        nuevo.setContrasena(txtPassword.getText());
        // Rol por defecto para usuarios registrados desde la app
        nuevo.setRol("Comensal");

        try {
            usuarioDAO.guardar(nuevo);
            mostrarAlerta("Éxito", "Usuario registrado correctamente.");
            limpiarCampos();
            irALogin();
        } catch (Exception e) {
            mostrarAlerta("Error", "No se pudo registrar: " + e.getMessage());
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
            System.err.println("Error al ir a login: " + e.getMessage());
        }
    }

    private void limpiarCampos() {
        txtNombre.clear();
        txtEmail.clear();
        txtPassword.clear();
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}