package Model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface ICalendar {


  public void createEvent(String subject, LocalDateTime localStartDateTime,
                          LocalDateTime localEndDateTime,
                          Map<String, Object> allMetaDeta) throws Exception;


  // EDIT
  public void editEvent(Map<String, Object> allMetaDeta) throws Exception;


  //
  public void printEvents();
  //


  // PRINT
  public List<Event> getMatchingEvents(Map<String, Object> allMetaData);


  // STATUS
  public boolean isUserBusy(LocalDateTime date);

  // EXPORT
 // public String exportCalendarAndGetFilePath();


}
