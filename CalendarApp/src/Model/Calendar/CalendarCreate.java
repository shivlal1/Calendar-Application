package Model.Calendar;

import Model.Event.EventToBeAdded;

public interface CalendarCreate {
  void createEvent(int year, int month, int day, EventToBeAdded event);
}
