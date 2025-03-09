package Controller;

import org.junit.Test;

import Controller.CommandHandler.CreateCommand;
import Controller.CommandHandler.ICommand;
import Model.Calendar.ACalendar;
import Model.Calendar.Calendar;

public class CreateCommandTest {

  @Test
  public void testSimpleCommand() {
    String command = "event \"International Conference\" from 2025-03-01T09:00 to 2025-03-03T13:00";
    ACalendar cal = new Calendar();
    ICommand createCommand = new CreateCommand();
    createCommand.execute(command, cal);
    cal.printEvents();
  }

  @Test
  public void commandWithAutoDecline() {
    String command = "event \"International Conference\" from 2025-03-01T09:00 to 2025-03-05T13:00";
    ACalendar cal = new Calendar();
    ICommand createCommand = new CreateCommand();
    createCommand.execute(command, cal);
    cal.printEvents();
    System.out.println("done");

    command = "event \"International\" from 2025-03-01T09:00 to 2025-03-03T13:00";
    createCommand.execute(command, cal);
    System.out.println("PRINTING EVENTS");
    cal.printEvents();
  }

  @Test
  public void commandWithOn() {
    String command = "event --autoDecline \"International Conference\" on 2025-03-01T09:00";
    ACalendar cal = new Calendar();
    ICommand createCommand = new CreateCommand();
    createCommand.execute(command, cal);
    cal.printEvents();
  }

  @Test
  public void recurringEvent() {
    String command = "event --autoDecline \"International Conference\" on 2025-03-01T09:00";
    ACalendar cal = new Calendar();
    ICommand createCommand = new CreateCommand();
    createCommand.execute(command, cal);
    cal.printEvents();
  }

  @Test
  public void recurringEvent1() {
    String command = "event  \"Annual\" on 2025-04-15 repeats M until 2025-06-01";
    ACalendar cal = new Calendar();
    ICommand createCommand = new CreateCommand();
    createCommand.execute(command, cal);
    cal.printEvents();
  }

  @Test
  public void recurringEvent2() {
    String command = "event \"R E\" on 2025-04-15 repeats MT for 5 times";
    ACalendar cal = new Calendar();
    ICommand createCommand = new CreateCommand();
    createCommand.execute(command, cal);
    cal.printEvents();
  }

  @Test
  public void recurringEvent3() {

    String command = "event --autoDecline \"International Conference\" on 2025-04-27T09:00";
    ACalendar cal = new Calendar();
    ICommand createCommand = new CreateCommand();
    createCommand.execute(command, cal);
    cal.printEvents();

    command = "event \"R E\" on 2025-04-20 repeats MT for 5 times";
    createCommand.execute(command, cal);
    cal.printEvents();
  }

  @Test
  public void recurringEvent4() {

    String command = "event \"International Conference\" from 2025-06-09T09:45 to 2025-06-09T11:00";
    //String command = "event \"International Conference\" from 2025-03-01T09:00:00 to 2025-03-05T13:00:00";

    ACalendar cal = new Calendar();
    ICommand createCommand = new CreateCommand();
    createCommand.execute(command, cal);
    cal.printEvents();

    command = "event --autoDecline \"NN\" from 2025-04-28T09:00 to 2025-04-28T10:00 repeats MTW until 2025-06-28T09:00";
    createCommand.execute(command, cal);
    cal.printEvents();
  }


  @Test
  public void recurringEvent5() {

    String command = "event \"International Conference\" from 2025-04-30T09:45 to 2025-04-30T10:00";
    //String command = "event \"International Conference\" from 2025-03-01T09:00:00 to 2025-03-05T13:00:00";

    ACalendar cal = new Calendar();
    ICommand createCommand = new CreateCommand();
    createCommand.execute(command, cal);
    cal.printEvents();

    //command = "event --autoDecline \"NN\" from 2025-04-28T09:00:00 to 2025-04-28T10:00:00 repeats MTW until 2025-06-28T09:00:00";
    command = "event --autoDecline \"N\" from 2025-04-28T09:00 to 2025-04-28T10:00 repeats MTW for 5 times";
    createCommand.execute(command, cal);
    cal.printEvents();
  }

  @Test
  public void recurringEvent52() {

    String command = "event \"International Conference\" from 2025-04-30T09:45 to 2025-04-30T10:00";
    //String command = "event \"International Conference\" from 2025-03-01T09:00:00 to 2025-03-05T13:00:00";

    ACalendar cal = new Calendar();
    ICommand createCommand = new CreateCommand();
    createCommand.execute(command, cal);
    cal.printEvents();

    //command = "event --autoDecline \"NN\" from 2025-04-28T09:00:00 to 2025-04-28T10:00:00 repeats MTW until 2025-06-28T09:00:00";
    command = "event --autoDecline \"N\" from 2025-04-28T09:00 to 2025-04-28T10:00 repeats MTW for 5 times";
    createCommand.execute(command, cal);
    cal.printEvents();
  }

}