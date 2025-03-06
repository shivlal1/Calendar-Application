package Model.Event;

import java.time.LocalDateTime;

public class AdditionalEventInformation {
  public String description;
  public boolean isPublic;
  public String location;
  public LocalDateTime endDate;

  public AdditionalEventInformation(String description, String location, LocalDateTime endDate, boolean isPublic) {
    this.description = description;
    this.location = location;
    this.endDate = endDate;
    this.isPublic = isPublic;
  }

  @Override
  public String toString(){
    System.out.println("description "+description);
    System.out.println("location "+location);
    System.out.println("endDate "+endDate);
    System.out.println("isPublic "+isPublic);
    return  null;
  }
}
