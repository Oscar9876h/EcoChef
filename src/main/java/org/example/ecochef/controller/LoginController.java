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
 * Gestiona la autenticación de usuarios, la validación de formatos y
 * la inicialización de la sesión activa al acceder al sistema.
 */
public class LoginController {

    @FXML private TextField txtEmail;
    @FXML private PasswordField txtPassword;
    @FXML private Button btnLogin;

    private final UsuarioDAOImpl usuarioDAO = new UsuarioDAOImpl();

    /**
     * Configura los listeners para habilitar el botón de login solo
     * cuando los campos obligatorios contienen texto.
     */
    @FXML
    public void initialize() {
        btnLogin.setDisable(true);
        txtEmail.textProperty().addListener((obs, old, newValue) -> validarBoton());
        txtPassword.textProperty().addListener((obs, old, newValue) -> validarBoton());
    }

    /**
     * Determina el estado del botón de inicio de sesión según el contenido de los campos.
     */
    private void validarBoton() {
        boolean camposVacios = txtEmail.getText().trim().isEmpty() ||
                txtPassword.getText().trim().isEmpty();
        btnLogin.setDisable(camposVacios);
    }

    /**
     * Procesa la autenticación llamando al DAO.
     * Utiliza la clase ValidacionUtils para asegurar la integridad de los datos antes de consultar la BBDD.
     */
    @FXML
    private void handleLogin() {
        String email = txtEmail.getText().trim();
        String pass = txtPassword.getText();

        // Validamos el formato del email antes de consultar la persistencia
        if (!ValidacionUtils.emailValido(email)) {
            mostrarAlerta("Formato no válido", "El email debe ser válido (ejemplo@dominio.com).");
            return;
        }

        // Ejecución de login mediante el DAO
        Usuario usuarioLogueado = usuarioDAO.login(email, pass);

        if (usuarioLogueado != null) {
            // Gestión de sesión: se almacenan los datos del usuario en la clase estática de sesión
            SesionActiva.nombreUsuarioLogueado = usuarioLogueado.getNombre();
            SesionActiva.rolUsuarioLogueado = (usuarioLogueado.getRol() != null)
                    ? usuarioLogueado.getRol().toLowerCase().trim()
                    : "usuario";

            irAMenuPrincipal();
        } else {
            mostrarAlerta("Error de acceso", "Email o contraseña incorrectos.");
        }
    }

    /**
     * Realiza el cambio de escena hacia la ventana principal (HelloView).
     */
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

    /**
     * Muestra alertas de error al usuario mediante componentes del sistema.
     * @param titulo Título de la ventana de alerta.
     * @param mensaje Descripción del error.
     */
    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}