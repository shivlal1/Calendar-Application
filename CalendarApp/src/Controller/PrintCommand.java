package controller;


import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import model.ICalendar;
import utils.DateUtils;
import view.ConsoleView;

/**
 * This class implements the ICommand interface and is responsible for
 * printing events within a specified time range from a calendar.
 */
public class PrintCommand implements ICommand {
  private LocalDateTime localStart;
  private LocalDateTime localEnd;
  private Map<String, Object> metaData;


  private static final String regex = "^events (?:" +
          "from \"(\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2})\" " +
          "to \"(\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2})\"|" +
          "on \"(\\d{4}-\\d{2}-\\d{2})\")$";

  /**
   * This method creates a new object and initializes the metadata map.
   */
  public PrintCommand() {
    metaData = new HashMap<>();
  }

  /**
   * This method validates errors in the print command format and provides specific error messages.
   *
   * @param command the command string to diagnose.
   * @return a string describing the error in the command format.
   */
  private String diagnoseCommandError(String command) {
    if (!command.startsWith("events")) {
      return "Missing events";
    }
    boolean hasFrom = command.contains("from");
    boolean hasTo = command.contains("to");
    boolean hasOn = command.contains("on");
    if (hasFrom && !hasTo) {
      return "Missing To";
    }
    if (!hasFrom && !hasOn) {
      return ("Missing From/On");
    }
    if (!hasFrom && hasTo) {
      return "Missing From";
    }
    if (!hasFrom && !hasTo && !hasOn) {
      return "Missing On";
    }
    return "Invalid command: Does not match expected format.";
  }

  /**
   * This method parses the command arguments to extract the date range for printing events.
   *
   * @param commandArgs The command arguments as a string.
   * @throws Exception if the command format is invalid or missing required fields.
   */
  private void commandParser(String commandArgs) throws Exception {
    Pattern pattern;
    Matcher matcher;
    String startDate;
    String endDate;
    pattern = Pattern.compile(regex);
    matcher = pattern.matcher(commandArgs);
    if (!matcher.matches()) {
      throw new Exception("Invalid Command " + diagnoseCommandError(commandArgs));
    }


    startDate = matcher.group(1) != null ? matcher.group(1) : matcher.group(3);
    endDate = matcher.group(2);
    startDate = DateUtils.removeTinDateTime(startDate);
    endDate = DateUtils.removeTinDateTime(endDate);
    if (startDate.indexOf(":") == -1) {
      startDate = DateUtils.changeDateToDateTime(startDate);
    }
    localStart = DateUtils.stringToLocalDateTime(startDate);
    if (endDate != null) {
      localEnd = DateUtils.stringToLocalDateTime(endDate);
    }
    addValuesInMetaDataObject();
  }

  /**
   * This method adds the parsed value to the metadata map for event filtering.
   */
  private void addValuesInMetaDataObject() {
    metaData.put("localStartTime", localStart);
    metaData.put("localEndTime", localEnd);
  }

  /**
   * This method handles printing events based on the parsed metadata.
   *
   * @param calendar The ICalendar object containing events to be printed.
   */
  private void printCommandUtil(ICalendar calendar) {
    List<Map<String, Object>> eventDetails = calendar.getMatchingEvents(metaData);
    ConsoleView v = new ConsoleView();
    v.viewEvents(eventDetails);
  }

  /**
   * This method executes the print command by parsing the arguments and displaying
   * events within the specified time range.
   *
   * @param commandArgs the command arguments as a string, including date range.
   * @param calendar    the ICalendar object containing events to be printed.
   * @throws Exception if there's an error in parsing or printing events.
   */
  @Override
  public void execute(String commandArgs, ICalendar calendar) throws Exception {
    commandParser(commandArgs);
    printCommandUtil(calendar);
  }
}