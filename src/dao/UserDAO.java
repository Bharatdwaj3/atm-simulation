package dao;

import database.DatabaseConnection;
import model.User;
import java.sql.*;
import org.mindrot.jbcrypt.BCrypt;

public class UserDAO {
    
    public User validateUser(String cardNumber, String pin) {
        String sql = "SELECT u.*, a.account_id, a.account_number, a.balance " +
                    "FROM users u LEFT JOIN accounts a ON u.user_id = a.user_id " +
                    "WHERE u.card_number = ? AND u.is_active = TRUE";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, cardNumber);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                String storedHash = rs.getString("pin_hash");
                if (BCrypt.checkpw(pin, storedHash)) {
                    return new User(
                        rs.getInt("user_id"),
                        rs.getString("card_number"),
                        rs.getString("pin_hash"),
                        rs.getString("full_name"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getBoolean("is_active")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Error validating user: " + e.getMessage());
        }
        return null;
    }
    
    public boolean updatePin(int userId, String newPin) {
        String sql = "UPDATE users SET pin_hash = ? WHERE user_id = ?";
        String hashedPin = BCrypt.hashpw(newPin, BCrypt.gensalt());
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, hashedPin);
            pstmt.setInt(2, userId);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating PIN: " + e.getMessage());
            return false;
        }
    }
}