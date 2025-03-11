package Model;

import java.time.LocalDateTime;
import java.util.List;

public abstract class Event {

  protected String subject;
  protected String description;
  protected String location;
  protected LocalDateTime startDate;
  protected LocalDateTime endDate;
  protected boolean isPublic;

  Event(String subject, LocalDateTime startDate, LocalDateTime endDate) {
    this.subject = subject;
    this.startDate = startDate;
    this.endDate = endDate;
    this.isPublic = false;
    this.description = null;
    this.location = null;
  }

  protected abstract List<Event> generateEventsForCalendar() throws Exception;

  protected abstract boolean isAutoDeclineEnabled();

  protected abstract boolean canBeEditedToDifferentDay();

  protected boolean isStartBeforeEnd(LocalDateTime start, LocalDateTime end) {
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


  protected boolean isOverlapWith(Event newEvent) {

    LocalDateTime newStartTime = newEvent.startDate;
    LocalDateTime newEndTime = newEvent.endDate;
    LocalDateTime existingStartTime = this.startDate;
    LocalDateTime existingEndTime = this.endDate;

    boolean isConflictExists = false;

    if (newStartTime.isBefore(existingEndTime) && newEndTime.equals(existingStartTime)) {
      isConflictExists = false;
    }
    if (existingStartTime.isBefore(newEndTime) && existingEndTime.equals(newStartTime)) {
      isConflictExists = false;
    }

    if (newStartTime.isBefore(existingEndTime) && existingStartTime.isBefore(newEndTime)) {
      isConflictExists = true;
    }

    if (existingStartTime.isBefore(newEndTime) && newStartTime.isBefore(existingEndTime)) {
      isConflictExists = true;
    }

    if (existingStartTime.equals(newStartTime) || existingEndTime.equals(newEndTime)) {
      isConflictExists = true;
    }

    return isConflictExists;
  }

}
