package Model.Event;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import Model.Calendar.ACalendar;

public class RecurringEvent extends AEvent {

  private LocalDateTime getRecurringEventEndDate(LocalDateTime start, EventMetaDetails allMetaDeta){
    String finalUntilDateTime= allMetaDeta.getAddUntilDateTime();
    LocalDateTime until;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    if (finalUntilDateTime != null) {
      until = LocalDateTime.parse(finalUntilDateTime, formatter);
    } else {
      int num = Integer.valueOf( allMetaDeta.getForTimes()) * 7;
      until = start.plusDays(num);
    }
    return until;
  }
  public void addEvent(EventDetails event, ACalendar calendar, EventMetaDetails allMetaDeta) {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    LocalDateTime start = event.getStartDate();
    LocalDateTime end = event.getEndDate();

    LocalDateTime currDate = start;
    LocalDateTime until = getRecurringEventEndDate(start,allMetaDeta);

    while (!currDate.isAfter(until)) {
      DayOfWeek day = currDate.getDayOfWeek();
      char d = getDayAbbreviation(day.name());

      if (allMetaDeta.getWeekdays().indexOf(d) != -1) {

        event.setStartDate(currDate);
        event.setEndDate(end);

        CalendarEvent eventToBeAdded = new CalendarEvent(event, currDate, end);
        int year = currDate.getYear();
        int month = currDate.getMonthValue();
        int day_ = currDate.getDayOfMonth();
        System.out.println("adding day " + day_);
        calendar.createEvent(year, month, day_, eventToBeAdded);
      }

      currDate = currDate.plusDays(1);
      end = end.plusDays(1);
    }
  }

  @Override
  public void breakEvents() {

  }

  public char getDayAbbreviation(String day) {
    switch (day) {
      case "MONDAY":
        return 'M';
      case "TUESDAY":
        return 'T';
      case "WEDNESDAY":
        return 'W';
      case "THURSDAY":
        return 'R';
      case "FRIDAY":
        return 'F';
      case "SATURDAY":
        return 'S';
      case "SUNDAY":
        return 'U';
      default:
        return '?';
    }
  }

}
