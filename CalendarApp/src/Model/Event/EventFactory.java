package Model.Event;

import java.time.LocalDateTime;
import java.util.Map;

public class EventFactory {

  private Event event;

  public Event getEvent(String subject, LocalDateTime localStartDateTime, LocalDateTime localEndDateTime,
                        Map<String, Object> allMetaDeta) {

    if ((Boolean) allMetaDeta.get("isRecurring")) {
      event = new RecurringEvent(subject, localStartDateTime, localEndDateTime, allMetaDeta);
    } else {
      event = new SingleEvent(subject, localStartDateTime, localEndDateTime, allMetaDeta);
    }
    return event;
  }

}
