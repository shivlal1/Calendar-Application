package Controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Model.Calendar.ACalendar;
import Model.Event.AEvent;
import Model.Event.EventDetails;
import Model.Event.EventFactory;
import Model.Event.EventMetaDetails;
import Model.Utils.DateUtils;

public class CreateCommand implements ICommand {

  private String subject;
  private String finalStartDate;
  private String finalEndDate;
  private boolean isAllDayEvent;
  private boolean isRecurring;
  private String weekdays;
  private String forTimes;
  private String finalUntilDateTime;
  LocalDateTime localStartDateTime;
  LocalDateTime localEndDateTime = null;
  EventMetaDetails.EventMetaDetailsBuilder metaDeta;

  private static final String regex = "^event\\s+(--autoDecline\\s+)?\"(.*?)\"\\s+(?=from\\s+|on\\s+)(?:" +
          "(?:from\\s+(\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2})\\s+to\\s+(\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}))" +
          "|" +
          "(?:on\\s+(\\d{4}-\\d{2}-\\d{2})(?:T(\\d{2}:\\d{2}:\\d{2}))?)" +
          ")" +
          "(?:\\s+repeats\\s+([MTWRFSU]+)\\s+(?:(?:for\\s+(\\d+)\\s+times)|(?:until\\s+(\\d{4}-\\d{2}-\\d{2}(?:T\\d{2}:\\d{2}:\\d{2})?))))?$";


  public CreateCommand() {
  }

  private String removeTinDateTime(String date) {
    if (date != null) {
      date = date.replace("T", " ");
    }
    return date;
  }

  private String getFinalStartDateFromOndate(String onDate, String onTime) {
    if (onTime != null) {
      finalStartDate = onDate + " " + onTime;
    } else {
      finalStartDate = onDate + " " + "00:00:00";
    }
    return finalStartDate;
  }

  void commandParser(String commandArgs, ACalendar calendar) {

    metaDeta = new EventMetaDetails.EventMetaDetailsBuilder();

    Pattern pattern = Pattern.compile(regex);
    Matcher matcher = pattern.matcher(commandArgs);
    if (matcher.matches()) {

      String autoDeclineStr = matcher.group(1);
      boolean autoDecline = autoDeclineStr != null && !autoDeclineStr.trim().isEmpty();

      subject = matcher.group(2).trim();

      String startDateTime = removeTinDateTime(matcher.group(3));
      String endDateTime = removeTinDateTime(matcher.group(4));

      String onDate = matcher.group(5);
      String onTime = matcher.group(6);

      weekdays = matcher.group(7);
      metaDeta.addWeekdays(weekdays);

      forTimes = matcher.group(8);
      metaDeta.addForTimes(forTimes);

      String untilDateTime = matcher.group(9);

      isRecurring = (weekdays != null);
      metaDeta.addIsRecurring(isRecurring);

      isAllDayEvent = (onDate != null);
      metaDeta.addIsAllDay(isAllDayEvent);

      if (startDateTime != null && endDateTime != null) {
        finalStartDate = startDateTime;
        finalEndDate = endDateTime;
      } else if (onDate != null) {
        finalStartDate = getFinalStartDateFromOndate(onDate, onTime);
      }

      if (isRecurring) {
        processUntilTime(untilDateTime);
      }

    } else {
      System.out.println("  Command did not match the pattern.");
    }

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    DateUtils forFinalStartDate = new DateUtils(finalStartDate);
    localStartDateTime = forFinalStartDate.stringToLocalDateTime();

    if (finalEndDate != null) {
      localEndDateTime = LocalDateTime.parse(finalEndDate, formatter);
    }

    if (isAllDayEvent) {
      LocalDate currentDay = localStartDateTime.toLocalDate();
      localEndDateTime = currentDay.atTime(23, 59);
    }

    createEventUtil(calendar);
  }

  private void processUntilTime(String untilDateTime) {
    if (untilDateTime != null) {
      finalUntilDateTime = untilDateTime;
      if (finalUntilDateTime != null) {
        if (finalUntilDateTime.indexOf('T') != -1) {
          finalUntilDateTime = finalUntilDateTime.replace("T", " ");
        } else {
          finalUntilDateTime = finalUntilDateTime + " " + "00:00:00";
        }
      }
      metaDeta.addUntilDateTime(finalUntilDateTime);
    }
  }

  private void createEventUtil(ACalendar calendar) {
    EventDetails eventDetails = new EventDetails(subject, localStartDateTime,
            "", "", localEndDateTime, false);

    EventMetaDetails allMetaDeta = metaDeta.build();
    EventFactory factory = new EventFactory();

    AEvent event = factory.getEvent(eventDetails, allMetaDeta);
    event.pushEventToCalendar(eventDetails, calendar, allMetaDeta);
  }

  @Override
  public void execute(String commandArgs, ACalendar calendar) {
    commandParser(commandArgs, calendar);
  }
}
