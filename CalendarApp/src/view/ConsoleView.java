package view;

import java.util.List;
import java.util.Map;

public class ConsoleView implements View {

  public void viewMessage(String message) {
    System.out.println(message);
  }

  public void viewEvents(List<Map<String, Object>> events) {
    for (Map<String, Object> event : events) {
      System.out.println(event.get("subject"));
      System.out.println(event.get("startDate"));
      System.out.println(event.get("endDate"));
      System.out.println(event.get("location"));
      System.out.println(event.get("description"));
    }

  }


  public void viewStatus(boolean status) {
    System.out.println("status " + status);
  }
}
