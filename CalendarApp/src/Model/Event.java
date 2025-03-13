package model;

import java.time.LocalDateTime;
import java.util.List;

/**
 * This class is an abstract base class for all types of events in the calendar system.
 * It provides common properties and methods for events, such as subject, start and end dates,
 * description, location, and whether the event is public.
 */
public abstract class Event {

  protected String subject;
  protected String description;
  protected String location;
  protected LocalDateTime startDate;
  protected LocalDateTime endDate;
  protected boolean isPublic;

  Event(String subject, LocalDateTime startDate, LocalDateTime endDate) {
    this.subject = subject;
    this.startDate = startDate;
    this.endDate = endDate;
    this.isPublic = false;
    this.description = null;
    this.location = null;
  }

  /**
   * Generates a list of events for the calendar based on the event's type and properties.
   * This method must be implemented by subclasses.
   *
   * @return A list of events to be added to the calendar.
   * @throws Exception If there's an error in generating events.
   */
  protected abstract List<Event> generateEventsForCalendar() throws Exception;

  /**
   * Checks if auto-decline is enabled for this event.
   * This method must be implemented by subclasses.
   *
   * @return true if auto-decline is enabled; false otherwise.
   */
  protected abstract boolean isAutoDeclineEnabled();

  /**
   * Checks if the event can be edited to a different day.
   * This method must be implemented by subclasses.
   *
   * @return true if the event can be edited to a different day; false otherwise.
   */
  protected abstract boolean canBeEditedToDifferentDay();

  /**
   * Checks if the start date is before the end date.
   *
   * @param start The start date and time.
   * @param end   The end date and time.
   * @return true if the start date is before the end date, else false.
   */
  protected boolean isStartBeforeEnd(LocalDateTime start, LocalDateTime end) {
    if (end.isAfter(start)) {
      return true;
    }
    return false;
  }

  /**
   * Returns a string representation of the event, including its subject, start and end dates,
   * description, location, and whether it is public.
   *
   * @return A string containing the event details.
   */
  @Override
  public String toString() {
    StringBuilder details = new StringBuilder();

    if (subject != null) {
      details.append(" Subject " + subject);
    }
    if (startDate != null) {
      details.append(" Start Date : " + startDate);
    }
    if (description != null) {
      details.append(" Description: " + description);
    }
    if (location != null) {
      details.append(" Location: " + location);
    }
    if (endDate != null) {
      details.append(" End Date :" + endDate);
    }
    details.append(" isPublic: " + isPublic);

    return details.toString();
  }

  /**
   * Checks if this event overlaps with another event.
   *
   * @param newEvent The event to check for overlap.
   * @return true if there is an overlap else false.
   */
  protected boolean isOverlapWith(Event newEvent) {

    LocalDateTime newStartTime = newEvent.startDate;
    LocalDateTime newEndTime = newEvent.endDate;
    LocalDateTime existingStartTime = this.startDate;
    LocalDateTime existingEndTime = this.endDate;

    boolean isConflictExists = false;

    if (newStartTime.isBefore(existingEndTime) && newEndTime.equals(existingStartTime)) {
      isConflictExists = false;
    }
    if (existingStartTime.isBefore(newEndTime) && existingEndTime.equals(newStartTime)) {
      isConflictExists = false;
    }

    if (newStartTime.isBefore(existingEndTime) && existingStartTime.isBefore(newEndTime)) {
      isConflictExists = true;
    }

    if (existingStartTime.isBefore(newEndTime) && newStartTime.isBefore(existingEndTime)) {
      isConflictExists = true;
    }

    if (existingStartTime.equals(newStartTime) || existingEndTime.equals(newEndTime)) {
      isConflictExists = true;
    }
    return isConflictExists;
  }

}
