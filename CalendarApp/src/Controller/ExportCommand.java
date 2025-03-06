package Controller;

import Model.Calendar.ACalendar;

// Concrete command for exporting the calendar
public class ExportCommand implements ICommand {
  //private ICalendar calendar;

  public ExportCommand() {
    // this.calendar = calendar;
  }

  @Override
  public void execute(String commandArgs, ACalendar calendar) {
    // Export the calendar data (e.g., to CSV)
    System.out.println("Executing export calendar command");
  }
}