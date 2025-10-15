/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.codeup.novabook.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.codeup.novabook.exceptions.db.DatabaseException;
import com.codeup.novabook.infra.config.AppConfig;

/**
 * Singleton class for managing database connections.
 * Implements the Singleton pattern to ensure only one instance exists.
 * 
 * @author Adrián Gutiérrez
 */
public class ConnectionFactory {
    private static volatile ConnectionFactory instance;
    private final AppConfig config;
    
    /**
     * Private constructor to prevent instantiation from outside the class.
     * Loads the application configuration.
     */
    private ConnectionFactory() {
        this.config = new AppConfig();
    }
    
    /**
     * Returns the singleton instance of ConnectionFactory.
     * Uses double-checked locking for thread safety.
     * 
     * @return the singleton ConnectionFactory instance
     */
    public static ConnectionFactory getInstance() {
        if (instance == null) {
            synchronized (ConnectionFactory.class) {
                if (instance == null) {
                    instance = new ConnectionFactory();
                }
            }
        }
        return instance;
    }
    
    /**
     * Creates and returns a new database connection.
     * Supports both MySQL and PostgreSQL databases.
     * 
     * @return a new database connection
     * @throws DatabaseException if connection fails
     */
    public Connection open() throws DatabaseException {
        String vendor = config.get("db.vendor");
        String host = config.get("db.host");
        String port = config.get("db.port");
        String name = config.get("db.name");
        String user = config.get("db.user");
        String pass = config.get("db.password");

        String url;
        if ("postgres".equalsIgnoreCase(vendor)) {
            url = String.format("jdbc:postgresql://%s:%s/%s", host, port, name);
        } else {
            String useSSL = config.get("db.useSSL");
            url = String.format("jdbc:mysql://%s:%s/%s?useSSL=%s&serverTimezone=UTC", host, port, name, useSSL);
        }
        
        try {
            return DriverManager.getConnection(url, user, pass);
        } catch (SQLException e) {
            throw new DatabaseException("Failed to connect to database", e);
        }
    }
    
    /**
     * Tests the database connection.
     * 
     * @return true if connection is successful, false otherwise
     * @throws SQLException 
     */
    public boolean testConnection() throws SQLException {
        try (Connection conn = open()) {
            return conn != null && !conn.isClosed();
        } catch (DatabaseException e) {
            return false;
        }
    }
}