package Model.Calendar;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Model.Event.CalendarEvent;

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

  public void printFromToEvents(LocalDateTime from, LocalDateTime to) {

    List<CalendarEvent> events = getEventsForDate(from);

    for (CalendarEvent event : events) {

      if ((event.getEvent().getStartDate().isAfter(from) || event.getEvent().getStartDate().equals(from))
              && (event.getEvent().getEndDate().isBefore(to) || event.getEvent().getEndDate().equals(to))) {
        System.out.println("Event updated: " + event);
      }
    }

  }

  public void editEvent(String eventName, LocalDateTime start, LocalDateTime end, String newValue, String property) {

    List<CalendarEvent> events = getEventsForDate(start);
    boolean found = false;

    for (CalendarEvent event : events) {

      System.out.println(event);


      if (event.getEvent().getSubject().equals(eventName) && event.getEvent().getStartDate().equals(start)
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
  }


  public void editEvent(String eventName, String newValue, String property) {
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

  public void editEvent(LocalDateTime start, String name, String newValue, String property) {

    List<CalendarEvent> events = getEventsForDate(start);
    boolean found = false;

    for (CalendarEvent event : events) {

      if (event.getEvent().getSubject().equals(name) && event.getEvent().getStartDate().equals(start)) {

        setPropertyValue(property, newValue, event);

        found = true;
        System.out.println("Event updated: " + event);
      }
    }
    if (!found) {
      System.out.println("Event not found: " + name);
    }


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

}