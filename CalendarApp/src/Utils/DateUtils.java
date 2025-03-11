package Utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtils {

  private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

  public static LocalDateTime stringToLocalDateTime(String date) {
    return LocalDateTime.parse(date, formatter);
  }

  public static String getFinalStartDateFromOndate(String onDate, String onTime) {
    String date;
    if (onTime != null) {
      date = onDate + " " + onTime;
    } else {
      date = onDate + " " + "00:00:00";
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

}
