package model;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import utils.DateUtils;

/**
 * This class extends the Calendar class and implements the ICalendarV2 interface. It has
 * operations to do operations related to timezone and also do the copy of events from one
 * calendar to another.
 */
public class CalendarV2
        extends Calendar
        implements ICalendarV2 {

  private ZoneId timeZone;

  /**
   * This method constructs the Calendar object with the information from its parent class and
   * it also intializes the time zone.
   *
   * @param timeZone the timezone provided.
   */
  public CalendarV2(ZoneId timeZone) {
    super();
    this.timeZone = timeZone;
  }

  /**
   * This method changes the time zone of the entire calendar.
   *
   * @param newTimeZone the new timezone to which the calendar needs to be assigned to.
   */
  @Override
  public void changeCalendarTimeZone(ZoneId newTimeZone) {
    for (Event events : calendarStorage) {
      events.startDate = DateUtils.changeTimeZone(events.startDate, this.timeZone, newTimeZone);
      events.endDate = DateUtils.changeTimeZone(events.endDate, this.timeZone, newTimeZone);
    }
    this.timeZone = newTimeZone;
  }

  /**
   * This method gets the timezone of the particular calendar.
   *
   * @return the timezone of the current calendar.
   */
  @Override
  public ZoneId getCalendarTimeZone() {
    return this.timeZone;
  }

  /**
   * This method checks for matching events based on start, end time and event name.
   *
   * @param event     The event to check.
   * @param start     The start time to match.
   * @param end       The end time to match.
   * @param eventName The event name to match.
   * @return true if there is a match, else false.
   */
  protected boolean isMatchingEvent(Event event, LocalDateTime start, LocalDateTime end,
                                    String eventName) {
    return super.isMatchingEvent(event, start, end, eventName)
            || (event.subject.equals(eventName)
            && (start != null && event.startDate.compareTo(start) >= 0));
  }

  /**
   * This method checks if the date is in between the given start and end dates.
   *
   * @param start the start date.
   * @param middle the middle date for which we need to check.
   * @param end the end date.
   * @return true if the middle date is in between those dates else, false.
   */
  private boolean isBetweenDates(LocalDate start, LocalDate middle, LocalDate end) {
    return ((middle.isAfter(start) || middle.isEqual(start))
            && (middle.isBefore(end) || middle.isEqual(end)));
  }

  /**
   * This method filters the events matching a specific name and exact start date/time,
   * then creates copies at a new specified time.
   *
   * @param metaDetails Metadata map containing event name, original date/time, and new target
   *                    date/time.
   * @return List of event copies matching criteria.
   */
  private List<Event> filterEventsOnDateWithTime(Map<String, Object> metaDetails) {
    List<Event> events = new ArrayList<>();
    EventFactory factory = new EventFactory();
    for (Event event : calendarStorage) {
      LocalDateTime onDateTime = (LocalDateTime) metaDetails.get("onDateTime");
//      String eventName = (String) metaDetails.get("eventName");
//      boolean val = event.subject.equals((eventName));
//     boolean val2 = event.startDate.equals(onDateTime);
//     // System.out.println("val "+event.subject.equals((String) metaDetails.get("eventName")));
//      //System.out.println("val 2 "+event.startDate.equals(onDateTime));
      if (event.subject.equals( (String) metaDetails.get("eventName") )
              && event.startDate.equals(onDateTime)) {
        Duration offsetTime = Duration.between(event.startDate, event.endDate);
        LocalDateTime newStartDateTime = (LocalDateTime) metaDetails.get("newStartTime");
        LocalDateTime newEndDateTime = newStartDateTime.plus(offsetTime);
        Event carbonCopy = factory.getCarbonCopy(event, newStartDateTime, newEndDateTime);
        events.add(carbonCopy);
      }
    }
    return events;
  }

  /**
   * This method makes a timezone-adjusted carbon copy of an event for the target calendar.
   *
   * @param event Original event to be copied.
   * @param targetCalendar Target calendar where the event will be copied.
   * @param newStartDateTime Adjusted start date/time for the copied event.
   * @param newEndDateTime Adjusted end date/time for the copied event.
   * @return A timezone-adjusted carbon copy of the original event.
   */
  private Event getNewCarbonCopy(Event event, ICalendarV2 targetCalendar,
                                 LocalDateTime newStartDateTime, LocalDateTime newEndDateTime) {
    EventFactory factory = new EventFactory();
    newStartDateTime = DateUtils.changeTimeZone(newStartDateTime, this.timeZone,
            targetCalendar.getCalendarTimeZone());
    newEndDateTime = DateUtils.changeTimeZone(newEndDateTime, this.timeZone,
            targetCalendar.getCalendarTimeZone());

    return factory.getCarbonCopy(event, newStartDateTime, newEndDateTime);

  }

  /**
   * Filters and copies all events occurring on a specific date to a target calendar at
   * a new specified date.
   *
   * @param targetCalendar Target calendar where events will be copied.
   * @param metaDetails Metadata map containing original date and destination date.
   * @return List of copied events matching criteria.
   */
  private List<Event> filterEventsOnDate(ICalendarV2 targetCalendar, Map<String,
          Object> metaDetails) {
    List<Event> events = new ArrayList<>();
    LocalDate onDate = (LocalDate) metaDetails.get("onDateForCopy");
    LocalDate toDate = (LocalDate) metaDetails.get("toDateDestination");
    for (Event event : calendarStorage) {
      LocalDate startDate = event.startDate.toLocalDate();
      if (startDate.equals(onDate)) {
        LocalDateTime newStartDateTime = LocalDateTime.of(toDate, event.startDate.toLocalTime());
        Duration offsetTime = Duration.between(event.startDate, event.endDate);
        LocalDateTime newEndDateTime = newStartDateTime.plus(offsetTime);
        events.add(getNewCarbonCopy(event, targetCalendar, newStartDateTime, newEndDateTime));
      }
    }
    return events;
  }

  /**
   * This method filters the events occurring within a specified date range and copies them to a
   * new date range on the target calendar.
   *
   * @param metaDetails Metadata map containing start and end dates for original and
   *                    new date ranges.
   * @param targetCalendar Target calendar where events will be copied.
   * @return List of copied events matching the date-range criteria.
   */
  private List<Event> filteredEventsInBetween(Map<String, Object> metaDetails,
                                              ICalendarV2 targetCalendar) {
    List<Event> events = new ArrayList<>();

    for (Event event : calendarStorage) {
      LocalDate startDate = (LocalDate) metaDetails.get("fromDate");
      LocalDate toDate = (LocalDate) metaDetails.get("toDate");
      LocalDate onDate = (LocalDate) metaDetails.get("onDate");

      if (isBetweenDates(startDate, event.startDate.toLocalDate(), toDate)) {
        LocalDateTime newStartDateTime = LocalDateTime.of(onDate, event.startDate.toLocalTime())
                .plusDays(ChronoUnit.DAYS.between(startDate, event.startDate.toLocalDate()));
        Duration offsetTime = Duration.between(event.startDate, event.endDate);
        LocalDateTime newEndDateTime = newStartDateTime.plus(offsetTime);
        events.add(getNewCarbonCopy(event, targetCalendar, newStartDateTime, newEndDateTime));
      }
    }
    return events;
  }

  /**
   * This method helps determine the type of copy operation to perform and delegates the request
   * to the appropriate filter method.
   *
   * @param metaDetails Metadata map specifying the type of copy operation.
   * @param targetCalendar Target calendar for copied events.
   * @return List of events copied based on the specified criteria.
   */
  private List<Event> filterEventsToCopy(Map<String, Object> metaDetails,
                                         ICalendarV2 targetCalendar) {
    String copyType = (String) metaDetails.get("copyType");
    if (copyType.equals("eventsOnDateWithTime")) {
      return filterEventsOnDateWithTime(metaDetails);
    } else if (copyType.equals("eventsOnDate")) {
      return filterEventsOnDate(targetCalendar, metaDetails);
    } else {
      return filteredEventsInBetween(metaDetails, targetCalendar);
    }
  }

  /**
   * This method copies the events from the current calendar to the specified target calendar,
   * ensuring no event overlaps occur.
   *
   * @param targetCalendar The calendar where events will be copied to.
   * @param metaDetails Metadata specifying the copy criteria and details.
   * @throws Exception If an error occurs during the copying process.
   */
  @Override
  public void copyToTargetCalendar(ICalendarV2 targetCalendar, Map<String, Object> metaDetails)
          throws Exception {
    List<Event> newFilteredEventsList = filterEventsToCopy(metaDetails, targetCalendar);
    CalendarV2 newTargetCalendar = (CalendarV2) targetCalendar;

    for (Event newEvent : newFilteredEventsList) {
      boolean isOverLap = false;
      for (Event oldEvent : newTargetCalendar.calendarStorage) {
        if (newEvent.isOverlapWith(oldEvent)) {
          isOverLap = true;
          break;
        }
      }
      if (!isOverLap) {
        newTargetCalendar.calendarStorage.add(newEvent);
      }
    }
  }

  /**
   * This method makes a new event in the calendar with automatic conflict resolution.
   *
   * @param subject Subject/title of the new event.
   * @param localStartDateTime Event start date/time.
   * @param localEndDateTime Event end date/time.
   * @param allMetaDeta Metadata map containing additional event details and settings.
   * @throws Exception If event creation encounters an issue (e.g., conflicting events).
   */
  @Override
  public void createEvent(String subject, LocalDateTime localStartDateTime,
                          LocalDateTime localEndDateTime,
                          Map<String, Object> allMetaDeta) throws Exception {

    allMetaDeta.put("autoDecline", true);
    super.createEvent(subject, localStartDateTime, localEndDateTime, allMetaDeta);
  }
}