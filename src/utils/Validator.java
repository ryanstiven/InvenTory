package utils;

public class Validator {
    
    public static boolean isValidPlate(String plate) {
        return plate != null && !plate.trim().isEmpty() && plate.matches("^[A-Z0-9-]+$");
    }

    public static boolean isValidYear(int year) {
        int currentYear = java.time.Year.now().getValue();
        return year >= 1900 && year <= currentYear + 1;
    }

    public static boolean isValidPrice(double price) {
        return price > 0;
    }

    public static boolean isValidString(String str) {
        return str != null && !str.trim().isEmpty();
    }

    public static boolean isValidContact(String contact) {
        return contact != null && contact.trim().length() >= 7;
    }

    public static boolean isNumeric(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
