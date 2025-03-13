package Model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Utils.DateUtils;

public class Calendar implements ICalendar {

  private List<Event> calendarStorage;

  public Calendar() {
    calendarStorage = new ArrayList();
  }

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

  private void putGeneratedEventsIntoCalendar(List<Event> events, boolean autoDecline) throws Exception {
    if (autoDecline && hasConflicts(events)) {
      throw new Exception("the event conflicts with another event");
    }
    for (Event event : events) {
      calendarStorage.add(event);
    }
  }


  private boolean isBetween(LocalDateTime start, LocalDateTime middle, LocalDateTime end) {
    return ((middle.isAfter(start) || middle.isEqual(start))
            && (middle.isBefore(end) || middle.isEqual(end)));
  }


  private List<Map<String, Object>> getEventsInBetween(LocalDateTime from, LocalDateTime to) {
    List<Map<String, Object>> eventDetails = new ArrayList<>();
    for (Event event : calendarStorage) {
      if (isBetween(from, event.startDate, to)) {
        eventDetails.add(getEventInMap(event));
      }
    }
    return eventDetails;
  }


  public void printEvents() {
    for (Event event : calendarStorage) {
      System.out.println(event);
    }
  }


  private void setPropertyValue(String property, String newValue, Event event) {
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
        System.out.println("No such property!");
    }
  }

  private void updateStartDate(Event event, String newValue) {
    LocalDateTime newDate = DateUtils.pareStringToLocalDateTime(newValue);

    if ((event.canBeEditedToDifferentDay() && newDate.isBefore(event.endDate)) ||
            newDate.toLocalDate().equals(event.endDate.toLocalDate())) {
      event.startDate = (newDate);
    }
  }

  private void updateEndDate(Event event, String newValue) {
    LocalDateTime newDate = DateUtils.pareStringToLocalDateTime(newValue);

    if ((event.canBeEditedToDifferentDay() && newDate.isAfter(event.startDate)) ||
            newDate.toLocalDate().equals(event.startDate.toLocalDate())) {
      event.endDate = (newDate);
    }
  }

  private boolean isEditByEventNameAndStartTime(Map<String, Object> data) {
    return (data.get("eventName") != null && data.get("startTime") != null && data.get("endTime") == null);
  }

  private boolean isEditByEventName(Map<String, Object> data) {
    return (data.get("eventName") != null && data.get("startTime") == null && data.get("endTime") == null);
  }

  private boolean isEditByEventNameAndTime(Map<String, Object> data) {
    return (data.get("eventName") != null && data.get("startTime") != null && data.get("endTime") != null);
  }

  public void editEvent(Map<String, Object> data) throws Exception {

    String newValue = (String) data.get("newValue");
    String property = (String) data.get("property");
    String eventName = (String) data.get("eventName");

    if (isEditByEventNameAndStartTime(data)) {
      updateMatchingEvents(eventName, (LocalDateTime) data.get("localStartTime"), null, newValue, property);
    } else if (isEditByEventName(data)) {
      updateMatchingEvents(eventName, null, null, newValue, property);
    } else if (isEditByEventNameAndTime(data)) {
      updateMatchingEvents(eventName, (LocalDateTime) data.get("localStartTime"),
              (LocalDateTime) data.get("localEndTime"), newValue, property);
    }
  }


  private boolean isMatchingEvent(Event event, LocalDateTime start, LocalDateTime end, String eventName) {
    return (event.subject.equals(eventName) &&
            (start == null || event.startDate.equals(start)) &&
            (end == null || event.endDate.equals(end)));
  }


  private void updateMatchingEvents(String eventName, LocalDateTime start, LocalDateTime end, String newValue, String property) throws Exception {
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

  public List<Map<String, Object>> getMatchingEvents(Map<String, Object> allMetaDeta) {
    List<Map<String, Object>> eventDetailsList = new ArrayList<>();
    LocalDateTime startDateTime = (LocalDateTime) allMetaDeta.get("localStartTime");
    if (isStartToEndDatePrintCommand(allMetaDeta)) {
      eventDetailsList = getEventsInBetween(startDateTime, (LocalDateTime) allMetaDeta.get("localEndTime"));
    } else if (isOnDatePrintCommand(allMetaDeta)) {
      eventDetailsList = getEventsOnDate(startDateTime);
    }
    return eventDetailsList;
  }


  private boolean isStartToEndDatePrintCommand(Map<String, Object> allMetaDeta) {
    return (allMetaDeta.get("localStartTime") != null && allMetaDeta.get("localEndTime") != null);
  }

  private boolean isOnDatePrintCommand(Map<String, Object> allMetaDeta) {
    return (allMetaDeta.get("localStartTime") != null);
  }

  private Map<String, Object> getEventInMap(Event event) {
    Map<String, Object> mapEvent = new HashMap<>();
    mapEvent.put("subject", (event.subject));
    mapEvent.put("startDate", (event.startDate));
    mapEvent.put("endDate", (event.endDate));
    mapEvent.put("description", (event.description));
    mapEvent.put("location", (event.location));
    mapEvent.put("isPublic", (event.isPublic));
    return mapEvent;
  }

  private List<Map<String, Object>> getEventsOnDate(LocalDateTime onDate) {
    List<Map<String, Object>> events = new ArrayList<>();

    LocalDate localOnDate = onDate.toLocalDate();

    for (Event event : calendarStorage) {
      LocalDate startDate = event.startDate.toLocalDate();
      LocalDate endDate = event.endDate.toLocalDate();

      if (startDate.equals(localOnDate) ||
              localOnDate.isAfter(startDate) && localOnDate.isBefore(endDate)) {
        events.add(getEventInMap(event));
      }
    }
    return events;
  }


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


  public void createEvent(String subject,
                          LocalDateTime localStartDateTime,
                          LocalDateTime localEndDateTime,
                          Map<String, Object> allMetaDeta) throws Exception {

    EventFactory factory = new EventFactory();
    Event event = factory.getEvent(subject, localStartDateTime, localEndDateTime, allMetaDeta);

    List<Event> allEvents = event.generateEventsForCalendar();

    if (event.isAutoDeclineEnabled()) {
      putGeneratedEventsIntoCalendar(allEvents, true);
    } else {
      putGeneratedEventsIntoCalendar(allEvents, (Boolean) allMetaDeta.get("autoDecline"));
    }
  }

  @Override
  public List<Map<String, Object>> getAllCalendarEvents() {
    List<Map<String, Object>> events = new ArrayList<>();

    for (Event event : calendarStorage) {
      events.add(getEventInMap(event));
    }
    return events;

  }
}