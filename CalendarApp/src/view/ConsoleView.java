package view;

import java.util.List;

import Model.Event.EventDetails;

public class ConsoleView {

  public void printInConsole(List<EventDetails> events) {
    System.out.println("CONSOLE VIEW");
    for (EventDetails event : events) {
      System.out.println(event);
    }
  }

  public void showStatusInConsole(boolean status) {
    System.out.println("status " + status);
  }
}
