package utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Helper class for date operations.
 * Object cannot be created for this class.
 * provides methods to parse string as localdatetime.
 */
public class DateUtils {

  private static final DateTimeFormatter dateTimeformat =
          DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
  private static final DateTimeFormatter csvDateFormat =
          DateTimeFormatter.ofPattern("MM/dd/yyyy");
  private static final DateTimeFormatter csvTimeFormat =
          DateTimeFormatter.ofPattern("hh:mm a");

  public DateUtils() {
    throw new UnsupportedOperationException("Date Utility cannot be instantiated");
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

  public static boolean isValidZoneId(String timezone) {
    try {
      ZoneId.of(timezone);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  public static LocalDate stringToLocalDate(String date) {
    return LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE);
  }
  public static LocalDateTime changeTimeZone(LocalDateTime date, ZoneId sourceZoneId, ZoneId targetZoneId) {
    ZonedDateTime sourceZonedDateTime = date.atZone(sourceZoneId);
    return sourceZonedDateTime.withZoneSameInstant(targetZoneId).toLocalDateTime();
  }
}
