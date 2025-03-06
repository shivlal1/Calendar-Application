package Model.Event;

import java.time.LocalDateTime;

public class EventToBeAdded {
  public EventInformation event;
  public LocalDateTime startTime;
  public LocalDateTime endTime;

  EventToBeAdded(EventInformation event, LocalDateTime startTime, LocalDateTime endTime) {
    this.event = event;
    this.startTime = startTime;
    this.endTime = endTime;
  }

  @Override
  public  String toString(){
   System.out.println(event.toString());
   System.out.println("Segment start: " + startTime);
   System.out.println("Segment end: " + endTime);
    return null;
  }
}
