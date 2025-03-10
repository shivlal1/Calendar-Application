package Model.Event;

import java.time.LocalDateTime;
import java.util.List;

public abstract class Event {

  protected String subject;
  protected String description;
  protected String location;
  protected LocalDateTime startDate;
  protected LocalDateTime endDate;
  protected boolean isPublic;

  public Event(String subject, LocalDateTime startDate, LocalDateTime endDate) {
    this.subject = subject;
    this.startDate = startDate;
    this.endDate = endDate;
    this.isPublic = false;
    this.description = null;
    this.location = null;
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

  public abstract List<Event> generateEventsForCalendar();

  public abstract boolean isAutoDeclineEnabled();

  public abstract boolean canBeEditedToDifferentDay();


  boolean isStartBeforeEnd(LocalDateTime start, LocalDateTime end) {
    if (end.isAfter(start)) {
      return true;
    }
    return false;
  }


  @Override
  public String toString() {
    StringBuilder details = new StringBuilder();

    if (subject != null) {
      details.append(" Subject " + subject);
    }
    if (startDate != null) {
      details.append(" Start Date : " + startDate);
    }
    if (description != null) {
      details.append(" Description: " + description);
    }
    if (location != null) {
      details.append(" Location: " + location);
    }
    if (endDate != null) {
      details.append(" End Date :" + endDate);
    }
    details.append(" isPublic: " + isPublic);

    return details.toString();
  }


}
