package Controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Model.Calendar.ACalendar;

// Concrete command for printing events
public class PrintCommand implements ICommand {

  public PrintCommand() {
//    this.calendar = calendar;
  }

  void commandParser(String commandArgs, ACalendar calendar) {
    String startDate = null;
    String endDate = null;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    String regex = "^events (?:from \"(\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2})\" to \"(\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2})\"|on \"(\\d{4}-\\d{2}-\\d{2})\")$";
    Pattern pattern = Pattern.compile(regex);
    Matcher matcher = pattern.matcher(commandArgs);
    if (matcher.matches()) {
      startDate = matcher.group(1) != null ? matcher.group(1) : matcher.group(3); // Use singleDate as startDate
      endDate = matcher.group(2); // Captures endDateTime if present

      System.out.println("Valid Command:");
      System.out.println("Start Date: " + startDate);
      if (endDate != null) {
        System.out.println("End Date: " + endDate);
      }
    } else {
      System.out.println("Invalid format: " + commandArgs);
    }
    startDate = startDate.replace("T", " ");
    LocalDateTime localStart = LocalDateTime.parse(startDate, formatter);

    endDate = endDate.replace("T", " ");
    LocalDateTime localEnd = LocalDateTime.parse(endDate, formatter);

    int year = localStart.getYear();
    int month = localStart.getMonthValue();
    int day = localStart.getDayOfMonth();

    calendar.printFromToEvents(localStart,localEnd);

  }

  @Override
  public void execute(String commandArgs, ACalendar calendar) {
    // Print events for a given date
    commandParser(commandArgs, calendar);
    System.out.println("Executing print event command");
  }
}