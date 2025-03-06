package Model.Event;


import java.time.LocalDateTime;

public class EventInformation {
  public String subject;
  public LocalDateTime startDate;
  public AdditionalEventInformation info;

  public EventInformation(String subject, LocalDateTime startDate, AdditionalEventInformation info) {
    this.subject = subject;
    this.startDate = startDate;
    this.info = info;
  }

  @Override
  public String toString(){
    System.out.println("subject 1 "+subject);
    System.out.println("startDate 2 "+startDate);
    System.out.println("info 3"+info);
    return null;
  }
}
