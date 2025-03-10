package Model.Event;

import java.time.LocalDateTime;

import Controller.MetaData.CreateCommandMetaDetails;

public class EventFactory {

  private Event event;

  public Event getEvent(String subject, LocalDateTime localStartDateTime, LocalDateTime localEndDateTime,
                        CreateCommandMetaDetails allMetaDeta) {

    if (allMetaDeta.getIsRecurring()) {
      event = new RecurringEvent(subject, localStartDateTime, localEndDateTime, allMetaDeta);
    } else {
      event = new SingleEvent(subject, localStartDateTime, localEndDateTime, allMetaDeta);
    }
    return event;
  }

}
