package Controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import Model.Calendar.ACalendar;
import Model.Utils.DateUtils;

public class CreateCommand extends AbstractCommand {

  private String subject, weekdays, forTimes;
  private String startDateTime, endDateTime, untilDateTime;
  private String finalStartDate, finalEndDate, finalUntilDateTime;
  private LocalDateTime localStartDateTime, localEndDateTime;
  private String onDate, onTime;
  private boolean isAllDayEvent;
  private boolean isRecurring;
  private boolean autoDecline;
  private Map<String, Object> metaData = new HashMap<>();

  private static String regex = "^event\\s+(--autoDecline\\s+)?\"(.*?)\"\\s+(?=from\\s+|on\\s+)(?:" +
          "(?:from\\s+(\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2})\\s+to\\s+(\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}))|" +
          "(?:on\\s+(\\d{4}-\\d{2}-\\d{2})(?:T(\\d{2}:\\d{2}))?))" +
          "(?:\\s+repeats\\s+([MTWRFSU]+)\\s+(?:(?:for\\s+(\\d+)\\s+times)|" +
          "(?:until\\s+(\\d{4}-\\d{2}-\\d{2}(?:T\\d{2}:\\d{2})?))))?$";


  private void commandParser(String commandArgs) throws Exception {
    initRegexPatter(regex, commandArgs);
    if (!matcher.matches()) {
      throw new Exception("error :" + diagnoseCommandError(commandArgs));
    }
    String autoDeclineStr = matcher.group(1);
    autoDecline = autoDeclineStr != null && !autoDeclineStr.trim().isEmpty();
    subject = matcher.group(2).trim();
    startDateTime = DateUtils.removeTinDateTime(matcher.group(3));
    endDateTime = DateUtils.removeTinDateTime(matcher.group(4));
    onDate = matcher.group(5);
    onTime = matcher.group(6);
    weekdays = matcher.group(7);
    forTimes = matcher.group(8);
    untilDateTime = matcher.group(9);
    isRecurring = (weekdays != null);
    isAllDayEvent = (onDate != null);
    addValuesInMetaDataObject();
    processDateValues();
    setEndTime();
  }


  private String diagnoseCommandError(String command) {
    if (!command.startsWith("event")) {
      return "Must start with create event";
    }
    if (command.contains("--autoDecline")) {
      if (!command.matches("event\\s+--autoDecline\\s+\".*?\".*")) {
        return "--autoDecline must appear immediately after create event";
      }
    }
    if (!command.contains(" from ") && !command.contains(" on ")) {
      return "Missing 'from' or 'on' keyword.";
    }
    if (command.contains("from") && !command.contains(" to ")) {
      return "from must be followed by to date";
    }
    if (command.contains("repeats") && !command.contains("for") && !command.contains("until")) {
      return "'repeats' must be followed by 'for <N> times' or 'until <date>'.";
    }
    if (command.contains("for") && !command.matches(".*for \\d+ times.*")) {
      return "'for' must be followed by a valid number of times.";
    }
    if (command.contains("until") && !command.matches(".*until \\d{4}-\\d{2}-\\d{2}.*")) {
      return "'until' must be followed by a valid date.";
    }
    return "Does not match expected format.";
  }

  private void addValuesInMetaDataObject() {
    metaData.put("weekdays", weekdays);
    metaData.put("forTimes", forTimes);
    metaData.put("isRecurring", isRecurring);
    metaData.put("isAllDay", isAllDayEvent);
    metaData.put("autoDecline", autoDecline);
  }

  private void formatUntilTimeForRecurringEvent() {
    if (untilDateTime != null) {
      finalUntilDateTime = untilDateTime;
      if (finalUntilDateTime != null) {
        if (finalUntilDateTime.indexOf('T') != -1) {
          finalUntilDateTime = DateUtils.removeTinDateTime(finalUntilDateTime);
        } else {
          finalUntilDateTime = DateUtils.changeDateToDateTime(finalUntilDateTime);
        }
      }
      metaData.put("untilTime", finalUntilDateTime);
    }
  }

  private void setEndTime() {
    localStartDateTime = DateUtils.stringToLocalDateTime(finalStartDate);
    if (finalEndDate != null) {
      localEndDateTime = DateUtils.stringToLocalDateTime(finalEndDate);
    }
    if (isAllDayEvent) {
      setDatesForAllDayEvent();
    }
  }

  private void setDatesForAllDayEvent() {
    LocalDate currentDay = localStartDateTime.toLocalDate();
    localStartDateTime = currentDay.atStartOfDay();
    localEndDateTime = currentDay.atTime(23, 59);
  }

  private void processDateValues() {
    if (startDateTime != null && endDateTime != null) {
      finalStartDate = startDateTime;
      finalEndDate = endDateTime;
    } else if (onDate != null) {
      finalStartDate = DateUtils.getFinalStartDateFromOndate(onDate, onTime);
    }
    if (isRecurring) {
      formatUntilTimeForRecurringEvent();
    }
  }
  
  @Override
  public void execute(String commandArgs, ACalendar calendar) throws Exception {
    commandParser(commandArgs);
    calendar.createEvent(subject, localStartDateTime, localEndDateTime, metaData);
  }

}
