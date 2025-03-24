package model;

import java.util.Map;

public class CalendarExtended
        extends Calendar
        implements ICalendarExtended {

  private String calendarName;
  private String timeZone;

  public CalendarExtended(String calendarName, String timeZone) {
    super();
    this.calendarName = calendarName;
    this.timeZone = timeZone;
    System.out.println("done");
  }

  private void changeMyTimeZone(String newTimeZone) {
  }

  @Override
  public void changeCalendarProperty(String property, String newValue) {
    if (property.equals("name")) {
      this.calendarName = calendarName;
    } else if (property.equals("timeZone")) {
      changeMyTimeZone(newValue);
    }
  }

  @Override
  public void copyEventsToAnotherCalendar(ICalendarExtended calendar, Map<String, Object> metaDetails) {
  }


}