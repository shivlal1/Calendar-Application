package Controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Model.Calendar.ACalendar;

// Concrete command for editing an event
public class EditCommand implements ICommand {


  public EditCommand() {
  }

  void commandParser(String commandArgs, ACalendar calendar) {
    String eventName = null;
    String start = null;
    String end = null;
    String property = null;
    String newValue = null;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    String regex = "^event(?:s)?\\s+" +
            "(?<property>\\S+)\\s+" +
            "\"(?<eventName>.*?)\"\\s+" +
            "(?:" +
            "from\\s+(?<start>\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}(?::\\d{2})?)\\s+" +
            "(?:to\\s+(?<end>\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}(?::\\d{2})?)\\s+)?" +
            "with\\s+\"(?<newValue>.*?)\"" +
            "|" +
            "\"(?<newValue2>.*?)\"" +
            ")$";

    Pattern pattern = Pattern.compile(regex);
    System.out.println("Processing command: " + regex);
    Matcher matcher = pattern.matcher(commandArgs);
    if (matcher.matches()) {
      property = matcher.group("property");
      eventName = matcher.group("eventName").trim();
      // Option A: date/time clause fields
      start = matcher.group("start"); // may be null if not provided
      end = matcher.group("end");     // may be null if not provided
      // Option A new value, or Option B if date clause is absent.
      newValue = matcher.group("newValue") != null
              ? matcher.group("newValue").trim()
              : matcher.group("newValue2").trim();

      System.out.println("  Property: " + property);
      System.out.println("  Event Name: " + eventName);
      if (start != null) {
        // For display, you can replace the "T" with a space if desired.
        System.out.println("  Start DateTime: " + start.replace("T", " "));
      }
      if (end != null) {
        System.out.println("  End DateTime: " + end.replace("T", " "));
      }
      System.out.println("  New Property Value: " + newValue);
    } else {
      System.out.println("  Command did not match the pattern.");
    }
    System.out.println("----------");

    if (eventName != null && start != null && end == null) {
      start = start.replace("T", " ");

      LocalDateTime localStart = LocalDateTime.parse(start, formatter);

      int year = localStart.getYear();
      int month = localStart.getMonthValue();
      int day = localStart.getDayOfMonth();
      calendar.editEvent(year, month, day, localStart, eventName, newValue, property);


    }

  }

  @Override
  public void execute(String commandArgs, ACalendar calendar) {
    commandParser(commandArgs, calendar);
    System.out.println("Executing edit event command");
  }
}