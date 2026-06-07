package org.example.ecochef.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.ecochef.dao.UsuarioDAOImpl;
import org.example.ecochef.model.Usuario;
import org.example.ecochef.model.SesionActiva;
import org.example.ecochef.util.ValidacionUtils;

import java.io.IOException;

/**
 * Controlador de la vista de Login.
 * Gestiona la autenticación y la inicialización de la sesión global.
 */
public class LoginController {

    @FXML private TextField txtEmail;
    @FXML private PasswordField txtPassword;
    @FXML private Button btnLogin;

    private final UsuarioDAOImpl usuarioDAO = new UsuarioDAOImpl();

    @FXML
    public void initialize() {
        btnLogin.setDisable(true);
        txtEmail.textProperty().addListener((obs, old, newValue) -> validarBoton());
        txtPassword.textProperty().addListener((obs, old, newValue) -> validarBoton());
    }

    private void validarBoton() {
        boolean camposVacios = txtEmail.getText().trim().isEmpty() ||
                txtPassword.getText().trim().isEmpty();
        btnLogin.setDisable(camposVacios);
    }

    @FXML
    private void handleLogin() {
        String email = txtEmail.getText().trim();
        String pass = txtPassword.getText();

        if (!ValidacionUtils.emailValido(email)) {
            mostrarAlerta("Formato no válido", "El email debe ser válido (ejemplo@dominio.com).");
            return;
        }

        Usuario usuarioLogueado = usuarioDAO.login(email, pass);

        if (usuarioLogueado != null) {
            // Guardamos nombre y rol. Convertimos a mayúsculas para asegurar coincidencia total.
            SesionActiva.nombreUsuarioLogueado = usuarioLogueado.getNombre();
            SesionActiva.rolUsuarioLogueado = (usuarioLogueado.getRol() != null)
                    ? usuarioLogueado.getRol().toUpperCase().trim()
                    : "USUARIO";

            irAMenuPrincipal();
        } else {
            mostrarAlerta("Error de acceso", "Email o contraseña incorrectos.");
        }
    }

    private void irAMenuPrincipal() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/ecochef/hello-view.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) txtEmail.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("EcoChef - Menú Principal");
        } catch (IOException e) {
            System.err.println("Error al cargar menú: " + e.getMessage());
        }
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}