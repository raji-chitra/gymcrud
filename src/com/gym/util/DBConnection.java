package com.gym.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Utility class for managing MySQL Database Connection using JDBC.
 * Implements Singleton-like connection retrieval with proper resource management.
 */
public class DBConnection {

    // Database connection parameters
    // Update the DB_PASSWORD with your local MySQL root password if different.
    private static final String DB_URL = System.getenv("GYM_DB_URL") != null 
            ? System.getenv("GYM_DB_URL") 
            : "jdbc:mysql://localhost:3306/gym_management?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
            
    private static final String DB_USER = System.getenv("GYM_DB_USER") != null 
            ? System.getenv("GYM_DB_USER") 
            : "root";
            
    private static final String DB_PASSWORD = System.getenv("GYM_DB_PASS") != null 
            ? System.getenv("GYM_DB_PASS") 
            : "Rajalakshmi@05"; // Default local password, update if your MySQL root password differs

    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";

    static {
        try {
            // Explicitly load the MySQL JDBC Driver
            Class.forName(JDBC_DRIVER);
        } catch (ClassNotFoundException e) {
            System.err.println("[ERROR] MySQL JDBC Driver not found! Make sure mysql-connector-j.jar is in the classpath.");
            e.printStackTrace();
        }
    }

    /**
     * Obtains a new connection to the MySQL database.
     * @return Connection object
     * @throws SQLException if a database access error occurs
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    /**
     * Standalone test method to verify database connectivity.
     */
    public static void main(String[] args) {
        System.out.println("Testing MySQL Database Connection...");
        System.out.println("URL: " + DB_URL);
        System.out.println("User: " + DB_USER);

        try (Connection conn = getConnection()) {
            if (conn != null && !conn.isClosed()) {
                System.out.println("SUCCESS: Connected to MySQL database '" + conn.getCatalog() + "' successfully!");
            }
        } catch (SQLException e) {
            System.err.println("FAILED: Could not connect to database.");
            System.err.println("Error Message: " + e.getMessage());
            System.err.println("Hint: Verify that MySQL service is running, 'gym_management' database is created, and username/password are correct in DBConnection.java.");
        }
    }
}
