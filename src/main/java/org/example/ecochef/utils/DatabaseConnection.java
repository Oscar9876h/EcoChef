package org.example.ecochef.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Clase de utilidad para gestionar la conexión con la base de datos.
 * Se encarga de leer los parámetros de configuración y establecer nuevas conexiones JDBC.
 */
public class DatabaseConnection {

    /**
     * Establece una nueva conexión con la base de datos utilizando los parámetros
     * leídos desde el archivo XML de configuración.
     * * @return Un objeto Connection si la conexión es exitosa, o null en caso de error.
     */
    public static Connection getConnection() {
        try {
            // Lee la configuración desde el recurso XML en el classpath
            Configuracion config = XMLManager.readXMLFromResources(
                    Configuracion.class,
                    "/org/example/ecochef/db_config.xml"
            );

            if (config == null) {
                System.err.println("Error: No se pudo cargar la configuración del XML.");
                return null;
            }

            // Construcción de la URL de conexión JDBC
            String url = "jdbc:mysql://" + config.getHost() + ":" + config.getPort() + "/" + config.getDatabase();

            // Retorna una nueva instancia de conexión activa
            return DriverManager.getConnection(url, config.getUser(), config.getPassword());

        } catch (SQLException e) {
            System.err.println("❌ Error al conectar con la base de datos: " + e.getMessage());
            return null;
        }
    }

    /**
     * Método para cerrar recursos de conexión.
     * Actualmente la gestión se delega al cierre automático en los bloques try-with-resources.
     */
    public static void closeConnection() {
        // Implementación vacía: el cierre se gestiona mediante try-with-resources en las clases DAO
    }
}