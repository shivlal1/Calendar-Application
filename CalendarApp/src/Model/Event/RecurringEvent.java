package Model.Event;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import Controller.MetaData.EventMetaDetails;
import Model.Calendar.ACalendar;
import Model.Utils.DateUtils;

public class RecurringEvent extends AEvent {

  private EventDetails eventDetails;
  private EventMetaDetails allMetaDetails;

  RecurringEvent(EventDetails eventDetails, EventMetaDetails allMetaDetails) {
    this.eventDetails = eventDetails;
    this.allMetaDetails = allMetaDetails;
  }

  private LocalDateTime getRecurringEventEndDate() {
    String finalUntilDateTime = allMetaDetails.getAddUntilDateTime();
    return DateUtils.stringToLocalDateTime(finalUntilDateTime);
  }

  private boolean isWeekDayIncluded(LocalDateTime date) {

    DayOfWeek day = date.getDayOfWeek();
    char dayNameAsChar = getDayAbbreviation(day.name());

    if (allMetaDetails.getWeekdays().indexOf(dayNameAsChar) != -1) {
      return true;
    }
    return false;
  }

  private List<CalendarEvent> getForTimeRecurringEvent(ACalendar calendar, LocalDateTime start,
                                                       LocalDateTime end, List<CalendarEvent> eventsList) {

    LocalDateTime currDate = start;

    int requriredRecurringEvents = Integer.valueOf(allMetaDetails.getForTimes());
    int eventsEncountered = 0;

    while (eventsEncountered < requriredRecurringEvents) {

      if (isWeekDayIncluded(currDate)) {
        EventDetails e = new EventDetails(eventDetails, currDate, end);
        eventsList.add(getCreatedSegmentEvent(calendar, currDate, e, end));
        eventsEncountered++;
      }

      if (eventsEncountered == requriredRecurringEvents) {
        break;
      }
      currDate = currDate.plusDays(1);
      end = end.plusDays(1);
    }
    return eventsList;
  }

  private List<CalendarEvent> getUntilTimeRecurringEvent(ACalendar calendar, LocalDateTime start,
                                                         LocalDateTime end, List<CalendarEvent> eventsList) {

    LocalDateTime currDate = start;
    LocalDateTime until = getRecurringEventEndDate();

    while (!currDate.isAfter(until)) {
      if (isWeekDayIncluded(currDate)) {
        EventDetails e = new EventDetails(eventDetails, currDate, end);
        eventsList.add(getCreatedSegmentEvent(calendar, currDate, e, end));
      }
      currDate = currDate.plusDays(1);
      end = end.plusDays(1);
    }
    return eventsList;
  }

  public void pushEventToCalendar(ACalendar calendar) {

    LocalDateTime start = eventDetails.getStartDate();
    LocalDateTime end = eventDetails.getEndDate();
    List<CalendarEvent> newEventsList = new ArrayList<>();

    if (allMetaDetails.getAddUntilDateTime() != null) {
      newEventsList = getUntilTimeRecurringEvent(calendar, start, end, newEventsList);

    } else if (allMetaDetails.getForTimes() != null) {
      newEventsList = getForTimeRecurringEvent(calendar, start, end, newEventsList);
    }

//    System.out.println("printing all generated recurring event");
//    for (CalendarEvent e : newEventsList) {
//      System.out.println(e);
//    }
//    System.out.println("finished printing all generated recurring event");

    calendar.createEvent(newEventsList, true);

  }


  public char getDayAbbreviation(String day) {
    switch (day) {
      case "MONDAY":
        return 'M';
      case "TUESDAY":
        return 'T';
      case "WEDNESDAY":
        return 'W';
      case "THURSDAY":
        return 'R';
      case "FRIDAY":
        return 'F';
      case "SATURDAY":
        return 'S';
      case "SUNDAY":
        return 'U';
      default:
        return '?';
    }
  }

}
