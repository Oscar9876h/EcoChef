package org.example.ecochef.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static Connection connection = null;

    public static Connection getConnection() {
        if (connection == null) {
            try {
                // CAMBIO CLAVE: Usamos el nuevo método que lee desde Resources
                Configuracion config = XMLManager.readXMLFromResources(
                        Configuracion.class,
                        "/org/example/ecochef/db_config.xml"
                );

                if (config == null) {
                    System.err.println("Error: No se pudo cargar la configuración del XML.");
                    return null;
                }

                String url = "jdbc:mysql://" + config.getHost() + ":" + config.getPort() + "/" + config.getDatabase();

                connection = DriverManager.getConnection(url, config.getUser(), config.getPassword());
                // Este mensaje saldrá si la conexión física con MySQL tiene éxito
                System.out.println("✅ Conexión establecida con éxito a: " + config.getDatabase());

            } catch (SQLException e) {
                System.err.println("❌ Error al conectar con la base de datos: " + e.getMessage());
            }
        }
        return connection;
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
                System.out.println("Conexión cerrada correctamente.");
            } catch (SQLException e) {
                System.err.println("Error al cerrar la conexión: " + e.getMessage());
            }
        }
    }
}