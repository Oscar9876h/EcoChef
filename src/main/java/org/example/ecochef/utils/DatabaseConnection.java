package org.example.ecochef.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static Connection connection = null;

    public static Connection getConnection() {
        if (connection == null) {
            try {
                Configuracion config = XMLManager.readXML(new Configuracion(), "src/main/resources/org/example/ecochef/db_config.xml");

                String url = "jdbc:mysql://" + config.getHost() + ":" + config.getPort() + "/" + config.getDatabase();

                connection = DriverManager.getConnection(url, config.getUser(), config.getPassword());
                System.out.println("Conexión exitosa a la base de datos");

            } catch (SQLException e) {
                System.err.println("Error al conectar con la base de datos: " + e.getMessage());
            }
        }
        return connection;
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
                System.out.println("Conexión cerrada");
            } catch (SQLException e) {
                System.err.println("Error al cerrar la conexión: " + e.getMessage());
            }
        }
    }
}