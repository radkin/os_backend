package co.inajar.oursponsors.helpers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;

public class DateTimeConversion {

    public static LocalDate formatStringForLocalDate(String localDateString) throws DateTimeParseException {
        List<String> formatStrings = Arrays.asList("yyyy-MM-dd", "MM/dd/yyyy", "MM/d/yyyy", "M/d/yyyy", "M/dd/yyyy");
        for (var dateString : formatStrings) {
            try {
                var dateFormatter = DateTimeFormatter.ofPattern(dateString);
                return LocalDate.parse(localDateString, dateFormatter);
            } catch (DateTimeParseException e) {
                System.out.println("Cannot format LocalDate String:" + localDateString + e);
            }
        }
        return null;
    }
    public static LocalDateTime formatStringForLocalDateTime(String localDateTimeString) throws DateTimeParseException {
        List<String> formatStrings = Arrays.asList("yyyy-MM-dd HH:mm:ss Z", "yyyy-MM-dd'T'HH:mm:ssZ");
        for (var dateString : formatStrings) {
            try {
                var dateFormatter = DateTimeFormatter.ofPattern(dateString);
                return LocalDateTime.parse(localDateTimeString, dateFormatter);
            } catch (DateTimeParseException e) {
                System.out.println("Cannot format LocalDateTime String:" + localDateTimeString + e);
            }
        }
        return null;
    }
}
