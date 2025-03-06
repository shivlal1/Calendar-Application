package Model.Calendar;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Model.Event.EventToBeAdded;

public class Calendar extends ACalendar {

  int year, month, day;
  EventToBeAdded event;

  public Calendar() {
  }

  @Override
  public void showStatus() {

  }

  @Override
  public void export() {

  }

  @Override
  public void printEvents() {

  }

  @Override
  public void createEvent(int year, int month, int day, EventToBeAdded event) {

    addData(year, month, day, event);
  }
  
  public void setPropertyValue(String property,String newValue,EventToBeAdded e) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    if (property.equals("name")) {
      e.event.subject = newValue;
    } else if (property.equals("location")) {
      e.event.info.location = newValue;
    }else if(property.contains("start")){
      e.event.startDate = LocalDateTime.parse(newValue,formatter);
    }else if(property.contains("end")){
      e.event.info.endDate = LocalDateTime.parse(newValue,formatter);
    }else if(property.contains("date")){
      e.event.startDate = LocalDateTime.parse(newValue,formatter);
    } else if (property.contains("public")) {
      e.event.info.isPublic = Boolean.parseBoolean(newValue);
    } else if (property.contains("description")) {
      e.event.info.description = newValue;
    } else {
      System.out.println("No such property!");
    }
  }
  
  public void editEvent(int year, int month, int day, LocalDateTime start, String name, String newValue, String property) {
    
    
    if (!yearMonthDayData.containsKey(year)) {
      System.out.println("Year not found: " + year);
      return;
    }

    // Check if the month exists
    Map<Integer, Map<Integer, List<EventToBeAdded>>> monthMap = yearMonthDayData.get(year);
    if (!monthMap.containsKey(month)) {
      System.out.println("Month not found: " + month);
      return;
    }

    // Check if the day exists
    Map<Integer, List<EventToBeAdded>> dayMap = monthMap.get(month);
    if (!dayMap.containsKey(day)) {
      System.out.println("Day not found: " + day);
      return;
    }

    // Get the list of events for the given day
    List<EventToBeAdded> events = dayMap.get(day);
    boolean found = false;

    // Iterate through events and update the matching event
    for (EventToBeAdded event : events) {

      System.out.println("_______________");
      System.out.println(event);

      System.out.println("_______________"+event.event.subject+" "+name);

      if (event.event.subject.equals(name)) {

        setPropertyValue(property,newValue,event);

        //event.startTime = start;
        found = true;
        //System.out.println("Event updated: " + event);
         // Stop searching once found
      }
    }
    if (!found) {
      System.out.println("Event not found: " + name);
    }

    printAllData(yearMonthDayData);

  }

  public void addData(int year, int month, int day, EventToBeAdded value) {
    yearMonthDayData.computeIfAbsent(year, y -> initializeYear(year));

    Map<Integer, Map<Integer, List<EventToBeAdded>>> monthMap = yearMonthDayData.get(year);
    Map<Integer, List<EventToBeAdded>> dayMap = monthMap.get(month);

    // Ensure the day is valid
    if (!dayMap.containsKey(day)) {
      System.out.println("Invalid day: " + day + " for " + year + "-" + month);
      return;
    }

    // Add the value to the corresponding day list
    dayMap.get(day).add(value);
//    System.out.println("ADDED");
    printAllData(yearMonthDayData);

  }


  private Map<Integer, Map<Integer, List<EventToBeAdded>>> initializeYear(int year) {
    Map<Integer, Map<Integer, List<EventToBeAdded>>> monthMap = new HashMap<>();
    for (int month = 1; month <= 12; month++) {
      monthMap.put(month, initializeMonth(year, month));
    }
    return monthMap;
  }

  private Map<Integer, List<EventToBeAdded>> initializeMonth(int year, int month) {
    Map<Integer, List<EventToBeAdded>> dayMap = new HashMap<>();
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


  public  void printAllData(Map<Integer, Map<Integer, Map<Integer, List<EventToBeAdded>>>> dataMap) {
    for (int year : dataMap.keySet()) {
      for (int month : dataMap.get(year).keySet()) {
        for (int day : dataMap.get(year).get(month).keySet()) {
          List<EventToBeAdded> events = dataMap.get(year).get(month).get(day);
          if (!events.isEmpty()) {
            for (EventToBeAdded event : events) {
              System.out.println("----------Printing---------- "+event);
            }
          }
        }
      }
    }
  }
}