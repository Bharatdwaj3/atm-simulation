package model;

public class User {
    private int userId;
    private String cardNumber;
    private String pinHash;
    private String fullName;
    private String email;
    private String phone;
    private boolean isActive;
    
    public User() {}
    
    public User(int userId, String cardNumber, String pinHash, String fullName, 
                String email, String phone, boolean isActive) {
        this.userId = userId;
        this.cardNumber = cardNumber;
        this.pinHash = pinHash;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.isActive = isActive;
    }
    
    // Getters and Setters
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    
    public String getCardNumber() { return cardNumber; }
    public void setCardNumber(String cardNumber) { this.cardNumber = cardNumber; }
    
    public String getPinHash() { return pinHash; }
    public void setPinHash(String pinHash) { this.pinHash = pinHash; }
    
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    
    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }
}