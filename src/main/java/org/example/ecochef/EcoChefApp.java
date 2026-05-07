package org.example.ecochef;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.ecochef.utils.DatabaseConnection;

import java.io.IOException;
import java.sql.Connection;

public class EcoChefApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        // PRUEBA DE CONEXIÓN
        verificarConexion();

        // CAMBIO: Ahora cargamos "registro-view.fxml" como pantalla inicial
        FXMLLoader fxmlLoader = new FXMLLoader(EcoChefApp.class.getResource("registro-view.fxml"));

        // Ajustamos un poco el alto (600) porque el registro tiene más campos que el login
        Scene scene = new Scene(fxmlLoader.load(), 400, 600);

        stage.setTitle("EcoChef - Crear Cuenta");
        stage.setScene(scene);
        stage.show();
    }

    private void verificarConexion() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            if (conn != null && !conn.isClosed()) {
                System.out.println("✅ [Database] Conexión establecida correctamente con EcoChef DB.");
            }
        } catch (Exception e) {
            System.err.println("❌ [Database] ERROR: No se pudo conectar a la base de datos.");
            System.err.println("Detalle: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        launch();
    }
}