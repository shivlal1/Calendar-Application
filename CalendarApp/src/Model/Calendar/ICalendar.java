package Model.Calendar;

import java.time.LocalDateTime;
import java.util.List;

import Controller.MetaData.CreateCommandMetaDetails;
import Controller.MetaData.EditCommandMetaDetails;
import Controller.MetaData.PrintCommandMetaDetails;
import Model.Event.Event;

public interface ICalendar {


  //
  public void printEvents();
  //


  // EDIT
  public void editEvent(EditCommandMetaDetails allMetaDeta);

  // PRINT
  public List<Event> getMatchingEvents(PrintCommandMetaDetails allMetaData);


  // STATUS
  public boolean isBusyOnDay(LocalDateTime date);

  // EXPORT
  public String exportCalendarAndGetFilePath();

  public void createEvent(String subject, LocalDateTime localStartDateTime,
                          LocalDateTime localEndDateTime,
                          CreateCommandMetaDetails allMetaDeta);

}
