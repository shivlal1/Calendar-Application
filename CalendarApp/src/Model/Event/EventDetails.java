package Model.Event;


import java.time.LocalDateTime;

public class EventDetails {
  private String subject;
  private LocalDateTime startDate;
  private String description;
  private boolean isPublic;
  private String location;
  private LocalDateTime endDate;

  public EventDetails(String subject, LocalDateTime startDate, String description,
                      String location, LocalDateTime endDate, boolean isPublic) {
    this.subject = subject;
    this.startDate = startDate;
    this.description = description;
    this.location = location;
    this.endDate = endDate;
    this.isPublic = isPublic;
  }

  public String getSubject() {
    return subject;
  }

  public LocalDateTime getStartDate() {
    return startDate;
  }

  public String getDescription() {
    return description;
  }

  public String getLocation() {
    return location;
  }

  public LocalDateTime getEndDate() {
    return endDate;
  }

  public boolean getIsPublic() {
    return isPublic;
  }

  public void setSubject(String subject) {
    this.subject = subject;
  }

  public void setStartDate(LocalDateTime startDate) {
    this.startDate = startDate;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public void setEndDate(LocalDateTime endDate) {
    this.endDate = endDate;
  }

  public void setIsPublic(boolean isPublic) {
    this.isPublic = isPublic;
  }

  @Override
  public String toString() {

    StringBuilder details = new StringBuilder();

    details.append("Subject " + subject);
    details.append("Start Date " + startDate);
    details.append("Description " + description);
    details.append("Location " + location);
    details.append("End Date " + endDate);
    details.append("isPublic " + isPublic);

    return details.toString();
  }
}
