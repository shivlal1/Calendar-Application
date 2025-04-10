package controller;

import java.time.ZoneId;

import model.CalendarV2;

public class CalendarManagerV2 extends CalendarManager implements ICalendarManagerV2 {

  public CalendarManagerV2(String calendarName, String timeZone) {
    calendarMap.put(calendarName, new CalendarV2(ZoneId.of(timeZone)));
    activeCalendar = calendarMap.get(calendarName);
  }

  @Override
  public void createCalendar(String calendarName, String timeZone) {
    calendarMap.put(calendarName, new CalendarV2(ZoneId.of(timeZone)));
  }

}
