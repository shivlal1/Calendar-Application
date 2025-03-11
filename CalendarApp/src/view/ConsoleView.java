package view;

import java.util.List;

import Model.Event;

public class ConsoleView {

  public void printInConsole(List<Event> events) {
    System.out.println("CONSOLE VIEW");
    for (Event event : events) {
      System.out.println(event);
    }
  }

  public void showStatusInConsole(boolean status) {
    System.out.println("status " + status);
  }
}
