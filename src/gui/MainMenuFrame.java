package gui;

import model.User;
import model.Account;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenuFrame extends JFrame {
    private User user;
    private Account account;
    
    public MainMenuFrame(User user, Account account) {
        this.user = user;
        this.account = account;
        
        initializeUI();
    }
    
    private void initializeUI() {
        setTitle("ATM Simulation - Main Menu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 400);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(Color.WHITE);
        
        // Welcome panel
        JPanel welcomePanel = new JPanel(new BorderLayout());
        welcomePanel.setBackground(Color.WHITE);
        
        JLabel welcomeLabel = new JLabel("Welcome, " + user.getFullName(), JLabel.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        welcomeLabel.setForeground(Color.BLUE);
        
        JLabel accountLabel = new JLabel("Account: " + account.getAccountNumber(), JLabel.CENTER);
        accountLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        
        welcomePanel.add(welcomeLabel, BorderLayout.NORTH);
        welcomePanel.add(accountLabel, BorderLayout.CENTER);
        
        // Buttons panel
        JPanel buttonPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        buttonPanel.setBackground(Color.WHITE);
        
        String[] buttonLabels = {
            "Balance Inquiry", "Withdraw Cash", 
            "Deposit Cash", "Transfer Funds",
            "Mini Statement", "Change PIN", 
            "Logout", "Exit"
        };
        
        Color[] buttonColors = {
            Color.BLUE, Color.ORANGE,
            Color.GREEN, Color.MAGENTA,
            Color.CYAN, Color.PINK,
            Color.GRAY, Color.RED
        };
        
        for (int i = 0; i < buttonLabels.length; i++) {
            JButton button = new JButton(buttonLabels[i]);
            button.setFont(new Font("Arial", Font.BOLD, 12));
            button.setBackground(buttonColors[i]);
            button.setForeground(Color.WHITE);
            button.setFocusPainted(false);
            
            final String action = buttonLabels[i];
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    handleButtonClick(action);
                }
            });
            
            buttonPanel.add(button);
        }
        
        // Add components to main panel
        mainPanel.add(welcomePanel, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.CENTER);
        
        add(mainPanel);
    }
    
    private void handleButtonClick(String action) {
        switch (action) {
            case "Balance Inquiry":
                new BalanceInquiryFrame(user, account).setVisible(true);
                break;
            case "Withdraw Cash":
                new WithdrawalFrame(user, account).setVisible(true);
                break;
            case "Deposit Cash":
                new DepositFrame(user, account).setVisible(true);
                break;
            case "Transfer Funds":
                new TransferFrame(user, account).setVisible(true);
                break;
            case "Mini Statement":
                new MiniStatementFrame(user, account).setVisible(true);
                break;
            case "Change PIN":
                new ChangePinFrame(user, account).setVisible(true);
                break;
            case "Logout":
                int confirm = JOptionPane.showConfirmDialog(this, 
                    "Are you sure you want to logout?", "Confirm Logout", 
                    JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    new LoginFrame().setVisible(true);
                    dispose();
                }
                break;
            case "Exit":
                confirm = JOptionPane.showConfirmDialog(this, 
                    "Are you sure you want to exit?", "Confirm Exit", 
                    JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
                break;
        }
    }
}