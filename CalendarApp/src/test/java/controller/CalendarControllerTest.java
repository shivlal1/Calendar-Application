package controller;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import model.Calendar;
import model.ICalendar;

import static org.junit.Assert.assertEquals;

public class CalendarControllerTest {


  @Rule
  public ExpectedException thrown = ExpectedException.none();


  @Test
  public void commandTest() throws Exception {

    thrown.expect(Exception.class);
    thrown.expectMessage("command should start with only create,edit,print,show,status");

    ICalendar calendar = new Calendar();

    CalendarController controller = new CalendarController();
    controller.execute("hello", calendar);

  }

  @Test
  public void printEmptyEventsOn() throws Exception {
    ICalendar calendar = new Calendar();
    CalendarController controller = new CalendarController();
    ICommand createCommand = new CreateCommand();

    String command;
    command = "event \"Event 1\" from 2025-03-01T09:00 to 2025-03-05T10:00";
    createCommand.execute(command, calendar);
    command = "event \"Event 1\" from 2025-03-02T09:00 to 2025-03-02T10:00";
    createCommand.execute(command, calendar);
    command = "create event \"Event 2\" from 2025-03-02T09:00 to 2025-03-02T10:00";
    controller.execute(command, calendar);

    assertEquals(calendar.getAllCalendarEvents().size(), 3);
  }
}