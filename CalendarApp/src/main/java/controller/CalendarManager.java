package controller;

import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

import model.CalendarExtended;
import model.ICalendarExtended;
import utils.DateUtils;

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
    String[] result = splitStringIntoTwo(commandArgs); // result[0] = "<calName>", result[1] = "--timezone area/location"
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
    result = splitStringIntoTwo(result[1]);
    // result[0] = "--proeperty", result[1] = "<property-name> <new-property-value>"

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

    if (DateUtils.isValidZoneId(calendarTimeZone)) {
      calendarMap.put(calendarName, new CalendarExtended(ZoneId.of(calendarTimeZone)));
    } else {
      throw new Exception("Missing timeZone value");
    }
  }

  private void processCalendarEdit(String commandArgs) throws Exception {
    String calendarDetails[] = getCalendarEditDetails(commandArgs);
    String calendarName = calendarDetails[0];
    String propertyName = calendarDetails[1];
    String propertyValue = calendarDetails[2];

    ICalendarExtended calendar = calendarMap.get(calendarName);
    if (calendar == null) {
      throw new Exception("No such calendar exists");
    }
    if (propertyValue.equals("")) {
      throw new Exception("property name/value is not present");
    }
    if (propertyName.equals("name")) {
      ICalendarExtended calReference = calendar;
      calendarMap.remove(calendarName);
      calendarMap.put(propertyValue, calReference);

    } else if (propertyName.equals("timezone") && DateUtils.isValidZoneId(propertyValue)) {
      calendar.changeCalendarTimeZone(ZoneId.of(propertyValue));
    } else {
      throw new Exception("invalid calendarProperty timezone");
    }
  }

  private void processCalendarUse(String commandArgs) throws Exception {
    String[] result = processTillCalendarName(commandArgs);
    String calendarName = result[0];

    if (!calendarMap.containsKey(calendarName)) {
      throw new Exception("calendar Doesn't exists to Use");
    }
    activeCalendar = calendarMap.get(calendarName);
  }

  public ICalendarExtended getActiveCalendar() {
    return activeCalendar;
  }

  @Override
  public ICalendarExtended getCalendarByName(String calendarName) {
    return calendarMap.get(calendarName);
  }

  public void execute(String commandArgs) throws Exception {
    String[] result = splitCommand(commandArgs);

    if (result[0].equals("create calendar --name ")) {
      processNewCalendarCreate(result[1]);
    } else if (result[0].equals("edit calendar --name ")) {
      processCalendarEdit(result[1]);
    } else if (result[0].equals("use calendar --name ")) {
      processCalendarUse(result[1]);
    } else {
      throw new Exception("start with commad+calendar+name");
    }
  }

  private String[] splitCommand(String input) {
    int nameFlagIndex = input.indexOf("--name");

    if (nameFlagIndex != -1) {
      String firstPart = input.substring(0, nameFlagIndex + 7);
      String secondPart = input.substring(nameFlagIndex + 7).trim();
      return new String[]{firstPart, secondPart};
    } else {
      return null;
    }
  }

}
