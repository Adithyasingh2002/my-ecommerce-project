package com.adi.ecomerce.payload;

public class RegisterRequest {

    private String fullName;
    private String email;
    private String password;
    private String address;
    private String phoneNumber;
    private String role;         // "CUSTOMER" or "ADMIN"
    private String adminSecret;  // Only required if role == ADMIN
    // Getters and Setters
    public String getFullName() {
        return fullName;
    }
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }
    public String getAdminSecret() {
        return adminSecret;
    }
    public void setAdminSecret(String adminSecret) {
        this.adminSecret = adminSecret;
    }
}
