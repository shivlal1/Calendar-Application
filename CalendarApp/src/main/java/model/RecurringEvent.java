package model;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import utils.DateUtils;

/**
 * This class extends the Event class and represents events that recur over time.
 * It handles recurring events based on specified weekdays and until a certain date or
 * for a specified number of times.
 */
public class RecurringEvent extends Event {

  /**
   * This method constructs a new RecurringEvent object with the specified subject, start
   * and end dates, and metadata.
   *
   * @param subject        The subject or title of the event.
   * @param startDate      The start date and time of the event.
   * @param endDate        The end date and time of the event.
   * @param allMetaDetails Metadata containing recurrence settings.
   */
  RecurringEvent(String subject, LocalDateTime startDate, LocalDateTime endDate,
                 Map<String, Object> allMetaDetails) {

    super(subject, startDate, endDate, allMetaDetails);
  }

  /**
   * Constructs a new RecurringEvent object by copying details from another event and adjusting
   * the start and end times.
   *
   * @param other        The event to copy details from.
   * @param newStartTime The new start date and time.
   * @param newEndTime   The new end date and time.
   */
  public RecurringEvent(Event other, LocalDateTime newStartTime, LocalDateTime newEndTime,
                        Map<String, Object> allMetaDetails) {
    super(other.subject, other.startDate, other.endDate, allMetaDetails);
    this.isPublic = other.isPublic;
    this.description = other.description;
    this.location = other.location;
    this.startDate = newStartTime;
    this.endDate = newEndTime;
  }

  /**
   * Retrieves the end date for recurring events based on the "until" metadata.
   *
   * @return The end date as a LocalDateTime object.
   */
  private LocalDateTime getRecurringEventEndDate() {
    String finalUntilDateTime = (String) allMetaDetails.get("untilTime");
    return DateUtils.stringToLocalDateTime(finalUntilDateTime);
  }

  /**
   * Checks if a given date is included in the recurring event's weekdays.
   *
   * @param date The date to check.
   * @return true if the date is included; false otherwise.
   */
  private boolean isWeekDayIncluded(LocalDateTime date) {

    DayOfWeek day = date.getDayOfWeek();
    char dayNameAsChar = getDayAbbreviation(day.name());
    String weekdayText = (String) allMetaDetails.get("weekdays");
    return (weekdayText.indexOf(dayNameAsChar) != -1);
  }

  /**
   * Generates recurring events for a specified number of times.
   *
   * @param start The start date and time of the recurring event.
   * @param end   The end date and time of the recurring event.
   * @return A list of recurring events.
   */
  private List<Event> getForTimeRecurringEvent(LocalDateTime start,
                                               LocalDateTime end) {

    LocalDateTime currDate = start;
    int requriredRecurringEvents = Integer.valueOf((String) allMetaDetails.get("forTimes"));
    List<Event> eventsList = new ArrayList<>();
    int eventsEncountered = 0;

    while (eventsEncountered < requriredRecurringEvents) {

      if (isWeekDayIncluded(currDate)) {
        eventsList.add(new RecurringEvent(this, currDate, end, allMetaDetails));
        eventsEncountered++;
      }

      if (eventsEncountered == requriredRecurringEvents) {
        break;
      }
      currDate = currDate.plusDays(1);
      end = end.plusDays(1);
    }
    return eventsList;
  }

  /**
   * Generates recurring events until a specified date.
   *
   * @param start The start date and time of the recurring event.
   * @param end   The end date and time of the recurring event.
   * @return A list of recurring events.
   */
  private List<Event> getUntilTimeRecurringEvent(LocalDateTime start,
                                                 LocalDateTime end) {

    List<Event> eventsList = new ArrayList<>();
    LocalDateTime until = getRecurringEventEndDate();
    LocalDateTime currDate = start;

    while (!currDate.isAfter(until)) {
      if (isWeekDayIncluded(currDate)) {
        eventsList.add(new RecurringEvent(this, currDate, end, allMetaDetails));
      }
      currDate = currDate.plusDays(1);
      end = end.plusDays(1);
    }
    return eventsList;
  }

  /**
   * Generates all recurring events based on the event's metadata.
   *
   * @return A list of recurring events to be added to the calendar.
   * @throws Exception If the start date is after the end date.
   */
  protected List<Event> generateEventsForCalendar() throws Exception {

    LocalDateTime start = this.startDate;
    LocalDateTime end = this.endDate;

    List<Event> newEventsList = new ArrayList<>();

    if (!isStartBeforeEnd(start, end)) {
      throw new Exception("end date cannot be before start date");
    }

    if ((String) allMetaDetails.get("untilTime") != null) {
      newEventsList = getUntilTimeRecurringEvent(start, end);

    } else if (allMetaDetails.get("forTimes") != null) {
      newEventsList = getForTimeRecurringEvent(start, end);
    }

    return newEventsList;

  }


  /**
   * Converts a day of the week to its abbreviation (e.g., Monday to 'M').
   *
   * @param day The day of the week as a string.
   * @return The abbreviation of the day.
   */
  private char getDayAbbreviation(String day) {

    if (day.equals("TUESDAY")) {
      return 'T';
    } else if (day.equals("WEDNESDAY")) {
      return 'W';
    } else if (day.equals("THURSDAY")) {
      return 'R';
    } else if (day.equals("FRIDAY")) {
      return 'F';
    } else if (day.equals("SATURDAY")) {
      return 'S';
    } else if (day.equals("SUNDAY")) {
      return 'U';
    } else {
      return 'M';
    }
  }

  /**
   * This method checks if the recurring event can be edited to a different day.
   *
   * @return false, as recurring events cannot be edited to different days.
   */
  @Override
  protected boolean canBeEditedToDifferentDay() {
    return false;
  }

}
