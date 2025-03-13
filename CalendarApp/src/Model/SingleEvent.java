package Model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This class extends the Event class and represents a single, non-recurring event.
 * It handles events that occur once and may be all-day events.
 */
public class SingleEvent extends Event {

  private Map<String, Object> allMetaDetails;

  /**
   * Constructs a new SingleEvent object with the specified subject, start and end dates,
   * and the metadata.
   *
   * @param subject        The subject or title of the event.
   * @param startDate      The start date and time of the event.
   * @param endDate        The end date and time of the event.
   * @param allMetaDetails Metadata containing event settings.
   */
  SingleEvent(String subject, LocalDateTime startDate, LocalDateTime endDate,
              Map<String, Object> allMetaDetails) {

    super(subject, startDate, endDate);
    this.allMetaDetails = allMetaDetails;
  }

  /**
   * Generates the event for the calendar. For single events, this simply returns the event itself.
   * If the event is marked as all-day, it adjusts the end date to the end of the day.
   *
   * @return A list containing the single event.
   * @throws Exception If the start date is after the end date.
   */
  protected List<Event> generateEventsForCalendar() throws Exception {
    List<Event> newEventsList = new ArrayList<>();

    if (!isStartBeforeEnd(this.startDate, this.endDate)) {
      throw new Exception("end date cannot be before start date");
    }

    if ((Boolean) allMetaDetails.get("isAllDay")) {
      LocalDate currentDay = this.startDate.toLocalDate();
      LocalDateTime newEndDate = currentDay.atTime(23, 59);
      this.endDate = newEndDate;
    }

    newEventsList.add(this);
    return newEventsList;
  }

  /**
   * Checks if auto-decline is enabled for this single event.
   *
   * @return false, as auto-decline is not enabled for single events.
   */
  @Override
  protected boolean isAutoDeclineEnabled() {
    return false;
  }

  /**
   * Checks if the single event can be edited to a different day.
   *
   * @return true, as single events can be edited to different days.
   */
  @Override
  protected boolean canBeEditedToDifferentDay() {
    return true;
  }

}







