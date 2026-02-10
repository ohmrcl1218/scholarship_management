package com.example.scholarship.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.regex.Pattern;

@Component
public class PasswordUtil {

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    private final SecureRandom random = new SecureRandom();
    
    // Password complexity requirements
    private static final int MIN_LENGTH = 8;
    private static final int MAX_LENGTH = 128;
    private static final Pattern UPPER_CASE = Pattern.compile("[A-Z]");
    private static final Pattern LOWER_CASE = Pattern.compile("[a-z]");
    private static final Pattern DIGIT = Pattern.compile("[0-9]");
    private static final Pattern SPECIAL_CHAR = Pattern.compile("[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]");

    /**
     * Hash a plain text password
     */
    public String hashPassword(String plainPassword) {
        return encoder.encode(plainPassword);
    }

    /**
     * Verify if a plain text password matches a hashed password
     */
    public boolean verifyPassword(String plainPassword, String hashedPassword) {
        return encoder.matches(plainPassword, hashedPassword);
    }

    /**
     * Validate password complexity
     * Returns true if password meets requirements, false otherwise
     */
    public boolean validatePasswordComplexity(String password) {
        if (password == null || password.length() < MIN_LENGTH || password.length() > MAX_LENGTH) {
            return false;
        }

        boolean hasUpper = UPPER_CASE.matcher(password).find();
        boolean hasLower = LOWER_CASE.matcher(password).find();
        boolean hasDigit = DIGIT.matcher(password).find();
        boolean hasSpecial = SPECIAL_CHAR.matcher(password).find();

        // At least 3 out of 4 complexity requirements
        int complexityScore = 0;
        if (hasUpper) complexityScore++;
        if (hasLower) complexityScore++;
        if (hasDigit) complexityScore++;
        if (hasSpecial) complexityScore++;

        return complexityScore >= 3;
    }

    /**
     * Get password strength score (0-4)
     */
    public int getPasswordStrength(String password) {
        if (password == null) return 0;
        
        int score = 0;
        
        // Length score
        if (password.length() >= 12) score += 2;
        else if (password.length() >= 8) score += 1;
        
        // Complexity score
        if (UPPER_CASE.matcher(password).find()) score++;
        if (LOWER_CASE.matcher(password).find()) score++;
        if (DIGIT.matcher(password).find()) score++;
        if (SPECIAL_CHAR.matcher(password).find()) score++;
        
        // Cap at 4 for consistency
        return Math.min(score, 4);
    }

    /**
     * Get password strength category
     */
    public String getPasswordStrengthCategory(String password) {
        int strength = getPasswordStrength(password);
        
        switch (strength) {
            case 0:
            case 1:
                return "Very Weak";
            case 2:
                return "Weak";
            case 3:
                return "Good";
            case 4:
                return "Strong";
            default:
                return "Very Weak";
        }
    }

    /**
     * Generate a random secure password
     */
    public String generateSecurePassword(int length) {
        if (length < MIN_LENGTH || length > MAX_LENGTH) {
            length = 12; // Default length
        }

        String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lower = "abcdefghijklmnopqrstuvwxyz";
        String digits = "0123456789";
        String special = "!@#$%^&*()_+-=[]{}|;:,.<>?";
        String all = upper + lower + digits + special;

        StringBuilder password = new StringBuilder();
        
        // Ensure at least one character from each category
        password.append(upper.charAt(random.nextInt(upper.length())));
        password.append(lower.charAt(random.nextInt(lower.length())));
        password.append(digits.charAt(random.nextInt(digits.length())));
        password.append(special.charAt(random.nextInt(special.length())));
        
        // Fill the rest with random characters
        for (int i = 4; i < length; i++) {
            password.append(all.charAt(random.nextInt(all.length())));
        }
        
        // Shuffle the password
        return shuffleString(password.toString());
    }

    private String shuffleString(String input) {
        char[] characters = input.toCharArray();
        for (int i = 0; i < characters.length; i++) {
            int randomIndex = random.nextInt(characters.length);
            char temp = characters[i];
            characters[i] = characters[randomIndex];
            characters[randomIndex] = temp;
        }
        return new String(characters);
    }

    /**
     * Check if password is in common passwords list (basic check)
     */
    public boolean isCommonPassword(String password) {
        if (password == null) return false;
        
        String[] commonPasswords = {
            "password", "123456", "12345678", "1234", "qwerty", 
            "admin", "welcome", "password123", "123456789",
            "12345", "1234567", "1234567890", "abc123", "letmein"
        };
        
        String lowerPassword = password.toLowerCase();
        for (String common : commonPasswords) {
            if (lowerPassword.equals(common) || lowerPassword.contains(common)) {
                return true;
            }
        }
        
        return false;
    }

    /**
     * Generate a random reset token
     */
    public String generateResetToken() {
        byte[] bytes = new byte[32];
        random.nextBytes(bytes);
        return bytesToHex(bytes);
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }
}