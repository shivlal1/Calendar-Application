package view;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * This class implements the View interface and is responsible for displaying
 * messages and events in the console.
 */
public class ConsoleView implements View {

  /**
   * Displays a message in the console.
   *
   * @param message The message to display.
   */
  public void viewMessage(String message) {
    System.out.println(message);
  }

  /**
   * Displays a list of events in the console.
   *
   * @param events A list of events, where each event is represented as a map.
   */
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
        bulletEvent.append("description : " + event.get("description") + ",");
      }

      if (event.get("isPublic") != null) {
        bulletEvent.append("isPublic : " + (Boolean) event.get("isPublic") + ",");
      }
      bulletEvent.deleteCharAt(bulletEvent.length() - 1);

      System.out.println(bullet + bulletEvent.toString());
    }
  }

  /**
   * Displays the user's status in the console.
   *
   * @param status The status to display, indicating if the user is busy or not.
   */
  public void viewStatus(boolean status) {
    System.out.println("status " + status);
  }
}
