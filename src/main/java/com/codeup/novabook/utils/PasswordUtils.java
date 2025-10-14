/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.codeup.novabook.utils;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Utility class for handling password hashing and verification.
 * 
 * @author Adrián Gutiérrez
 */
public class PasswordUtils {
    
    /**
     * Hashes a password using BCrypt.
     * 
     * @param plainTextPassword the password to hash
     * @return the hashed password
     */
    public static String hashPassword(String plainTextPassword) {
        return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt(12));
    }
    
    /**
     * Verifies a password against a hash.
     * 
     * @param plainTextPassword the password to check
     * @param hashedPassword the hash to check against
     * @return true if the password matches the hash
     */
    public static boolean verifyPassword(String plainTextPassword, String hashedPassword) {
        return BCrypt.checkpw(plainTextPassword, hashedPassword);
    }
}