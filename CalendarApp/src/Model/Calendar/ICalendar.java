package Model.Calendar;

import java.time.LocalDateTime;

public interface ICalendar {
  public void showStatus();

  public void export();

  public void printEvents();

  public void printOnTimeEvents(LocalDateTime from);

  public void printFromToEvents(LocalDateTime from, LocalDateTime to);
}
