package Model.Event;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import Model.Calendar.ACalendar;

public class SingleEvent extends AEvent {


  private void pushAllDayEvent(ACalendar calendar, LocalDateTime start, EventDetails info) {
    LocalDate currentDay = start.toLocalDate();
    LocalDateTime segmentEnd = currentDay.atTime(23, 59, 59);
    info.setEndDate(segmentEnd);
    pushCreatedSegmentEvent(calendar, start, info, segmentEnd);
  }

  private void pushMultiDayEvent(ACalendar calendar, LocalDateTime start, LocalDateTime end, EventDetails info) {

    long daysBetween = ChronoUnit.DAYS.between(start, end) + 1;
    System.out.println("days in between " + daysBetween);
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

      pushCreatedSegmentEvent(calendar, segmentStart, info, segmentEnd);
    }
  }

  public void pushEventToCalendar(EventDetails info, ACalendar calendar, EventMetaDetails allMetaData) {

    LocalDateTime start = info.getStartDate();
    LocalDateTime end = info.getEndDate();

    if (allMetaData.getIsAllDay()) {
      pushAllDayEvent(calendar, start, info);
    } else {
      pushMultiDayEvent(calendar, start, end, info);
    }

  }

}
