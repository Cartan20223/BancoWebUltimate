package com.sena.bancoweb.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
    private static final String URL = "jdbc:mysql://localhost:3306/banco_web";  //Conexion DB
    private static final String USER = "root";
    private static final String PASS = "brahian2015?";

    public static Connection getConexion() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver MySQL no encontrado", e);
        }
        return DriverManager.getConnection(URL, USER, PASS);
    }
}