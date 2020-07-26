package utils;

public class Helper {
    public static int tryParseInt(String value, int defaultValue) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public static String tryParseString(String value, String defaultValue) {
        return value == null || value == "" ? defaultValue : value;
    }

    public static Boolean tryParseBoolean(String value, Boolean defaultValue) {
        return value == null ? defaultValue : Boolean.parseBoolean(value);
    }
}
