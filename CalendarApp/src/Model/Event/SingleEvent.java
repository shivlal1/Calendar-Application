package Model.Event;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import Controller.MetaData.CreateCommandMetaDetails;

public class SingleEvent extends Event {

  private CreateCommandMetaDetails allMetaDetails;

  SingleEvent(String subject, LocalDateTime startDate, LocalDateTime endDate,
              CreateCommandMetaDetails allMetaDetails) {

    super(subject, startDate, endDate);
    this.allMetaDetails = allMetaDetails;
  }

  private List<Event> getAllDayEventList(LocalDateTime start) {
    List<Event> list = new ArrayList<>();
    LocalDate currentDay = start.toLocalDate();
    LocalDateTime segmentEnd = currentDay.atTime(23, 59);
    this.endDate = segmentEnd;
    list.add(geCalendarEvent(start, this, segmentEnd));
    return list;
  }


  private List<CalendarEvent> getMultiDayEventList(LocalDateTime start, LocalDateTime end) {

    List<CalendarEvent> list = new ArrayList<>();
    long daysBetween = ChronoUnit.DAYS.between(start, end) + 1;

    for (int i = 0; i < daysBetween; i++) {
      LocalDate currentDay = start.toLocalDate().plusDays(i);
      LocalDateTime segmentStart;
      LocalDateTime segmentEnd;

      if (i == 0) {
        segmentStart = start;
        segmentEnd = currentDay.atTime(23, 59);
      } else if (i == daysBetween - 1) {
        segmentStart = currentDay.atStartOfDay();
        segmentEnd = end;
      } else {
        segmentStart = currentDay.atStartOfDay();
        segmentEnd = currentDay.atTime(23, 59);
      }

      list.add(geCalendarEvent(segmentStart, this, segmentEnd));
    }

    return list;
  }

  public List<Event> generateEventsForCalendar() {

    List<Event> newEventsList = new ArrayList<>();

    if (!isStartBeforeEnd(this.startDate, this.endDate)) {
      return newEventsList;
    }

    if (allMetaDetails.getIsAllDay()) {
      LocalDate currentDay = this.startDate.toLocalDate();
      LocalDateTime newEndDate = currentDay.atTime(23, 59);
      this.endDate = newEndDate;
    }

    newEventsList.add(this);

    return newEventsList;
  }

  @Override
  public boolean isAutoDeclineEnabled() {
    return false;
  }

  @Override
  public boolean canBeEditedToDifferentDay() {
    return true;
  }


}







