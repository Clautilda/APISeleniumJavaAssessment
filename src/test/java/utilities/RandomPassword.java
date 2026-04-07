package utilities;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// Utility class to generate a random strong password
public class RandomPassword {

    public static String password = generateStrongPassword(10);

    // Helper method to generate a strong password
    public static String generateStrongPassword(int length) {
        if (length < 4) length = 4; // minimum to include all character types

        List<Character> passwordChars = new ArrayList<>();

        // Add one random character of each type
        passwordChars.add(RandomStringUtils.randomAlphabetic(1).toUpperCase().charAt(0)); // uppercase
        passwordChars.add(RandomStringUtils.randomAlphabetic(1).toLowerCase().charAt(0)); // lowercase
        passwordChars.add(RandomStringUtils.randomNumeric(1).charAt(0)); // digit
        passwordChars.add(RandomStringUtils.random(1, "!@#$%^&*()_+[]{}<>?".toCharArray()).charAt(0)); // special char

        // Fill remaining characters randomly
        int remainingLength = length - 4;
        for (int i = 0; i < remainingLength; i++) {
            String allChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()_+[]{}<>?";
            passwordChars.add(allChars.charAt((int)(Math.random() * allChars.length())));
        }

        // Shuffle to avoid predictable pattern
        Collections.shuffle(passwordChars);

        // Convert to string
        StringBuilder password = new StringBuilder();
        for (char c : passwordChars) password.append(c);

        return password.toString();
    }
}
