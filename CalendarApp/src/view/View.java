package view;

import java.util.List;
import java.util.Map;

/**
 * This interface sets a base for the classes that handle displaying data.
 * It includes methods for displaying messages, events, and status.
 */
public interface View {

  /**
   * Displays a message.
   *
   * @param message The message to display.
   */
  public void viewMessage(String message);


  /**
   * Displays a list of events.
   *
   * @param events A list of events, where each event is represented as a map.
   */
  public void viewEvents(List<Map<String, Object>> events);

  /**
   * Displays the user's status.
   *
   * @param status The status to display.
   */
  public void viewStatus(boolean status);
}
