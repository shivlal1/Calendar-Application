package Model.Calendar;

import java.time.LocalDateTime;
import java.util.List;

import Controller.MetaData.CreateCommandMetaDetails;
import Controller.MetaData.EditCommandMetaDetails;
import Controller.MetaData.PrintCommandMetaDetails;
import Model.Event.Event;

public interface ICalendar {



  public void createEvent(String subject, LocalDateTime localStartDateTime,
                          LocalDateTime localEndDateTime,
                          CreateCommandMetaDetails allMetaDeta);



  // EDIT
  public void editEvent(EditCommandMetaDetails allMetaDeta);


  //
  public void printEvents();
  //


  // PRINT
  public List<Event> getMatchingEvents(PrintCommandMetaDetails allMetaData);


  // STATUS
  public boolean isUserBusy(LocalDateTime date);

  // EXPORT
  public String exportCalendarAndGetFilePath();


}
