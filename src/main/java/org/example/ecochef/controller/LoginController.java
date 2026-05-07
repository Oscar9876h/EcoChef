package org.example.ecochef.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.ecochef.dao.UsuarioDAOImpl;
import org.example.ecochef.model.Usuario;

import java.io.IOException;

public class LoginController {

    @FXML private TextField txtEmail;
    @FXML private PasswordField txtPassword;
    @FXML private Button btnLogin; // RECUERDA: Ponle este fx:id al botón en SceneBuilder

    private UsuarioDAOImpl usuarioDAO = new UsuarioDAOImpl();

    @FXML
    public void initialize() {
        // El botón empieza desactivado hasta que Pepe escriba algo
        btnLogin.setDisable(true);

        // Escuchamos los cambios en el texto (Expresiones Lambda)
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

        // 1. Buscamos a Pepe en la DB
        Usuario usuarioLogueado = usuarioDAO.login(email, pass);

        if (usuarioLogueado != null) {
            System.out.println("✅ Login correcto: Bienvenido " + usuarioLogueado.getNombre());
            // 2. Si Pepe existe, entramos al menú
            irAMenuPrincipal();
        } else {
            // 3. Si se equivoca, le avisamos
            mostrarAlerta("Error de acceso", "Email o contraseña incorrectos.");
        }
    }

    private void irAMenuPrincipal() {
        try {
            // Cargamos la pantalla principal (ajusta el nombre si no es hello-view.fxml)
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
