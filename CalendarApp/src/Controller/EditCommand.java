package controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import model.ICalendar;
import utils.DateUtils;

/**
 * This class implements the ICommand interface and has methods for parsing
 * and running edit commands.
 */
public class EditCommand implements ICommand {
  private String eventName;
  private String start, end;
  private String property, newValue;
  private LocalDateTime localStartTime, localEndTime;
  private Map<String, Object> metaData = new HashMap<>();
  private Pattern pattern;
  private Matcher matcher;

  private static final String regex = "^event(?:s)?\\s+" +
          "(?<property>\\S+)\\s+" +
          "\"(?<eventName>.*?)\"\\s+" +
          "(?:" +
          "from\\s+(?<start>\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}(?::\\d{2})?)\\s+" +
          "(?:to\\s+(?<end>\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}(?::\\d{2})?)\\s+)?" +
          "with\\s+\"(?<newValue>.*?)\"" +
          "|" +
          "\"(?<newValue2>.*?)\"" +
          ")$";

  /**
   * This method constructs an EditCommand object and initializes the metadata map.
   */
  public EditCommand() {
    metaData = new HashMap<>();
  }

  /**
   * Parses the command to get the details about the event to be edited.
   *
   * @param commandArgs the command arguments in String format.
   * @throws Exception if there's an error while parsing the fields.
   */
  private void commandParser(String commandArgs) throws Exception {
    pattern = Pattern.compile(regex);
    matcher = pattern.matcher(commandArgs);
    if (!matcher.matches()) {
      throw new Exception("Invalid Command " + diagnoseCommandError(commandArgs));
    }
    property = matcher.group("property");
    eventName = matcher.group("eventName").trim();
    start = matcher.group("start");
    end = matcher.group("end");
    newValue = matcher.group("newValue") != null
            ? matcher.group("newValue").trim()
            : matcher.group("newValue2").trim();
    if (start != null) {
      start = DateUtils.removeTinDateTime(start);
      localStartTime = DateUtils.stringToLocalDateTime(start);
    }
    if (end != null) {
      end = DateUtils.removeTinDateTime(end);
      localEndTime = DateUtils.stringToLocalDateTime(end);
    }
    addValuesInMetaDataObject();
  }

  /**
   * This method adds the parsed values to the metadata map.
   */
  private void addValuesInMetaDataObject() {
    metaData.put("property", property);
    metaData.put("eventName", eventName);
    metaData.put("newValue", newValue);
    metaData.put("endTime", end);
    metaData.put("startTime", start);
    metaData.put("localStartTime", localStartTime);
    metaData.put("localEndTime", localEndTime);
  }

  /**
   * This method checks if a given string contains only uppercase letters.
   *
   * @param str the string we need to check.
   * @return true if all letters in the string are uppercase, else false.
   */
  private boolean isStringUppercase(String str) {
    for (int i = 0; i < str.length(); i++) {
      if (Character.isLetter(str.charAt(i)) && !Character.isUpperCase(str.charAt(i))) {
        return false;
      }
    }
    return true;
  }

  /**
   * This method validates and checks the errors in the command format and provides error messages
   * for the respective cases.
   *
   * @param command the command we need to validate.
   * @return a string representing the error message.
   */
  private String diagnoseCommandError(String command) {
    if (isStringUppercase(command)) {
      return "Command should be lower case.";
    }
    if (!command.startsWith("event") && !command.startsWith("events")) {
      return "Should start with 'edit event(s)' or it is Missing 'edit event(s)'";
    }
    if (!command.matches("^event(?:s)?\\s+\\S+\\s+\".*?\".*")) {
      return "Missing 'eventName' or Property is missing or incorrectly placed";
    }
    boolean hasFrom = command.contains("from");
    boolean hasTo = command.contains("to");
    boolean hasWith = command.contains("with");
    if (hasWith && !hasFrom) {
      return ("Missing From");
    }
    if (!hasTo && hasFrom) {
      return ("Missing To");
    }
    if (!hasWith) {
      return ("Missing With");
    }
    if (hasWith) {
      if (!command.matches(".*with\\s+\".*?\".*")) {
        return ("Missing or incorrect 'newValue' format (Expected: with \"NewValue\")");
      }
    } else if (!command.matches(".*\".*?\"$")) {
      return ("Missing 'newValue'");
    }
    return "Invalid command: Does not match expected format.";
  }

  /**
   * This method runs the edit event command by parsing the arguments and calling
   * the calendar's editEvent method with the updated details.
   *
   * @param commandArgs the command arguments in String format.
   * @param calendar    the ICalendar object where the event will be edited.
   * @throws Exception if at all there's an error while running the edit event program.
   */
  @Override
  public void execute(String commandArgs, ICalendar calendar) throws Exception {
    commandParser(commandArgs);
    calendar.editEvent(metaData);
  }
}