package controller;

import java.util.HashMap;
import java.util.Map;

import model.CalendarExtended;
import model.ICalendarExtended;

public class CalendarManager implements ICalendarManager {

  private ICalendarExtended activeCalendar;
  private Map<String, ICalendarExtended> calendarMap;

  public CalendarManager() {
    calendarMap = new HashMap<>();
    activeCalendar = null;
  }

  private String[] splitStringIntoTwo(String s) {
    int firstSpaceIndex = s.indexOf(" ");
    if (firstSpaceIndex == -1) {
      return new String[]{s, ""};
    }
    String firstPart = s.substring(0, firstSpaceIndex);
    String secondPart = s.substring(firstSpaceIndex + 1);
    return new String[]{firstPart, secondPart};
  }

  private String[] processTillCalendarName(String commandArgs) throws Exception {
    if (!commandArgs.startsWith("calendar --name")) {
      throw new Exception("Command must start with 'calendar --name'");
    }

    String[] result = splitStringIntoTwo(commandArgs); // result[0] = "calendar", result[1] = "--name <calName> --timezone area/location"
    result = splitStringIntoTwo(result[1]); // result[0] = "name", result[1] = "<calName> --timezone area/location"
    result = splitStringIntoTwo(result[1]); // result[0] = "<calName>", result[1] = "--timezone area/location"
    String calendarName = result[0];

    return new String[]{calendarName, result[1]};
  }

  private String[] getCalendarCreateDetails(String commandArgs) throws Exception {

    String[] result = processTillCalendarName(commandArgs);
    String calendarName = result[0];
    result = splitStringIntoTwo(result[1]); // result[0] = "--timezone", result[1] = "area/location"

    if (!result[0].equals("--timezone")) {
      throw new Exception("Missing --timezone flag");
    }

    String calendarTimeZone = result[1];
    return new String[]{calendarName, calendarTimeZone};
  }

  private String[] getCalendarEditDetails(String commandArgs) throws Exception {

    String[] result = processTillCalendarName(commandArgs);
    String calendarName = result[0];
    result = splitStringIntoTwo(result[1]); // result[0] = "--timezone", result[1] = "area/location"

    if (!result[0].equals("--property")) {
      throw new Exception("--property missing");
    }

    result = splitStringIntoTwo(result[1]);
    String propertyName = result[0];
    String propertyValue = result[1];

    return new String[]{calendarName, propertyName, propertyValue};
  }


  private void processNewCalendarCreate(String commandArgs) throws Exception {
    String calendarDetails[] = getCalendarCreateDetails(commandArgs);
    String calendarName = calendarDetails[0];
    String calendarTimeZone = calendarDetails[1];

    calendarMap.put(calendarName, new CalendarExtended(calendarName, calendarTimeZone));
  }

  private void processCalendarEdit(String commandArgs) throws Exception {
    String calendarDetails[] = getCalendarEditDetails(commandArgs);

    String calendarName = calendarDetails[0];
    String propertyName = calendarDetails[1];
    String propertyValue = calendarDetails[2];

    ICalendarExtended calendar = calendarMap.get(calendarName);
    calendar.changeCalendarProperty(propertyName, propertyValue);
  }

  private void processCalendarUse(String commandArgs) throws Exception {
    String[] result = processTillCalendarName(commandArgs);
    String calendarName = result[0];

    if (!calendarMap.containsKey(calendarName)) {
      throw new Exception("Calendar with name '" + calendarName + "' does not exist.");
    }

    activeCalendar = calendarMap.get(calendarName);
  }

  public ICalendarExtended getActiveCalendar() {
    return activeCalendar;
  }

  public void execute(String commandArgs) throws Exception {
    String[] result = splitStringIntoTwo(commandArgs);

    if (result[0].equals("create")) {
      processNewCalendarCreate(result[1]);
    } else if (result[0].equals("edit")) {
      processCalendarEdit(result[1]);
    } else if (result[0].equals("use")) {
      processCalendarUse(result[1]);
    }

  }

}
