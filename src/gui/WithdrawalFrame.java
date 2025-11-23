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

public class WithdrawalFrame extends JFrame {
    private User user;
    private Account account;
    private AccountDAO accountDAO;
    private TransactionDAO transactionDAO;
    
    private JComboBox<String> amountComboBox;
    private JTextField customAmountField;
    private JButton withdrawButton;
    private JButton cancelButton;
    
    public WithdrawalFrame(User user, Account account) {
        this.user = user;
        this.account = account;
        this.accountDAO = new AccountDAO();
        this.transactionDAO = new TransactionDAO();
        
        initializeUI();
    }
    
    private void initializeUI() {
        setTitle("Cash Withdrawal");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setResizable(false);
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(Color.WHITE);
        
        JLabel titleLabel = new JLabel("CASH WITHDRAWAL", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.BLUE);
        
        JPanel amountPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        amountPanel.setBackground(Color.WHITE);
        
        JLabel selectLabel = new JLabel("Select withdrawal amount:");
        selectLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        
        String[] amounts = {"$20", "$40", "$60", "$100", "$200", "$500"};
        amountComboBox = new JComboBox<>(amounts);
        amountComboBox.setFont(new Font("Arial", Font.PLAIN, 14));
        
        JLabel customLabel = new JLabel("Or enter custom amount:");
        customLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        
        customAmountField = new JTextField();
        customAmountField.setFont(new Font("Arial", Font.PLAIN, 14));
        
        amountPanel.add(selectLabel);
        amountPanel.add(amountComboBox);
        amountPanel.add(customLabel);
        amountPanel.add(customAmountField);
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(Color.WHITE);
        
        withdrawButton = new JButton("Withdraw");
        withdrawButton.setFont(new Font("Arial", Font.BOLD, 14));
        withdrawButton.setBackground(Color.GREEN);
        withdrawButton.setForeground(Color.WHITE);
        
        cancelButton = new JButton("Cancel");
        cancelButton.setFont(new Font("Arial", Font.PLAIN, 14));
        cancelButton.setBackground(Color.RED);
        cancelButton.setForeground(Color.WHITE);
        
        buttonPanel.add(withdrawButton);
        buttonPanel.add(cancelButton);
        
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(amountPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
        
        // Event listeners
        withdrawButton.addActionListener(new WithdrawButtonListener());
        cancelButton.addActionListener(e -> dispose());
    }
    
    private class WithdrawButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            double amount = 0;
            
            // Get amount from custom field or combo box
            String customAmount = customAmountField.getText().trim();
            if (!customAmount.isEmpty()) {
                try {
                    amount = Double.parseDouble(customAmount);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(WithdrawalFrame.this, 
                        "Please enter a valid amount!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } else {
                String selectedAmount = (String) amountComboBox.getSelectedItem();
                amount = Double.parseDouble(selectedAmount.substring(1)); // Remove '$'
            }
            
            if (amount <= 0) {
                JOptionPane.showMessageDialog(WithdrawalFrame.this, 
                    "Amount must be greater than 0!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (amount > account.getBalance()) {
                JOptionPane.showMessageDialog(WithdrawalFrame.this, 
                    "Insufficient funds!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Confirm withdrawal
            int confirm = JOptionPane.showConfirmDialog(WithdrawalFrame.this, 
                "Withdraw $" + amount + " from your account?", "Confirm Withdrawal", 
                JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                // Update balance
                double newBalance = account.getBalance() - amount;
                if (accountDAO.updateBalance(account.getAccountId(), newBalance)) {
                    account.setBalance(newBalance);
                    
                    // Record transaction
                    Transaction transaction = new Transaction();
                    transaction.setAccountId(account.getAccountId());
                    transaction.setTransactionType(TransactionType.WITHDRAWAL);
                    transaction.setAmount(amount);
                    transaction.setDescription("ATM withdrawal");
                    
                    if (transactionDAO.recordTransaction(transaction)) {
                        JOptionPane.showMessageDialog(WithdrawalFrame.this, 
                            "Successfully withdrew: $" + amount + "\nNew Balance: $" + newBalance, 
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                        dispose();
                    }
                } else {
                    JOptionPane.showMessageDialog(WithdrawalFrame.this, 
                        "Transaction failed!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
}