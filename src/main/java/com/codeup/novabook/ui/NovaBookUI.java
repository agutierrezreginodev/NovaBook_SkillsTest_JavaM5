/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.codeup.novabook.ui;

import com.codeup.novabook.domain.Book;
import com.codeup.novabook.domain.Member;
import com.codeup.novabook.domain.Lending;
import com.codeup.novabook.domain.User;
import com.codeup.novabook.service.BookService;
import com.codeup.novabook.service.MemberService;
import com.codeup.novabook.service.LendingService;
import com.codeup.novabook.service.UserService;
import com.codeup.novabook.service.impl.BookServiceImpl;
import com.codeup.novabook.service.impl.MemberServiceImpl;
import com.codeup.novabook.service.impl.LendingServiceImpl;
import com.codeup.novabook.service.impl.UserServiceImpl;

import javax.swing.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Main UI class for NovaBook Library Management System.
 * Provides JOptionPane-based user interface for all library operations.
 * 
 * @author Adrián Gutiérrez
 */
public class NovaBookUI {
    
    private final BookService bookService;
    private final MemberService memberService;
    private final LendingService lendingService;
    private final UserService userService;
    private volatile User currentUser;
    private volatile Member currentMember;
    
    /**
     * Constructor that initializes all services.
     */
    public NovaBookUI() {
        this.bookService = new BookServiceImpl();
        this.memberService = new MemberServiceImpl();
        this.lendingService = new LendingServiceImpl();
        this.userService = new UserServiceImpl();
        clearSession();
    }
    
    /**
     * Main entry point for the UI - handles login and main menu navigation.
     */
    public void start() {
        try {
            // Check if any admin users exist, if not create one
            ensureAdminExists();
            
            // Show login screen until user exits
            while (true) {
                clearSession(); // Ensure clean session state
                if (!showLoginScreen()) {
                    break; // User chose to exit
                }
                
                // Show appropriate menu based on user type
                if (currentUser != null) {
                    if ("MEMBER".equals(currentUser.getRole()) && currentMember != null) {
                        showMemberMenu();
                    } else {
                        showMainMenu();
                    }
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                "An error occurred: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        } finally {
            clearSession();
        }
    }
    
    /**
     * Ensures at least one admin user exists in the system.
     * If no admin exists, creates a default admin user.
     */
    /**
     * Shows a list of available books to the public without requiring login.
     */
    private void showPublicBookList() {
        try {
            List<Book> books = bookService.getAllBooks();
            
            if (books.isEmpty()) {
                JOptionPane.showMessageDialog(null,
                    "No books are currently available in the library.",
                    "Book Catalog",
                    JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            
            StringBuilder bookList = new StringBuilder();
            bookList.append("Available Books:\n\n");
            
            for (Book book : books) {
                if (book.getStock() > 0) {
                    bookList.append("Title: ").append(book.getTitle()).append("\n");
                    bookList.append("Author: ").append(book.getAuthor()).append("\n");
                    bookList.append("ISBN: ").append(book.getIsbn()).append("\n");
                    bookList.append("Available Copies: ").append(book.getStock()).append("\n");
                    bookList.append("------------------------\n");
                }
            }
            
            JTextArea textArea = new JTextArea(bookList.toString());
            textArea.setEditable(false);
            textArea.setWrapStyleWord(true);
            textArea.setLineWrap(true);
            
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new java.awt.Dimension(400, 300));
            
            JOptionPane.showMessageDialog(null,
                scrollPane,
                "Book Catalog",
                JOptionPane.PLAIN_MESSAGE);
                
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                "Error retrieving book list: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void ensureAdminExists() {
        try {
            List<User> allUsers = userService.getAllUsers();
            boolean adminExists = allUsers.stream().anyMatch(user -> "ADMIN".equals(user.getRole()));
            
            if (!adminExists) {
                JOptionPane.showMessageDialog(null, 
                    "No admin user found in the system.\n" +
                    "Creating default admin user...",
                    "First Time Setup", 
                    JOptionPane.INFORMATION_MESSAGE);
                createDefaultAdminUser();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, 
                "Error checking for admin users: " + e.getMessage() + "\n" +
                "Please ensure the database is properly configured.",
                "Setup Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Displays the login screen and handles authentication.
     * 
     * @return true if login successful, false otherwise
     */
    private boolean showLoginScreen() {
        clearSession(); // Ensure clean session state at start
        
        while (true) {
            JPanel loginPanel = new JPanel();
            loginPanel.setLayout(new BoxLayout(loginPanel, BoxLayout.Y_AXIS));
            
            JLabel titleLabel = new JLabel("NovaBook Library Management System");
            titleLabel.setFont(titleLabel.getFont().deriveFont(16f));
            titleLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
            loginPanel.add(titleLabel);
            
            loginPanel.add(Box.createVerticalStrut(20));
            
            JTextField emailField = new JTextField(20);
            JPasswordField passwordField = new JPasswordField(20);
            
            JPanel emailPanel = new JPanel();
            emailPanel.add(new JLabel("Email:"));
            emailPanel.add(emailField);
            
            JPanel passwordPanel = new JPanel();
            passwordPanel.add(new JLabel("Password:"));
            passwordPanel.add(passwordField);
            
            loginPanel.add(emailPanel);
            loginPanel.add(passwordPanel);
            
            String[] options = {"Login", "Register as Member", "Browse Books", "Exit"};
            int choice = JOptionPane.showOptionDialog(
                null,
                loginPanel,
                "Login - NovaBook",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                options[0]
            );
            
            switch (choice) {
                case 0: // Login
                    String email = emailField.getText().trim();
                    String password = new String(passwordField.getPassword());
                    
                    if (email.isEmpty() || password.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Please enter both email and password.", "Login Error", JOptionPane.ERROR_MESSAGE);
                        continue;
                    }
                    
                    clearSession(); // Clear any existing session
                    
                    Optional<User> userOpt = userService.authenticateUser(email, password);
                    if (userOpt.isPresent()) {
                        User authenticatedUser = userOpt.get();
                        
                        // Check if it's a member
                        if ("MEMBER".equals(authenticatedUser.getRole())) {
                            // Find the corresponding member record
                            List<Member> members = memberService.getAllMembers();
                            Optional<Member> memberOpt = members.stream()
                                .filter(member -> member.getName().equals(authenticatedUser.getName()))
                                .findFirst();
                            
                            if (memberOpt.isPresent()) {
                                synchronized(this) {
                                    currentUser = authenticatedUser;
                                    currentMember = memberOpt.get();
                                    JOptionPane.showMessageDialog(null, 
                                        "Welcome, " + currentMember.getName() + "!\n" +
                                        "You can browse books and manage your lendings.",
                                        "Member Login Successful",
                                        JOptionPane.INFORMATION_MESSAGE);
                                    return true;
                                }
                            } else {
                                clearSession();
                                JOptionPane.showMessageDialog(null, 
                                    "Member record not found.",
                                    "Login Error",
                                    JOptionPane.ERROR_MESSAGE);
                            }
                        } else {
                            // Regular user/admin
                            synchronized(this) {
                                currentUser = authenticatedUser;
                                JOptionPane.showMessageDialog(null, 
                                    "Welcome, " + currentUser.getName() + "!\n" +
                                    "Role: " + currentUser.getRole() + "\n" +
                                    "Access Level: " + currentUser.getAccessLevel(),
                                    "Login Successful",
                                    JOptionPane.INFORMATION_MESSAGE);
                                return true;
                            }
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Invalid email or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
                    }
                    break;
                    
                case 1: // Register as Member
                    registerMemberFromLogin();
                    break;

                case 2: // Browse Books
                    showPublicBookList();
                    break;
                    
                case 3: // Exit
                default:
                    return false;
            }
        }
    }
    
    /**
     * Registers a new member from the login screen.
     */
    private void registerMemberFromLogin() {
        try {
            JPanel memberPanel = new JPanel();
            memberPanel.setLayout(new BoxLayout(memberPanel, BoxLayout.Y_AXIS));
            
            JTextField nameField = new JTextField(20);
            JTextField emailField = new JTextField(20);
            JPasswordField passwordField = new JPasswordField(20);
            JTextField phoneField = new JTextField(20);
            
            memberPanel.add(new JLabel("Member Registration"));
            memberPanel.add(Box.createVerticalStrut(10));
            
            JPanel namePanel = new JPanel();
            namePanel.add(new JLabel("Name:"));
            namePanel.add(nameField);
            
            JPanel emailPanel = new JPanel();
            emailPanel.add(new JLabel("Email:"));
            emailPanel.add(emailField);
            
            JPanel passwordPanel = new JPanel();
            passwordPanel.add(new JLabel("Password:"));
            passwordPanel.add(passwordField);
            
            JPanel phonePanel = new JPanel();
            phonePanel.add(new JLabel("Phone:"));
            phonePanel.add(phoneField);
            
            memberPanel.add(namePanel);
            memberPanel.add(emailPanel);
            memberPanel.add(passwordPanel);
            memberPanel.add(phonePanel);
            
            int result = JOptionPane.showConfirmDialog(null, memberPanel, "Register as Member", JOptionPane.OK_CANCEL_OPTION);
            
            if (result == JOptionPane.OK_OPTION) {
                String name = nameField.getText().trim();
                String email = emailField.getText().trim();
                String password = new String(passwordField.getPassword());
                String phone = phoneField.getText().trim();
                
                if (name.isEmpty() || email.isEmpty() || password.isEmpty() || phone.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "All fields are required.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Check if email already exists
                List<User> existingUsers = userService.getAllUsers();
                boolean emailExists = existingUsers.stream()
                    .anyMatch(u -> email.equalsIgnoreCase(u.getEmail()));
                
                if (emailExists) {
                    JOptionPane.showMessageDialog(null, "This email is already registered.", "Registration Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // Create user with MEMBER role
                Instant now = Instant.now();
                User user = new User(0, name, email, password, phone, "MEMBER", "READ_WRITE", true, false, now, now);
                User savedUser = userService.registerUser(user);
                
                // Create member record with same ID as user
                Member member = new Member(0, name, true, false, "REGULAR", "READ_WRITE", now, now);
                Member savedMember = memberService.registerMember(member);
                
                // Set current member
                currentMember = savedMember;
                currentUser = savedUser;
                
                JOptionPane.showMessageDialog(null, 
                    "Member registered successfully!\n" +
                    "Welcome, " + savedMember.getName() + "!\n" +
                    "You can now browse books and make lendings.",
                    "Registration Successful", 
                    JOptionPane.INFORMATION_MESSAGE);
                
                // Show member menu
                showMemberMenu();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error registering member: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Creates a default admin user for initial setup.
     */
    private void createDefaultAdminUser() {
        try {
            // Check if any admin users exist
            List<User> allUsers = userService.getAllUsers();
            boolean adminExists = allUsers.stream().anyMatch(user -> "ADMIN".equals(user.getRole()));
            
            if (adminExists) {
                JOptionPane.showMessageDialog(null, "Admin user already exists in the system.", "Admin User Exists", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            
            JPanel adminPanel = new JPanel();
            adminPanel.setLayout(new BoxLayout(adminPanel, BoxLayout.Y_AXIS));
            
            JTextField nameField = new JTextField(20);
            JTextField emailField = new JTextField(20);
            JPasswordField passwordField = new JPasswordField(20);
            JTextField phoneField = new JTextField(20);
            
            adminPanel.add(new JLabel("Admin User Registration"));
            adminPanel.add(Box.createVerticalStrut(10));
            
            JPanel namePanel = new JPanel();
            namePanel.add(new JLabel("Name:"));
            namePanel.add(nameField);
            
            JPanel emailPanel = new JPanel();
            emailPanel.add(new JLabel("Email:"));
            emailPanel.add(emailField);
            
            JPanel passwordPanel = new JPanel();
            passwordPanel.add(new JLabel("Password:"));
            passwordPanel.add(passwordField);
            
            JPanel phonePanel = new JPanel();
            phonePanel.add(new JLabel("Phone:"));
            phonePanel.add(phoneField);
            
            adminPanel.add(namePanel);
            adminPanel.add(emailPanel);
            adminPanel.add(passwordPanel);
            adminPanel.add(phonePanel);
            
            int result = JOptionPane.showConfirmDialog(null, adminPanel, "Create Admin User", JOptionPane.OK_CANCEL_OPTION);
            
            if (result == JOptionPane.OK_OPTION) {
                String name = nameField.getText().trim();
                String email = emailField.getText().trim();
                String password = new String(passwordField.getPassword());
                String phone = phoneField.getText().trim();
                
                if (name.isEmpty() || email.isEmpty() || password.isEmpty() || phone.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "All fields are required.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                User adminUser = new User(1, name, email, password, phone, "ADMIN", "MANAGE", true, false, Instant.now(), Instant.now());
                userService.registerUser(adminUser);
                
                JOptionPane.showMessageDialog(null, "Admin user created successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error creating admin user: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Displays the member menu with limited functionality.
     */
    private void showMemberMenu() {
        while (true) {
            String[] options = {
                "Browse Books",
                "Search Books",
                "Borrow Book",
                "Return Book",
                "My Current lendings",
                "My Overdue lendings",
                "My Due Soon lendings",
                "Logout"
            };
            
            int choice = JOptionPane.showOptionDialog(
                null,
                "Member Menu - Welcome " + currentMember.getName() + "\n\nSelect an option:",
                "Member Dashboard",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[0]
            );
            
            switch (choice) {
                case 0:
                    browseBooks();
                    break;
                case 1:
                    searchBooksForMember();
                    break;
                case 2:
                    borrowBook();
                    break;
                case 3:
                    returnBookForMember();
                    break;
                case 4:
                    viewMyCurrentLendings();
                    break;
                case 5:
                    viewMyOverdueLendings();
                    break;
                case 6:
                    viewMyDueSoonLendings();
                    break;
                case 7:
                default:
                    // Clear user state before returning
                    String memberName;
                    synchronized(this) {
                        memberName = currentMember != null ? currentMember.getName() : "Guest";
                        clearSession();
                    }
                    JOptionPane.showMessageDialog(null, 
                        "Goodbye, " + memberName + "!\nThank you for using NovaBook!", 
                        "Logout Successful", 
                        JOptionPane.INFORMATION_MESSAGE);
                    return;
            }
        }
    }
    
    /**
     * Displays the main menu and handles user navigation.
     */
    private void showMainMenu() {
        while (true) {
            String[] options = getAvailableMenuOptions();
            
            int choice = JOptionPane.showOptionDialog(
                null,
                "Welcome " + currentUser.getName() + "!\nRole: " + currentUser.getRole() + "\nAccess Level: " + currentUser.getAccessLevel() + "\n\nSelect an option:",
                "NovaBook - Main Menu",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[0]
            );
            
            switch (choice) {
                case 0:
                    showBookManagementMenu();
                    break;
                case 1:
                    showMemberManagementMenu();
                    break;
                case 2:
                    showLendingManagementMenu();
                    break;
                case 3:
                    if (hasAdminAccess()) {
                        showUserManagementMenu();
                    }
                    break;
                case 4:
                    if (hasAdminAccess()) {
                        showReportsMenu();
                    }
                    break;
                case 5:
                    if (hasAdminAccess()) {
                        showChangePasswordDialog();
                    }
                    break;
                case 6:
                default:
                    JOptionPane.showMessageDialog(null, "Thank you for using NovaBook!", "Goodbye", JOptionPane.INFORMATION_MESSAGE);
                    return;
            }
        }
    }
    
    /**
     * Gets available menu options based on user role and access level.
     * 
     * @return array of available menu options
     */
    private String[] getAvailableMenuOptions() {
        java.util.List<String> options = new java.util.ArrayList<>();
        
        // Basic options available to all users
        options.add("Book Management");
        options.add("Member Management");
        options.add("Lending Management");
        
        // Admin-only options
        if (hasAdminAccess()) {
            options.add("User Management");
            options.add("Reports");
            options.add("Change Password");
        }
        
        options.add("Logout");
        
        return options.toArray(new String[0]);
    }
    
    /**
     * Checks if the current user has admin access.
     * 
     * @return true if user has admin role or manage access level
     */
    private boolean hasAdminAccess() {
        return currentUser != null && 
               ("ADMIN".equals(currentUser.getRole()) || "MANAGE".equals(currentUser.getAccessLevel()));
    }
    
    /**
     * Checks if the current user has write access.
     * 
     * @return true if user has read_write or manage access level
     */
    private boolean hasWriteAccess() {
        return currentUser != null && 
               ("READ_WRITE".equals(currentUser.getAccessLevel()) || "MANAGE".equals(currentUser.getAccessLevel()));
    }
    
    /**
     * Clears the current user session.
     */
    private synchronized void clearSession() {
        this.currentUser = null;
        this.currentMember = null;
    }
    
    /**
     * Shows change password dialog for current user.
     */
    private void showChangePasswordDialog() {
        JPanel passwordPanel = new JPanel();
        passwordPanel.setLayout(new BoxLayout(passwordPanel, BoxLayout.Y_AXIS));
        
        JPasswordField oldPasswordField = new JPasswordField(20);
        JPasswordField newPasswordField = new JPasswordField(20);
        JPasswordField confirmPasswordField = new JPasswordField(20);
        
        JPanel oldPasswordPanel = new JPanel();
        oldPasswordPanel.add(new JLabel("Current Password:"));
        oldPasswordPanel.add(oldPasswordField);
        
        JPanel newPasswordPanel = new JPanel();
        newPasswordPanel.add(new JLabel("New Password:"));
        newPasswordPanel.add(newPasswordField);
        
        JPanel confirmPasswordPanel = new JPanel();
        confirmPasswordPanel.add(new JLabel("Confirm Password:"));
        confirmPasswordPanel.add(confirmPasswordField);
        
        passwordPanel.add(oldPasswordPanel);
        passwordPanel.add(newPasswordPanel);
        passwordPanel.add(confirmPasswordPanel);
        
        int result = JOptionPane.showConfirmDialog(null, passwordPanel, "Change Password", JOptionPane.OK_CANCEL_OPTION);
        
        if (result == JOptionPane.OK_OPTION) {
            String oldPassword = new String(oldPasswordField.getPassword());
            String newPassword = new String(newPasswordField.getPassword());
            String confirmPassword = new String(confirmPasswordField.getPassword());
            
            if (oldPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
                JOptionPane.showMessageDialog(null, "All fields are required.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (!newPassword.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(null, "New passwords do not match.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            boolean success = userService.changePassword(currentUser.getId(), oldPassword, newPassword);
            if (success) {
                JOptionPane.showMessageDialog(null, "Password changed successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Failed to change password. Please check your current password.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    /**
     * Displays the book management menu.
     */
    private void showBookManagementMenu() {
        while (true) {
            java.util.List<String> optionsList = new java.util.ArrayList<>();
            
            // Read-only operations available to all users
            optionsList.add("Search Books");
            optionsList.add("View All Books");
            
            // Write operations only for users with write access
            if (hasWriteAccess()) {
                optionsList.add("Add Book");
                optionsList.add("Update Book");
                optionsList.add("Remove Book");
            }
            
            optionsList.add("Back to Main Menu");
            
            String[] options = optionsList.toArray(new String[0]);
            
            int choice = JOptionPane.showOptionDialog(
                null,
                "Book Management Menu\n\nSelect an option:",
                "Book Management",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[0]
            );
            
            switch (choice) {
                case 0:
                    searchBooks();
                    break;
                case 1:
                    viewAllBooks();
                    break;
                case 2:
                    if (hasWriteAccess()) {
                        addBook();
                    }
                    break;
                case 3:
                    if (hasWriteAccess()) {
                        updateBook();
                    }
                    break;
                case 4:
                    if (hasWriteAccess()) {
                        removeBook();
                    }
                    break;
                case 5:
                default:
                    return;
            }
        }
    }
    
    /**
     * Displays the member management menu.
     */
    private void showMemberManagementMenu() {
        while (true) {
            java.util.List<String> optionsList = new java.util.ArrayList<>();
            
            // Read-only operations available to all users
            optionsList.add("Search Members");
            optionsList.add("View All Members");
            
            // Write operations only for users with write access
            if (hasWriteAccess()) {
                optionsList.add("Register Member");
                optionsList.add("Update Member");
                optionsList.add("Deactivate Member");
            }
            
            optionsList.add("Back to Main Menu");
            
            String[] options = optionsList.toArray(new String[0]);
            
            int choice = JOptionPane.showOptionDialog(
                null,
                "Member Management Menu\n\nSelect an option:",
                "Member Management",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[0]
            );
            
            switch (choice) {
                case 0:
                    searchMembers();
                    break;
                case 1:
                    viewAllMembers();
                    break;
                case 2:
                    if (hasWriteAccess()) {
                        registerMember();
                    }
                    break;
                case 3:
                    if (hasWriteAccess()) {
                        updateMember();
                    }
                    break;
                case 4:
                    if (hasWriteAccess()) {
                        deactivateMember();
                    }
                    break;
                case 5:
                default:
                    return;
            }
        }
    }
    
    /**
     * Displays the lending management menu.
     */
    private void showLendingManagementMenu() {
        while (true) {
            java.util.List<String> optionsList = new java.util.ArrayList<>();
            
            // Read-only operations available to all users
            optionsList.add("View Active Lendings");
            optionsList.add("View Overdue Lendings");
            
            // Write operations only for users with write access
            if (hasWriteAccess()) {
                optionsList.add("Lend Book");
                optionsList.add("Return Book");
                optionsList.add("Extend Lending");
            }
            
            optionsList.add("Back to Main Menu");
            
            String[] options = optionsList.toArray(new String[0]);
            
            int choice = JOptionPane.showOptionDialog(
                null,
                "Lending Management Menu\n\nSelect an option:",
                "Lending Management",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[0]
            );
            
            switch (choice) {
                case 0:
                    viewActiveLendings();
                    break;
                case 1:
                    viewOverdueLendings();
                    break;
                case 2:
                    if (hasWriteAccess()) {
                        lendBook();
                    }
                    break;
                case 3:
                    if (hasWriteAccess()) {
                        returnBook();
                    }
                    break;
                case 4:
                    if (hasWriteAccess()) {
                        extendLending();
                    }
                    break;
                case 5:
                default:
                    return;
            }
        }
    }
    
    /**
     * Displays the user management menu.
     */
    private void showUserManagementMenu() {
        while (true) {
            List<String> optionList = new ArrayList<>();
            optionList.add("Register User");
            optionList.add("Authenticate User");
            optionList.add("Update User");
            optionList.add("Deactivate User");
            optionList.add("View All Users");
            
            // Only show role change option for admins
            if (hasAdminAccess()) {
                optionList.add("Change User Role");
            }
            
            optionList.add("Back to Main Menu");
            
            String[] options = optionList.toArray(new String[0]);
            
            int choice = JOptionPane.showOptionDialog(
                null,
                "User Management\n\nSelect an option:",
                "User Management",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[0]
            );
            
            switch (choice) {
                case 0:
                    registerUser();
                    break;
                case 1:
                    authenticateUser();
                    break;
                case 2:
                    updateUser();
                    break;
                case 3:
                    deactivateUser();
                    break;
                case 4:
                    viewAllUsers();
                    break;
                case 5:
                    if (hasAdminAccess()) {
                        changeUserRole();
                    } else {
                        return;
                    }
                    break;
                case 6:
                default:
                    return;
            }
        }
    }
    
    /**
     * Displays the reports menu.
     */
    private void showReportsMenu() {
        String[] options = {
            "Library Statistics",
            "Member Statistics",
            "Lending Statistics",
            "Back to Main Menu"
        };
        
        int choice = JOptionPane.showOptionDialog(
            null,
            "Reports\n\nSelect an option:",
            "Reports",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.INFORMATION_MESSAGE,
            null,
            options,
            options[0]
        );
        
        switch (choice) {
            case 0:
                showLibraryStatistics();
                break;
            case 1:
                showMemberStatistics();
                break;
            case 2:
                showLendingStatistics();
                break;
            case 3:
            default:
                return;
        }
    }
    
    /**
     * Adds a new book to the library.
     */
    private void addBook() {
        try {
            String isbn = JOptionPane.showInputDialog("Enter ISBN:");
            if (isbn == null || isbn.trim().isEmpty()) return;
            
            String title = JOptionPane.showInputDialog("Enter Title:");
            if (title == null || title.trim().isEmpty()) return;
            
            String author = JOptionPane.showInputDialog("Enter Author:");
            if (author == null || author.trim().isEmpty()) return;
            
            String stockStr = JOptionPane.showInputDialog("Enter Stock:");
            if (stockStr == null || stockStr.trim().isEmpty()) return;
            
            int stock = Integer.parseInt(stockStr);
            
            Instant now = Instant.now();
            Book book = new Book(1, isbn.trim(), title.trim(), author.trim(), stock, now, now);
            
            Book savedBook = bookService.addBook(book);
            JOptionPane.showMessageDialog(null, 
                "Book added successfully!\nID: " + savedBook.getId() + 
                "\nTitle: " + savedBook.getTitle(),
                "Success", JOptionPane.INFORMATION_MESSAGE);
                
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error adding book: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Searches for books.
     */
    private void searchBooks() {
        String[] options = {"By Title", "By Author", "Available Books"};
        
        int choice = JOptionPane.showOptionDialog(
            null,
            "Search Books By:",
            "Book Search",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            options,
            options[0]
        );
        
        try {
            List<Book> books;
            switch (choice) {
                case 0:
                    String title = JOptionPane.showInputDialog("Enter title to search:");
                    if (title == null || title.trim().isEmpty()) return;
                    books = bookService.searchBooksByTitle(title.trim());
                    break;
                case 1:
                    String author = JOptionPane.showInputDialog("Enter author to search:");
                    if (author == null || author.trim().isEmpty()) return;
                    books = bookService.searchBooksByAuthor(author.trim());
                    break;
                case 2:
                    books = bookService.getAvailableBooks(1);
                    break;
                default:
                    return;
            }
            
            displayBooks(books);
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error searching books: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Updates a book.
     */
    private void updateBook() {
        try {
            String idStr = JOptionPane.showInputDialog("Enter Book ID to update:");
            if (idStr == null || idStr.trim().isEmpty()) return;
            
            int id = Integer.parseInt(idStr);
            Optional<Book> bookOpt = bookService.findBookById(id);
            
            if (!bookOpt.isPresent()) {
                JOptionPane.showMessageDialog(null, "Book not found!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            Book book = bookOpt.get();
            
            String isbn = JOptionPane.showInputDialog("Enter ISBN:", book.getIsbn());
            if (isbn == null || isbn.trim().isEmpty()) return;
            
            String title = JOptionPane.showInputDialog("Enter Title:", book.getTitle());
            if (title == null || title.trim().isEmpty()) return;
            
            String author = JOptionPane.showInputDialog("Enter Author:", book.getAuthor());
            if (author == null || author.trim().isEmpty()) return;
            
            String stockStr = JOptionPane.showInputDialog("Enter Stock:", String.valueOf(book.getStock()));
            if (stockStr == null || stockStr.trim().isEmpty()) return;
            
            int stock = Integer.parseInt(stockStr);
            
            book.setIsbn(isbn.trim());
            book.setTitle(title.trim());
            book.setAuthor(author.trim());
            book.setStock(stock);
            
            Book updatedBook = bookService.updateBook(book);
            JOptionPane.showMessageDialog(null, 
                "Book updated successfully!\nID: " + updatedBook.getId() + 
                "\nTitle: " + updatedBook.getTitle(),
                "Success", JOptionPane.INFORMATION_MESSAGE);
                
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error updating book: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Removes a book.
     */
    private void removeBook() {
        try {
            String idStr = JOptionPane.showInputDialog("Enter Book ID to remove:");
            if (idStr == null || idStr.trim().isEmpty()) return;
            
            int id = Integer.parseInt(idStr);
            
            int confirm = JOptionPane.showConfirmDialog(null, 
                "Are you sure you want to remove this book?", 
                "Confirm Removal", JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                boolean success = bookService.removeBook(id);
                if (success) {
                    JOptionPane.showMessageDialog(null, "Book removed successfully!", 
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Failed to remove book!", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error removing book: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Views all books.
     */
    private void viewAllBooks() {
        try {
            List<Book> books = bookService.getAllBooks();
            displayBooks(books);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error retrieving books: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Displays a list of books.
     */
    private void displayBooks(List<Book> books) {
        if (books.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No books found.", "Search Results", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append("Found ").append(books.size()).append(" book(s):\n\n");
        
        for (Book book : books) {
            sb.append("ID: ").append(book.getId()).append("\n");
            sb.append("ISBN: ").append(book.getIsbn()).append("\n");
            sb.append("Title: ").append(book.getTitle()).append("\n");
            sb.append("Author: ").append(book.getAuthor()).append("\n");
            sb.append("Stock: ").append(book.getStock()).append("\n");
            sb.append("---\n");
        }
        
        JOptionPane.showMessageDialog(null, sb.toString(), "Books", JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Registers a new member.
     */
    private void registerMember() {
        try {
            String name = JOptionPane.showInputDialog("Enter Member Name:");
            if (name == null || name.trim().isEmpty()) return;
            
            String[] roleOptions = {"REGULAR", "PREMIUM"};
            String role = (String) JOptionPane.showInputDialog(null,
                "Select Member Role:",
                "Member Role",
                JOptionPane.QUESTION_MESSAGE,
                null,
                roleOptions,
                roleOptions[0]);
            
            if (role == null) return;
            
            String[] accessLevelOptions = {"READ_ONLY", "READ_WRITE", "MANAGE"};
            String accessLevel = (String) JOptionPane.showInputDialog(null,
                "Select Access Level:",
                "Access Level",
                JOptionPane.QUESTION_MESSAGE,
                null,
                accessLevelOptions,
                accessLevelOptions[1]);
            
            if (accessLevel == null) return;
            
            Instant now = Instant.now();
            Member member = new Member(1, name.trim(), true, false, role, accessLevel, now, now);
            
            Member savedMember = memberService.registerMember(member);
            JOptionPane.showMessageDialog(null, 
                "Member registered successfully!\nID: " + savedMember.getId() + 
                "\nName: " + savedMember.getName(),
                "Success", JOptionPane.INFORMATION_MESSAGE);
                
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error registering member: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Searches for members.
     */
    private void searchMembers() {
        String name = JOptionPane.showInputDialog("Enter member name to search:");
        if (name == null || name.trim().isEmpty()) return;
        
        try {
            List<Member> members = memberService.searchMembersByName(name.trim());
            displayMembers(members);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error searching members: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Updates a member.
     */
    private void updateMember() {
        try {
            String idStr = JOptionPane.showInputDialog("Enter Member ID to update:");
            if (idStr == null || idStr.trim().isEmpty()) return;
            
            int id = Integer.parseInt(idStr);
            Optional<Member> memberOpt = memberService.findMemberById(id);
            
            if (!memberOpt.isPresent()) {
                JOptionPane.showMessageDialog(null, "Member not found!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            Member member = memberOpt.get();
            
            String name = JOptionPane.showInputDialog("Enter Member Name:", member.getName());
            if (name == null || name.trim().isEmpty()) return;
            
            String[] roleOptions = {"REGULAR", "PREMIUM"};
            String role = (String) JOptionPane.showInputDialog(null,
                "Select Member Role:",
                "Member Role",
                JOptionPane.QUESTION_MESSAGE,
                null,
                roleOptions,
                member.getRole());
            
            if (role == null) return;
            
            member.setName(name.trim());
            member.setRole(role);
            
            Member updatedMember = memberService.updateMember(member);
            JOptionPane.showMessageDialog(null, 
                "Member updated successfully!\nID: " + updatedMember.getId() + 
                "\nName: " + updatedMember.getName(),
                "Success", JOptionPane.INFORMATION_MESSAGE);
                
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error updating member: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Deactivates a member.
     */
    private void deactivateMember() {
        try {
            String idStr = JOptionPane.showInputDialog("Enter Member ID to deactivate:");
            if (idStr == null || idStr.trim().isEmpty()) return;
            
            int id = Integer.parseInt(idStr);
            
            int confirm = JOptionPane.showConfirmDialog(null, 
                "Are you sure you want to deactivate this member?", 
                "Confirm Deactivation", JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                boolean success = memberService.deactivateMember(id);
                if (success) {
                    JOptionPane.showMessageDialog(null, "Member deactivated successfully!", 
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Failed to deactivate member!", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error deactivating member: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Views all members.
     */
    private void viewAllMembers() {
        try {
            List<Member> members = memberService.getAllMembers();
            displayMembers(members);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error retrieving members: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Displays a list of members.
     */
    private void displayMembers(List<Member> members) {
        if (members.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No members found.", "Search Results", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append("Found ").append(members.size()).append(" member(s):\n\n");
        
        for (Member member : members) {
            sb.append("ID: ").append(member.getId()).append("\n");
            sb.append("Name: ").append(member.getName()).append("\n");
            sb.append("Role: ").append(member.getRole()).append("\n");
            sb.append("Access Level: ").append(member.getAccessLevel()).append("\n");
            sb.append("Active: ").append(member.isActive() ? "Yes" : "No").append("\n");
            sb.append("---\n");
        }
        
        JOptionPane.showMessageDialog(null, sb.toString(), "Members", JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Lends a book to a member.
     */
    private void lendBook() {
        try {
            String memberIdStr = JOptionPane.showInputDialog("Enter Member ID:");
            if (memberIdStr == null || memberIdStr.trim().isEmpty()) return;
            
            String bookIdStr = JOptionPane.showInputDialog("Enter Book ID:");
            if (bookIdStr == null || bookIdStr.trim().isEmpty()) return;
            
            String daysStr = JOptionPane.showInputDialog("Enter lending days (default 14):", "14");
            if (daysStr == null || daysStr.trim().isEmpty()) daysStr = "14";
            
            int memberId = Integer.parseInt(memberIdStr);
            int bookId = Integer.parseInt(bookIdStr);
            int days = Integer.parseInt(daysStr);
            
            Lending lending = lendingService.lendBook(memberId, bookId, days);
            JOptionPane.showMessageDialog(null, 
                "Book lent successfully!\nLending ID: " + lending.getId() + 
                "\nDue Date: " + lending.getDueDate(),
                "Success", JOptionPane.INFORMATION_MESSAGE);
                
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error lending book: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Returns a book.
     */
    private void returnBook() {
        try {
            String lendingIdStr = JOptionPane.showInputDialog("Enter Lending ID:");
            if (lendingIdStr == null || lendingIdStr.trim().isEmpty()) return;
            
            int lendingId = Integer.parseInt(lendingIdStr);
            
            boolean success = lendingService.returnBook(lendingId);
            if (success) {
                JOptionPane.showMessageDialog(null, "Book returned successfully!", 
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Failed to return book!", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error returning book: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Views active lendings.
     */
    private void viewActiveLendings() {
        try {
            List<Lending> lendings = lendingService.getActiveLendings();
            displayLendings(lendings, "Active Lendings");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error retrieving active lendings: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Views overdue lendings.
     */
    private void viewOverdueLendings() {
        try {
            List<Lending> lendings = lendingService.getOverdueLendings();
            displayLendings(lendings, "Overdue Lendings");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error retrieving overdue lendings: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Extends a lending period.
     */
    private void extendLending() {
        try {
            String lendingIdStr = JOptionPane.showInputDialog("Enter Lending ID:");
            if (lendingIdStr == null || lendingIdStr.trim().isEmpty()) return;
            
            String daysStr = JOptionPane.showInputDialog("Enter additional days:");
            if (daysStr == null || daysStr.trim().isEmpty()) return;
            
            int lendingId = Integer.parseInt(lendingIdStr);
            int days = Integer.parseInt(daysStr);
            
            boolean success = lendingService.extendLending(lendingId, days);
            if (success) {
                JOptionPane.showMessageDialog(null, "Lending extended successfully!", 
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Failed to extend lending!", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error extending lending: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Displays a list of lendings.
     */
    private void displayLendings(List<Lending> lendings, String title) {
        if (lendings.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No lendings found.", title, JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append("Found ").append(lendings.size()).append(" lending(s):\n\n");
        
        for (Lending lending : lendings) {
            sb.append("ID: ").append(lending.getId()).append("\n");
            sb.append("Member ID: ").append(lending.getMemberId()).append("\n");
            sb.append("Book ID: ").append(lending.getBookId()).append("\n");
            sb.append("Lending Date: ").append(lending.getLendingDate()).append("\n");
            sb.append("Due Date: ").append(lending.getDueDate()).append("\n");
            sb.append("Returned: ").append(lending.isReturned() ? "Yes" : "No").append("\n");
            sb.append("---\n");
        }
        
        JOptionPane.showMessageDialog(null, sb.toString(), title, JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Registers a new user.
     */
    private void registerUser() {
        try {
            String name = JOptionPane.showInputDialog("Enter User Name:");
            if (name == null || name.trim().isEmpty()) return;
            
            String email = JOptionPane.showInputDialog("Enter Email:");
            if (email == null || email.trim().isEmpty()) return;
            
            String password = JOptionPane.showInputDialog("Enter Password:");
            if (password == null || password.trim().isEmpty()) return;
            
            String phone = JOptionPane.showInputDialog("Enter Phone:");
            if (phone == null || phone.trim().isEmpty()) return;
            
            // Only allow creating regular users, not admins
            String role = "USER";
            
            String[] accessLevelOptions = {"READ_ONLY", "READ_WRITE", "MANAGE"};
            String accessLevel = (String) JOptionPane.showInputDialog(null,
                "Select Access Level:",
                "Access Level",
                JOptionPane.QUESTION_MESSAGE,
                null,
                accessLevelOptions,
                accessLevelOptions[1]);
            
            if (accessLevel == null) return;
            
            Instant now = Instant.now();
            User user = new User(1, name.trim(), email.trim(), password.trim(), phone.trim(), 
                               role, accessLevel, true, false, now, now);
            
            User savedUser = userService.registerUser(user);
            JOptionPane.showMessageDialog(null, 
                "User registered successfully!\nID: " + savedUser.getId() + 
                "\nName: " + savedUser.getName(),
                "Success", JOptionPane.INFORMATION_MESSAGE);
                
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error registering user: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Authenticates a user.
     */
    private void authenticateUser() {
        try {
            String email = JOptionPane.showInputDialog("Enter Email:");
            if (email == null || email.trim().isEmpty()) return;
            
            String password = JOptionPane.showInputDialog("Enter Password:");
            if (password == null || password.trim().isEmpty()) return;
            
            Optional<User> userOpt = userService.authenticateUser(email.trim(), password.trim());
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                JOptionPane.showMessageDialog(null, 
                    "Authentication successful!\nWelcome, " + user.getName() + 
                    "\nRole: " + user.getRole(),
                    "Login Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Authentication failed!", 
                    "Login Error", JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error authenticating user: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Updates a user.
     */
    private void updateUser() {
        try {
            String idStr = JOptionPane.showInputDialog("Enter User ID to update:");
            if (idStr == null || idStr.trim().isEmpty()) return;
            
            int id = Integer.parseInt(idStr);
            Optional<User> userOpt = userService.findUserById(id);
            
            if (!userOpt.isPresent()) {
                JOptionPane.showMessageDialog(null, "User not found!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            User user = userOpt.get();
            
            String name = JOptionPane.showInputDialog("Enter User Name:", user.getName());
            if (name == null || name.trim().isEmpty()) return;
            
            String email = JOptionPane.showInputDialog("Enter Email:", user.getEmail());
            if (email == null || email.trim().isEmpty()) return;
            
            String phone = JOptionPane.showInputDialog("Enter Phone:", user.getPhone());
            if (phone == null || phone.trim().isEmpty()) return;
            
            user.setName(name.trim());
            user.setEmail(email.trim());
            user.setPhone(phone.trim());
            
            User updatedUser = userService.updateUser(user);
            JOptionPane.showMessageDialog(null, 
                "User updated successfully!\nID: " + updatedUser.getId() + 
                "\nName: " + updatedUser.getName(),
                "Success", JOptionPane.INFORMATION_MESSAGE);
                
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error updating user: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Deactivates a user.
     */
    private void deactivateUser() {
        try {
            String idStr = JOptionPane.showInputDialog("Enter User ID to deactivate:");
            if (idStr == null || idStr.trim().isEmpty()) return;
            
            int id = Integer.parseInt(idStr);
            
            int confirm = JOptionPane.showConfirmDialog(null, 
                "Are you sure you want to deactivate this user?", 
                "Confirm Deactivation", JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                boolean success = userService.deactivateUser(id);
                if (success) {
                    JOptionPane.showMessageDialog(null, "User deactivated successfully!", 
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Failed to deactivate user!", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error deactivating user: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Views all users.
     */
    private void viewAllUsers() {
        try {
            List<User> users = userService.getAllUsers();
            displayUsers(users);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error retrieving users: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Displays a list of users.
     */
    private void displayUsers(List<User> users) {
        if (users.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No users found.", "Users", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append("Found ").append(users.size()).append(" user(s):\n\n");
        
        for (User user : users) {
            sb.append("ID: ").append(user.getId()).append("\n");
            sb.append("Name: ").append(user.getName()).append("\n");
            sb.append("Email: ").append(user.getEmail()).append("\n");
            sb.append("Phone: ").append(user.getPhone()).append("\n");
            sb.append("Role: ").append(user.getRole()).append("\n");
            sb.append("Access Level: ").append(user.getAccessLevel()).append("\n");
            sb.append("Active: ").append(user.isActive() ? "Yes" : "No").append("\n");
            sb.append("---\n");
        }
        
        JOptionPane.showMessageDialog(null, sb.toString(), "Users", JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Changes user role (admin only).
     */
    private void changeUserRole() {
        try {
            // First, show all users to help admin select
            List<User> allUsers = userService.getAllUsers();
            if (allUsers.isEmpty()) {
                JOptionPane.showMessageDialog(null, "No users found.", "Change User Role", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            
            // Create user selection dialog
            String[] userOptions = new String[allUsers.size()];
            for (int i = 0; i < allUsers.size(); i++) {
                User user = allUsers.get(i);
                userOptions[i] = "ID: " + user.getId() + " - " + user.getName() + " (" + user.getEmail() + ") - Role: " + user.getRole();
            }
            
            String selectedUser = (String) JOptionPane.showInputDialog(null,
                "Select user to change role:",
                "Change User Role",
                JOptionPane.QUESTION_MESSAGE,
                null,
                userOptions,
                userOptions[0]);
            
            if (selectedUser == null) return;
            
            // Extract user ID from selection
            int userId = Integer.parseInt(selectedUser.substring(4, selectedUser.indexOf(" -")));
            
            // Get current user to show current role
            Optional<User> userOpt = userService.findUserById(userId);
            if (!userOpt.isPresent()) {
                JOptionPane.showMessageDialog(null, "User not found.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            User user = userOpt.get();
            String currentRole = user.getRole();
            
            // Select new role
            String[] roleOptions = {"USER", "ADMIN", "MEMBER"};
            String newRole = (String) JOptionPane.showInputDialog(null,
                "Current role: " + currentRole + "\nSelect new role:",
                "Change User Role",
                JOptionPane.QUESTION_MESSAGE,
                null,
                roleOptions,
                currentRole.equals("USER") ? roleOptions[0] : currentRole.equals("ADMIN") ? roleOptions[1] : roleOptions[2]);
            
            if (newRole == null) return;
            
            if (currentRole.equals(newRole)) {
                JOptionPane.showMessageDialog(null, "User already has this role.", "No Change", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            
            // Confirm the change
            int confirm = JOptionPane.showConfirmDialog(null,
                "Are you sure you want to change " + user.getName() + "'s role from " + currentRole + " to " + newRole + "?",
                "Confirm Role Change",
                JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                boolean success = userService.changeUserRole(userId, newRole);
                if (success) {
                    JOptionPane.showMessageDialog(null, 
                        "User role changed successfully!\n" +
                        "User: " + user.getName() + "\n" +
                        "New role: " + newRole,
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Failed to change user role.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error changing user role: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Shows library statistics.
     */
    private void showLibraryStatistics() {
        try {
            long bookCount = bookService.getBookCount();
            long memberCount = memberService.getMemberCount();
            long userCount = userService.getUserCount();
            long lendingCount = lendingService.getLendingCount();
            long activeLendingCount = lendingService.getActiveLendingCount();
            
            StringBuilder sb = new StringBuilder();
            sb.append("=== LIBRARY STATISTICS ===\n\n");
            sb.append("Total Books: ").append(bookCount).append("\n");
            sb.append("Total Members: ").append(memberCount).append("\n");
            sb.append("Total Users: ").append(userCount).append("\n");
            sb.append("Total Lendings: ").append(lendingCount).append("\n");
            sb.append("Active Lendings: ").append(activeLendingCount).append("\n");
            
            JOptionPane.showMessageDialog(null, sb.toString(), "Library Statistics", JOptionPane.INFORMATION_MESSAGE);
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error retrieving statistics: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Shows member statistics.
     */
    private void showMemberStatistics() {
        try {
            long totalMembers = memberService.getMemberCount();
            long regularMembers = memberService.getMemberCountByRole("REGULAR");
            long premiumMembers = memberService.getMemberCountByRole("PREMIUM");
            List<Member> activeMembers = memberService.getActiveMembers();
            
            StringBuilder sb = new StringBuilder();
            sb.append("=== MEMBER STATISTICS ===\n\n");
            sb.append("Total Members: ").append(totalMembers).append("\n");
            sb.append("Regular Members: ").append(regularMembers).append("\n");
            sb.append("Premium Members: ").append(premiumMembers).append("\n");
            sb.append("Active Members: ").append(activeMembers.size()).append("\n");
            
            JOptionPane.showMessageDialog(null, sb.toString(), "Member Statistics", JOptionPane.INFORMATION_MESSAGE);
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error retrieving member statistics: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Shows lending statistics.
     */
    private void showLendingStatistics() {
        try {
            long totalLendings = lendingService.getLendingCount();
            long activeLendings = lendingService.getActiveLendingCount();
            List<Lending> overdueLendings = lendingService.getOverdueLendings();
            
            StringBuilder sb = new StringBuilder();
            sb.append("=== LENDING STATISTICS ===\n\n");
            sb.append("Total Lendings: ").append(totalLendings).append("\n");
            sb.append("Active Lendings: ").append(activeLendings).append("\n");
            sb.append("Overdue Lendings: ").append(overdueLendings.size()).append("\n");
            
            JOptionPane.showMessageDialog(null, sb.toString(), "Lending Statistics", JOptionPane.INFORMATION_MESSAGE);
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error retrieving lending statistics: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // ==================== MEMBER-SPECIFIC METHODS ====================
    
    /**
     * Displays all available books for browsing.
     */
    private void browseBooks() {
        try {
            List<Book> books = bookService.getAllBooks();
            displayBooks(books);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error retrieving books: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Allows members to search for books.
     */
    private void searchBooksForMember() {
        try {
            String[] searchOptions = {"Search by Title", "Search by Author", "Search by ISBN"};
            String searchType = (String) JOptionPane.showInputDialog(null,
                "Select search type:",
                "Search Books",
                JOptionPane.QUESTION_MESSAGE,
                null,
                searchOptions,
                searchOptions[0]);
            
            if (searchType == null) return;
            
            String searchTerm = JOptionPane.showInputDialog("Enter search term:");
            if (searchTerm == null || searchTerm.trim().isEmpty()) return;
            
            List<Book> books;
            switch (searchType) {
                case "Search by Title":
                    books = bookService.searchBooksByTitle(searchTerm);
                    break;
                case "Search by Author":
                    books = bookService.searchBooksByAuthor(searchTerm);
                    break;
                case "Search by ISBN":
                    Optional<Book> bookOpt = bookService.findBookByIsbn(searchTerm);
                    books = bookOpt.map(List::of).orElse(List.of());
                    break;
                default:
                    books = List.of();
            }
            
            if (books.isEmpty()) {
                JOptionPane.showMessageDialog(null, "No books found matching your search.", "Search Results", JOptionPane.INFORMATION_MESSAGE);
            } else {
                displayBooks(books);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error searching books: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Allows members to borrow a book.
     */
    private void borrowBook() {
        try {
            // First show available books
            List<Book> availableBooks = bookService.getAvailableBooks(1);
            if (availableBooks.isEmpty()) {
                JOptionPane.showMessageDialog(null, "No books available for borrowing.", "No Books Available", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            
            // Create book selection dialog
            String[] bookOptions = new String[availableBooks.size()];
            for (int i = 0; i < availableBooks.size(); i++) {
                Book book = availableBooks.get(i);
                bookOptions[i] = "ID: " + book.getId() + " - " + book.getTitle() + " by " + book.getAuthor() + " (Stock: " + book.getStock() + ")";
            }
            
            String selectedBook = (String) JOptionPane.showInputDialog(null,
                "Select book to borrow:",
                "Borrow Book",
                JOptionPane.QUESTION_MESSAGE,
                null,
                bookOptions,
                bookOptions[0]);
            
            if (selectedBook == null) return;
            
            // Extract book ID
            int bookId = Integer.parseInt(selectedBook.substring(4, selectedBook.indexOf(" -")));
            
            // Get lending period
            String daysStr = JOptionPane.showInputDialog("Enter lending period in days (default: 14):");
            int lendingDays = 14;
            if (daysStr != null && !daysStr.trim().isEmpty()) {
                try {
                    lendingDays = Integer.parseInt(daysStr.trim());
                    if (lendingDays <= 0) {
                        JOptionPane.showMessageDialog(null, "Lending period must be positive.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "Invalid number format.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            
            synchronized(this) {
                // Check if member can borrow more books
                if (!lendingService.canMemberBorrowMoreBooks(currentMember.getId(), 3)) {
                    JOptionPane.showMessageDialog(null, "You have reached the maximum number of borrowed books (3).", "Borrowing Limit", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                // Check if book is available and has stock
                if (!bookService.isBookAvailable(bookId)) {
                    JOptionPane.showMessageDialog(null, "This book is not available for borrowing.", "Book Not Available", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                Lending lending = null;
                try {
                    // Try to create lending
                    lending = lendingService.lendBook(currentMember.getId(), bookId, lendingDays);
                    
                    // If lending was successful, decrease stock
                    if (lending != null && lending.getId() > 0) {
                        if (bookService.decreaseStock(bookId)) {
                            JOptionPane.showMessageDialog(null, 
                                "Book borrowed successfully!\n" +
                                "Lending ID: " + lending.getId() + "\n" +
                                "Due Date: " + lending.getDueDate() + "\n" +
                                "Please return the book on time.",
                                "Borrowing Successful", 
                                JOptionPane.INFORMATION_MESSAGE);
                            return;
                        }
                    }
                    
                    JOptionPane.showMessageDialog(null,
                        "Failed to borrow the book. Please try again.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, 
                        "Error while borrowing book: " + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
                
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error borrowing book: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Allows members to return a book.
     */
    private void returnBookForMember() {
        try {
            // Get member's active lendings
            List<Lending> activelendings = lendingService.getLendingsByMember(currentMember.getId());
            activelendings = activelendings.stream().filter(lending -> !lending.isReturned()).collect(java.util.stream.Collectors.toList());
            
            if (activelendings.isEmpty()) {
                JOptionPane.showMessageDialog(null, "You have no active lendings to return.", "No Active lendings", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            
            // Create lending selection dialog
            String[] lendingOptions = new String[activelendings.size()];
            for (int i = 0; i < activelendings.size(); i++) {
                Lending lending = activelendings.get(i);
                Optional<Book> bookOpt = bookService.findBookById(lending.getBookId());
                String bookTitle = bookOpt.map(Book::getTitle).orElse("Unknown Book");
                lendingOptions[i] = "ID: " + lending.getId() + " - " + bookTitle + " (Due: " + lending.getDueDate() + ")";
            }
            
            String selectedlending = (String) JOptionPane.showInputDialog(null,
                "Select lending to return:",
                "Return Book",
                JOptionPane.QUESTION_MESSAGE,
                null,
                lendingOptions,
                lendingOptions[0]);
            
            if (selectedlending == null) return;
            
            // Extract lending ID
            int lendingId = Integer.parseInt(selectedlending.substring(4, selectedlending.indexOf(" -")));
            
            // Return the book
            boolean success = lendingService.returnBook(lendingId);
            if (success) {
                // Increase book stock
                Optional<Lending> lendingOpt = lendingService.findLendingById(lendingId);
                if (lendingOpt.isPresent()) {
                    bookService.increaseStock(lendingOpt.get().getBookId());
                }
                
                JOptionPane.showMessageDialog(null, "Book returned successfully!", "Return Successful", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Failed to return book.", "Return Failed", JOptionPane.ERROR_MESSAGE);
            }
                
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error returning book: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Shows member's current active lendings.
     */
    private void viewMyCurrentLendings() {
        try {
            List<Lending> activeLoans = lendingService.getLendingsByMember(currentMember.getId());
            activeLoans = activeLoans.stream().filter(lending -> !lending.isReturned()).collect(java.util.stream.Collectors.toList());
            
            if (activeLoans.isEmpty()) {
                JOptionPane.showMessageDialog(null, "You have no active loans.", "My Current Loans", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            
            displayMemberLoans(activeLoans, "My Current Loans");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error retrieving your lendings: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Shows member's overdue lendings.
     */
    private void viewMyOverdueLendings() {
        try {
            List<Lending> allLoans = lendingService.getLendingsByMember(currentMember.getId());
            List<Lending> overdueLoans = allLoans.stream()
                .filter(lending -> !lending.isReturned())
                .filter(lending -> lending.getDueDate().isBefore(Instant.now()))
                .collect(java.util.stream.Collectors.toList());
            
            if (overdueLoans.isEmpty()) {
                JOptionPane.showMessageDialog(null, "You have no overdue loans.", "My Overdue Loans", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            
            displayMemberLoans(overdueLoans, "My Overdue Loans");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error retrieving your overdue lendings: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Shows member's lendings due soon (within 3 days).
     */
    private void viewMyDueSoonLendings() {
        try {
            List<Lending> allLoans = lendingService.getLendingsByMember(currentMember.getId());
            Instant threeDaysFromNow = Instant.now().plus(3, java.time.temporal.ChronoUnit.DAYS);
            
            List<Lending> dueSoonLoans = allLoans.stream()
                .filter(lending -> !lending.isReturned())
                .filter(lending -> lending.getDueDate().isBefore(threeDaysFromNow) && lending.getDueDate().isAfter(Instant.now()))
                .collect(java.util.stream.Collectors.toList());
            
            if (dueSoonLoans.isEmpty()) {
                JOptionPane.showMessageDialog(null, "You have no loans due soon.", "My Due Soon Loans", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            
            displayMemberLoans(dueSoonLoans, "My Due Soon Loans");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error retrieving your due soon lendings: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Displays member's lendings in a formatted way.
     */
    private void displayMemberLoans(List<Lending> loans, String title) {
        if (loans.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No loans found.", title, JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append(title).append(" (").append(loans.size()).append(" loan(s)):\n\n");
        
        for (Lending loan : loans) {
            try {
                Optional<Book> bookOpt = bookService.findBookById(loan.getBookId());
                String bookTitle = bookOpt.map(Book::getTitle).orElse("Unknown Book");
                String author = bookOpt.map(Book::getAuthor).orElse("Unknown Author");
                
                sb.append("Loan ID: ").append(loan.getId()).append("\n");
                sb.append("Book: ").append(bookTitle).append(" by ").append(author).append("\n");
                sb.append("Borrowed: ").append(loan.getLendingDate()).append("\n");
                sb.append("Due: ").append(loan.getDueDate()).append("\n");
                
                if (loan.getDueDate().isBefore(Instant.now())) {
                    long overdueDays = java.time.temporal.ChronoUnit.DAYS.between(loan.getDueDate(), Instant.now());
                    sb.append("Status: OVERDUE (").append(overdueDays).append(" days)\n");
                } else {
                    long daysLeft = java.time.temporal.ChronoUnit.DAYS.between(Instant.now(), loan.getDueDate());
                    sb.append("Status: Due in ").append(daysLeft).append(" days\n");
                }
                sb.append("---\n");
            } catch (Exception e) {
                sb.append("Error loading book details for loan ").append(loan.getId()).append("\n");
            }
        }
        
        JOptionPane.showMessageDialog(null, sb.toString(), title, JOptionPane.INFORMATION_MESSAGE);
    }
}
