package model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import utils.DateUtils;

/**
 * The calendar class implements the ICalendar interface and provides functionality
 * for managing events in a calendar system.
 */
public class Calendar implements ICalendar {

  protected List<Event> calendarStorage;

  /**
   * This method creates a new Calendar object and initializes the event storage.
   */
  public Calendar() {
    calendarStorage = new ArrayList();
  }

  /**
   * Checks if there are any conflicts between existing events and new events.
   *
   * @param newEvents The list of new events to check for conflicts.
   * @return true if any conflict is found; false otherwise.
   */
  private boolean hasConflicts(List<Event> newEvents) {
    for (Event existingEvent : calendarStorage) {
      for (Event newEvent : newEvents) {
        if (newEvent.isOverlapWith(existingEvent)) {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * This method adds a new events to the calendar, considering auto-decline if conflicts occur.
   *
   * @param events      The list of events to add.
   * @param autoDecline Whether to auto-decline if conflicts are found.
   * @throws Exception If auto-decline is enabled and conflicts exist.
   */
  private void putGeneratedEventsIntoCalendar(List<Event> events, boolean autoDecline)
          throws Exception {
    if (autoDecline && hasConflicts(events)) {
      throw new Exception("the event conflicts with another event");
    }
    for (Event event : events) {
      calendarStorage.add(event);
    }
  }

  /**
   * Checks if a given date is between two other dates.
   *
   * @param start  The start date.
   * @param middle The date to check.
   * @param end    The end date.
   * @return true if the middle date is between start and end, else false.
   */
  private boolean isBetween(LocalDateTime start, LocalDateTime middle, LocalDateTime end) {
    return ((middle.isAfter(start) || middle.isEqual(start))
            && (middle.isBefore(end) || middle.isEqual(end)));
  }

  /**
   * This method events that occur between two specified dates.
   *
   * @param from The start date.
   * @param to   The end date.
   * @return A list of event details in map format.
   */
  protected List<Map<String, Object>> getEventsInBetween(LocalDateTime from, LocalDateTime to) {
    List<Map<String, Object>> eventDetails = new ArrayList<>();
    for (Event event : calendarStorage) {
      if (isBetween(from, event.startDate, to)) {
        eventDetails.add(getEventInMap(event));
      }
    }
    return eventDetails;
  }


  /**
   * Sets a property of an event to a new value.
   *
   * @param property The property to update.
   * @param newValue The new value for the property.
   * @param event    The event to update.
   * @throws Exception throws exception for invalid property.
   */
  private void setPropertyValue(String property, String newValue, Event event) throws Exception {
    switch (property) {
      case "name":
        event.subject = newValue;
        break;
      case "location":
        event.location = newValue;
        break;
      case "startDate":
        updateStartDate(event, newValue);
        break;
      case "endDate":
        updateEndDate(event, newValue);
        break;
      case "isPublic":
        event.isPublic = (Boolean.parseBoolean(newValue));
        break;
      case "description":
        event.description = (newValue);
        break;
      default:
        throw new Exception("invalid property");
    }
  }

  /**
   * Updates the start date of an event.
   *
   * @param event    the event we need to update.
   * @param newValue the new value which we want to give for updating.
   * @throws Exception exception if invalid start or end time is given.
   */
  private void updateStartDate(Event event, String newValue) throws Exception {
    LocalDateTime newDate = DateUtils.pareStringToLocalDateTime(newValue);

    if (event.canBeEditedToDifferentDay()) {
      if (newDate.isBefore(event.endDate)) {
        event.startDate = newDate;
      } else {
        throw new Exception("start date should be before end date");
      }
    } else {
      if ((newDate.toLocalDate().equals(event.endDate.toLocalDate()) && newDate.isBefore(event.endDate))) {
        event.startDate = newDate;
      } else {
        throw new Exception("invalid date for recurring event");
      }
    }
  }

  /**
   * This method updates the end date of an event.
   *
   * @param event    The event to update.
   * @param newValue The new end date as a string.
   * @throws Exception exception if invalid start or end time is given.
   */
  private void updateEndDate(Event event, String newValue) throws Exception {
    LocalDateTime newDate = DateUtils.pareStringToLocalDateTime(newValue);

    if (event.canBeEditedToDifferentDay()) {
      if (newDate.isAfter(event.startDate)) {
        event.endDate = (newDate);
      } else {
        throw new Exception("Single Event End date should be before start date");
      }
    } else {
      if (newDate.toLocalDate().equals(event.endDate.toLocalDate())
              && newDate.isAfter(event.startDate)) {
        event.endDate = newDate;
      } else {
        throw new Exception("Recurring event invalid dates");
      }
    }
  }

  /**
   * Edits an event based on the provided metadata.
   *
   * @param data Metadata containing details for the edit operation.
   * @throws Exception If no matching event is found.
   */
  public void editEvent(Map<String, Object> data) throws Exception {

    String newValue = (String) data.get("newValue");
    String property = (String) data.get("property");
    String eventName = (String) data.get("eventName");
    LocalDateTime localStartTime = data.get("localStartTime") != null ?
            (LocalDateTime) data.get("localStartTime") : null;
    LocalDateTime localEndTime = data.get("localEndTime") != null ?
            (LocalDateTime) data.get("localEndTime") : null;

    updateMatchingEvents(eventName, localStartTime, localEndTime, newValue, property);

//    if (isEditByEventTime(data) && data.get("endTime")==null ) {
//      updateMatchingEvents(eventName, localStartTime, localEndTime, newValue, property);
//    } else if (isEditByEventName(data)) {
//      updateMatchingEvents(eventName, localStartTime, localEndTime, newValue, property);
//    } else if (isEditByEventTime(data) && data.get("endTime")!=null) {
//      updateMatchingEvents(eventName, localStartTime, localEndTime, newValue, property);
//    }

  }

  /**
   * This method checks if an event matches the specified criteria (name, start time, end time).
   *
   * @param event     The event to check.
   * @param start     The start time to match.
   * @param end       The end time to match.
   * @param eventName The event name to match.
   * @return true if the event matches; false otherwise.
   */
  protected boolean isMatchingEvent(Event event, LocalDateTime start, LocalDateTime end,

                                    String eventName) {
    return (event.subject.equals(eventName)
            && (start == null || event.startDate.equals(start))
            && (end == null || event.endDate.equals(end)));
  }

  /**
   * This updates matching events based on the provided criteria.
   *
   * @param eventName The name of the event to update.
   * @param start     The start time to match.
   * @param end       The end time to match.
   * @param newValue  The new value for the property.
   * @param property  The property to update.
   * @throws Exception If no matching event is found.
   */
  private void updateMatchingEvents(String eventName, LocalDateTime start, LocalDateTime end,
                                    String newValue, String property) throws Exception {
    boolean found = false;
    for (Event event : calendarStorage) {
      if (isMatchingEvent(event, start, end, eventName)) {
        setPropertyValue(property, newValue, event);
        found = true;
      }
    }

    if (!found) {
      throw new Exception("no matching update");
    }
  }

  /**
   * This method retrieves the events matching the specified metadata.
   *
   * @param allMetaDeta metadata containing criteria for event matching.
   * @return A list of event details in map format.
   */
  public List<Map<String, Object>> getMatchingEvents(Map<String, Object> allMetaDeta) {
    List<Map<String, Object>> eventDetailsList = new ArrayList<>();
    LocalDateTime startDateTime = (LocalDateTime) allMetaDeta.get("localStartTime");
    if (isStartToEndDatePrintCommand(allMetaDeta)) {
      eventDetailsList = getEventsInBetween(startDateTime,
              (LocalDateTime) allMetaDeta.get("localEndTime"));
    } else if (allMetaDeta.get("localEndTime") == null) {
      eventDetailsList = getEventsOnDate(startDateTime);
    }
    return eventDetailsList;
  }

  /**
   * Checks if the metadata indicates a print command for a date range.
   *
   * @param allMetaDeta Metadata containing print criteria.
   * @return true if it's a print command for a date range; false otherwise.
   */
  private boolean isStartToEndDatePrintCommand(Map<String, Object> allMetaDeta) {
    return (allMetaDeta.get("localStartTime") != null && allMetaDeta.get("localEndTime") != null);
  }


  /**
   * Converts an event into a map for easier data access.
   *
   * @param event The event to convert.
   * @return A map containing event details.
   */
  protected Map<String, Object> getEventInMap(Event event) {
    Map<String, Object> mapEvent = new HashMap<>();
    mapEvent.put("subject", (event.subject));
    mapEvent.put("startDate", (event.startDate));
    mapEvent.put("endDate", (event.endDate));
    mapEvent.put("description", (event.description));
    mapEvent.put("location", (event.location));
    mapEvent.put("isPublic", (event.isPublic));
    mapEvent.put("isRecurring", false);
    return mapEvent;
  }

  /**
   * Retrieves events occurring on a specific date.
   *
   * @param onDate The date to retrieve events for.
   * @return A list of event details in map format.
   */
  private List<Map<String, Object>> getEventsOnDate(LocalDateTime onDate) {
    List<Map<String, Object>> events = new ArrayList<>();
    LocalDate localOnDate = onDate.toLocalDate();

    for (Event event : calendarStorage) {
      LocalDate startDate = event.startDate.toLocalDate();
      LocalDate endDate = event.endDate.toLocalDate();

      if (startDate.equals(localOnDate)
              || localOnDate.isAfter(startDate) && localOnDate.isBefore(endDate)) {
        events.add(getEventInMap(event));
      }
    }
    return events;
  }

  /**
   * This method checks if there is an event during the same date.
   *
   * @param date the date to check for.
   * @return true if the schedule is busy else false.
   */
  public boolean isUserBusy(LocalDateTime date) {
    for (Event event : calendarStorage) {
      LocalDateTime startTime = event.startDate;
      LocalDateTime endTime = event.endDate;
      if (isBetween(startTime, date, endTime)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Creates a new event in the calendar with the specified details.
   *
   * @param subject            The subject or title of the event.
   * @param localStartDateTime The start date and time of the event.
   * @param localEndDateTime   The end date and time of the event.
   * @param allMetaDeta        Additional metadata for the event, such as recurrence or auto-decline
   *                           settings.
   * @throws Exception if there's an error in creating the events.
   */
  public void createEvent(String subject,
                          LocalDateTime localStartDateTime,
                          LocalDateTime localEndDateTime,
                          Map<String, Object> allMetaDeta) throws Exception {

    EventFactory factory = new EventFactory();
    Event event = factory.getEvent(subject, localStartDateTime, localEndDateTime, allMetaDeta);
    List<Event> allEvents = event.generateEventsForCalendar();
    putGeneratedEventsIntoCalendar(allEvents, (Boolean) allMetaDeta.get("autoDecline"));
  }

  /**
   * This method retrieves all the events stored in the calendar as a list of maps.
   *
   * @return A list of event details in map format containing event properties.
   */
  @Override
  public List<Map<String, Object>> getAllCalendarEvents() {
    List<Map<String, Object>> events = new ArrayList<>();
    for (Event event : calendarStorage) {
      events.add(getEventInMap(event));
    }
    return events;
  }
}