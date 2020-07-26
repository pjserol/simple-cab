package utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validation {

    /**
     * This method is used to validate the input format of the date.
     *
     * @param input must match yyyy-mm-dd or yyyy-m-d
     * @return bool This return true, if the input is correct.
     */
    public static boolean verifyInputDate(String input) {
        Pattern p = Pattern.compile("^\\d{4}\\-(0?[1-9]|1[012])\\-(0?[1-9]|[12][0-9]|3[01])$");
        Matcher m = p.matcher(input);
        return m.matches();
    }
}
