package Model.Event;

import java.time.LocalDateTime;

import Model.Calendar.ACalendar;

public abstract class AEvent {

  public abstract void pushEventToCalendar(ACalendar calendar);


  CalendarEvent getCreatedSegmentEvent(ACalendar calendar, LocalDateTime start, EventDetails info,
                                       LocalDateTime segmentEnd) {
    CalendarEvent event = new CalendarEvent(info, start, segmentEnd);
    return event;
  }
}
