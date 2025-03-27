package controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import model.ICalendar;
import model.ICalendarV2;
import utils.DateUtils;

/**
 * This class represents the copy command functionality which implements the ICommand interface.
 * It has the functionality to parse the command with the given regex and insert all the
 * details/features extracted into a hashmap containing the metadata.
 */
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

  private static final String regex = "^events?(?: (\\S+))? (?:on|between) " +
          "(\\d{4}-\\d{2}-\\d{2}(?:T\\d{2}:\\d{2})?)(?: and (\\d{4}-\\d{2}-\\d{2}))? " +
          "--target (\\S+) to (\\d{4}-\\d{2}-\\d{2}(?:T\\d{2}:\\d{2})?)$";

  /**
   * This method constructs the metaData hashmap and initializes the calendar manager.
   *
   * @param calendarManager the calendar manager object we need to initialize.
   */
  public CopyCommand(ICalendarManager calendarManager) {
    metaData = new HashMap<>();
    this.calendarManager = calendarManager;
  }

  /**
   * This method parses the provided command string using a regular expression and
   * extracts relevant details required for further processing of the events.
   *
   * @param commandArgs the input command string to be parsed.
   * @throws Exception if the command format doesn't match the expected pattern.
   */
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
    addValuesInMetaDataObject(commandArgs);
  }

  /**
   * This method stores the parsed command details into a metadata object
   * used during the execution of the copy operation. It identifies the type of copy operation
   * and extracts the fields accordingly.
   *
   * @param commandArgs the original command string to determine the type of command.
   */
  private void addValuesInMetaDataObject(String commandArgs) {
    if (eventName != null) {
      metaData.put("copyType", "eventsOnDateWithTime");
      metaData.put("eventName", eventName);
      metaData.put("onDateTime", DateUtils.pareStringToLocalDateTime(startString));
      metaData.put("newStartTime", DateUtils.pareStringToLocalDateTime(targetDateString));
    } else if (commandArgs.contains("between")) {
      metaData.put("copyType", "onBetweenEvents");
      metaData.put("fromDate", DateUtils.stringToLocalDate(startString));
      metaData.put("toDate", DateUtils.stringToLocalDate(endString));
      metaData.put("onDate", DateUtils.stringToLocalDate(targetDateString));
    } else {
      metaData.put("copyType", "eventsOnDate");
      metaData.put("onDateForCopy", DateUtils.stringToLocalDate(startString));
      metaData.put("toDateDestination", DateUtils.stringToLocalDate(targetDateString));
    }
  }

  /**
   * This method checks for common formatting errors in the provided command string and
   * returns a descriptive error message.
   *
   * @param command the command string to be analyzed.
   * @return an error message indicating the specific issue with the command.
   */
  private String diagnoseCommandError(String command) {
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

  /**
   * This method exectures the copy command, copying events from the current calendar to
   * the specified target calendar.
   *
   * @param commandArgs the arguments specifying the copy details.
   * @param calendar    the source calendar from where the events will be copied.
   * @throws Exception if the target calendar doesn't exist or the operation encounters an error.
   */
  @Override
  public void execute(String commandArgs, ICalendar calendar) throws Exception {
    commandParser(commandArgs);
    ICalendarV2 targetCal = calendarManager.getCalendarByName(targetCalendar);
    if (targetCal == null) {
      throw new Exception("Target calendar doesn't exists");
    }
    ICalendarV2 cal = (ICalendarV2) calendar;
    cal.copyToTargetCalendar(targetCal, metaData);

  }
}