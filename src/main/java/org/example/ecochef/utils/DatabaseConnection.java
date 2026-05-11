package org.example.ecochef.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    public static Connection getConnection() {
        try {

            Configuracion config = XMLManager.readXMLFromResources(
                    Configuracion.class,
                    "/org/example/ecochef/db_config.xml"
            );

            if (config == null) {
                System.err.println("Error: No se pudo cargar la configuración del XML.");
                return null;
            }

            String url = "jdbc:mysql://" + config.getHost() + ":" + config.getPort() + "/" + config.getDatabase();

            // DEVOLVEMOS UNA CONEXIÓN NUEVA SIEMPRE
            return DriverManager.getConnection(url, config.getUser(), config.getPassword());

        } catch (SQLException e) {
            System.err.println("❌ Error al conectar con la base de datos: " + e.getMessage());
            return null;
        }
    }

    public static void closeConnection() {
        // Ya no gestionamos una variable única
    }
}