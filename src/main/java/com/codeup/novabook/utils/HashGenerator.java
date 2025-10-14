package com.codeup.novabook.utils;

public class HashGenerator {
    public static void main(String[] args) {
        String adminPass = "admin123";
        String memberPass = "pass123";
        
        System.out.println("Admin password hash:");
        System.out.println(PasswordUtils.hashPassword(adminPass));
        System.out.println("\nMember password hash:");
        System.out.println(PasswordUtils.hashPassword(memberPass));
    }
}