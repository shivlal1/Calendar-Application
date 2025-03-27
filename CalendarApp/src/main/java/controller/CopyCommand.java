package controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import model.ICalendar;
import model.ICalendarExtended;
import utils.DateUtils;

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
  private ICalendarManager calendarManager;

  private static final String regex = "^copy events?(?: (\\S+))? (?:on|between) " +
          "(\\d{4}-\\d{2}-\\d{2}(?:T\\d{2}:\\d{2})?)(?: and (\\d{4}-\\d{2}-\\d{2}))? " +
          "--target (\\S+) to (\\d{4}-\\d{2}-\\d{2}(?:T\\d{2}:\\d{2})?)$";


  public CopyCommand(ICalendarManager calendarManager) {
    metaData = new HashMap<>();
    this.calendarManager = calendarManager;
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
//
//    System.out.println("Parsed Command Successfully:");
//    System.out.println("  Event Name: " + eventName);
//    System.out.println("  Start Date/Time: " + startString);
//    System.out.println("  End Date/Time: " + endString);
//    System.out.println("  Target Calendar: " + targetCalendar);
//    System.out.println("  Destination Date/Time: " + targetDateString);
    addValuesInMetaDataObject(commandArgs);
  }


  private void addValuesInMetaDataObject(String commandArgs) {

    // metaData.put("endTime", endDateTime);
    metaData.put("startTime", startDateTime);
    metaData.put("targetCalendar", targetCalendar);
    //metaData.put("destinationDate", targetDateTime);

    if (eventName != null) {
      metaData.put("type", "eventsOnDateWithTime");
      metaData.put("eventName", eventName);
      metaData.put("onDateTime", DateUtils.pareStringToLocalDateTime(startString));
      metaData.put("newStartTime", DateUtils.pareStringToLocalDateTime(targetDateString));
    } else if (commandArgs.contains("between")) {
      metaData.put("type", "onBetweenEvents");
      metaData.put("fromDate", DateUtils.stringToLocalDate(startString));
      metaData.put("toDate", DateUtils.stringToLocalDate(endString));
      metaData.put("onDate", DateUtils.stringToLocalDate(targetDateString));
    } else {
      metaData.put("type", "eventsOnDate");
    }
  }

  private String diagnoseCommandError(String command) {
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

  @Override
  public void execute(String commandArgs, ICalendar calendar) throws Exception {
    commandParser(commandArgs);

    ICalendarExtended targetCal = calendarManager.getCalendarByName(targetCalendar);

    if (targetCal == null) {
      throw new Exception("Target calendar doesn't exists");
    }
    ICalendarExtended cal = (ICalendarExtended) calendar;
    cal.copyToTargetCalendar(targetCal, metaData);

  }
}