package com.codeup.novabook.ui.components;

import com.codeup.novabook.domain.Book;
import com.codeup.novabook.utils.ErrorHandler;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class BookTablePanel extends JPanel {
    private final JTable tblBooks;
    private final DefaultTableModel tableModel;
    
    public BookTablePanel() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Libros Disponibles"),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        
        // Initialize table (include hidden ID column as first column)
        String[] columns = {"ID", "ISBN", "Título", "Autor", "Stock"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table read-only
            }
        };
        
        tblBooks = new JTable(tableModel);
        tblBooks.getTableHeader().setReorderingAllowed(false);

    // Hide ID column from view but keep it in the model so we can retrieve the book id
    tblBooks.getColumnModel().getColumn(0).setMinWidth(0);
    tblBooks.getColumnModel().getColumn(0).setMaxWidth(0);
    tblBooks.getColumnModel().getColumn(0).setWidth(0);
    tblBooks.getColumnModel().getColumn(0).setPreferredWidth(0);
        
        // Add table to scroll pane
        JScrollPane scrollPane = new JScrollPane(tblBooks);
        add(scrollPane, BorderLayout.CENTER);
        
        // Add search panel
        JPanel searchPanel = createSearchPanel();
        add(searchPanel, BorderLayout.NORTH);
    }
    
    private JPanel createSearchPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField txtSearch = new JTextField(20);
        JButton btnSearch = new JButton("Buscar");
        
        panel.add(new JLabel("Buscar:"));
        panel.add(txtSearch);
        panel.add(btnSearch);
        
        return panel;
    }
    
    public void updateBooks(List<Book> books) {
        try {
            tableModel.setRowCount(0);
            
            for (Book book : books) {
                Object[] row = {
                    book.getId(),
                    book.getIsbn(),
                    book.getTitle(),
                    book.getAuthor(),
                    book.getStock()
                };
                tableModel.addRow(row);
            }
        } catch (Exception e) {
            ErrorHandler.handleError(
                "Error al actualizar tabla",
                "No se pudo actualizar la lista de libros",
                e
            );
        }
    }
    
    public Book getSelectedBook() {
        int selectedRow = tblBooks.getSelectedRow();
        if (selectedRow < 0) {
            return null;
        }
        
        return new Book(
            Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString()), // ID
            tableModel.getValueAt(selectedRow, 1).toString(), // ISBN
            tableModel.getValueAt(selectedRow, 2).toString(), // Title
            tableModel.getValueAt(selectedRow, 3).toString(), // Author
            Integer.parseInt(tableModel.getValueAt(selectedRow, 4).toString()), // Stock
            null, // Created at not available in table
            null  // Updated at not available in table
        );
    }

    public int getSelectedBookId() {
        int selectedRow = tblBooks.getSelectedRow();
        if (selectedRow < 0) return -1;
        return Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());
    }
}