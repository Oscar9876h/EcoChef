package org.example.ecochef.utils;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Clase modelo para gestionar la configuración de la conexión a la base de datos.
 * Utiliza anotaciones JAXB para permitir la carga/guardado desde un archivo XML.
 */
@XmlRootElement(name = "Configuracion")
public class Configuracion {

    private String host;
    private String port;
    private String database;
    private String user;
    private String password;

    // --- GETTERS Y SETTERS ---


    @XmlElement public String getHost() { return host; }
    public void setHost(String host) { this.host = host; }

    @XmlElement public String getPort() { return port; }
    public void setPort(String port) { this.port = port; }

    @XmlElement public String getDatabase() { return database; }
    public void setDatabase(String database) { this.database = database; }

    @XmlElement public String getUser() { return user; }
    public void setUser(String user) { this.user = user; }

    @XmlElement public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}