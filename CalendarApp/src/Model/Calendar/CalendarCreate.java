package Model.Calendar;

import java.util.List;

import Model.Event.CalendarEvent;

public interface CalendarCreate {
  //void createEvent(int year, int month, int day, CalendarEvent event);
  void createEvent(List<CalendarEvent> event, boolean autoDecline);

}
