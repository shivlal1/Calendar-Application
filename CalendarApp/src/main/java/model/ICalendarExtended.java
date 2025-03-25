package model;

import java.util.Map;

public interface ICalendarExtended extends ICalendar {

  public void changeCalendarProperty(String property, String newValue);

  public void copyEventsToAnotherCalendar(ICalendarExtended toCalendar, Map<String, Object> metaDetails);
}
