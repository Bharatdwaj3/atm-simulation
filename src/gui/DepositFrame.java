package gui;

import dao.AccountDAO;
import dao.TransactionDAO;
import model.User;
import model.Account;
import model.Transaction;
import model.TransactionType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DepositFrame extends JFrame {
    private User user;
    private Account account;
    private AccountDAO accountDAO;
    private TransactionDAO transactionDAO;
    
    private JTextField amountField;
    private JButton depositButton;
    private JButton cancelButton;
    
    public DepositFrame(User user, Account account) {
        this.user = user;
        this.account = account;
        this.accountDAO = new AccountDAO();
        this.transactionDAO = new TransactionDAO();
        
        initializeUI();
    }
    
    private void initializeUI() {
        setTitle("Cash Deposit");
        setSize(400, 250);
        setLocationRelativeTo(null);
        setResizable(false);
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(Color.WHITE);
        
        JLabel titleLabel = new JLabel("CASH DEPOSIT", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.BLUE);
        
        JPanel amountPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        amountPanel.setBackground(Color.WHITE);
        
        JLabel amountLabel = new JLabel("Enter deposit amount:");
        amountLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        
        amountField = new JTextField();
        amountField.setFont(new Font("Arial", Font.PLAIN, 14));
        
        amountPanel.add(amountLabel);
        amountPanel.add(amountField);
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(Color.WHITE);
        
        depositButton = new JButton("Deposit");
        depositButton.setFont(new Font("Arial", Font.BOLD, 14));
        depositButton.setBackground(Color.GREEN);
        depositButton.setForeground(Color.WHITE);
        
        cancelButton = new JButton("Cancel");
        cancelButton.setFont(new Font("Arial", Font.PLAIN, 14));
        cancelButton.setBackground(Color.RED);
        cancelButton.setForeground(Color.WHITE);
        
        buttonPanel.add(depositButton);
        buttonPanel.add(cancelButton);
        
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(amountPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
        
        // Event listeners
        depositButton.addActionListener(new DepositButtonListener());
        cancelButton.addActionListener(e -> dispose());
    }
    
    private class DepositButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String amountText = amountField.getText().trim();
            
            if (amountText.isEmpty()) {
                JOptionPane.showMessageDialog(DepositFrame.this, 
                    "Please enter an amount!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            try {
                double amount = Double.parseDouble(amountText);
                
                if (amount <= 0) {
                    JOptionPane.showMessageDialog(DepositFrame.this, 
                        "Amount must be greater than 0!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // Confirm deposit
                int confirm = JOptionPane.showConfirmDialog(DepositFrame.this, 
                    "Deposit $" + amount + " to your account?", "Confirm Deposit", 
                    JOptionPane.YES_NO_OPTION);
                
                if (confirm == JOptionPane.YES_OPTION) {
                    // Update balance
                    double newBalance = account.getBalance() + amount;
                    if (accountDAO.updateBalance(account.getAccountId(), newBalance)) {
                        account.setBalance(newBalance);
                        
                        // Record transaction
                        Transaction transaction = new Transaction();
                        transaction.setAccountId(account.getAccountId());
                        transaction.setTransactionType(TransactionType.DEPOSIT);
                        transaction.setAmount(amount);
                        transaction.setDescription("ATM deposit");
                        
                        if (transactionDAO.recordTransaction(transaction)) {
                            JOptionPane.showMessageDialog(DepositFrame.this, 
                                "Successfully deposited: $" + amount + "\nNew Balance: $" + newBalance, 
                                "Success", JOptionPane.INFORMATION_MESSAGE);
                            dispose();
                        }
                    } else {
                        JOptionPane.showMessageDialog(DepositFrame.this, 
                            "Transaction failed!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(DepositFrame.this, 
                    "Please enter a valid amount!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}