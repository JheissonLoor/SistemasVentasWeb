/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {

    private static final String DEFAULT_HOST = "127.0.0.1";
    private static final String DEFAULT_PORT = "3307";
    private static final String DEFAULT_DATABASE = "bd_ventas";
    private static final String DEFAULT_USER = "root";
    private static final String DEFAULT_PASSWORD = "";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new ExceptionInInitializerError("No se encontro el driver MySQL: " + e.getMessage());
        }
    }

    public Connection Conexion() {
        try {
            return getConnection();
        } catch (SQLException e) {
            throw new DatabaseException("No se pudo conectar a MySQL. Revise DB_HOST, DB_PORT, DB_NAME, DB_USER y DB_PASSWORD.", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        String url = envOrProperty("DB_URL", null);
        if (url == null || url.trim().isEmpty()) {
            String host = envOrProperty("DB_HOST", DEFAULT_HOST);
            String port = envOrProperty("DB_PORT", DEFAULT_PORT);
            String database = envOrProperty("DB_NAME", DEFAULT_DATABASE);
            url = "jdbc:mysql://" + host + ":" + port + "/" + database
                    + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&characterEncoding=UTF-8";
        }
        String user = envOrProperty("DB_USER", DEFAULT_USER);
        String password = envOrProperty("DB_PASSWORD", DEFAULT_PASSWORD);
        return DriverManager.getConnection(url, user, password);
    }

    private static String envOrProperty(String key, String defaultValue) {
        String value = System.getProperty(key);
        if (value == null || value.trim().isEmpty()) {
            value = System.getenv(key);
        }
        return (value == null || value.trim().isEmpty()) ? defaultValue : value;
    }
}
