package model;

import java.util.Map;

public interface ICalendarExtended extends ICalendar {

  public void changeCalendarName(String newCalendarName);
  public void changeCalendarTimeZone(String newTimeZone);
  public void copyToTargetCalendar(ICalendarExtended toCalendar, Map<String, Object> metaDetails) throws Exception;
}
