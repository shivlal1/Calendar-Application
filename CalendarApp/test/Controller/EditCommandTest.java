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

import static Utils.DateUtils.pareStringToLocalDateTime;
import static org.junit.Assert.assertEquals;

/**
 * A unit test for the EditCommand class.
 */
public class EditCommandTest {

  ICommand editCommand;
  ICalendar cal;
  String command;

  ICommand createCommand;
  ICommand printCommand;
  ICommand showStatusCommand;
  View view;

  @Before
  public void init() {
    createCommand = new CreateCommand();
    editCommand = new EditCommand();
    printCommand = new PrintCommand();
    showStatusCommand = new ShowStatusCommand();
    view = new ConsoleView();
    cal = new Calendar();
  }


//  @Test
//  public void testSimpleCommand() throws Exception {
//    String command = "event \"International Conference\" from 2025-03-01T09:00 to 2025-03-03T13:00";
//    ACalendar cal = new Calendar();
//    ICommand createCommand = new CreateCommand();
//    createCommand.execute(command, cal);
//    //cal.printEvents();
//
//    //            "edit event name \"Annual Meeting\" from 2025-03-01T09:00:00 to 2025-03-01T10:00:00 with \"Weekly Meeting\"",

  /// /            "edit events public \"Annual Meeting\" from 2025-03-01T09:00:00 with \"true\""
  /// /            "edit events name \"Annual Meeting\" \"Weekly Meeting\""
//
//    command = "event name \"International Conference\" from 2025-03-01T09:00 to 2025-03-03T13:00 with \"Weekly Meeting\"";
//    ICommand editCommand = new EditCommand();
//    editCommand.execute(command, cal);
//    cal.printEvents();
//  }
//
//
//  @Test
//  public void recurringEvent52() throws Exception {
//
//    String command = "event \"International Conference\" from 2025-04-05T09:45 to 2025-04-05T10:00";
//    //String command = "event \"International Conference\" from 2025-03-01T09:00:00 to 2025-03-05T13:00:00";
//
//    ACalendar cal = new Calendar();
//    ICommand createCommand = new CreateCommand();
//    createCommand.execute(command, cal);
//    //cal.printEvents();
//
//    //command = "event --autoDecline \"NN\" from 2025-04-28T09:00:00 to 2025-04-28T10:00:00 repeats MTW until 2025-06-28T09:00:00";
//    command = "event --autoDecline \"N \" from 2025-04-28T09:00 to 2025-04-28T10:00 repeats MTW for 5 times";
//    createCommand.execute(command, cal);
//    // cal.printEvents();
//
//    command = "events name \"N \" \"Weekly Meeting\"";
//    ICommand editCommand = new EditCommand();
//    editCommand.execute(command, cal);
//    cal.printEvents();
//
//    //"print events from \"2025-03-01T09:00:00\" to \"2025-03-03T13:00:00\"",
//
//  }
  @Test
  public void missingEditEvent() throws Exception {
    try {
      command = "\"Meeting\" from 2025-03-01T09:00 to 2025-03-01T10:00 with \"Weekly Meeting\"";
      editCommand.execute(command, cal);
    } catch (Exception e) {
      String errorMsg = "Invalid Command Should start with 'edit event(s)' or it is Missing 'edit event(s)'";
      assertEquals(e.getMessage(), errorMsg);
    }
  }

  @Test
  public void missingNameOrProperty() throws Exception {
    try {
      command = "event \"Meeting\" from 2025-03-01T09:00 to 2025-03-01T10:00 with \"Weekly Meeting\"";
      editCommand.execute(command, cal);
    } catch (Exception e) {
      String errorMsg = "Invalid Command Missing 'eventName' or Property is missing or incorrectly placed";
      assertEquals(e.getMessage(), errorMsg);
    }
  }

  @Test
  public void missingFromTo() throws Exception {
    try {
      command = "event name \"Meeting\" 2025-03-01T09:00 to 2025-03-01T10:00 with \"Weekly Meeting\"";
      editCommand.execute(command, cal);
    } catch (Exception e) {
      assertEquals(e.getMessage(), "Invalid Command Missing From");
    }
  }

  @Test
  public void missingTo() throws Exception {
    try {
      command = "event name \"Meeting\" from 2025-03-01T09:00 2025-03-01T10:00 with \"Weekly Meeting\"";
      editCommand.execute(command, cal);
    } catch (Exception e) {
      assertEquals(e.getMessage(), "Invalid Command Missing To");
    }
  }

  @Test
  public void missingNewProperty() throws Exception {
    try {
      command = "event name \"Meeting\" from 2025-03-01T09:00 to 2025-03-01T10:00 with";
      editCommand.execute(command, cal);
    } catch (Exception e) {
      assertEquals(e.getMessage(), "Invalid Command Missing or incorrect 'newValue' format (Expected: with \"NewValue\")");
    }
  }

  @Test
  public void missingWith() throws Exception {
    try {
      command = "event name \"Meeting\" from 2025-03-01T09:00 to 2025-03-01T10:00 \"Weekly Meeting\"";
      editCommand.execute(command, cal);
    } catch (Exception e) {
      assertEquals(e.getMessage(), "Invalid Command Missing With");
    }
  }

  @Test
  public void incorrectDateFormat() throws Exception {
    try {
      command = "event name \"Meeting\" from 2025-03-0109:00 to 2025-03-01T10:00 with \"Weekly Meeting\"";
      editCommand.execute(command, cal);
    } catch (Exception e) {
      assertEquals(e.getMessage(), "Invalid Command Invalid command: Does not match expected format.");
    }
  }

  @Test
  public void testExtraWords() throws Exception {
    try {
      command = "event name \"Meeting\" from 2025-03-0109:00 to 2025-03-01T10:00 with \"Weekly Meeting\" hello there";
      editCommand.execute(command, cal);
    } catch (Exception e) {
      assertEquals(e.getMessage(), "Invalid Command Invalid command: Does not match expected format.");
    }
  }

  @Test
  public void testExtraWordsBeforeCommand() throws Exception {
    try {
      command = "Hello there event name \"Meeting\" from 2025-03-0109:00 to 2025-03-01T10:00 with \"Weekly Meeting\"";
      editCommand.execute(command, cal);
    } catch (Exception e) {
      assertEquals(e.getMessage(), "Invalid Command Should start with 'edit event(s)' or it is Missing 'edit event(s)'");
    }
  }

  @Test
  public void testDuplicateKeywords() throws Exception {
    try {
      command = "event name \"Meeting\" from from 2025-03-0109:00 to 2025-03-01T10:00 with \"Weekly Meeting\"";
      editCommand.execute(command, cal);
    } catch (Exception e) {
      assertEquals(e.getMessage(), "Invalid Command Invalid command: Does not match expected format.");
    }
  }

  @Test
  public void testDuplicateKeywords2() throws Exception {
    try {
      command = "event name \"Meeting\" from from 2025-03-0109:00 to 2025-03-01T10:00 with \"Weekly Meeting\"";
      editCommand.execute(command, cal);
    } catch (Exception e) {
      assertEquals(e.getMessage(), "Invalid Command Invalid command: Does not match expected format.");
    }
  }

  @Test
  public void testEmptyPropertyValue() throws Exception {
    try {
      command = "event name \"Meeting\" from from 2025-03-0109:00 to 2025-03-01T10:00 with \"\"";
      editCommand.execute(command, cal);
    } catch (Exception e) {
      assertEquals(e.getMessage(), "Invalid Command Invalid command: Does not match expected format.");
    }
  }

  @Test
  public void testCaseSensitive() throws Exception {

    try {
      command = "EVENT LOCATION BIRTHDAYPARTY FROM 2024-05-12T14:00 TO 2024-05-12T16:00 WITH JOHN’S HOUSE";
      editCommand.execute(command, cal);
    } catch (Exception e) {
      assertEquals(e.getMessage(), "Invalid Command Command should be lower case.");
    }
  }

  @Test
  public void testIncorrectOrderOfArguments() throws Exception {
    try {
      command = "event \"Meeting\" name from 2025-03-0109:00 to 2025-03-01T10:00 with \"Weekly Meeting\"";
      editCommand.execute(command, cal);
    } catch (Exception e) {
      assertEquals(e.getMessage(), "Invalid Command Missing 'eventName' or Property is missing or incorrectly placed");
    }
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





  public String getEventStringOnADate(String date, ICalendar calendar) {
    String onDate = DateUtils.changeDateToDateTime(date);
    LocalDateTime newOnDate = DateUtils.stringToLocalDateTime(onDate);
    Map<String, Object> metaData = new HashMap<>();
    metaData.put("localStartTime", newOnDate);
    List<Map<String, Object>> events = calendar.getMatchingEvents(metaData);
    String actualOutput = getViewCalendarOutput(events);
    return actualOutput;
  }

//
//  @Test
//  public void editSingleEventNameOnlySingleEvent() throws Exception {
//    // Recurring Event creation with future until time
//    ICalendar calendar = new Calendar();
//    command = "event \"Event\" from 2025-03-01T09:00 to 2025-03-01T10:00";
//    createCommand.execute(command, calendar);
//
//    command = "event \"Hello\" from 2025-05-07T11:00 to 2025-05-07T12:00";
//    createCommand.execute(command, calendar);
//
//    command = "event \"Event\" from 2025-05-05T11:00 to 2025-05-06T12:00";
//    createCommand.execute(command, calendar);
//
//    //change events with name "Event" to "New Name"
//    command = "events name \"Event\" \"New Name\"";
//    editCommand.execute(command, calendar);
//
//    view.viewEvents( calendar.getAllCalendarEvents());
//
//    // check whether the first event is added properly
//    String actualOutput = getEventStringOnADate("2025-03-01", calendar);
//    actualOutput = actualOutput + getEventStringOnADate("2025-03-01", calendar);
//
//    assertEquals("", actualOutput);
//    assertEquals(calendar.isUserBusy(pareStringToLocalDateTime("2025-03-13T01:05")), true);
//
//  }
//
//  @Test
//  public void editEventNameOnlyRecurring() throws Exception {
//    // Recurring Event creation with future until time
//    ICalendar calendar = new Calendar();
//    command = "event \"Event\" from 2025-03-01T09:00 to 2025-03-01T10:00";
//    createCommand.execute(command, calendar);
//
//    command = "event \"Hello\" from 2025-05-07T11:00 to 2025-05-07T12:00";
//    createCommand.execute(command, calendar);
//
//    command = "event \"Event\" from 2025-05-05T11:00 to 2025-05-06T12:00";
//    createCommand.execute(command, calendar);
//
//    command = "event \"Event\" from 2025-03-12T01:00 to 2025-03-12T02:00 " +
//            "repeats W until 2025-03-12T06:00";
//    createCommand.execute(command, calendar);
//
//    //change events with name "Event" to "New Name"
//
//    command = "events name \"Event\" \"New Name\"";
//    editCommand.execute(command, calendar);
//
//    getViewCalendarOutput( calendar.getAllCalendarEvents() );
//
//    // check whether the first event is added properly
//    String actualOutput = getEventStringOnADate("2025-03-01", calendar);
//    actualOutput = actualOutput + getEventStringOnADate("2025-03-01", calendar);
//
//    assertEquals("", actualOutput);
//    assertEquals(calendar.isUserBusy(pareStringToLocalDateTime("2025-03-13T01:05")), true);
//
//  }
}