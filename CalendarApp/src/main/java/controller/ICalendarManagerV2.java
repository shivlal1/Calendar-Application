package controller;

/**
 * ICalendarManagerV2 extends the ICalendarManager interface
 * It providesfunctionality to support the creation of calendars using name and timezone.
 */
public interface ICalendarManagerV2 extends ICalendarManager {

  /**
   * Creates a new calendar with the specified name and time zone.
   *
   * @param calendarName the name of the calendar to be created
   * @param timeZone     the time zone ID ("Asia/Kolkata") for the calendar
   */
  public void createCalendar(String calendarName, String timeZone);

}
