package Model.Event;

import java.time.LocalDateTime;

public class CalendarEvent {
  private EventDetails event;
  private LocalDateTime startTime;
  private LocalDateTime endTime;

  CalendarEvent(EventDetails event, LocalDateTime startTime, LocalDateTime endTime) {
    this.event = event;
    this.startTime = startTime;
    this.endTime = endTime;
  }

  public EventDetails getEvent() {
    return event;
  }

  @Override
  public String toString() {
    return event.toString();
  }
}
