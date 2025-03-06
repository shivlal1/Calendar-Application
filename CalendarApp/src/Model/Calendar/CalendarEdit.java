package Model.Calendar;

import java.time.LocalDateTime;

public interface CalendarEdit {
  public void editEvent(int year, int month, int day, LocalDateTime start, String name, String newValue, String property);
}
