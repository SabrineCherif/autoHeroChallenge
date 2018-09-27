package qa.challenge.autohero.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;

import static java.util.Locale.GERMAN;

public class NumberUtils {

    public static BigDecimal parseToBigDecimal(final String amount) {
        final NumberFormat format = NumberFormat.getNumberInstance(GERMAN);
        if (format instanceof DecimalFormat) {
            ((DecimalFormat) format).setParseBigDecimal(true);
        }
        try {
            return (BigDecimal) format.parse(amount.replaceAll("[^\\d.,]", ""));
        } catch (ParseException e) {
            throw new IllegalArgumentException(e);
        }
    }
}