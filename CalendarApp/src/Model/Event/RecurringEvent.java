package Model.Event;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import Model.Calendar.ACalendar;
import Model.Utils.DateUtils;

public class RecurringEvent extends AEvent {

  private LocalDateTime getRecurringEventEndDate(LocalDateTime start, EventMetaDetails allMetaDeta) {
    String finalUntilDateTime = allMetaDeta.getAddUntilDateTime();
    LocalDateTime until;

    if (finalUntilDateTime != null) {
      DateUtils finalUtil = new DateUtils(finalUntilDateTime);
      until = finalUtil.stringToLocalDateTime();
    } else {
      int num = Integer.valueOf(allMetaDeta.getForTimes()) * 7;
      until = start.plusDays(num);
    }
    return until;
  }

  public void pushEventToCalendar(EventDetails event, ACalendar calendar, EventMetaDetails allMetaDeta) {

    LocalDateTime start = event.getStartDate();
    LocalDateTime end = event.getEndDate();
    LocalDateTime currDate = start;

    LocalDateTime until = getRecurringEventEndDate(start, allMetaDeta);

    List<EventDetails> list = new ArrayList<>();

    while (!currDate.isAfter(until)) {
      DayOfWeek day = currDate.getDayOfWeek();
      char d = getDayAbbreviation(day.name());

      if (allMetaDeta.getWeekdays().indexOf(d) != -1) {
        event.setStartDate(currDate);
        event.setEndDate(end);


        pushCreatedSegmentEvent(calendar, currDate, event, end);
      }

      currDate = currDate.plusDays(1);
      end = end.plusDays(1);
    }

    //RecurringEventStorage newRecurringEvent = new RecurringEventStorage();


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
