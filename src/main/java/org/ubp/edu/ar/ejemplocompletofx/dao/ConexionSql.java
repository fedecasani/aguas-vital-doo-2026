/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ubp.edu.ar.ejemplocompletofx.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author agustin
 */
public final class ConexionSql {

//    private static final String URL = "jdbc:mysql://localhost:3306/ejemplo-completo-fx-2024?allowPublicKeyRetrieval=true&useSSL=false";
//    private static final String USER = "doo-2024";
//    private static final String PASSWORD = "doo-2024";
    private final String URL = "jdbc:sqlite:" + getClass().getResource("/ejemploCompletoFx.db").getPath();
    private Connection connection = null;

    public ConexionSql() {
        abrir();
    }

    private void abrir() {
        try {
// Esto es por si usan un servidor local como mysql, sql server            
//            this.connection = DriverManager.getConnection(URL, USER, PASSWORD);
            this.connection = DriverManager.getConnection(URL);
        } catch (SQLException e) {
            this.connection = null;
        } 
    }

    public void cerrar() {
        try {
            this.connection.close();
        } catch (SQLException ex) {
            this.connection = null;
        }
    }

    public String getURL() {
        return URL;
    }

    public Connection getConnection() {
        return connection;
    }

}
