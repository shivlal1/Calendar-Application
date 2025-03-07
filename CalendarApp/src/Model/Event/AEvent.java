package Model.Event;

import java.time.LocalDateTime;

import Model.Calendar.ACalendar;

public abstract class AEvent {

  public abstract void pushEventToCalendar(EventDetails event, ACalendar calendar, EventMetaDetails allMetaDeta);


  protected void pushCreatedSegmentEvent(ACalendar calendar, LocalDateTime start, EventDetails info,
                                         LocalDateTime segmentEnd) {
    int year = start.getYear();
    int month = start.getMonthValue();
    int day = start.getDayOfMonth();
    CalendarEvent event = new CalendarEvent(info, start, segmentEnd);
    calendar.createEvent(year, month, day, event);

  }
}
