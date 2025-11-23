package main;

import database.DatabaseInitializer;
import gui.LoginFrame;
import javax.swing.*;

public class ATMMain {
    public static void main(String[] args) {
        // Set look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeel());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Initialize database
        SwingUtilities.invokeLater(() -> {
            DatabaseInitializer.initializeDatabase();
            
            // Start the application
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        });
    }
}