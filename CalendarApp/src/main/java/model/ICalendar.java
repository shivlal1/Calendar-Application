package model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * This interface sets a base for the classes that implement calendar functionality.
 * It specifies methods for creating events, editing events, printing events, checking if
 * the user is busy on a particular date, and retrieving events.
 */
public interface ICalendar {

  /**
   * Creates a new event in the calendar with the specified details.
   *
   * @param subject            The subject or title of the event.
   * @param localStartDateTime The start date and time of the event.
   * @param localEndDateTime   The end date and time of the event.
   * @param allMetaDeta        Additional metadata for the event.
   * @throws Exception If there's an error in creation like conflicts.
   */
  public void createEvent(String subject, LocalDateTime localStartDateTime,
                          LocalDateTime localEndDateTime,
                          Map<String, Object> allMetaDeta) throws Exception;


  /**
   * Edits an existing event based on the provided metadata.
   *
   * @param allMetaDeta Metadata containing details for the edit operation.
   * @throws Exception If no matching event is found or if there's an error in editing.
   */
  public void editEvent(Map<String, Object> allMetaDeta) throws Exception;


  /**
   * Retrieves events that match the specified criteria (e.g., date range).
   *
   * @param allMetaData Metadata containing criteria for event matching.
   * @return A list of event details in map format.
   */
  public List<Map<String, Object>> getMatchingEvents(Map<String, Object> allMetaData);

  /**
   * Checks if the user is busy at the specified date and time.
   *
   * @param date The date and time to check.
   * @return true if the user is busy; false otherwise.
   */
  public boolean isUserBusy(LocalDateTime date);

  /**
   * Retrieves all events stored in the calendar as a list of maps.
   *
   * @return A list of event details in map format.
   */
  public List<Map<String, Object>> getAllCalendarEvents();
}
