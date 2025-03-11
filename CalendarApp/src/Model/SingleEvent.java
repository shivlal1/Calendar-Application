package Model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SingleEvent extends Event {

  private Map<String, Object> allMetaDetails;

  SingleEvent(String subject, LocalDateTime startDate, LocalDateTime endDate,
              Map<String, Object> allMetaDetails) {

    super(subject, startDate, endDate);
    this.allMetaDetails = allMetaDetails;
  }


  public List<Event> generateEventsForCalendar() throws Exception {

    List<Event> newEventsList = new ArrayList<>();

    if (!isStartBeforeEnd(this.startDate, this.endDate)) {
      throw new Exception("end date cannot be before start date");
      //return newEventsList;
    }

    if ((Boolean) allMetaDetails.get("isAllDay")) {
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







