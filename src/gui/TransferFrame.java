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

public class TransferFrame extends JFrame {
    private User user;
    private Account account;
    private AccountDAO accountDAO;
    private TransactionDAO transactionDAO;
    
    private JTextField targetAccountField;
    private JTextField amountField;
    private JButton transferButton;
    private JButton cancelButton;
    
    public TransferFrame(User user, Account account) {
        this.user = user;
        this.account = account;
        this.accountDAO = new AccountDAO();
        this.transactionDAO = new TransactionDAO();
        
        initializeUI();
    }
    
    private void initializeUI() {
        setTitle("Transfer Funds");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setResizable(false);
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(Color.WHITE);
        
        JLabel titleLabel = new JLabel("FUND TRANSFER", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.BLUE);
        
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        formPanel.setBackground(Color.WHITE);
        
        JLabel targetLabel = new JLabel("Target Account:");
        targetLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        targetAccountField = new JTextField();
        targetAccountField.setFont(new Font("Arial", Font.PLAIN, 14));
        
        JLabel amountLabel = new JLabel("Amount:");
        amountLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        amountField = new JTextField();
        amountField.setFont(new Font("Arial", Font.PLAIN, 14));
        
        formPanel.add(targetLabel);
        formPanel.add(targetAccountField);
        formPanel.add(amountLabel);
        formPanel.add(amountField);
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(Color.WHITE);
        
        transferButton = new JButton("Transfer");
        transferButton.setFont(new Font("Arial", Font.BOLD, 14));
        transferButton.setBackground(Color.GREEN);
        transferButton.setForeground(Color.WHITE);
        
        cancelButton = new JButton("Cancel");
        cancelButton.setFont(new Font("Arial", Font.PLAIN, 14));
        cancelButton.setBackground(Color.RED);
        cancelButton.setForeground(Color.WHITE);
        
        buttonPanel.add(transferButton);
        buttonPanel.add(cancelButton);
        
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
        
        // Event listeners
        transferButton.addActionListener(new TransferButtonListener());
        cancelButton.addActionListener(e -> dispose());
    }
    
    private class TransferButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String targetAccountNumber = targetAccountField.getText().trim();
            String amountText = amountField.getText().trim();
            
            if (targetAccountNumber.isEmpty() || amountText.isEmpty()) {
                JOptionPane.showMessageDialog(TransferFrame.this, 
                    "Please fill all fields!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Check if transferring to same account
            if (targetAccountNumber.equals(account.getAccountNumber())) {
                JOptionPane.showMessageDialog(TransferFrame.this, 
                    "Cannot transfer to the same account!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            try {
                double amount = Double.parseDouble(amountText);
                
                if (amount <= 0) {
                    JOptionPane.showMessageDialog(TransferFrame.this, 
                        "Amount must be greater than 0!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                if (amount > account.getBalance()) {
                    JOptionPane.showMessageDialog(TransferFrame.this, 
                        "Insufficient funds!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // Check if target account exists
                Account targetAccount = accountDAO.getAccountByNumber(targetAccountNumber);
                if (targetAccount == null) {
                    JOptionPane.showMessageDialog(TransferFrame.this, 
                        "Target account not found!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // Confirm transfer
                int confirm = JOptionPane.showConfirmDialog(TransferFrame.this, 
                    "Transfer $" + amount + " to account " + targetAccountNumber + "?", 
                    "Confirm Transfer", JOptionPane.YES_NO_OPTION);
                
                if (confirm == JOptionPane.YES_OPTION) {
                    // Update source account balance
                    double newSourceBalance = account.getBalance() - amount;
                    if (!accountDAO.updateBalance(account.getAccountId(), newSourceBalance)) {
                        JOptionPane.showMessageDialog(TransferFrame.this, 
                            "Transfer failed!", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    
                    // Update target account balance
                    double newTargetBalance = targetAccount.getBalance() + amount;
                    if (!accountDAO.updateBalance(targetAccount.getAccountId(), newTargetBalance)) {
                        // Rollback source account
                        accountDAO.updateBalance(account.getAccountId(), account.getBalance());
                        JOptionPane.showMessageDialog(TransferFrame.this, 
                            "Transfer failed!", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    
                    // Update local account object
                    account.setBalance(newSourceBalance);
                    
                    // Record transaction for source account
                    Transaction sourceTransaction = new Transaction();
                    sourceTransaction.setAccountId(account.getAccountId());
                    sourceTransaction.setTransactionType(TransactionType.TRANSFER);
                    sourceTransaction.setAmount(amount);
                    sourceTransaction.setDescription("Transfer to " + targetAccountNumber);
                    sourceTransaction.setTargetAccountNumber(targetAccountNumber);
                    
                    // Record transaction for target account
                    Transaction targetTransaction = new Transaction();
                    targetTransaction.setAccountId(targetAccount.getAccountId());
                    targetTransaction.setTransactionType(TransactionType.TRANSFER);
                    targetTransaction.setAmount(amount);
                    targetTransaction.setDescription("Transfer from " + account.getAccountNumber());
                    targetTransaction.setTargetAccountNumber(account.getAccountNumber());
                    
                    if (transactionDAO.recordTransaction(sourceTransaction) && 
                        transactionDAO.recordTransaction(targetTransaction)) {
                        
                        JOptionPane.showMessageDialog(TransferFrame.this, 
                            "Successfully transferred: $" + amount + 
                            "\nTo Account: " + targetAccountNumber + 
                            "\nNew Balance: $" + newSourceBalance, 
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                        dispose();
                    }
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(TransferFrame.this, 
                    "Please enter a valid amount!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}