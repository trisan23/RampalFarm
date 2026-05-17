package com.rampal.farm2home.utilities;

import jakarta.servlet.http.Part;

import java.nio.file.Paths;
import java.util.regex.Pattern;

public class ValidationUtil {

    private ValidationUtil() {
    }

    public static boolean isNullOrEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }

    public static boolean isValidName(String value) {
        return value != null && value.matches("^[A-Za-z][A-Za-z\\s]{1,99}$");
    }

    public static boolean isValidEmail(String email) {
        String emailRegex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        return email != null && Pattern.matches(emailRegex, email);
    }

    public static boolean isValidPhoneNumber(String number) {
        return number != null && number.matches("^98\\d{8}$");
    }

    public static boolean isValidPassword(String password) {
        String passwordRegex = "^(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
        return password != null && password.matches(passwordRegex);
    }

    public static boolean isValidImageExtension(Part imagePart) {
        if (imagePart == null || isNullOrEmpty(imagePart.getSubmittedFileName())) {
            return false;
        }
        String fileName = imagePart.getSubmittedFileName().toLowerCase();
        return fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") || fileName.endsWith(".png") || fileName.endsWith(".gif");
    }

    public static boolean doPasswordsMatch(String password, String retypePassword) {
        return password != null && password.equals(retypePassword);
    }

    public static boolean isPositiveWholeNumber(String value) {
        return value != null && value.matches("^\\d+$");
    }

    public static boolean isPositiveDecimal(String value) {
        return value != null && value.matches("^\\d+(\\.\\d{1,2})?$");
    }

    public static String sanitizeFileName(String fileName) {
        if (isNullOrEmpty(fileName)) {
            return "";
        }
        return Paths.get(fileName).getFileName().toString().replaceAll("[^A-Za-z0-9._-]", "_");
    }
}
