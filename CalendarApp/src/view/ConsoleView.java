package view;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class ConsoleView implements View {

  public void viewMessage(String message) {
    System.out.println(message);
  }

  public void viewEvents(List<Map<String, Object>> events) {
    char bullet = '\u2022';

    for (Map<String, Object> event : events) {
      StringBuilder bulletEvent = new StringBuilder();
      bulletEvent.append(" Subject : " + event.get("subject") + ",");

      LocalDateTime startDateTime = (LocalDateTime) event.get("startDate");
      bulletEvent.append("Start date : " + startDateTime.toLocalDate() + ",");
      bulletEvent.append("Start time : " + startDateTime.toLocalTime() + ",");

      if (event.get("endDate") != null) {
        LocalDateTime endDateTime = (LocalDateTime) event.get("endDate");
        bulletEvent.append("End date : " + endDateTime.toLocalDate() + ",");
        bulletEvent.append("End time : " + endDateTime.toLocalTime() + ",");
      }
      if (event.get("location") != null) {
        bulletEvent.append("location : " + event.get("location") + ",");
      }
      if (event.get("description") != null) {
        bulletEvent.append("Location : " + event.get("location") + ",");
      }
      bulletEvent.deleteCharAt(bulletEvent.length() - 1);

      System.out.println(bullet + bulletEvent.toString());
    }
  }

  public void viewStatus(boolean status) {
    System.out.println("status " + status);
  }
}
