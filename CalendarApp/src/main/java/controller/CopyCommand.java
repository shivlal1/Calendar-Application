package controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import model.ICalendar;

public class CopyCommand implements ICommand {

  private String eventName;
  private String startString;
  private String endString;
  private String targetCalendar;
  private String targetDateString;
  private LocalDateTime startDateTime;
  private LocalDateTime endDateTime;
  private LocalDateTime targetDateTime;
  private Map<String, Object> metaData;

  public CopyCommand() {
    metaData = new HashMap<>();
  }

  private static final String regex = "^copy events?(?: (\\S+))? (?:on|between) " +
          "(\\d{4}-\\d{2}-\\d{2}(?:T\\d{2}:\\d{2})?)(?: and (\\d{4}-\\d{2}-\\d{2}))? " +
          "--target (\\S+) to (\\d{4}-\\d{2}-\\d{2}(?:T\\d{2}:\\d{2})?)$";

  private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd['T'HH:mm]");

  private static String diagnoseCommandError(String command) {
    if (!command.startsWith("copy")) {
      return "Copy Missing";
    }
    if (!(command.contains(" on ") || command.contains(" between "))) {
      return "Missing on or between";
    }
    if (!command.contains("--target")) {
      return "Missing --target";
    }
    if (!command.contains(" to ")) {
      return "Missing destination date";
    }
    return "Missing Date/invalid format";
  }

  private void addValuesInMetaDataObject() {
    metaData.put("eventName", eventName);
    metaData.put("endTime", endDateTime);
    metaData.put("startTime", startDateTime);
    metaData.put("targetCalendar", targetCalendar);
    metaData.put("destinationDate", targetDateTime);
  }

  private void commandParser(String commandArgs) throws Exception {
    Pattern pattern = Pattern.compile(regex);
    Matcher matcher = pattern.matcher(commandArgs);
    if (!matcher.matches()) {
      throw new Exception("Invalid Command: " + diagnoseCommandError(commandArgs));
    }

    eventName = matcher.group(1);
    startString = matcher.group(2);
    endString = matcher.group(3);
    targetCalendar = matcher.group(4);
    targetDateString = matcher.group(5);

    startDateTime = parseDateTime(startString);
    endDateTime = endString != null ? parseDate(endString).atStartOfDay() : null;
    targetDateTime = parseDateTime(targetDateString);

    System.out.println("Parsed Command Successfully:");
    System.out.println("  Event Name: " + eventName);
    System.out.println("  Start Date/Time: " + startDateTime);
    System.out.println("  End Date/Time: " + endDateTime);
    System.out.println("  Target Calendar: " + targetCalendar);
    System.out.println("  Destination Date/Time: " + targetDateTime);
    addValuesInMetaDataObject();
  }

  private static LocalDateTime parseDateTime(String dateTime) {
    if (dateTime.contains("T")) {
      return LocalDateTime.parse(dateTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    } else {
      return LocalDate.parse(dateTime, DateTimeFormatter.ISO_LOCAL_DATE).atStartOfDay();
    }
  }

  private static LocalDate parseDate(String date) {
    return LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE);
  }

  @Override
  public void execute(String commandArgs, ICalendar calendar) throws Exception {
    commandParser(commandArgs);
//    calendar.copyEvent(metaData);
  }
}