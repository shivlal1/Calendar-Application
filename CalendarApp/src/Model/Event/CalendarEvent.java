package Model.Event;

import java.time.LocalDateTime;

public class CalendarEvent {
  private Event event;
  private LocalDateTime startTime;
  private LocalDateTime endTime;

  CalendarEvent(Event event, LocalDateTime startTime, LocalDateTime endTime) {
    this.event = event;
    this.startTime = startTime;
    this.endTime = endTime;
  }

  public Event getEvent() {
    return event;
  }

  public LocalDateTime getStartDate() {
    return startTime;
  }

  public LocalDateTime getEndDate() {
    return endTime;
  }


  @Override
  public String toString() {
    return event.toString();
  }
}
