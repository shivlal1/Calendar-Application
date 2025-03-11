package Model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import Utils.DateUtils;

public class Calendar extends ACalendar {

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
   * CREATE EVENT function
   **/


  private void putGeneratedEventsIntoCalendar(List<Event> events, boolean autoDecline) {

    if (autoDecline && hasConflicts(events)) {
      System.out.println("HAS CONFLICTS");
      return;
    }

    System.out.println("trying to create");
    for (Event event : events) {
      calendarStorage.add(event);
    }
  }


  private boolean isBetween(LocalDateTime start, LocalDateTime middle, LocalDateTime end) {
    return ((middle.isAfter(start) || middle.isEqual(start))
            && (middle.isBefore(end) || middle.isEqual(end)));
  }

  public List<Event> getEventsInBetween(LocalDateTime from, LocalDateTime to) {
    List<Event> eventDetails = new ArrayList<>();
    for (Event event : calendarStorage) {
      if (isBetween(from, event.getStartDate(), to)) {
        eventDetails.add(event);
      }
    }
    return eventDetails;
  }


  private void makeCsvFile(List<Event> events) {

    for (Event e : events) {
      StringBuilder csvString;

      if (e.getSubject() != null) {
        e.getSubject();
      }
    }
  }

  public String exportCalendarAndGetFilePath() {
    String filePath = "output.csv";
    // List<Event> events = getEventsToExport();
    //  makeCsvFile(events);
    return filePath;
  }

  public void printEvents() {

    for (Event event : calendarStorage) {
      System.out.println(event);
    }
  }


  public void setPropertyValue(String property, String newValue, Event event) {
    switch (property) {
      case "name":
        event.setSubject(newValue);
        break;
      case "location":
        event.setLocation(newValue);
        break;
      case "startDate":
        updateStartDate(event, newValue);
        break;

      case "endDate":
        updateEndDate(event, newValue);
        break;

      case "isPublic":
        event.setIsPublic(Boolean.parseBoolean(newValue));
        break;
      case "description":
        event.setDescription(newValue);
        break;
      default:
        System.out.println("No such property!");
    }
  }

  private void updateStartDate(Event event, String newValue) {
    LocalDateTime newDate = DateUtils.pareStringToLocalDateTime(newValue);

    if ((event.canBeEditedToDifferentDay() && newDate.isBefore(event.getEndDate())) ||
            newDate.toLocalDate().equals(event.getEndDate().toLocalDate())) {
      event.setStartDate(newDate);
    }
  }

  private void updateEndDate(Event event, String newValue) {
    LocalDateTime newDate = DateUtils.pareStringToLocalDateTime(newValue);

    if ((event.canBeEditedToDifferentDay() && newDate.isAfter(event.getStartDate())) ||
            newDate.toLocalDate().equals(event.getStartDate().toLocalDate())) {
      event.setEndDate(newDate);
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

  public void editEvent(Map<String, Object> data) {

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
    return (event.getSubject().equals(eventName) &&
            (start == null || event.getStartDate().equals(start)) &&
            (end == null || event.getEndDate().equals(end)));
  }


  private void updateMatchingEvents(String eventName, LocalDateTime start, LocalDateTime end, String newValue, String property) {
    boolean found = false;

    for (Event event : calendarStorage) {
      if (isMatchingEvent(event, start, end, eventName)) {
        setPropertyValue(property, newValue, event);
      }
    }

    if (!found) {
      //  System.out.println("Event not found: " + eventName);
    }
  }

  public List<Event> getMatchingEvents(Map<String, Object> allMetaDeta) {

    List<Event> eventDetailsList = new ArrayList<>();
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


  private List<Event> getEventsOnDate(LocalDateTime onDate) {
    List<Event> events = new ArrayList<>();

    for (Event event : calendarStorage) {
      if (event.getStartDate().toLocalDate().equals(onDate.toLocalDate())) {
        events.add(event);
      }
    }
    return events;
  }


  public boolean isUserBusy(LocalDateTime date) {
    for (Event event : calendarStorage) {
      LocalDateTime startTime = event.getStartDate();
      LocalDateTime endTime = event.getEndDate();
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

}