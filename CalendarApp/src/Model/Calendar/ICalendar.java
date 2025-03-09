package Model.Calendar;

import java.time.LocalDateTime;
import java.util.List;

import Controller.MetaData.PrintEventMetaDetails;
import Model.Event.EventDetails;

public interface ICalendar {
  public void showStatus();

  public void export();

  public void printEvents();

  public List<EventDetails> getMatchingEvents(PrintEventMetaDetails allMetaData);

  public void printOnTimeEvents(LocalDateTime from);

  public List<EventDetails> printFromToEvents(LocalDateTime from, LocalDateTime to);

  public boolean isBusyOnDay(LocalDateTime date);

}
