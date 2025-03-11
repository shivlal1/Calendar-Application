package Controller;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Model.ACalendar;
import Model.Event;
import Utils.DateUtils;
import view.ConsoleView;

public class PrintCommand implements ICommand {
  private String startDate, endDate;
  private LocalDateTime localStart, localEnd;
  private Map<String, Object> metaData;
  private Pattern pattern;
  private Matcher matcher;

  private static final String regex = "^events (?:" +
          "from \"(\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2})\" " +
          "to \"(\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2})\"|" +
          "on \"(\\d{4}-\\d{2}-\\d{2})\")$";

  private String diagnoseCommandError(String command) {
    if (!command.startsWith("events"))
      return "Missing events";
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

  private void commandParser(String commandArgs) throws Exception {
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

  private void addValuesInMetaDataObject() {
    metaData.put("localStartTime", localStart);
    metaData.put("localEndTime", localEnd);
  }

  private void printCommandUtil(ACalendar calendar) {
    List<Event> eventDetails = calendar.getMatchingEvents(metaData);
    ConsoleView v = new ConsoleView();
    v.printInConsole(eventDetails);
  }

  @Override
  public void execute(String commandArgs, ACalendar calendar) throws Exception {
    commandParser(commandArgs);
    printCommandUtil(calendar);
  }
}