package Model.Event;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import Controller.MetaData.CreateCommandMetaDetails;
import Model.Calendar.ACalendar;

public class SingleEvent extends Event {

  private CreateCommandMetaDetails allMetaDetails;

  SingleEvent(String subject, LocalDateTime startDate, LocalDateTime endDate,
              CreateCommandMetaDetails allMetaDetails) {

    super(subject, startDate, endDate);

    this.allMetaDetails = allMetaDetails;
  }

  private List<CalendarEvent> getAllDayEventList(ACalendar calendar, LocalDateTime start) {
    List<CalendarEvent> list = new ArrayList<>();

    LocalDate currentDay = start.toLocalDate();
    LocalDateTime segmentEnd = currentDay.atTime(23, 59, 59);
    endDate = segmentEnd;

    list.add(getCreatedSegmentEvent(calendar, start, this, segmentEnd));

    return list;
  }


  private List<CalendarEvent> getMultiDayEventList(ACalendar calendar, LocalDateTime start, LocalDateTime end) {

    List<CalendarEvent> list = new ArrayList<>();

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

      list.add(getCreatedSegmentEvent(calendar, segmentStart, this, segmentEnd));
    }

    return list;
  }

  public void pushEventToCalendar(ACalendar calendar) {

    LocalDateTime start = this.startDate;
    LocalDateTime end = this.endDate;

    if (!isStartBeforeEnd(start, end)) {
      return;
    }
    System.out.println("valid");

    List<CalendarEvent> newEventsList;
    if (allMetaDetails.getIsAllDay()) {
      newEventsList = getAllDayEventList(calendar, start);
    } else {
      newEventsList = getMultiDayEventList(calendar, start, end);
    }

    calendar.createEvent(newEventsList, allMetaDetails.getAutoDecline());
  }


}







