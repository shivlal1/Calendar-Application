package view;

import java.util.List;
import java.util.Map;

import Model.Event;

public class ConsoleView {

  public void printInConsole( List<Map<String, Object>>  events) {
    System.out.println("CONSOLE VIEW");

    for (Map<String, Object> event : events) {
      System.out.println(event.get("subject"));
      System.out.println(event.get("startDate"));
      System.out.println(event.get("endDate"));
      System.out.println(event.get("location"));
      System.out.println(event.get("description"));
    }

  }

  public void showStatusInConsole(boolean status) {
    System.out.println("status " + status);
  }
}
