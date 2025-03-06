package Model.Event;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import Model.Calendar.ACalendar;

public class SimpleEvent extends AEvent {

  public void addEvent(EventDetails info, ACalendar calendar, EventMetaDetails allMetaData) {

    LocalDateTime start = info.getStartDate();
    LocalDateTime end = info.getEndDate();

    if (!allMetaData.getIsAllDay()) {
      System.out.println("In if:");

      long daysBetween = ChronoUnit.DAYS.between(start, end) + 1;

      for (int i = 0; i < daysBetween; i++) {
        LocalDate currentDay = start.toLocalDate().plusDays(i);
        LocalDateTime segmentStart;
        LocalDateTime segmentEnd;

        if (i == 0) {
          segmentStart = start;
          segmentEnd = currentDay.atTime(23, 59);
        } else if (i == daysBetween - 1) {
          segmentStart = currentDay.atStartOfDay();
          segmentEnd = end;
        } else {
          segmentStart = currentDay.atStartOfDay();
          segmentEnd = currentDay.atTime(23, 59);
        }

        CalendarEvent event = new CalendarEvent(info, segmentStart, segmentEnd);

        int year = segmentStart.getYear();
        int month = segmentStart.getMonthValue();
        int day = segmentStart.getDayOfMonth();

        calendar.createEvent(year, month, day, event);
      }
    } else {


      int year = start.getYear();
      int month = start.getMonthValue();
      int day = start.getDayOfMonth();

      LocalDate currentDay = start.toLocalDate();
      LocalDateTime segmentEnd = currentDay.atTime(23, 59, 59);
      info.setEndDate(segmentEnd);
      CalendarEvent event = new CalendarEvent(info, start, segmentEnd);
      calendar.createEvent(year, month, day, event);
    }


  }

  @Override
  public void breakEvents() {

  }
}
