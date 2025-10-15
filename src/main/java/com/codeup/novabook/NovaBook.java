/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.codeup.novabook;

import com.codeup.novabook.connection.ConnectionFactory;
import com.codeup.novabook.ui.NovaBookUI;
import javax.swing.JOptionPane;

/**
 * Main class for NovaBook Library Management System.
 * This is the entry point of the application.
 * 
 * @author Adrián Gutiérrez
 */
public class NovaBook {

    /**
     * Main method - entry point of the application.
     * 
     * @param args command line arguments
     */
    public static void main(String[] args) {
        try {
            // Initialize logging
            com.codeup.novabook.utils.LoggerConfig.init();

            // Test database connection first
            System.out.println("Starting NovaBook Library Management System...");
            System.out.println("Testing database connection...");

            ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
            boolean connectionSuccessful = connectionFactory.testConnection();

            if (connectionSuccessful) {
                System.out.println("✓ Database connection successful!");

                // Show welcome message
                JOptionPane.showMessageDialog(null,
                        "Welcome to NovaBook Library Management System!\n\n" +
                                "Database connection established successfully.\n" +
                                "You can now access all library features.",
                        "NovaBook - Welcome",
                        JOptionPane.INFORMATION_MESSAGE);

                // Initialize and start the UI with login
                NovaBookUI ui = new NovaBookUI();
                ui.start();

            } else {
                System.err.println("✗ Database connection failed!");
                JOptionPane.showMessageDialog(null,
                        "Database connection failed!\n\n" +
                                "Please check your database configuration in app.props file.\n" +
                                "Make sure MySQL is running and the database 'novabook_db' exists.",
                        "Connection Error",
                        JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception e) {
            System.err.println("Application startup failed: " + e.getMessage());
            e.printStackTrace();

            JOptionPane.showMessageDialog(null,
                    "Application startup failed!\n\n" +
                            "Error: " + e.getMessage() + "\n\n" +
                            "Please check your configuration and try again.",
                    "Startup Error",
                    JOptionPane.ERROR_MESSAGE);
        }

        System.out.println("NovaBook application closed.");
    }
}
