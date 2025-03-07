package Controller;

import org.junit.Test;

import Model.Calendar.ACalendar;
import Model.Calendar.Calendar;

public class CreateCommandTest {

  @Test
  public void testSimpleCommand() {
    String command = "event \"International Conference\" from 2025-03-01T09:00:00 to 2025-03-03T13:00:00";
    ACalendar cal = new Calendar();
    ICommand createCommand = new CreateCommand();
    createCommand.execute(command, cal);
    cal.printEvents();
  }

  @Test
  public void commandWithAutoDecline() {
    String command = "event --autoDecline \"International Conference\" from 2025-03-01T09:00:00 to 2025-03-05T13:00:00";
    ACalendar cal = new Calendar();
    ICommand createCommand = new CreateCommand();
    createCommand.execute(command, cal);
    cal.printEvents();
  }

}