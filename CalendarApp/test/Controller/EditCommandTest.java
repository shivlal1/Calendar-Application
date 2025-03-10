package Controller;

import org.junit.Test;

import Controller.CommandHandler.CreateCommand;
import Controller.CommandHandler.EditCommand;
import Controller.CommandHandler.ICommand;
import Model.Calendar.ACalendar;
import Model.Calendar.Calendar;

public class EditCommandTest {
  @Test
  public void testSimpleCommand() throws Exception {
    String command = "event \"International Conference\" from 2025-03-01T09:00 to 2025-03-03T13:00";
    ACalendar cal = new Calendar();
    ICommand createCommand = new CreateCommand();
    createCommand.execute(command, cal);
    //cal.printEvents();

    //            "edit event name \"Annual Meeting\" from 2025-03-01T09:00:00 to 2025-03-01T10:00:00 with \"Weekly Meeting\"",
//            "edit events public \"Annual Meeting\" from 2025-03-01T09:00:00 with \"true\""
//            "edit events name \"Annual Meeting\" \"Weekly Meeting\""

    command = "event name \"International Conference\" from 2025-03-01T09:00 to 2025-03-03T13:00 with \"Weekly Meeting\"";
    ICommand editCommand = new EditCommand();
    editCommand.execute(command, cal);
    // cal.printEvents();
  }


  @Test
  public void recurringEvent52() throws Exception {

    String command = "event \"International Conference\" from 2025-04-05T09:45 to 2025-04-05T10:00";
    //String command = "event \"International Conference\" from 2025-03-01T09:00:00 to 2025-03-05T13:00:00";

    ACalendar cal = new Calendar();
    ICommand createCommand = new CreateCommand();
    createCommand.execute(command, cal);
    //cal.printEvents();

    //command = "event --autoDecline \"NN\" from 2025-04-28T09:00:00 to 2025-04-28T10:00:00 repeats MTW until 2025-06-28T09:00:00";
    command = "event --autoDecline \"N \" from 2025-04-28T09:00 to 2025-04-28T10:00 repeats MTW for 5 times";
    createCommand.execute(command, cal);
    // cal.printEvents();

    command = "events name \"N \" \"Weekly Meeting\"";
    ICommand editCommand = new EditCommand();
    editCommand.execute(command, cal);
    //cal.printEvents();

    //"print events from \"2025-03-01T09:00:00\" to \"2025-03-03T13:00:00\"",

  }
}