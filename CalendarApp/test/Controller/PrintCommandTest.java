package controller;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.Calendar;
import model.ICalendar;
import utils.DateUtils;
import view.ConsoleView;
import view.View;

import static org.junit.Assert.assertEquals;

/**
 * A unit test for the PrintCommand class.
 */
public class PrintCommandTest {
  ICommand printCommand;
  ICommand createCommand;
  ICalendar cal = new Calendar();
  String command;
  View view;

  @Before
  public void init() {
    printCommand = new PrintCommand();
    createCommand = new CreateCommand();
    view = new ConsoleView();
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

  /**
   * A method to get the event on a given date.
   *
   * @param date     the date given.
   * @param calendar the calendar object.
   * @return the processed output.
   */
  public String getEventStringOnADate(String date, ICalendar calendar, String endDate) {
    String onDate = DateUtils.changeDateToDateTime(date);
    Map<String, Object> metaData = new HashMap<>();

    if (endDate == null) {
      LocalDateTime newOnDate = DateUtils.stringToLocalDateTime(onDate);
      metaData.put("localStartTime", newOnDate);
    } else {

      LocalDateTime dateT = DateUtils.pareStringToLocalDateTime(date);
      metaData.put("localStartTime", dateT);

      LocalDateTime endDateT = DateUtils.pareStringToLocalDateTime(endDate);
      metaData.put("localEndTime", endDateT);
    }
    List<Map<String, Object>> events = calendar.getMatchingEvents(metaData);
    String actualOutput = getViewCalendarOutput(events);
    return actualOutput;
  }

  /**
   * A method to get the calendar output events after being processed.
   *
   * @param events the list of events provided.
   * @return the processed output.
   */
  public String getViewCalendarOutput(List<Map<String, Object>> events) {
    PrintStream originalOut = System.out;
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    PrintStream customOut = new PrintStream(outputStream);
    System.setOut(customOut);
    customOut.flush();
    int captureStartIndex = outputStream.size();
    view.viewEvents(events);
    System.setOut(originalOut);
    String capturedOutput = outputStream.toString();
    String filteredOutput = capturedOutput.substring(captureStartIndex);
    return filteredOutput;
  }

  //print events on <dateString>

  @Test
  public void printEmptyEventsOn() throws Exception {

    ICalendar calendar = new Calendar();

    command = "event \"Event 1\" from 2025-03-01T09:00 to 2025-03-05T10:00";
    createCommand.execute(command, calendar);
    command = "event \"Event 1\" from 2025-03-02T09:00 to 2025-03-02T10:00";
    createCommand.execute(command, calendar);
    command = "event \"Event 2\" from 2025-03-02T09:00 to 2025-03-02T10:00";
    createCommand.execute(command, calendar);

    String eventsOnDate = getEventStringOnADate("2025-02-01", calendar, null);
    assertEquals(eventsOnDate, "");
  }

  @Test
  public void printEventsFromTo() throws Exception {
    ICalendar calendar = new Calendar();

    command = "event \"Recurring Event Match\" from 2025-03-02T09:00 to  2025-03-02T10:00 " +
            "repeats U for 2 times";
    createCommand.execute(command, calendar);

    command = "event \"Event 1\" from 2025-03-01T09:00 to 2025-03-05T10:00";
    createCommand.execute(command, calendar);
    command = "event \"Match 1\" from 2025-03-02T09:00 to 2025-03-02T10:00";
    createCommand.execute(command, calendar);
    command = "event \"Match 2\" from 2025-03-02T09:00 to 2025-03-02T10:00";
    createCommand.execute(command, calendar);

    String eventsOnDate = getEventStringOnADate("2025-03-02T09:00", calendar, "2025-03-02T10:00");
    String event1 = "• Subject : Recurring Event Match,Start date : 2025-03-02,Start time : 09:00,"
            + "End date : 2025-03-02,End time : 10:00,isPublic : false\n";
    String event2 = "• Subject : Match 1,Start date : 2025-03-02,Start time : 09:00," +
            "End date : 2025-03-02,"
            + "End time : 10:00,isPublic : false\n";
    String event3 = "• Subject : Match 2,Start date : 2025-03-02,Start time : 09:00,"
            + "End date : 2025-03-02,End time : 10:00,isPublic : false\n";

    assertEquals(eventsOnDate, event1 + event2 + event3);
  }

  @Test
  public void printEventsFromToWithNoMatchButSameTime() throws Exception {
    ICalendar calendar = new Calendar();

    command = "event \"Recurring Event Match\" from 2025-03-02T09:00 to  2025-03-02T10:00 " +
            "repeats U for 2 times";
    createCommand.execute(command, calendar);

    command = "event \"Event 1\" from 2025-03-01T09:00 to 2025-03-05T10:00";
    createCommand.execute(command, calendar);
    command = "event \"Match 1\" from 2025-03-02T09:00 to 2025-03-02T10:00";
    createCommand.execute(command, calendar);
    command = "event \"Match 2\" from 2025-03-02T09:00 to 2025-03-02T10:00";
    createCommand.execute(command, calendar);

    String eventsOnDate = getEventStringOnADate("2025-03-10T09:00", calendar, "2025-03-10T10:00");
    assertEquals(eventsOnDate, "");
  }

  @Test
  public void printEventsFromToWithNoMatchButDifferentDate() throws Exception {
    ICalendar calendar = new Calendar();

    command = "event \"Recurring Event Match\" from 2025-03-02T09:00 to  2025-03-02T10:00 " +
            "repeats U for 2 times";
    createCommand.execute(command, calendar);

    command = "event \"Event 1\" from 2025-03-01T09:00 to 2025-03-05T10:00";
    createCommand.execute(command, calendar);
    command = "event \"Match 1\" from 2025-03-02T09:00 to 2025-03-02T10:00";
    createCommand.execute(command, calendar);
    command = "event \"Match 2\" from 2025-03-02T09:00 to 2025-03-02T10:00";
    createCommand.execute(command, calendar);

    String eventsOnDate = getEventStringOnADate("2025-03-02T19:00", calendar, "2025-03-02T20:00");

    assertEquals(eventsOnDate, "");
  }
}
