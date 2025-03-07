package Model.Calendar;

import java.time.LocalDateTime;

public interface CalendarEdit {
  public void editEvent(int year, int month, int day, LocalDateTime start, String name, String newValue, String property);
  public void editAllEvents(String eventName, String newValue, String property);
  public void editFromToEvents(int year, int month, int day, String eventName, LocalDateTime start, LocalDateTime end, String newValue, String property);
}
