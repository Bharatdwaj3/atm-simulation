package gui;

import dao.UserDAO;
import dao.AccountDAO;
import model.User;
import model.Account;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginFrame extends JFrame {
    private JTextField cardNumberField;
    private JPasswordField pinField;
    private JButton loginButton;
    private JButton clearButton;
    
    private UserDAO userDAO;
    private AccountDAO accountDAO;
    
    public LoginFrame() {
        userDAO = new UserDAO();
        accountDAO = new AccountDAO();
        
        initializeUI();
    }
    
    private void initializeUI() {
        setTitle("ATM Simulation - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(Color.WHITE);
        
        // Title
        JLabel titleLabel = new JLabel("ATM SIMULATION", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.BLUE);
        
        // Form panel
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        formPanel.setBackground(Color.WHITE);
        
        JLabel cardLabel = new JLabel("Card Number:");
        cardLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        cardNumberField = new JTextField();
        cardNumberField.setFont(new Font("Arial", Font.PLAIN, 14));
        
        JLabel pinLabel = new JLabel("PIN:");
        pinLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        pinField = new JPasswordField();
        pinField.setFont(new Font("Arial", Font.PLAIN, 14));
        
        formPanel.add(cardLabel);
        formPanel.add(cardNumberField);
        formPanel.add(pinLabel);
        formPanel.add(pinField);
        
        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(Color.WHITE);
        
        loginButton = new JButton("Login");
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
        loginButton.setBackground(Color.GREEN);
        loginButton.setForeground(Color.WHITE);
        
        clearButton = new JButton("Clear");
        clearButton.setFont(new Font("Arial", Font.PLAIN, 14));
        clearButton.setBackground(Color.GRAY);
        clearButton.setForeground(Color.WHITE);
        
        buttonPanel.add(loginButton);
        buttonPanel.add(clearButton);
        
        // Add components to main panel
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
        
        // Event listeners
        loginButton.addActionListener(new LoginButtonListener());
        clearButton.addActionListener(new ClearButtonListener());
        
        // Enter key listener for login
        pinField.addActionListener(new LoginButtonListener());
    }
    
    private class LoginButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String cardNumber = cardNumberField.getText().trim();
            String pin = new String(pinField.getPassword());
            
            if (cardNumber.isEmpty() || pin.isEmpty()) {
                JOptionPane.showMessageDialog(LoginFrame.this, 
                    "Please enter both card number and PIN", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Validate user
            User user = userDAO.validateUser(cardNumber, pin);
            if (user != null) {
                // Get account details
                Account account = accountDAO.getAccountByUserId(user.getUserId());
                
                if (account != null) {
                    // Open main menu
                    new MainMenuFrame(user, account).setVisible(true);
                    dispose(); // Close login window
                } else {
                    JOptionPane.showMessageDialog(LoginFrame.this, 
                        "Account not found!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(LoginFrame.this, 
                    "Invalid card number or PIN!", "Error", JOptionPane.ERROR_MESSAGE);
                pinField.setText("");
                pinField.requestFocus();
            }
        }
    }
    
    private class ClearButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            cardNumberField.setText("");
            pinField.setText("");
            cardNumberField.requestFocus();
        }
    }
}