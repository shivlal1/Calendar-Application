package controller;

import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

import model.CalendarV2;
import model.ICalendarV2;
import utils.DateUtils;

/**
 * This class represents a calendar manager that implements the ICalendarManager interface. It has
 * the functionalities to get and process the details of various calendar commands such as
 * create, edit and process the details of a new calendar.
 */
public class CalendarManager implements ICalendarManager {
  protected ICalendarV2 activeCalendar;
  protected Map<String, ICalendarV2> calendarMap;

  /**
   * This method constructs the calendarMap and sets the activeCalendar to null initially since
   * in the beginning we won't be having an active calendar.
   */
  public CalendarManager() {
    calendarMap = new HashMap<>();
    activeCalendar = null;
  }

  /**
   * This method splits the input string into two parts at the first space.
   *
   * @param s the string to be split.
   * @return an array with two string as first word and second word.
   */
  private String[] splitStringIntoTwo(String s) {
    int firstSpaceIndex = s.indexOf(" ");
    if (firstSpaceIndex == -1) {
      return new String[]{s, ""};
    }
    String firstPart = s.substring(0, firstSpaceIndex);
    String secondPart = s.substring(firstSpaceIndex + 1);
    return new String[]{firstPart, secondPart};
  }

  /**
   * This method processes command arguments up to the calendar name.
   *
   * @param commandArgs the full command argument string.
   * @return a string array containing the calendar name and remaining arguments.
   * @throws Exception if the input format is incorrect.
   */
  private String[] processTillCalendarName(String commandArgs) throws Exception {
    String[] splitStringResult = splitStringIntoTwo(commandArgs);
    // result[0] = "<calName>", result[1] = "--timezone area/location"
    String calendarName = splitStringResult[0];
    return new String[]{calendarName, splitStringResult[1]};
  }

  /**
   * This method extracts the calendar creation details from the command arguments.
   *
   * @param commandArgs the command arguments for creating a calendar.
   * @return an array containing the calendar name and timezone.
   * @throws Exception if the '--timezone' is missing.
   */
  private String[] getCalendarCreateDetails(String commandArgs) throws Exception {
    String[] splitStringResult = processTillCalendarName(commandArgs);
    String calendarName = splitStringResult[0];
    splitStringResult = splitStringIntoTwo(splitStringResult[1]);
    // result[0] = "--timezone", result[1] = "area/location"

    if (!splitStringResult[0].equals("--timezone")) {
      throw new Exception("Missing --timezone flag");
    }

    String calendarTimeZone = splitStringResult[1];
    return new String[]{calendarName, calendarTimeZone};
  }

  /**
   * This method extracts the details required for editing a calendar.
   *
   * @param commandArgs the command arguments for editing a calendar.
   * @return an array containing the calendar name, property name, and new property value.
   * @throws Exception if the '--property' flag is missing.
   */
  private String[] getCalendarEditDetails(String commandArgs) throws Exception {
    String[] splitStringResult = processTillCalendarName(commandArgs);
    String calendarName = splitStringResult[0];
    splitStringResult = splitStringIntoTwo(splitStringResult[1]);
    // result[0] = "--proeperty", result[1] = "<property-name> <new-property-value>"

    if (!splitStringResult[0].equals("--property")) {
      throw new Exception("--property missing");
    }

    splitStringResult = splitStringIntoTwo(splitStringResult[1]);
    String propertyName = splitStringResult[0];
    String propertyValue = splitStringResult[1];
    return new String[]{calendarName, propertyName, propertyValue};
  }

  /**
   * This method processes the creation of a new calendar with provided arguments.
   *
   * @param commandArgs this includes the calendar name and timezone.
   * @throws Exception if the timezone is invalid or missing.
   */
  private void processNewCalendarCreate(String commandArgs) throws Exception {
    String[] calendarDetails = getCalendarCreateDetails(commandArgs);
    String calendarName = calendarDetails[0];
    String calendarTimeZone = calendarDetails[1];

    if (DateUtils.isValidZoneId(calendarTimeZone)) {
      calendarMap.put(calendarName, new CalendarV2(ZoneId.of(calendarTimeZone)));
    } else {
      throw new Exception("Missing timeZone value");
    }
  }

  /**
   * This method processes the calendar edit commands based on the arguments given to it.
   *
   * @param commandArgs this includes the calendar name, property to edit, and new value.
   * @throws Exception if the calendar doesn't exist or the property is invalid.
   */
  private void processCalendarEdit(String commandArgs) throws Exception {
    String[] calendarDetails = getCalendarEditDetails(commandArgs);
    String calendarName = calendarDetails[0];
    String propertyName = calendarDetails[1];
    String propertyValue = calendarDetails[2];

    ICalendarV2 calendar = calendarMap.get(calendarName);
    if (calendar == null) {
      throw new Exception("No such calendar exists");
    }
    if (propertyValue.equals("")) {
      throw new Exception("property name/value is not present");
    }
    if (propertyName.equals("name")) {
      ICalendarV2 calReference = calendar;
      calendarMap.remove(calendarName);
      calendarMap.put(propertyValue, calReference);
      System.out.println("Name Change Success: " + propertyValue);

    } else if (propertyName.equals("timezone") && DateUtils.isValidZoneId(propertyValue)) {
      calendar.changeCalendarTimeZone(ZoneId.of(propertyValue));
    } else {
      throw new Exception("invalid calendarProperty timezone");
    }
  }

  /**
   * This method processes the commands for activating (using) a calendar.
   *
   * @param commandArgs Arguments including the calendar name to activate.
   * @throws Exception If the specified calendar does not exist.
   */
  private void processCalendarUse(String commandArgs) throws Exception {
    String[] splitStringResult = processTillCalendarName(commandArgs);
    String calendarName = splitStringResult[0];
    if (!calendarMap.containsKey(calendarName)) {
      throw new Exception("calendar Doesn't exists to Use");
    }
    activeCalendar = calendarMap.get(calendarName);
  }

  /**
   * Returns which is the active calendar.
   *
   * @return the current active calendar.
   */
  public ICalendarV2 getActiveCalendar() {
    return activeCalendar;
  }

  /**
   * Returns the calendar given the name of it.
   *
   * @param calendarName the calendar name provided to the method.
   * @return the calendar object from the given the calendar name.
   */
  @Override
  public ICalendarV2 getCalendarByName(String calendarName) {
    return calendarMap.get(calendarName);
  }

  /**
   * Executes calendar-related commands based on provided arguments.
   *
   * <p>This method processes commands for creating, editing, or selecting a calendar.
   * It identifies the command type from the input arguments and delegates processing
   * to the appropriate internal method.</p>
   *
   * @param commandArgs The command arguments specifying the operation and details.
   * @throws Exception If the command is invalid or processing encounters errors.
   */
  public void execute(String commandArgs) throws Exception {
    String[] splitStringResult = splitCommand(commandArgs);
    if (splitStringResult[0].equals("create calendar --name ")) {
      processNewCalendarCreate(splitStringResult[1]);
    } else if (splitStringResult[0].equals("edit calendar --name ")) {
      processCalendarEdit(splitStringResult[1]);
    } else if (splitStringResult[0].equals("use calendar --name ")) {
      processCalendarUse(splitStringResult[1]);
    } else {
      throw new Exception("start with commad+calendar+name");
    }
  }

  /**
   * This method splits a command into two parts that separates at the '--name' keyword.
   *
   * @param input the full command string.
   * @return a String array containing two parts, command type and name/value arguments.
   */
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
