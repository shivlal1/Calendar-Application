package Model.Event;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import Model.Utils.DateUtils;

public class RecurringEvent extends Event {

  private Map<String, Object> allMetaDetails;

  RecurringEvent(String subject, LocalDateTime startDate, LocalDateTime endDate,
                 Map<String, Object> allMetaDetails) {

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
    String finalUntilDateTime = (String) allMetaDetails.get("untilTime");
    return DateUtils.stringToLocalDateTime(finalUntilDateTime);
  }

  private boolean isWeekDayIncluded(LocalDateTime date) {

    DayOfWeek day = date.getDayOfWeek();
    char dayNameAsChar = getDayAbbreviation(day.name());
    String weekdayText = (String) allMetaDetails.get("weekday");
    if (weekdayText.indexOf(dayNameAsChar) != -1) {
      return true;
    }
    return false;
  }

  private List<Event> getForTimeRecurringEvent(LocalDateTime start,
                                               LocalDateTime end) {

    LocalDateTime currDate = start;
    int requriredRecurringEvents = Integer.valueOf((String) allMetaDetails.get("forTimes"));
    List<Event> eventsList = new ArrayList<>();
    int eventsEncountered = 0;

    while (eventsEncountered < requriredRecurringEvents) {

      if (isWeekDayIncluded(currDate)) {
        eventsList.add(new RecurringEvent(this, currDate, end));
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

  private List<Event> getUntilTimeRecurringEvent(LocalDateTime start,
                                                 LocalDateTime end) {

    List<Event> eventsList = new ArrayList<>();
    LocalDateTime until = getRecurringEventEndDate();
    LocalDateTime currDate = start;

    while (!currDate.isAfter(until)) {
      if (isWeekDayIncluded(currDate)) {
        eventsList.add(new RecurringEvent(this, currDate, end));
      }
      currDate = currDate.plusDays(1);
      end = end.plusDays(1);
    }
    return eventsList;
  }

  public List<Event> generateEventsForCalendar() throws Exception {

    LocalDateTime start = this.startDate;
    LocalDateTime end = this.endDate;

    List<Event> newEventsList = new ArrayList<>();

    if (!isStartBeforeEnd(start, end)) {
      throw new Exception("end date cannot be before start date");
    }

    if ((String) allMetaDetails.get("untilTime") != null) {
      newEventsList = getUntilTimeRecurringEvent(start, end);

    } else if (allMetaDetails.get("forTimes") != null) {
      newEventsList = getForTimeRecurringEvent(start, end);
    }

    return newEventsList;

  }

  @Override
  public boolean isAutoDeclineEnabled() {
    return true;
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

  @Override
  public boolean canBeEditedToDifferentDay() {
    return false;
  }

}
