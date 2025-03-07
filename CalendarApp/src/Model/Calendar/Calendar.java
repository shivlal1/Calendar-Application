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

  @Override
  public void showStatus() {

  }

  @Override
  public void export() {

  }

  @Override
  public void createEvent(int year, int month, int day, CalendarEvent event) {

    addData(year, month, day, event);
  }

  public void setPropertyValue(String property, String newValue, CalendarEvent e) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    if (property.equals("name")) {
      e.getEvent().setSubject(newValue);
    } else if (property.equals("location")) {
      e.getEvent().setLocation(newValue);
    } else if (property.contains("start")) {
      e.getEvent().setStartDate(LocalDateTime.parse(newValue, formatter));
    } else if (property.contains("end")) {
      e.getEvent().setEndDate(LocalDateTime.parse(newValue, formatter));
    } else if (property.contains("date")) {
      e.getEvent().setStartDate(LocalDateTime.parse(newValue, formatter));
    } else if (property.contains("public")) {
      e.getEvent().setIsPublic(Boolean.parseBoolean(newValue));
    } else if (property.contains("description")) {
      e.getEvent().setDescription(newValue);
    } else {
      System.out.println("No such property!");
    }
  }


  public void printOnTimeEvents(LocalDateTime from) {

    int year = from.getYear();
    int month = from.getMonthValue();
    int day = from.getDayOfMonth();

    if (!yearMonthDayData.containsKey(year)) {
      System.out.println("Year not found: " + year);
      return;
    }

    Map<Integer, Map<Integer, List<CalendarEvent>>> monthMap = yearMonthDayData.get(year);
    if (!monthMap.containsKey(month)) {
      System.out.println("Month not found: " + month);
      return;
    }

    Map<Integer, List<CalendarEvent>> dayMap = monthMap.get(month);
    if (!dayMap.containsKey(day)) {
      System.out.println("Day not found: " + day);
      return;
    }

    List<CalendarEvent> events = dayMap.get(day);

    for (CalendarEvent event : events) {
              System.out.println("Event updated: " + event);
    }


  }

  public void printFromToEvents(LocalDateTime from, LocalDateTime to) {

    int year = from.getYear();
    int month = from.getMonthValue();
    int day = from.getDayOfMonth();

    if (!yearMonthDayData.containsKey(year)) {
      System.out.println("Year not found: " + year);
      return;
    }

    Map<Integer, Map<Integer, List<CalendarEvent>>> monthMap = yearMonthDayData.get(year);
    if (!monthMap.containsKey(month)) {
      System.out.println("Month not found: " + month);
      return;
    }

    Map<Integer, List<CalendarEvent>> dayMap = monthMap.get(month);
    if (!dayMap.containsKey(day)) {
      System.out.println("Day not found: " + day);
      return;
    }

    List<CalendarEvent> events = dayMap.get(day);

    for (CalendarEvent event : events) {
     // System.out.println("Event updated: " + event);
      //System.out.println("ater: " + event.getEvent().getStartDate().isAfter(from));

      if ( (event.getEvent().getStartDate().isAfter(from) || event.getEvent().getStartDate().equals(from))
              && ( event.getEvent().getEndDate().isBefore(to) || event.getEvent().getEndDate().equals(to))) {
        System.out.println("Event updated: " + event);
      }
    }


  }

  public void editFromToEvents(int year, int month, int day, String eventName, LocalDateTime start, LocalDateTime end, String newValue, String property) {
    if (!yearMonthDayData.containsKey(year)) {
      System.out.println("Year not found: " + year);
      return;
    }

    Map<Integer, Map<Integer, List<CalendarEvent>>> monthMap = yearMonthDayData.get(year);
    if (!monthMap.containsKey(month)) {
      System.out.println("Month not found: " + month);
      return;
    }

    Map<Integer, List<CalendarEvent>> dayMap = monthMap.get(month);
    if (!dayMap.containsKey(day)) {
      System.out.println("Day not found: " + day);
      return;
    }

    List<CalendarEvent> events = dayMap.get(day);
    boolean found = false;

    for (CalendarEvent event : events) {

      System.out.println("_______________");
      System.out.println(event);

      //System.out.println("_______________" + event.event.get + " " + name);

      if (event.getEvent().getSubject().equals(eventName) && event.getEvent().getStartDate().equals(start)
      && event.getEvent().getEndDate().equals(end)) {

        setPropertyValue(property, newValue, event);

        //event.startTime = start;
        found = true;
//        System.out.println("z");
        System.out.println("Event updated: " + event);
        // Stop searching once found
      }
    }
    if (!found) {
      System.out.println("Event not found: " + eventName);
    }
    printEvents();
  }


  public void editAllEvents(String eventName, String newValue, String property) {
    Map<Integer, Map<Integer, Map<Integer, List<CalendarEvent>>>> dataMap = yearMonthDayData;
    for (int year : dataMap.keySet()) {
      for (int month : dataMap.get(year).keySet()) {
        for (int day : dataMap.get(year).get(month).keySet()) {
          List<CalendarEvent> events = dataMap.get(year).get(month).get(day);
          if (!events.isEmpty()) {
            for (CalendarEvent event : events) {
              if (event.getEvent().getSubject().equals(eventName)) {
                setPropertyValue(property,newValue,event);
                System.out.println("Event edited successfully!");
              }
            }
          }
        }
      }
    }
    printEvents();
  }

  public void editEvent(int year, int month, int day, LocalDateTime start, String name, String newValue, String property) {
    if (!yearMonthDayData.containsKey(year)) {
      System.out.println("Year not found: " + year);
      return;
    }

    Map<Integer, Map<Integer, List<CalendarEvent>>> monthMap = yearMonthDayData.get(year);
    if (!monthMap.containsKey(month)) {
      System.out.println("Month not found: " + month);
      return;
    }

    Map<Integer, List<CalendarEvent>> dayMap = monthMap.get(month);
    if (!dayMap.containsKey(day)) {
      System.out.println("Day not found: " + day);
      return;
    }

    List<CalendarEvent> events = dayMap.get(day);
    boolean found = false;

    for (CalendarEvent event : events) {

      System.out.println("_______________");
      System.out.println(event);

      //System.out.println("_______________" + event.event.get + " " + name);

      if (event.getEvent().getSubject().equals(name) && event.getEvent().getStartDate().equals(start)) {

        setPropertyValue(property, newValue, event);

        //event.startTime = start;
        found = true;
//        System.out.println("z");
        System.out.println("Event updated: " + event);
        // Stop searching once found
      }
    }
    if (!found) {
      System.out.println("Event not found: " + name);
    }


  }

  public void addData(int year, int month, int day, CalendarEvent value) {
    yearMonthDayData.computeIfAbsent(year, y -> initializeYear(year));

    Map<Integer, Map<Integer, List<CalendarEvent>>> monthMap = yearMonthDayData.get(year);
    Map<Integer, List<CalendarEvent>> dayMap = monthMap.get(month);

    // Ensure the day is valid
    if (!dayMap.containsKey(day)) {
      System.out.println("Invalid day: " + day + " for " + year + "-" + month);
      return;
    }

    dayMap.get(day).add(value);

    printEvents();
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
              System.out.println(event);
            }
          }
        }
      }
    }
  }
}