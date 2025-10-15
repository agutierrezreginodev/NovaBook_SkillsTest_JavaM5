package com.codeup.novabook.utils;

import com.codeup.novabook.exceptions.book.DuplicateISBNException;
import com.codeup.novabook.exceptions.book.InvalidStockException;
import com.codeup.novabook.exceptions.member.InactiveMemberException;
import com.codeup.novabook.exceptions.lending.BookNotAvailableException;
import com.codeup.novabook.exceptions.lending.InvalidReturnDateException;

import javax.swing.JOptionPane;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Utility class for handling and displaying errors in a consistent way.
 */
public class ErrorHandler {
    private static final Logger LOGGER = Logger.getLogger(ErrorHandler.class.getName());
    
    /**
     * Shows an error message to the user and logs it.
     * @param title The title for the error dialog
     * @param message The message to show to the user
     * @param exception The exception that occurred
     */
    public static void handleError(String title, String message, Exception exception) {
        // Log the error with full stack trace
        LOGGER.log(Level.SEVERE, message, exception);
        
        // Show user-friendly message
        JOptionPane.showMessageDialog(
            null,
            message,
            title,
            JOptionPane.ERROR_MESSAGE
        );
    }
    
    /**
     * Shows a warning message to the user and logs it.
     * @param title The title for the warning dialog
     * @param message The message to show to the user
     */
    public static void handleWarning(String title, String message) {
        // Log the warning
        LOGGER.log(Level.WARNING, message);
        
        // Show warning message
        JOptionPane.showMessageDialog(
            null,
            message,
            title,
            JOptionPane.WARNING_MESSAGE
        );
    }
    
    /**
     * Gets a user-friendly message from an exception.
     * Maps known exceptions to readable messages.
     */
    public static String getUserFriendlyMessage(Exception e) {
        if (e instanceof DuplicateISBNException) {
            return "Este ISBN ya existe en la base de datos.";
        } else if (e instanceof InvalidStockException) {
            return "Error en el stock: " + e.getMessage();
        } else if (e instanceof InactiveMemberException) {
            return "El socio está inactivo y no puede realizar operaciones.";
        } else if (e instanceof BookNotAvailableException) {
            return "El libro no está disponible para préstamo.";
        } else if (e instanceof InvalidReturnDateException) {
            return "La fecha de devolución no es válida.";
        } else {
            return "Ha ocurrido un error inesperado: " + e.getMessage();
        }
    }
}