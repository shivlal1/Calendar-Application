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







