package Controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Model.Calendar.ACalendar;
import Model.Event.AdditionalEventInformation;
import Model.Event.EventInformation;
import Model.Event.RecurringEvent;
import Model.Event.SimpleEvent;

public class CreateCommand implements ICommand {

  String subject;
  String finalStartDate;
  String finalEndDate;
  boolean isAllDayEvent;
  boolean isRecurring;
  String weekdays;
  String forTimes;
  String finalUntilDateTime; 
  public CreateCommand() {
  }

  void commandParser(String commandArgs, ACalendar calendar) {
    String regex = "^event\\s+(--autoDecline\\s+)?\"(.*?)\"\\s+(?=from\\s+|on\\s+)(?:" +
            "(?:from\\s+(\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2})\\s+to\\s+(\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}))" +
            "|" +
            "(?:on\\s+(\\d{4}-\\d{2}-\\d{2})(?:T(\\d{2}:\\d{2}:\\d{2}))?)" +
            ")" +
            "(?:\\s+repeats\\s+([MTWRFSU]+)\\s+(?:(?:for\\s+(\\d+)\\s+times)|(?:until\\s+(\\d{4}-\\d{2}-\\d{2}(?:T\\d{2}:\\d{2}:\\d{2})?))))?$";
    Pattern pattern = Pattern.compile(regex);
    Matcher matcher = pattern.matcher(commandArgs);
    if (matcher.matches()) {
      // Group 1: Optional autoDecline flag.
      String autoDeclineStr = matcher.group(1);
      boolean autoDecline = autoDeclineStr != null && !autoDeclineStr.trim().isEmpty();

      // Group 2: Event name.
      subject = matcher.group(2).trim();

      // "from-to" segment: Groups 3 and 4.
      String startDateTime = matcher.group(3);
      String endDateTime = matcher.group(4);

//      System.out.println("startDateTime val "+startDateTime);
      if(startDateTime!=null){
        startDateTime = startDateTime.replace("T", " ");
      }
      if(endDateTime!=null){
        endDateTime = endDateTime.replace("T", " ");
      }

      // "on" segment: Group 5 for date, Group 6 for time (if any).
      String onDate = matcher.group(5);
      String onTime = matcher.group(6);

      // Recurring clause (if any):
      weekdays = matcher.group(7);
      forTimes = matcher.group(8);
      String untilDateTime = matcher.group(9);

      // Determine if the event is recurring.
      isRecurring = (weekdays != null);

      // Determine if it's an all-day event.
      // New logic: if the command uses "on", then it's considered an all-day event regardless of time.
      isAllDayEvent = (onDate != null);

//      System.out.println("  AutoDecline: " + autoDecline);
//      System.out.println("  Event Name: " + subject);

      if (startDateTime != null && endDateTime != null) {
        finalStartDate = startDateTime;
        finalEndDate = endDateTime;
//        System.out.println("  Start DateTime: " + startDateTime);
//        System.out.println("  End DateTime: " + endDateTime);
//        System.out.println("  All Day Event: false");
      }
      else if (onDate != null) {
        if (onTime != null) {
//          System.out.println("  On DateTime: " + onDate + " " + onTime);
          finalStartDate = onDate + " " + onTime;
        } else {
          finalStartDate = onDate + " " + "00:00:00";

//          System.out.println("  On DateTime 2: " + onDate);
        }
        System.out.println("  All Day Event: " + isAllDayEvent);
      }
      if (isRecurring) {
//        System.out.println("  Repeats on: " + weekdays);
        if (forTimes != null) {
//          System.out.println("  Repeats for: " + forTimes + " times");
        } else if (untilDateTime != null) {
          finalUntilDateTime = untilDateTime;
          if(finalUntilDateTime!=null){
            if(finalUntilDateTime.indexOf('T')!=-1){
              finalUntilDateTime = finalUntilDateTime.replace("T", " ");
            }else{
              finalUntilDateTime = finalUntilDateTime+" "+"00:00:00";
            }
          }
//          System.out.println("  Repeats until: " + untilDateTime);
        }
      }
    } else {
      System.out.println("  Command did not match the pattern.");
    }
//    System.out.println("----------");


    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


//    System.out.println("Formatted  " + finalStartDate + " " + finalEndDate);
    System.out.println("Final start date: " + finalStartDate);
    LocalDateTime localStartDateTime = LocalDateTime.parse(finalStartDate, formatter);
    LocalDateTime localEndDateTime = null;

    if(finalEndDate!=null){
      localEndDateTime = LocalDateTime.parse(finalEndDate, formatter);
    }


    if(isAllDayEvent){
      LocalDate currentDay = localStartDateTime.toLocalDate();
      localEndDateTime = currentDay.atTime(23, 59);
    }

    AdditionalEventInformation a = new AdditionalEventInformation("", "", localEndDateTime, false);
    EventInformation info = new EventInformation(subject, localStartDateTime, a);
    if (!isRecurring) {
      SimpleEvent event = new SimpleEvent();
      event.addEvent(info,calendar,isAllDayEvent);
    }
    else {
      RecurringEvent event = new RecurringEvent();

      LocalDateTime until;

      if(finalUntilDateTime!=null){
        until = LocalDateTime.parse(finalUntilDateTime, formatter);
      }else{
        int num =  Integer.valueOf(forTimes) * 7;
        until = localStartDateTime.plusDays(num);
      }

      event.addEvent(info,calendar,weekdays,forTimes,until);
    }
  }

  @Override
  public void execute(String commandArgs,ACalendar calendar) {

    commandParser(commandArgs,calendar);
  }
}
