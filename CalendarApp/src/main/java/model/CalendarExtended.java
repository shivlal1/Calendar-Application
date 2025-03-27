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

public class CalendarExtended
        extends Calendar
        implements ICalendarExtended {

  private ZoneId timeZone;

  public CalendarExtended(ZoneId timeZone) {
    super();
    this.timeZone = timeZone;
  }

  @Override
  public void changeCalendarTimeZone(ZoneId newTimeZone) {
    for (Event events : calendarStorage) {
      events.startDate = DateUtils.changeTimeZone(events.startDate, this.timeZone, newTimeZone);
      events.endDate = DateUtils.changeTimeZone(events.endDate, this.timeZone, newTimeZone);
    }
    this.timeZone = newTimeZone;
  }

  @Override
  public ZoneId getCalendarTimeZone() {
    return this.timeZone;
  }

  protected boolean isMatchingEvent(Event event, LocalDateTime start, LocalDateTime end, String eventName) {

    return super.isMatchingEvent(event, start, end, eventName)
            || (event.subject.equals(eventName)
            && (start != null && event.startDate.compareTo(start) >= 0));
  }

  private boolean isBetweenDates(LocalDate start, LocalDate middle, LocalDate end) {
    return ((middle.isAfter(start) || middle.isEqual(start))
            && (middle.isBefore(end) || middle.isEqual(end)));
  }

  private List<Event> filterEventsOnDateWithTime(Map<String, Object> metaDetails) {
    List<Event> events = new ArrayList<>();
    EventFactory factory = new EventFactory();
    for (Event event : calendarStorage) {
      LocalDateTime onDateTime = (LocalDateTime) metaDetails.get("onDateTime");
      if (event.subject.equals((String) metaDetails.get("eventName"))
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

  private Event getNewCarbonCopy(Event event, ICalendarExtended targetCalendar,
                                 LocalDateTime newStartDateTime, LocalDateTime newEndDateTime) {
    EventFactory factory = new EventFactory();
    newStartDateTime = DateUtils.changeTimeZone(newStartDateTime, this.timeZone,
            targetCalendar.getCalendarTimeZone());
    newEndDateTime = DateUtils.changeTimeZone(newEndDateTime, this.timeZone,
            targetCalendar.getCalendarTimeZone());

    return factory.getCarbonCopy(event, newStartDateTime, newEndDateTime);

  }

  private List<Event> filterEventsOnDate(ICalendarExtended targetCalendar, Map<String, Object> metaDetails) {
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

  private List<Event> filteredEventsInBetween(Map<String, Object> metaDetails, ICalendarExtended targetCalendar) {
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

  private List<Event> filterEventsToCopy(Map<String, Object> metaDetails, ICalendarExtended targetCalendar) {
    String copyType = (String) metaDetails.get("copyType");
    if (copyType.equals("eventsOnDateWithTime")) {
      return filterEventsOnDateWithTime(metaDetails);
    } else if (copyType.equals("eventsOnDate")) {
      return filterEventsOnDate(targetCalendar, metaDetails);
    } else {
      return filteredEventsInBetween(metaDetails, targetCalendar);
    }
  }

  @Override
  public void copyToTargetCalendar(ICalendarExtended targetCalendar, Map<String, Object> metaDetails) throws Exception {
    List<Event> newFilteredEventsList = filterEventsToCopy(metaDetails, targetCalendar);
    CalendarExtended newTargetCalendar = (CalendarExtended) targetCalendar;

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

  @Override
  public void createEvent(String subject, LocalDateTime localStartDateTime,
                          LocalDateTime localEndDateTime,
                          Map<String, Object> allMetaDeta) throws Exception {

    allMetaDeta.put("autoDecline", true);
    super.createEvent(subject, localStartDateTime, localEndDateTime, allMetaDeta);
  }
}