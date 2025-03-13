package view;

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
    for (Map<String, Object> event : events) {
      System.out.println(event.get("subject"));
      System.out.println(event.get("startDate"));
      System.out.println(event.get("endDate"));
      System.out.println(event.get("location"));
      System.out.println(event.get("description"));
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
