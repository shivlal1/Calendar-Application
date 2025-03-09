package Model.Calendar;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Controller.MetaData.EditEventMetaDetails;
import Controller.MetaData.PrintEventMetaDetails;
import Model.Event.CalendarEvent;
import Model.Event.EventDetails;

public class Calendar extends ACalendar {

  CalendarEvent event;

  public Calendar() {
  }

  boolean isOverlap(CalendarEvent newEvent, CalendarEvent existingEvent) {

    LocalDateTime newStartTime = newEvent.getStartDate();
    LocalDateTime newEndTime = newEvent.getEndDate();

    LocalDateTime existingStartTime = existingEvent.getStartDate();
    LocalDateTime existingEndTime = existingEvent.getEndDate();

    boolean isConflictExists = false;

    if (newStartTime.isBefore(existingEndTime) &&
            newEndTime.equals(existingStartTime)) {
      isConflictExists = false;
    }
    if (existingStartTime.isBefore(newEndTime) &&
            existingEndTime.equals(newStartTime)) {
      isConflictExists = false;
    }

    if (newStartTime.isBefore(existingEndTime) && existingStartTime.isBefore(newEndTime)) {
      isConflictExists = true;
    }

    if (existingStartTime.isBefore(newEndTime) && newStartTime.isBefore(existingEndTime)) {
      isConflictExists = true;
    }

    if (existingStartTime.equals(newStartTime)) {
      isConflictExists = true;
    }

    if (existingEndTime.equals(newEndTime)) {
      isConflictExists = true;
    }
    return isConflictExists;
  }

  boolean hasConflicts(List<CalendarEvent> newEvents) {

    Map<Integer, Map<Integer, Map<Integer, List<CalendarEvent>>>> dataMap = yearMonthDayData;

    for (CalendarEvent newEvent : newEvents) {

      for (int year : dataMap.keySet()) {
        for (int month : dataMap.get(year).keySet()) {
          for (int day : dataMap.get(year).get(month).keySet()) {
            List<CalendarEvent> events = dataMap.get(year).get(month).get(day);
            if (!events.isEmpty()) {
              for (CalendarEvent existingEvent : events) {
                if (isOverlap(newEvent, existingEvent)) {
                  return true;
                }
              }
            }
          }
        }
      }

    }

    return false;
  }

  @Override
  public void showStatus() {

  }

  @Override
  public void export() {

  }

  /**
   * CREATE EVENT function
   **/

  public void createEvent(int year, int month, int day, CalendarEvent event) {
    // addData(year, month, day, event);
    yearMonthDayData.computeIfAbsent(year, y -> initializeYear(year));

    Map<Integer, Map<Integer, List<CalendarEvent>>> monthMap = yearMonthDayData.get(year);
    Map<Integer, List<CalendarEvent>> dayMap = monthMap.get(month);

    // Ensure the day is valid
    if (!dayMap.containsKey(day)) {
      System.out.println("Invalid day: " + day + " for " + year + "-" + month);
      return;
    }

    dayMap.get(day).add(event);

    //printEvents();
  }

  public void createEvent(List<CalendarEvent> events, boolean autoDecline) {

    if (autoDecline && hasConflicts(events)) {
      System.out.println("HAS CONFLICTS");
      return;
    }

    for (CalendarEvent event : events) {
      LocalDateTime date = event.getStartDate();
      int year = date.getYear();
      int month = date.getMonthValue();
      int day = date.getDayOfMonth();
      yearMonthDayData.computeIfAbsent(year, y -> initializeYear(year));
      Map<Integer, Map<Integer, List<CalendarEvent>>> monthMap = yearMonthDayData.get(year);
      Map<Integer, List<CalendarEvent>> dayMap = monthMap.get(month);

      /* check if this can be uncommented lateer
      if (!dayMap.containsKey(day)) {
        System.out.println("Invalid day: " + day + " for " + year + "-" + month);
        return;
      }*/
      dayMap.get(day).add(event);
    }

  }


  public void printOnTimeEvents(LocalDateTime from) {
    List<CalendarEvent> events = getEventsForDate(from);
    for (CalendarEvent event : events) {
      System.out.println("Event updated: " + event);
    }
  }

  public List<EventDetails> printFromToEvents(LocalDateTime from, LocalDateTime to) {

    List<CalendarEvent> events = getEventsForDate(from);
    List<EventDetails> matchingDetails = new ArrayList<>();
    for (CalendarEvent event : events) {

      if ((event.getEvent().getStartDate().isAfter(from) || event.getEvent().getStartDate().equals(from))
              && (event.getEvent().getEndDate().isBefore(to) || event.getEvent().getEndDate().equals(to))) {
        matchingDetails.add(event.getEvent());
        System.out.println("Matched event: " + event);
      }
    }
    return matchingDetails;
  }

  private Map<Integer, Map<Integer, List<CalendarEvent>>> initializeYear(int year) {
    Map<Integer, Map<Integer, List<CalendarEvent>>> monthMap = new HashMap<>();
    for (int month = 1; month <= 12; month++) {
      monthMap.put(month, initializeMonth(year, month));
    }
    return monthMap;
  }

  private Map<Integer, List<CalendarEvent>> initializeMonth(int year, int month) {
    Map<Integer, List<CalendarEvent>> dayMap = new HashMap<>();
    int daysInMonth = getDaysInMonth(year, month);
    for (int day = 1; day <= daysInMonth; day++) {
      dayMap.put(day, new ArrayList<>());
    }
    return dayMap;
  }

  private int getDaysInMonth(int year, int month) {
    switch (month) {
      case 2: // February
        return isLeapYear(year) ? 29 : 28;
      case 4:
      case 6:
      case 9:
      case 11: // April, June, September, November
        return 30;
      default: // All other months
        return 31;
    }
  }

  private boolean isLeapYear(int year) {
    return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
  }


  @Override
  public void printEvents() {
    Map<Integer, Map<Integer, Map<Integer, List<CalendarEvent>>>> dataMap = yearMonthDayData;
    for (int year : dataMap.keySet()) {
      for (int month : dataMap.get(year).keySet()) {
        for (int day : dataMap.get(year).get(month).keySet()) {
          List<CalendarEvent> events = dataMap.get(year).get(month).get(day);
          if (!events.isEmpty()) {
            for (CalendarEvent event : events) {
              System.out.println("year :" + year + " month " + month + " date " + day + " " + event);
            }
          }
        }
      }
    }
  }


  private List<CalendarEvent> getEventsForDate(LocalDateTime start) {

    int year = start.getYear();
    int month = start.getMonthValue();
    int day = start.getDayOfMonth();

    if (!yearMonthDayData.containsKey(year)) {
      System.out.println("Year not found: " + year);
      return null;
    }

    Map<Integer, Map<Integer, List<CalendarEvent>>> monthMap = yearMonthDayData.get(year);

    if (!monthMap.containsKey(month)) {
      System.out.println("Month not found: " + month);
      return null;
    }

    Map<Integer, List<CalendarEvent>> dayMap = monthMap.get(month);

    if (!dayMap.containsKey(day)) {
      System.out.println("Day not found: " + day);
      return null;
    }

    return dayMap.get(day);
  }

  public void setPropertyValue(String property, String newValue, CalendarEvent e) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    switch (property) {
      case "name":
        e.getEvent().setSubject(newValue);
        break;
      case "location":
        e.getEvent().setLocation(newValue);
        break;
      case "start":
      case "date":
        e.getEvent().setStartDate(LocalDateTime.parse(newValue, formatter));
        break;
      case "end":
        e.getEvent().setEndDate(LocalDateTime.parse(newValue, formatter));
        break;
      case "public":
        e.getEvent().setIsPublic(Boolean.parseBoolean(newValue));
        break;
      case "description":
        e.getEvent().setDescription(newValue);
        break;
      default:
        System.out.println("No such property!");
    }
  }

  private boolean isEditByEventNameAndStartTime(EditEventMetaDetails data) {
    return (data.getEventName() != null && data.getStartTime() != null && data.getEndTime() == null);
  }

  private boolean isEditByEventName(EditEventMetaDetails data) {
    return (data.getEventName() != null && data.getStartTime() == null && data.getEndTime() == null);
  }

  private boolean isEditByEventNameAndTime(EditEventMetaDetails data) {
    return (data.getEventName() != null && data.getStartTime() != null && data.getEndTime() != null);
  }


  public void editEvent(EditEventMetaDetails data) {

    String newValue = data.getNewValue();
    String property = data.getProperty();
    String eventName = data.getEventName();

    if (isEditByEventNameAndStartTime(data)) {
      editEventUtil(data.getLocalStartTime(), eventName, newValue, property);
    } else if (isEditByEventName(data)) {
      editEventUtil(eventName, newValue, property);
    } else if (isEditByEventNameAndTime(data)) {
      editEventUtil(eventName, data.getLocalStartTime(), data.getLocalEndTime(),
              newValue, property);
    }

  }



 /* public void editEventUtil(String eventName, String newValue, String property) {
    Map<Integer, Map<Integer, Map<Integer, List<CalendarEvent>>>> dataMap = yearMonthDayData;
    for (int year : dataMap.keySet()) {
      for (int month : dataMap.get(year).keySet()) {
        for (int day : dataMap.get(year).get(month).keySet()) {
          List<CalendarEvent> events = dataMap.get(year).get(month).get(day);
          if (!events.isEmpty()) {
            for (CalendarEvent event : events) {
              if (event.getEvent().getSubject().equals(eventName)) {
                setPropertyValue(property, newValue, event);
                System.out.println("Event edited successfully!");
              }
            }
          }
        }
      }
    }
    //printEvents();
  }

  public void editEventUtil(LocalDateTime start, String name, String newValue, String property) {
    List<CalendarEvent> events = getEventsForDate(start);
    boolean found = false;
    for (CalendarEvent event : events) {
      if (event.getEvent().getSubject().equals(name) &&
              event.getEvent().getStartDate().equals(start)) {
        setPropertyValue(property, newValue, event);
        found = true;
        System.out.println("Event updated: " + event);
      }
    }
    if (!found) {
      System.out.println("Event not found: " + name);
    }
  }

  public void editEventUtil(String eventName, LocalDateTime start, LocalDateTime end, String newValue, String property) {
    List<CalendarEvent> events = getEventsForDate(start);
    boolean found = false;
    for (CalendarEvent event : events) {
      System.out.println(event);
      if (event.getEvent().getSubject().equals(eventName) &&
              event.getEvent().getStartDate().equals(start)
              && event.getEvent().getEndDate().equals(end)) {

        setPropertyValue(property, newValue, event);
        found = true;
        System.out.println("Event updated: " + event);
      }
    }
    if (!found) {
      System.out.println("Event not found: " + eventName);
    }
    //printEvents();
  } */

  public void editEventUtil(String eventName, LocalDateTime start, LocalDateTime end, String newValue, String property) {
    List<CalendarEvent> events = getEventsForDate(start);
    updateMatchingEvents(events, eventName, start, end, newValue, property);
  }

  public void editEventUtil(String eventName, String newValue, String property) {
    for (int year : yearMonthDayData.keySet()) {
      for (int month : yearMonthDayData.get(year).keySet()) {
        for (int day : yearMonthDayData.get(year).get(month).keySet()) {
          List<CalendarEvent> events = yearMonthDayData.get(year).get(month).get(day);
          updateMatchingEvents(events, eventName, null, null, newValue, property);
        }
      }
    }
  }

  public void editEventUtil(LocalDateTime start, String name, String newValue, String property) {
    List<CalendarEvent> events = getEventsForDate(start);
    updateMatchingEvents(events, name, start, null, newValue, property);
  }

  private void updateMatchingEvents(List<CalendarEvent> events, String eventName, LocalDateTime start, LocalDateTime end, String newValue, String property) {
    boolean found = false;

    for (CalendarEvent event : events) {
      if (event.getEvent().getSubject().equals(eventName) &&
              (start == null || event.getEvent().getStartDate().equals(start)) &&
              (end == null || event.getEvent().getEndDate().equals(end))) {

        setPropertyValue(property, newValue, event);
        found = true;
        System.out.println("Event updated: " + event);
      }
    }

    if (!found) {
      //  System.out.println("Event not found: " + eventName);
    }
  }

  public List<EventDetails> getMatchingEvents(PrintEventMetaDetails allMetaDeta) {

    List<EventDetails> eventDetailsList = new ArrayList<>();
    LocalDateTime startDateTime = allMetaDeta.getLocalStartDate();
    if (isStartToEndDatePrintCommand(allMetaDeta)) {
      eventDetailsList = printFromToEvents(startDateTime, allMetaDeta.getLocalEndDate());
    } else if (isOnDatePrintCommand(allMetaDeta)) {
      eventDetailsList = getEventsOnDateToPrint(startDateTime);
    }

    return eventDetailsList;

  }


  private boolean isStartToEndDatePrintCommand(PrintEventMetaDetails allMetaDeta) {
    return (allMetaDeta.getLocalStartDate() != null && allMetaDeta.getLocalEndDate() != null);
  }

  private boolean isOnDatePrintCommand(PrintEventMetaDetails allMetaDeta) {
    return (allMetaDeta.getLocalStartDate() != null);
  }


  private List<EventDetails> getEventsOnDateToPrint(LocalDateTime start) {

    int year = start.getYear();
    int month = start.getMonthValue();
    int day = start.getDayOfMonth();

    if (!yearMonthDayData.containsKey(year)) {
      System.out.println("Year not found: " + year);
      return null;
    }

    Map<Integer, Map<Integer, List<CalendarEvent>>> monthMap = yearMonthDayData.get(year);

    if (!monthMap.containsKey(month)) {
      System.out.println("Month not found: " + month);
      return null;
    }

    Map<Integer, List<CalendarEvent>> dayMap = monthMap.get(month);

    if (!dayMap.containsKey(day)) {
      System.out.println("Day not found: " + day);
      return null;
    }

    List<EventDetails> details = new ArrayList<>();
    for (CalendarEvent e : dayMap.get(day)) {
      details.add(e.getEvent());
    }

    return details;
  }

  private boolean isBetween(LocalDateTime start, LocalDateTime middle, LocalDateTime end) {
    return ((middle.isAfter(start) || middle.isEqual(start))
            && (middle.isBefore(end) || middle.isEqual(end)));
  }

  public boolean isBusyOnDay(LocalDateTime date) {

    List<EventDetails> eventDetails = getEventsOnDateToPrint(date);

    for (EventDetails event : eventDetails) {
      LocalDateTime startTime = event.getStartDate();
      LocalDateTime endTime = event.getEndDate();
      if (isBetween(startTime, date, endTime)) {
        return true;
      }
    }
    return false;
  }
}