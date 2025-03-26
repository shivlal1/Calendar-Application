package model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CalendarExtended
        extends Calendar
        implements ICalendarExtended {

  private String calendarName;
  private String timeZone;

  public CalendarExtended(String calendarName, String timeZone) {
    super();
    this.calendarName = calendarName;
    this.timeZone = timeZone;
  }


 @Override
  public void changeCalendarName(String newValue) {
      this.calendarName = newValue;
  }

  @Override
  public void changeCalendarTimeZone(String newTimeZone) {
     this.timeZone = newTimeZone;
  }



  public void createEvent(String subject,
                          LocalDateTime localStartDateTime,
                          LocalDateTime localEndDateTime,
                          Map<String, Object> allMetaDeta) throws Exception {

    allMetaDeta.put("autoDecline", true);
    super.createEvent(subject, localStartDateTime, localEndDateTime, allMetaDeta);
  }


  protected boolean isMatchingEvent(Event event, LocalDateTime start, LocalDateTime end,
                                    String eventName) {

    return (super.isMatchingEvent(event, start, end, eventName) ||
            (event.subject.equals(eventName)
                    && start != null && end == null
                    && event.startDate.compareTo(start) > 0));
  }

  List<Map<String, Object>> getEventsToCopy(Map<String, Object> metaDetails) {
    List<Map<String, Object>> events = new ArrayList<>();
    String copyType = (String) metaDetails.get("copyType");
    for (Event event : calendarStorage) {
      if (copyType.equals("eventsOnDate")) {
        LocalDate onDate = (LocalDate) metaDetails.get("onDate");
        LocalDate startDate = event.startDate.toLocalDate();
        if (startDate.equals(onDate)) {
          Map<String, Object> carbonCopyEvent = getEventInMap(event);
          events.add(carbonCopyEvent);
        }
      } else if (copyType.equals("eventsOnDateWithTime")) {
        LocalDateTime onDateTime = (LocalDateTime) metaDetails.get("onDateTime");
        if (event.startDate.equals(onDateTime)) {
          Map<String, Object> carbonCopyEvent = getEventInMap(event);
          events.add(carbonCopyEvent);
        }
      } else if (copyType.equals("eventsInBetween")) {
        LocalDateTime startDate = (LocalDateTime) metaDetails.get("fromDate");
        LocalDateTime toDate = (LocalDateTime) metaDetails.get("toDate");
        if (isBetween(startDate, event.startDate, toDate)) {
          Map<String, Object> carbonCopyEvent = getEventInMap(event);
          events.add(carbonCopyEvent);
        }
      }
    }
    return events;
  }

  @Override
  public void copyToTargetCalendar(ICalendarExtended targetCalendar, Map<String, Object> metaDetails) throws Exception {
    List<Map<String, Object>> copyMetaDetailsEvents = getEventsToCopy(metaDetails);

    for (Map<String, Object> eventDetails : copyMetaDetailsEvents) {
      String Subject = (String) eventDetails.get("subject");
      LocalDateTime startDate = (LocalDateTime) eventDetails.get("startDate");
      LocalDateTime endDate = (LocalDateTime) eventDetails.get("endDate");
      metaDetails.put("autoDecline", true);
      targetCalendar.createEvent(Subject, startDate, endDate, metaDetails);
    }
  }


}