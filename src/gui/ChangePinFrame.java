package gui;

import dao.UserDAO;
import model.User;
import model.Account;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChangePinFrame extends JFrame {
    private User user;
    private Account account;
    private UserDAO userDAO;
    
    private JPasswordField currentPinField;
    private JPasswordField newPinField;
    private JPasswordField confirmPinField;
    private JButton changeButton;
    private JButton cancelButton;
    
    public ChangePinFrame(User user, Account account) {
        this.user = user;
        this.account = account;
        this.userDAO = new UserDAO();
        
        initializeUI();
    }
    
    private void initializeUI() {
        setTitle("Change PIN");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setResizable(false);
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(Color.WHITE);
        
        JLabel titleLabel = new JLabel("CHANGE PIN", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.BLUE);
        
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        formPanel.setBackground(Color.WHITE);
        
        JLabel currentPinLabel = new JLabel("Current PIN:");
        currentPinLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        currentPinField = new JPasswordField();
        
        JLabel newPinLabel = new JLabel("New PIN:");
        newPinLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        newPinField = new JPasswordField();
        
        JLabel confirmPinLabel = new JLabel("Confirm PIN:");
        confirmPinLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        confirmPinField = new JPasswordField();
        
        formPanel.add(currentPinLabel);
        formPanel.add(currentPinField);
        formPanel.add(newPinLabel);
        formPanel.add(newPinField);
        formPanel.add(confirmPinLabel);
        formPanel.add(confirmPinField);
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(Color.WHITE);
        
        changeButton = new JButton("Change PIN");
        changeButton.setFont(new Font("Arial", Font.BOLD, 14));
        changeButton.setBackground(Color.GREEN);
        changeButton.setForeground(Color.WHITE);
        
        cancelButton = new JButton("Cancel");
        cancelButton.setFont(new Font("Arial", Font.PLAIN, 14));
        cancelButton.setBackground(Color.RED);
        cancelButton.setForeground(Color.WHITE);
        
        buttonPanel.add(changeButton);
        buttonPanel.add(cancelButton);
        
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
        
        // Event listeners
        changeButton.addActionListener(new ChangePinButtonListener());
        cancelButton.addActionListener(e -> dispose());
    }
    
    private class ChangePinButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String currentPin = new String(currentPinField.getPassword());
            String newPin = new String(newPinField.getPassword());
            String confirmPin = new String(confirmPinField.getPassword());
            
            if (currentPin.isEmpty() || newPin.isEmpty() || confirmPin.isEmpty()) {
                JOptionPane.showMessageDialog(ChangePinFrame.this, 
                    "Please fill all fields!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (!newPin.equals(confirmPin)) {
                JOptionPane.showMessageDialog(ChangePinFrame.this, 
                    "New PIN and confirm PIN do not match!", "Error", JOptionPane.ERROR_MESSAGE);
                newPinField.setText("");
                confirmPinField.setText("");
                newPinField.requestFocus();
                return;
            }
            
            if (newPin.length() != 4) {
                JOptionPane.showMessageDialog(ChangePinFrame.this, 
                    "PIN must be 4 digits!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Validate current PIN
            if (!userDAO.validateUser(user.getCardNumber(), currentPin)) {
                JOptionPane.showMessageDialog(ChangePinFrame.this, 
                    "Current PIN is incorrect!", "Error", JOptionPane.ERROR_MESSAGE);
                currentPinField.setText("");
                currentPinField.requestFocus();
                return;
            }
            
            // Change PIN
            if (userDAO.updatePin(user.getUserId(), newPin)) {
                JOptionPane.showMessageDialog(ChangePinFrame.this, 
                    "PIN changed successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(ChangePinFrame.this, 
                    "Failed to change PIN!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}