package Model.Calendar;

import java.time.LocalDateTime;

import Controller.MetaData.PrintEventMetaDetails;

public interface ICalendar {
  public void showStatus();

  public void export();

  public void printEvents();

  public void getMatchingEvents(PrintEventMetaDetails allMetaData);

  public void printOnTimeEvents(LocalDateTime from);

  public void printFromToEvents(LocalDateTime from, LocalDateTime to);
}
