package Controller.CommandHandler;

import java.time.LocalDate;
import java.time.LocalDateTime;

import Controller.MetaData.CreateCommandMetaDetails;
import Model.Calendar.ACalendar;
import Model.Utils.DateUtils;

public class CreateCommand extends AbstractCommand {

  private String subject, weekdays, forTimes;
  private CreateCommandMetaDetails.EventMetaDetailsBuilder metaDeta;
  private String startDateTime, endDateTime, untilDateTime;
  private String finalStartDate, finalEndDate, finalUntilDateTime;
  private LocalDateTime localStartDateTime, localEndDateTime;
  private String onDate, onTime;
  private CreateCommandMetaDetails allMetaDeta;
  private boolean isAllDayEvent;
  private boolean isRecurring;
  private boolean autoDecline;

  private static String regex = "^event\\s+(--autoDecline\\s+)?\"(.*?)\"\\s+(?=from\\s+|on\\s+)(?:" +
          "(?:from\\s+(\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2})\\s+to\\s+(\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}))" +
          "|" +
          "(?:on\\s+(\\d{4}-\\d{2}-\\d{2})(?:T(\\d{2}:\\d{2}))?)" +
          ")" +
          "(?:\\s+repeats\\s+([MTWRFSU]+)\\s+(?:(?:for\\s+(\\d+)\\s+times)|(?:until\\s+(\\d{4}-\\d{2}-\\d{2}(?:T\\d{2}:\\d{2})?))))?$";


  public void commandParser(String commandArgs) throws Exception {
    initRegexPatter(regex, commandArgs);
    metaDeta = new CreateCommandMetaDetails.EventMetaDetailsBuilder();

    if (!matcher.matches()) {
      throw new Exception("Create command not a match");
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

  private void addValuesInMetaDataObject() {
    metaDeta.addWeekdays(weekdays);
    metaDeta.addForTimes(forTimes);
    metaDeta.addIsRecurring(isRecurring);
    metaDeta.addIsAllDay(isAllDayEvent);
    metaDeta.addAutoDecline(autoDecline);
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
      metaDeta.addUntilDateTime(finalUntilDateTime);
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

  private void createEventUtil(ACalendar calendar) {
    allMetaDeta = metaDeta.build();
    calendar.createEvent(subject, localStartDateTime, localEndDateTime, allMetaDeta);

/*     EventDetails eventDetails = new EventDetails(subject, localStartDateTime,
            null, null, localEndDateTime, false);


    EventFactory factory = new EventFactory();

    AEvent event = factory.getEvent(subject,localStartDateTime, null,
            null,  localEndDateTime, false, allMetaDeta);
    event.pushEventToCalendar(calendar); */

  }

  private void createCommandProcess(String commandArgs, ACalendar calendar) throws Exception {
    commandParser(commandArgs);
    createEventUtil(calendar);
  }

  @Override
  public void execute(String commandArgs, ACalendar calendar) throws Exception {
    createCommandProcess(commandArgs, calendar);
  }
}
