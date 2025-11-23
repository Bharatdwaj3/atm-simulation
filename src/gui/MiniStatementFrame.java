package gui;

import dao.TransactionDAO;
import model.User;
import model.Account;
import model.Transaction;
import model.TransactionType;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class MiniStatementFrame extends JFrame {
    private User user;
    private Account account;
    private TransactionDAO transactionDAO;
    
    public MiniStatementFrame(User user, Account account) {
        this.user = user;
        this.account = account;
        this.transactionDAO = new TransactionDAO();
        
        initializeUI();
        recordTransaction();
    }
    
    private void initializeUI() {
        setTitle("Mini Statement");
        setSize(600, 400);
        setLocationRelativeTo(null);
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(Color.WHITE);
        
        JLabel titleLabel = new JLabel("MINI STATEMENT", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.BLUE);
        
        // Header panel
        JPanel headerPanel = new JPanel(new GridLayout(2, 1));
        headerPanel.setBackground(Color.WHITE);
        
        JLabel nameLabel = new JLabel("Account Holder: " + user.getFullName());
        nameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        
        JLabel accLabel = new JLabel("Account Number: " + account.getAccountNumber());
        accLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        
        headerPanel.add(nameLabel);
        headerPanel.add(accLabel);
        
        // Transactions panel
        List<Transaction> transactions = transactionDAO.getMiniStatement(account.getAccountId(), 10);
        
        String[] columnNames = {"Date", "Type", "Amount", "Description", "Target Account"};
        Object[][] data = new Object[transactions.size()][5];
        
        for (int i = 0; i < transactions.size(); i++) {
            Transaction t = transactions.get(i);
            data[i][0] = t.getTransactionDate().toString();
            data[i][1] = t.getTransactionType();
            data[i][2] = t.getAmount();
            data[i][3] = t.getDescription();
            data[i][4] = t.getTargetAccountNumber() != null ? t.getTargetAccountNumber() : "";
        }
        
        JTable transactionTable = new JTable(data, columnNames);
        transactionTable.setFont(new Font("Arial", Font.PLAIN, 12));
        transactionTable.setRowHeight(25);
        transactionTable.setEnabled(false);
        
        JScrollPane scrollPane = new JScrollPane(transactionTable);
        
        JButton closeButton = new JButton("Close");
        closeButton.setFont(new Font("Arial", Font.BOLD, 14));
        closeButton.setBackground(Color.BLUE);
        closeButton.setForeground(Color.WHITE);
        closeButton.addActionListener(e -> dispose());
        
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(headerPanel, BorderLayout.CENTER);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(closeButton, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private void recordTransaction() {
        Transaction transaction = new Transaction();
        transaction.setAccountId(account.getAccountId());
        transaction.setTransactionType(TransactionType.BALANCE_INQUIRY);
        transaction.setAmount(0.0);
        transaction.setDescription("Mini statement generated");
        
        transactionDAO.recordTransaction(transaction);
    }
}