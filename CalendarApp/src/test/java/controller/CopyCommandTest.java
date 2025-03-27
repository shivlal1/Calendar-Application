package controller;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.time.ZoneId;

import model.CalendarV2;
import model.ICalendar;

/**
 * A unit test for the copy command class.
 */
public class CopyCommandTest {

  ICalendarManager calManager;
  ICommand command;
  ICalendar cal;

  @Before
  public void init() {
    calManager = new CalendarManager();
    cal = new CalendarV2(ZoneId.of("Asia/Kolkata"));
  }

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Test
  public void test() throws Exception {
    thrown.expect(Exception.class);
    thrown.expectMessage("Target calendar doesn't exists");

    String command = "copy event \"Event\" on 2025-03-02T09:00 --target MyCal to 2025-03-02T10:00";
    //    System.out.println(command + "------");
    ICalendar cal = new CalendarV2(ZoneId.of("Asia/Kolkata"));
    CopyCommand cmd = new CopyCommand(calManager);
    //    cmd.execute(command, cal);

    command = "events between 2025-03-02 and 2025-03-03 --target <calendarName> to 2025-03-05";
    cmd.execute(command, cal);

    System.out.println(command + "------");
    command = "events on 2025-03-02 --target <calendarName> to 2025-03-05";
    cmd.execute(command, cal);
    System.out.println(command + "------");

  }


  @Test
  public void missingOnOrBetween() throws Exception {
    thrown.expect(Exception.class);
    thrown.expectMessage("Invalid Command: Missing on or between");
    command = new CopyCommand(calManager);
    command.execute("events 2025-03-01 --target work to 2025-04-01", cal);
  }

  @Test
  public void missingTargetKeyword() throws Exception {
    thrown.expect(Exception.class);
    thrown.expectMessage("Invalid Command: Missing --target");
    command = new CopyCommand(calManager);
    command.execute("events on 2025-03-01 work to 2025-04-01", cal);
  }

  @Test
  public void missingToKeyword() throws Exception {
    thrown.expect(Exception.class);
    thrown.expectMessage("Invalid Command: Missing destination date");
    command = new CopyCommand(calManager);
    command.execute("events on 2025-03-01 --target work 2025-04-01", cal);
  }

  @Test
  public void invalidDateFormat() throws Exception {
    thrown.expect(Exception.class);
    thrown.expectMessage("Invalid Command: Missing Date/invalid format");
    command = new CopyCommand(calManager);
    command.execute("events on 01-03-2025 --target work to 01-04-2025", cal);
  }

  @Test
  public void missingDestinationDate() throws Exception {
    thrown.expect(Exception.class);
    thrown.expectMessage("Invalid Command: Missing destination date");
    command = new CopyCommand(calManager);
    command.execute("events on 2025-03-01 --target work", cal);
  }


  @Test
  public void incompleteCommand() throws Exception {
    thrown.expect(Exception.class);
    thrown.expectMessage("Invalid Command: Missing on or between");
    command = new CopyCommand(calManager);
    command.execute("", cal);
  }

  @Test
  public void copyEventWithMissingSourceDate() throws Exception {
    thrown.expect(Exception.class);
    thrown.expectMessage("Invalid Command: Missing on or between");
    command = new CopyCommand(calManager);
    command.execute("events --target work to 2025-04-01", cal);
  }

  @Test
  public void copyEventWithInvalidSourceDateFormat() throws Exception {
    thrown.expect(Exception.class);
    thrown.expectMessage("Invalid Command: Missing Date/invalid format");
    CopyCommand command = new CopyCommand(calManager);
    command.execute("events on 2025/03/01 --target work to 2025-04-01", cal);
  }

  @Test
  public void copyEventWithInvalidDestinationDateFormat() throws Exception {
    thrown.expect(Exception.class);
    thrown.expectMessage("Invalid Command: Missing Date/invalid format");
    CopyCommand command = new CopyCommand(calManager);
    command.execute("events on 2025-03-01 --target work to 2025/04/01", cal);
  }

  @Test
  public void copyEventWithSpecialCharactersInEventName() throws Exception {
    thrown.expect(Exception.class);
    thrown.expectMessage("Invalid Command: Missing Date/invalid format");
    command = new CopyCommand(calManager);
    command.execute("\"Team Meeting!@#\" on 2025-03-01 --target work to 2025-04-01",
            cal);
  }


}