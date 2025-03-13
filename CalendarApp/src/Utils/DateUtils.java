package Utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtils {

  private static final DateTimeFormatter dateTimeformat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
  private static final DateTimeFormatter csvDateFormat = DateTimeFormatter.ofPattern("MM/dd/yyyy");
  private static final DateTimeFormatter csvTimeFormat = DateTimeFormatter.ofPattern("hh:mm a");

  private DateUtils() {
    throw new UnsupportedOperationException("Utility class should not be instantiated");
  }

  public static LocalDateTime stringToLocalDateTime(String date) {
    return LocalDateTime.parse(date, dateTimeformat);
  }

  public static String getFinalStartDateFromOndate(String onDate, String onTime) {
    String date;
    if (onTime != null) {
      date = onDate + " " + onTime;
    } else {
      date = onDate + " " + "00:00";
    }
    return date;
  }

  public static String removeTinDateTime(String date) {
    if (date != null) {
      date = date.replace("T", " ");
    }
    return date;
  }

  public static String changeDateToDateTime(String date) {
    date = date + " " + "00:00";
    return date;
  }

  public static LocalDateTime pareStringToLocalDateTime(String date) {
    String parsedDate = removeTinDateTime(date);
    return stringToLocalDateTime(parsedDate);
  }

  public static String getCsvDate(LocalDateTime date) {
    return (date != null) ?
            date.toLocalDate().format(csvDateFormat) : "";
  }

  public static String getCsvTime(LocalDateTime date) {
    return (date.toLocalDate() != null) ?
            date.toLocalTime().format(csvTimeFormat) : "";
  }
}
