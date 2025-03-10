package Model.Event;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import Controller.MetaData.CreateCommandMetaDetails;
import Model.Calendar.ACalendar;
import Model.Utils.DateUtils;

public class RecurringEvent extends Event {

  private CreateCommandMetaDetails allMetaDetails;

  RecurringEvent(String subject, LocalDateTime startDate, LocalDateTime endDate,
                 CreateCommandMetaDetails allMetaDetails) {

    super(subject, startDate, endDate);
    this.allMetaDetails = allMetaDetails;
  }

  public RecurringEvent(Event other, LocalDateTime newStartTime, LocalDateTime newEndTime) {
    super(other.subject, other.startDate, other.endDate);
    this.isPublic = other.isPublic;
    this.description = other.description;
    this.location = other.location;
    this.startDate = newStartTime;
    this.endDate = newEndTime;
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
        Event e = new RecurringEvent(this, currDate, end);

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
        Event e = new RecurringEvent(this, currDate, end);

        eventsList.add(getCreatedSegmentEvent(calendar, currDate, e, end));
      }
      currDate = currDate.plusDays(1);
      end = end.plusDays(1);
    }
    return eventsList;
  }

  public void pushEventToCalendar(ACalendar calendar) {

    LocalDateTime start = this.startDate;
    LocalDateTime end = this.endDate;

    if (!isStartBeforeEnd(start, end)) {
      return;
    }

    List<CalendarEvent> newEventsList = new ArrayList<>();

    if (allMetaDetails.getAddUntilDateTime() != null) {
      newEventsList = getUntilTimeRecurringEvent(calendar, start, end, newEventsList);

    } else if (allMetaDetails.getForTimes() != null) {
      newEventsList = getForTimeRecurringEvent(calendar, start, end, newEventsList);
    }

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
