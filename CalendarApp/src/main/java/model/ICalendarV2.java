package model;

import java.time.ZoneId;
import java.util.Map;

/**
 * This interface provides additional functionality for managing calendar time zones and
 * copying events between calendars. It also extends the ICalendar interface and its
 * functionalities.
 */
public interface ICalendarV2 extends ICalendar {

  /**
   * Changes the timezone of the calendar to the specified new timezone.
   *
   * @param newTimeZone The timezone representing the new calendar timezone.
   */
  public void changeCalendarTimeZone(ZoneId newTimeZone);

  /**
   * Gets the current timezone set for this calendar.
   *
   * @return The current timezone of the calendar.
   */
  public ZoneId getCalendarTimeZone();

  /**
   * This method copies events from the current calendar instance to a target calendar.
   *
   * @param toCalendar  The target instance where events will be copied.
   * @param metaDetails A map containing details having the parameters for the copying process.
   * @throws Exception If copying fails due to conflicting events or invalid criteria.
   */
  public void copyToTargetCalendar(ICalendarV2 toCalendar, Map<String, Object> metaDetails)
          throws Exception;
}
