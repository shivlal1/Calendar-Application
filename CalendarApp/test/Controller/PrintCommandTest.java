package Controller;

import org.junit.Before;
import org.junit.Test;

import Model.Calendar;
import Model.ICalendar;

import static org.junit.Assert.assertEquals;

public class PrintCommandTest {
  ICommand printCommand;
  ICalendar cal = new Calendar();
  String command;

  @Before
  public void init() {
    printCommand = new PrintCommand();
  }

  @Test
  public void testMissingTo() throws Exception {
    try {
      command = "events from 2025-04-01T10:00 2025-04-04";
      printCommand.execute(command, cal);
    } catch (Exception e) {
      assertEquals(e.getMessage(), "Invalid Command Missing To");
    }
  }

  @Test
  public void testMissingFrom() throws Exception {
    try {
      command = "events 2025-04-01T10:00 2025-04-04";
      printCommand.execute(command, cal);
    } catch (Exception e) {
      assertEquals(e.getMessage(), "Invalid Command Missing From/On");
    }
  }

  @Test
  public void testMissingDate() throws Exception {
    try {
      command = "events";
      printCommand.execute(command, cal);
    } catch (Exception e) {
      assertEquals(e.getMessage(), "Invalid Command Missing From/On");
    }
  }

  @Test
  public void testMissingPrintEvent() throws Exception {
    try {
      command = "from 2025-04-01T10:00 2025-04-04";
      printCommand.execute(command, cal);
    } catch (Exception e) {
      assertEquals(e.getMessage(), "Invalid Command Missing events");
    }
  }

  @Test
  public void printtest() throws Exception {
    command = "print events from \"2025-04-01T10:00\" to \"2025-04-04T11:00\"";
    command = "print events on \"2025-04-01\"";

    ICommand cmd = new CalendarController();
    cmd.execute(command, cal);
  }
}