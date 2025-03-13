package Utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * This class provides utility methods for handling date and time operations.
 * It also has methods for parsing strings to LocalDateTime objects and formatting date strings.
 */
public class DateUtils {

  private static final DateTimeFormatter dateTimeformat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
  private static final DateTimeFormatter csvDateFormat = DateTimeFormatter.ofPattern("MM/dd/yyyy");
  private static final DateTimeFormatter csvTimeFormat = DateTimeFormatter.ofPattern("hh:mm a");

  private DateUtils() {
    throw new UnsupportedOperationException("Utility class should not be instantiated");
  }

  /**
   * Converts a date string to a LocalDateTime object using the predefined formatter.
   *
   * @param date The date string to parse.
   * @return A LocalDateTime object representing the parsed date.
   */
  public static LocalDateTime stringToLocalDateTime(String date) {
    return LocalDateTime.parse(date, dateTimeformat);
  }

  /**
   * Constructs a complete date string by combining an "on date" with an optional "on time".
   * If no time is provided, it defaults to "00:00:00".
   *
   * @param onDate The date part of the event.
   * @param onTime The time part of the event, or null for default time.
   * @return A complete date string in the format "yyyy-MM-dd HH:mm:ss".
   */
  public static String getFinalStartDateFromOndate(String onDate, String onTime) {
    String date;
    if (onTime != null) {
      date = onDate + " " + onTime;
    } else {
      date = onDate + " " + "00:00";
    }
    return date;
  }

  /**
   * Removes the 'T' character from a date string, replacing it with a space.
   * This is useful for converting ISO 8601 date strings to a format that can be parsed by the formatter.
   *
   * @param date The date string to modify.
   * @return The modified date string with 'T' replaced by a space.
   */
  public static String removeTinDateTime(String date) {
    if (date != null) {
      date = date.replace("T", " ");
    }
    return date;
  }

  /**
   * Converts a date-only string to a date-time string by appending " 00:00".
   *
   * @param date The date-only string to convert.
   * @return The date-time string with " 00:00" appended.
   */
  public static String changeDateToDateTime(String date) {
    date = date + " " + "00:00";
    return date;
  }

  /**
   * Parses a date string to a LocalDateTime object, first removing any 'T' characters.
   *
   * @param date The date string to parse.
   * @return A LocalDateTime object representing the parsed date.
   */
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
