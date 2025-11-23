package gui;

import dao.TransactionDAO;
import model.User;
import model.Account;
import model.Transaction;
import model.TransactionType;

import javax.swing.*;
import java.awt.*;

public class BalanceInquiryFrame extends JFrame {
    private User user;
    private Account account;
    private TransactionDAO transactionDAO;
    
    public BalanceInquiryFrame(User user, Account account) {
        this.user = user;
        this.account = account;
        this.transactionDAO = new TransactionDAO();
        
        initializeUI();
        recordTransaction();
    }
    
    private void initializeUI() {
        setTitle("Balance Inquiry");
        setSize(400, 250);
        setLocationRelativeTo(null);
        setResizable(false);
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(Color.WHITE);
        
        JLabel titleLabel = new JLabel("ACCOUNT BALANCE", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.BLUE);
        
        JPanel infoPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        infoPanel.setBackground(Color.WHITE);
        
        JLabel nameLabel = new JLabel("Account Holder: " + user.getFullName());
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        
        JLabel accLabel = new JLabel("Account Number: " + account.getAccountNumber());
        accLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        
        JLabel typeLabel = new JLabel("Account Type: " + account.getAccountType());
        typeLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        
        JLabel balanceLabel = new JLabel("Available Balance: $" + account.getBalance());
        balanceLabel.setFont(new Font("Arial", Font.BOLD, 16));
        balanceLabel.setForeground(Color.GREEN);
        
        infoPanel.add(nameLabel);
        infoPanel.add(accLabel);
        infoPanel.add(typeLabel);
        infoPanel.add(balanceLabel);
        
        JButton okButton = new JButton("OK");
        okButton.setFont(new Font("Arial", Font.BOLD, 14));
        okButton.setBackground(Color.BLUE);
        okButton.setForeground(Color.WHITE);
        okButton.addActionListener(e -> dispose());
        
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(infoPanel, BorderLayout.CENTER);
        mainPanel.add(okButton, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private void recordTransaction() {
        Transaction transaction = new Transaction();
        transaction.setAccountId(account.getAccountId());
        transaction.setTransactionType(TransactionType.BALANCE_INQUIRY);
        transaction.setAmount(0.0);
        transaction.setDescription("Balance inquiry");
        
        transactionDAO.recordTransaction(transaction);
    }
}