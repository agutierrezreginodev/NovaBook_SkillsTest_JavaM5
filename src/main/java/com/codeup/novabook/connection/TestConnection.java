/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.codeup.novabook.connection;

import com.codeup.novabook.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.SQLException;

/**
 *
 * @author Adrián Gutiérrez
 */
public class TestConnection {
    public static void main(String[] args) {
        System.out.println("Testing MySQL Database Connection...\n");
        
        try {
            // Get singleton connection factory
            ConnectionFactory factory = ConnectionFactory.getInstance();
            System.out.println("✓ ConnectionFactory singleton instance created");
            
            // Test connection
            try (Connection conn = factory.open()) {
                if (conn != null && !conn.isClosed()) {
                    System.out.println("✓ Database connection established successfully!");
                } else {
                    System.err.println("✗ Connection is null or closed");
                }
            }
            
            System.out.println("\n✓ Connection closed successfully");
            System.out.println("\nTest completed successfully! 🎉");
            
        } catch (DatabaseException e) {
            System.err.println("\n✗ Database connection failed!");
            System.err.println("Error: " + e.getMessage());
            if (e.getCause() != null) {
                System.err.println("Cause: " + e.getCause().getMessage());
            }
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("\n✗ SQL Error!");
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("\n✗ Unexpected error!");
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}