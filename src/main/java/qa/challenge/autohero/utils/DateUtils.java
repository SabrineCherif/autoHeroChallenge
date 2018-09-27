package qa.challenge.autohero.utils;

import java.time.Year;

import static java.lang.Integer.valueOf;

public class DateUtils {


    public static Year parseToYear(String stringYear) {
        String isoYear = stringYear.replaceAll("[^\\d.]", "");
        return Year.of(valueOf(isoYear));
    }

    public static boolean isYearAfterOrEqual(Year year, int yearToCompareAsInt) {
        Year yearToCompare = Year.of(yearToCompareAsInt);
        return year.isAfter(yearToCompare) || year.equals(yearToCompare);
    }
}
