package org.example.ecochef;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.ecochef.utils.DatabaseConnection; // Importamos tu clase de conexión

import java.io.IOException;
import java.sql.Connection;

public class EcoChefApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        // --- PRUEBA DE CONEXIÓN ---
        verificarConexion();
        // ---------------------------

        FXMLLoader fxmlLoader = new FXMLLoader(EcoChefApp.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("EcoChef - Bienvenido");
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
            // Aquí podrías mostrar una alerta al usuario si quisieras
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
