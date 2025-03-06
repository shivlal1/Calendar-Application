package Model.Calendar;

import Model.Event.CalendarEvent;

public interface CalendarCreate {
  void createEvent(int year, int month, int day, CalendarEvent event);
}
