package org.example.ecochef;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.ecochef.utils.DatabaseConnection;

import java.io.IOException;
import java.sql.Connection;

/**
 * Clase principal de la aplicación EcoChef.
 * Extiende de {@link Application} para gestionar el ciclo de vida de la interfaz gráfica (JavaFX).
 */
public class EcoChefApp extends Application {

    /**
     * Configura y lanza la ventana inicial de la aplicación.
     * @param stage La ventana principal (Stage) de la aplicación.
     */
    @Override
    public void start(Stage stage) throws IOException {

        // Validamos la conectividad con la base de datos antes de cargar la UI
        verificarConexion();

        // Cargamos la vista inicial definida en el FXML
        FXMLLoader fxmlLoader = new FXMLLoader(EcoChefApp.class.getResource("registro-view.fxml"));

        // Definimos la escena y sus dimensiones (400x600 px)
        Scene scene = new Scene(fxmlLoader.load(), 400, 600);

        stage.setTitle("EcoChef - Crear Cuenta");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Realiza una prueba de conexión rápida para asegurar que la base de datos es accesible.
     */
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

    /**
     * Método de entrada estático para lanzar la aplicación JavaFX.
     * @param args Argumentos de línea de comandos.
     */
    public static void main(String[] args) {
        launch();
    }
}