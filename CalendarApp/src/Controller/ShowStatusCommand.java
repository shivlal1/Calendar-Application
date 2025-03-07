package Controller;

import Model.Calendar.ACalendar;

// Concrete command for showing event status
public class ShowStatusCommand extends AbstractCommand {
  //private ICalendar calendar;

  public ShowStatusCommand() {
    // this.calendar = calendar;
  }

  @Override
  public void execute(String commandArgs, ACalendar calendar) {
    // Display busy/free status for a given date/time
    System.out.println("Executing show status command");
  }
}