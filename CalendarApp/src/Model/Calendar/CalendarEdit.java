package Model.Calendar;

import java.time.LocalDateTime;

public interface CalendarEdit {
  public void editEvent(LocalDateTime start, String name, String newValue, String property);

  public void editEvent(String eventName, String newValue, String property);

  public void editEvent(String eventName, LocalDateTime start, LocalDateTime end, String newValue, String property);
}
