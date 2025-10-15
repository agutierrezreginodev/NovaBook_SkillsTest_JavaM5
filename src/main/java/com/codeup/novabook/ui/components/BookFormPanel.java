package com.codeup.novabook.ui.components;

import javax.swing.*;
import java.awt.*;

public class BookFormPanel extends JPanel {
    private final JTextField txtIsbn;
    private final JTextField txtTitle;
    private final JTextField txtAuthor;
    private final JTextField txtStock;
    private final JButton btnAdd;
    
    public BookFormPanel() {
        setLayout(new GridLayout(5, 2, 5, 5));
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Agregar Nuevo Libro"),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        
        // Initialize components
        txtIsbn = new JTextField();
        txtTitle = new JTextField();
        txtAuthor = new JTextField();
        txtStock = new JTextField();
        btnAdd = new JButton("Agregar Libro");
        
        // Add components
        add(new JLabel("ISBN:"));
        add(txtIsbn);
        add(new JLabel("Título:"));
        add(txtTitle);
        add(new JLabel("Autor:"));
        add(txtAuthor);
        add(new JLabel("Stock:"));
        add(txtStock);
        add(new JLabel("")); // Spacer
        add(btnAdd);
    }
    
    public String getIsbn() {
        return txtIsbn.getText().trim();
    }
    
    public String getTitle() {
        return txtTitle.getText().trim();
    }
    
    public String getAuthor() {
        return txtAuthor.getText().trim();
    }
    
    public String getStock() {
        return txtStock.getText().trim();
    }
    
    public void addActionListener(java.awt.event.ActionListener listener) {
        btnAdd.addActionListener(listener);
    }
    
    public void clearForm() {
        txtIsbn.setText("");
        txtTitle.setText("");
        txtAuthor.setText("");
        txtStock.setText("");
        txtIsbn.requestFocus();
    }
}