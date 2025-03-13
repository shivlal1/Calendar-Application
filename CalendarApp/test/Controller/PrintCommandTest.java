package Controller;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Model.Calendar;
import Model.ICalendar;
import Utils.DateUtils;
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

  @Test
  public void printtest() throws Exception {
    command = "print events from \"2025-04-01T10:00\" to \"2025-04-04T11:00\"";
    command = "print events on \"2025-04-01\"";

    ICommand cmd = new CalendarController();
    cmd.execute(command, cal);
  }


  public String getEventStringOnADate(String date, ICalendar calendar) {
    String onDate = DateUtils.changeDateToDateTime(date);
    LocalDateTime newOnDate = DateUtils.stringToLocalDateTime(onDate);
    Map<String, Object> metaData = new HashMap<>();
    metaData.put("localStartTime", newOnDate);
    List<Map<String, Object>> events = calendar.getMatchingEvents(metaData);
    String actualOutput = getViewCalendarOutput(events);
    return actualOutput;
  }

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

    String eventsOnDate = getEventStringOnADate("2025-02-01",calendar);

    assertEquals(eventsOnDate, "");
  }

  @Test
  public void printEventsOn() throws Exception {
    ICalendar calendar = new Calendar();

    command = "event \"Event 1\" from 2025-03-01T09:00 to 2025-03-05T10:00";
    createCommand.execute(command, calendar);
    command = "event \"Event 1\" from 2025-03-02T09:00 to 2025-03-02T10:00";
    createCommand.execute(command, calendar);
    command = "event \"Event 2\" from 2025-03-02T09:00 to 2025-03-02T10:00";
    createCommand.execute(command, calendar);


    command = "event \"Recurring Event\" from 2025-03-11T01:00 to 2025-03-11T02:00 " +
            "repeats TW for 2 times";
    createCommand.execute(command, calendar);

    String event1 = "• Subject : Event 1,Start date : 2025-03-01,Start time : 09:00," +
            "End date : 2025-03-05,End time : 10:00,isPublic : false\n";
    String event2 ="• Subject : Event 1,Start date : 2025-03-02,Start time : 09:00," +
            "End date : 2025-03-02,End time : 10:00,isPublic : false\n";
    String event3="• Subject : Event 2,Start date : 2025-03-02,Start time : 09:00," +
            "End date : 2025-03-02,End time : 10:00,isPublic : false\n";
    String eventsOnDate = getEventStringOnADate("2025-03-02",calendar);
    assertEquals(event1+event2+event3,eventsOnDate);

    //System.out.println("events \n"+eventsOnDate);
  }


}
