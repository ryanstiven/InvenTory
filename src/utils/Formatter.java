package utils;

import java.text.NumberFormat;
import java.util.Locale;

public class Formatter {
    
    public static String formatPrice(double price) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("es", "ES"));
        return formatter.format(price);
    }

    public static String formatPlate(String plate) {
        return plate.toUpperCase().trim();
    }

    public static String capitalizeFirst(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }
}
