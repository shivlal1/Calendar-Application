package model;

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
//    this.location = allMetaDetails.containsKey("location") ?
//            allMetaDetails.get("location").toString() : null;
  }

  /**
   * This method constructs a new SingleEvent object with the given new start time, new end time
   * and the other event object's details.
   *
   * @param other        the other event object details which it will extend.
   * @param newStartTime the new start time of the event.
   * @param newEndTime   the new end time of the event.
   */
  public SingleEvent(Event other, LocalDateTime newStartTime, LocalDateTime newEndTime) {
    super(other.subject, other.startDate, other.endDate);
    this.isPublic = other.isPublic;
    this.description = other.description;
    this.location = other.location;
    this.startDate = newStartTime;
    this.endDate = newEndTime;
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
   * Checks if the single event can be edited to a different day.
   *
   * @return true, as single events can be edited to different days.
   */
  @Override
  protected boolean canBeEditedToDifferentDay() {
    return true;
  }

}







