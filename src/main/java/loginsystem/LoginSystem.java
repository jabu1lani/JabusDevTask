/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package loginsystem;

/**
 *
 * @author Jabu
 */

/**
 * This class contains phone number validation that uses a regular expression
 * generated with the assistance of ChatGPT (OpenAI, 2023).
 * 
 * Reference: OpenAI. (2023). ChatGPT (May 24 version) [Large language model]. 
 * https://chat.openai.com
 */


import java.util.regex.Pattern;

public class LoginSystem {
    private String username;
    private String password;
    private String phoneNumber;
    private boolean isRegistered;

    // Username validation (must contain underscore and be ≤5 chars)
    public boolean checkUserName(String username) {
        return username != null && 
               username.contains("_") && 
               username.length() <= 5;
    }

    // Password complexity check
    public boolean checkPasswordComplexity(String password) {
        boolean hasUpper = !password.equals(password.toLowerCase());
        boolean hasNumber = password.matches(".*\\d.*");
        boolean hasSpecial = !password.matches("[A-Za-z0-9 ]*");
        return password.length() >= 8 && hasUpper && hasNumber && hasSpecial;
    }

    // South African phone number validation (+27 followed by 9 digits)
    public boolean checkCellPhoneNumber(String phoneNumber) {
        return Pattern.matches("^\\+27\\d{9}$", phoneNumber);
    }

    // Complete registration process
    public String registerUser(String username, String password, String phoneNumber) {
        if (!checkUserName(username)) {
            return "Username must contain '_' and be ≤5 characters (e.g., 'user_1')";
        }
        if (!checkPasswordComplexity(password)) {
            return "Password must contain:\n- 8+ characters\n- 1 capital letter\n- 1 number\n- 1 special character";
        }
        if (!checkCellPhoneNumber(phoneNumber)) {
            return "Phone number must be +27 followed by 9 digits";
        }

        this.username = username;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.isRegistered = true;
        return "User registered successfully!";
    }

    // login verification
    public boolean loginUser(String username, String password) {
        return isRegistered && 
               this.username.equals(username) && 
               this.password.equals(password);
    }

    // Login with personalized welcome
    public String returnLoginStatus(boolean loginStatus, String username) {
        if (loginStatus) {
            try {
                String[] parts = username.split("_");
                String firstName = parts.length > 0 ? parts[0] : "User";
                firstName = firstName.substring(0, 1).toUpperCase() + 
                          (firstName.length() > 1 ? firstName.substring(1) : "");
                return "Welcome " + firstName + ", it is great to see you again.";
            } catch (Exception e) {
                return "Welcome back!";
            }
        }
        return "Username or password incorrect, please try again";
    }

    // Password reset functionality
    public String resetPassword(String username, String oldPassword, String newPassword) {
        if (!isRegistered || !this.username.equals(username)) {
            return "User not found";
        }
        if (!this.password.equals(oldPassword)) {
            return "Current password incorrect";
        }
        if (!checkPasswordComplexity(newPassword)) {
            return "New password doesn't meet complexity requirements";
        }
        this.password = newPassword;
        return "Password reset successfully";
    }

    //account recovery
    public String getMaskedPhoneNumber() {
        return phoneNumber != null ? 
               phoneNumber.substring(0, 4) + "****" + phoneNumber.substring(8) : 
               "Not available";
    }
}

