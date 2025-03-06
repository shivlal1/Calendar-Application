package Controller;

import Model.Calendar.ACalendar;

// Concrete command for printing events
public class PrintCommand implements ICommand {

  public PrintCommand() {
//    this.calendar = calendar;
  }

  @Override
  public void execute(String commandArgs, ACalendar calendar) {
    // Print events for a given date
    System.out.println("Executing print event command");
  }
}