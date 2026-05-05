package org.example.ecochef.utils;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Configuracion")
public class Configuracion {

    private String host;
    private String port;
    private String database;
    private String user;
    private String password;

    @XmlElement public String getHost() { return host; }
    @XmlElement public String getPort() { return port; }
    @XmlElement public String getDatabase() { return database; }
    @XmlElement public String getUser() { return user; }
    @XmlElement public String getPassword() { return password; }

    public void setHost(String host) { this.host = host; }
    public void setPort(String port) { this.port = port; }
    public void setDatabase(String database) { this.database = database; }
    public void setUser(String user) { this.user = user; }
    public void setPassword(String password) { this.password = password; }
}