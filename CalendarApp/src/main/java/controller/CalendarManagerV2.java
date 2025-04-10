package controller;

import java.time.ZoneId;

import model.CalendarV2;

/**
 * CalendarManagerV2 is an extension of the CalendarManager class.
 * It implements the ICalendarManagerV2 interface.
 * It's main functionality is to provide.
 * calendar create functionality just by using calendar name and timezone.
 */
public class CalendarManagerV2 extends CalendarManager implements ICalendarManagerV2 {

  /**
   * Constructs a default calendar instance and initialises it as default calendar.
   *
   * @param calendarName the name of the initial calendar to be created.
   * @param timeZone     the time zone ID to associate with the calendar.
   */
  public CalendarManagerV2(String calendarName, String timeZone) {
    calendarMap.put(calendarName, new CalendarV2(ZoneId.of(timeZone)));
    activeCalendar = calendarMap.get(calendarName);
  }

  /**
   * Creates a new calendar with the specified name and time zone in calendar manager.
   *
   * @param calendarName the name of the new calendar.
   * @param timeZone     the time zone ID to associate with the calendar.
   */
  @Override
  public void createCalendar(String calendarName, String timeZone) {
    calendarMap.put(calendarName, new CalendarV2(ZoneId.of(timeZone)));
  }

}
