package model;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * The EventFactory class is responsible for creating different types of events
 * based on the provided metadata. It acts as a factory for both recurring and single events.
 */
public class EventFactory {

  private Event event;

  /**
   * This method creates an event based on the provided details and metadata.
   * If the event is recurring, a RecurringEvent is created; otherwise, a SingleEvent is created.
   *
   * @param subject            The subject or title of the event.
   * @param localStartDateTime The start date and time of the event.
   * @param localEndDateTime   The end date and time of the event.
   * @param allMetaDeta        Additional metadata for the event, such as recurrence settings.
   * @return The created event object.
   */
  Event getEvent(String subject, LocalDateTime localStartDateTime, LocalDateTime localEndDateTime,
                 Map<String, Object> allMetaDeta) {

    if ((Boolean) allMetaDeta.get("isRecurring")) {
      event = new RecurringEvent(subject, localStartDateTime, localEndDateTime, allMetaDeta);
    } else {
      event = new SingleEvent(subject, localStartDateTime, localEndDateTime, allMetaDeta);
    }
    return event;
  }

  Event getCarbonCopy(Event event, LocalDateTime localStartDateTime, LocalDateTime localEndDateTime) {
    if (event instanceof RecurringEvent) {
      event = new RecurringEvent(event, localStartDateTime, localEndDateTime);
    } else {
      event = new SingleEvent(event, localStartDateTime, localEndDateTime);
    }
    return event;
  }


}
