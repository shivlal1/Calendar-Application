package model;

import java.time.ZoneId;
import java.util.Map;

public interface ICalendarV2 extends ICalendar {

  public void changeCalendarTimeZone(ZoneId newTimeZone);

  public ZoneId getCalendarTimeZone();

  public void copyToTargetCalendar(ICalendarV2 toCalendar, Map<String, Object> metaDetails) throws Exception;
}
