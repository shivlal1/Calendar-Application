package Model.Calendar;

import java.time.LocalDateTime;

import Controller.MetaData.EditEventMetaDetails;

public interface CalendarEdit {

  public void editEventUtil(LocalDateTime start, String name, String newValue, String property);

  public void editEventUtil(String eventName, String newValue, String property);

  public void editEventUtil(String eventName, LocalDateTime start, LocalDateTime end, String newValue, String property);

  public void editEvent(EditEventMetaDetails allMetaDeta);
}
