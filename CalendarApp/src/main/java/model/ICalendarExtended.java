package model;

import java.time.ZoneId;
import java.util.Map;

public interface ICalendarExtended extends ICalendar {

  public void changeCalendarTimeZone(ZoneId newTimeZone);

  public ZoneId getCalendarTimeZone();

  public void copyToTargetCalendar(ICalendarExtended toCalendar, Map<String, Object> metaDetails) throws Exception;
}
