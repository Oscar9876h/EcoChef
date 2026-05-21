package org.example.ecochef.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.ecochef.dao.UsuarioDAOImpl;
import org.example.ecochef.model.Usuario;
import org.example.ecochef.model.SesionActiva; // 🌟 IMPORTANTE: Importamos la sesión activa

import java.io.IOException;

public class LoginController {

    @FXML private TextField txtEmail;
    @FXML private PasswordField txtPassword;
    @FXML private Button btnLogin;

    private UsuarioDAOImpl usuarioDAO = new UsuarioDAOImpl();

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
        String email = txtEmail.getText();
        String pass = txtPassword.getText();

        // 1. Buscamos al usuario en la DB (Tu DAO ya devuelve el objeto con su rol de MySQL)
        Usuario usuarioLogueado = usuarioDAO.login(email, pass);

        if (usuarioLogueado != null) {
            System.out.println("✅ Login correcto: Bienvenido " + usuarioLogueado.getNombre());

            // Guardamos el nombre en la sesión activa
            SesionActiva.nombreUsuarioLogueado = usuarioLogueado.getNombre();


            if (usuarioLogueado.getRol() != null) {
                SesionActiva.rolUsuarioLogueado = usuarioLogueado.getRol().toLowerCase().trim();
            } else {
                SesionActiva.rolUsuarioLogueado = "usuario"; // Por seguridad, si viene nulo
            }

            System.out.println("ℹ️ Rol asignado en sesión: " + SesionActiva.rolUsuarioLogueado);

            // 2. Entramos al menú principal con el rol ya cargado en memoria
            irAMenuPrincipal();
        } else {
            // 3. Si se equivoca, le avisamos
            mostrarAlerta("Error de acceso", "Email o contraseña incorrectos.");
        }
    }

    private void irAMenuPrincipal() {
        try {
            // Cargamos la pantalla principal
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