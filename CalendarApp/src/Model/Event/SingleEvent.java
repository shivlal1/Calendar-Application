package Model.Event;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import Controller.MetaData.EventMetaDetails;
import Model.Calendar.ACalendar;

public class SingleEvent extends AEvent {

  private EventDetails eventDetails;
  private EventMetaDetails allMetaDetails;

  SingleEvent(EventDetails eventDetails, EventMetaDetails allMetaDetails) {
    this.eventDetails = eventDetails;
    this.allMetaDetails = allMetaDetails;
  }

  private List<CalendarEvent> getAllDayEventList(ACalendar calendar, LocalDateTime start) {
    List<CalendarEvent> list = new ArrayList<>();

    LocalDate currentDay = start.toLocalDate();
    LocalDateTime segmentEnd = currentDay.atTime(23, 59, 59);
    eventDetails.setEndDate(segmentEnd);

    list.add(getCreatedSegmentEvent(calendar, start, eventDetails, segmentEnd));

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

      list.add(getCreatedSegmentEvent(calendar, segmentStart, eventDetails, segmentEnd));
    }

    return list;
  }

  public void pushEventToCalendar(ACalendar calendar) {

    LocalDateTime start = eventDetails.getStartDate();
    LocalDateTime end = eventDetails.getEndDate();

    if (!isValidSingleEvent(start, end)) {
      return;
    }

    List<CalendarEvent> newEventsList;
    if (allMetaDetails.getIsAllDay()) {
      newEventsList = getAllDayEventList(calendar, start);
    } else {
      newEventsList = getMultiDayEventList(calendar, start, end);
    }
//
//    System.out.println("printing all generated events");
//    for (CalendarEvent e : newEventsList) {
//      System.out.println(e);
//    }
//    System.out.println("printed all generated events");

    calendar.createEvent(newEventsList, allMetaDetails.getAutoDecline());

  }

  private boolean isValidSingleEvent(LocalDateTime start, LocalDateTime end) {

    if (end.isAfter(start)) {
      return true;
    }
    return false;
  }

}







