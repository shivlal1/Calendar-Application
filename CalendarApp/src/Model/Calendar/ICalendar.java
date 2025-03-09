package Model.Calendar;

import java.time.LocalDateTime;
import java.util.List;

import Controller.MetaData.PrintEventMetaDetails;
import Model.Event.EventDetails;

public interface ICalendar {
  public void showStatus();

  public void export();

  public List<EventDetails> getMatchingEvents(PrintEventMetaDetails allMetaData);

  public String  exportCalendarAndGetFilePath();

  public List<EventDetails> printFromToEvents(LocalDateTime from, LocalDateTime to);

  public boolean isBusyOnDay(LocalDateTime date);

}
