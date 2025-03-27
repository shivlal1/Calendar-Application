package controller;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.Calendar;
import model.CalendarV2;
import model.ICalendar;
import model.ICalendarV2;
import utils.DateUtils;
import view.ConsoleView;
import view.View;

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

  @Test
  public void missingEditEvent() throws Exception {
    try {
      command = "\"Meeting\" from 2025-03-01T09:00 to 2025-03-01T10:00 with \"Weekly Meeting\"";
      editCommand.execute(command, cal);
    } catch (Exception e) {
      String errorMsg = "Invalid Command Should start with 'edit event(s)' or it is Missing " +
              "'edit event(s)'";
      assertEquals(e.getMessage(), errorMsg);
    }
  }

  @Test
  public void missingNameOrProperty() throws Exception {
    try {
      command = "event \"Meeting\" from 2025-03-01T09:00 to 2025-03-01T10:00 with " +
              "\"Weekly Meeting\"";
      editCommand.execute(command, cal);
    } catch (Exception e) {
      String errorMsg = "Invalid Command Missing 'eventName' or Property is missing or " +
              "incorrectly placed";
      assertEquals(e.getMessage(), errorMsg);
    }
  }


  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Test()
  public void missingFromTo() throws Exception {

    thrown.expect(Exception.class);
    thrown.expectMessage("Invalid Command Missing From");

    command = "event name \"Meeting\" 2025-03-01T09:00 to 2025-03-01T10:00 with " +
            "\"Weekly Meeting\"";
    editCommand.execute(command, cal);
  }

  @Test
  public void missingTo() throws Exception {
    try {
      command = "event name \"Meeting\" from 2025-03-01T09:00 2025-03-01T10:00 with " +
              "\"Weekly Meeting\"";
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
      assertEquals(e.getMessage(), "Invalid Command Missing or incorrect 'newValue' " +
              "format (Expected: with \"NewValue\")");
    }
  }

  @Test
  public void missingWith() throws Exception {
    try {
      command = "event name \"Meeting\" from 2025-03-01T09:00 to 2025-03-01T10:00 " +
              "\"Weekly Meeting\"";
      editCommand.execute(command, cal);
    } catch (Exception e) {
      assertEquals(e.getMessage(), "Invalid Command Missing With");
    }
  }

  @Test
  public void incorrectDateFormat() throws Exception {
    try {
      command = "event name \"Meeting\" from 2025-03-0109:00 to 2025-03-01T10:00 with " +
              "\"Weekly Meeting\"";
      editCommand.execute(command, cal);
    } catch (Exception e) {
      assertEquals(e.getMessage(), "Invalid Command Invalid command: Does not " +
              "match expected format.");
    }
  }

  @Test
  public void testExtraWords() throws Exception {
    try {
      command = "event name \"Meeting\" from 2025-03-0109:00 to 2025-03-01T10:00 with " +
              "\"Weekly Meeting\" hello there";
      editCommand.execute(command, cal);
    } catch (Exception e) {
      assertEquals(e.getMessage(), "Invalid Command Invalid command: Does not match " +
              "expected format.");
    }
  }

  @Test
  public void testExtraWordsBeforeCommand() throws Exception {
    try {
      command = "Hello there event name \"Meeting\" from 2025-03-0109:00 to 2025-03-01T10:00 " +
              "with \"Weekly Meeting\"";
      editCommand.execute(command, cal);
    } catch (Exception e) {
      assertEquals(e.getMessage(), "Invalid Command Should start with 'edit event(s)' " +
              "or it is Missing 'edit event(s)'");
    }
  }

  @Test
  public void testDuplicateKeywords() throws Exception {
    try {
      command = "event name \"Meeting\" from from 2025-03-0109:00 to 2025-03-01T10:00 with " +
              "\"Weekly Meeting\"";
      editCommand.execute(command, cal);
    } catch (Exception e) {
      assertEquals(e.getMessage(), "Invalid Command Invalid command: Does not " +
              "match expected format.");
    }
  }

  @Test
  public void testDuplicateKeywords2() throws Exception {
    try {
      command = "event name \"Meeting\" from from 2025-03-0109:00 to 2025-03-01T10:00 " +
              "with \"Weekly Meeting\"";
      editCommand.execute(command, cal);
    } catch (Exception e) {
      assertEquals(e.getMessage(), "Invalid Command Invalid command: Does not match " +
              "expected format.");
    }
  }

  @Test
  public void testEmptyPropertyValue() throws Exception {
    try {
      command = "event name \"Meeting\" from from 2025-03-0109:00 to 2025-03-01T10:00 with \"\"";
      editCommand.execute(command, cal);
    } catch (Exception e) {
      assertEquals(e.getMessage(), "Invalid Command Invalid command: Does not match " +
              "expected format.");
    }
  }

  @Test
  public void testCaseSensitive() throws Exception {

    try {
      command = "EVENT LOCATION BIRTHDAYPARTY FROM 2024-05-12T14:00 TO 2024-05-12T16:00 WITH" +
              " JOHN’S HOUSE";
      editCommand.execute(command, cal);
    } catch (Exception e) {
      assertEquals(e.getMessage(), "Invalid Command Command should be lower case.");
    }
  }

  @Test
  public void testIncorrectOrderOfArguments() throws Exception {
    try {
      command = "event \"Meeting\" name from 2025-03-0109:00 to 2025-03-01T10:00 with " +
              "\"Weekly Meeting\"";
      editCommand.execute(command, cal);
    } catch (Exception e) {
      assertEquals(e.getMessage(), "Invalid Command Missing 'eventName' or Property is " +
              "missing or incorrectly placed");
    }
  }

  /**
   * A method to capture console output.
   *
   * @param events list of events that needs to be captured.
   * @return filteredoutput the captured output.
   */
  private String getViewCalendarOutput(List<Map<String, Object>> events) {
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

  /**
   * A method to get the event on a particular date.
   *
   * @param date     the date in String format.
   * @param calendar the calendar object.
   * @return the processed output.
   */
  private String getEventStringOnADate(String date, ICalendar calendar) {
    String onDate = DateUtils.changeDateToDateTime(date);
    LocalDateTime newOnDate = DateUtils.stringToLocalDateTime(onDate);
    Map<String, Object> metaData = new HashMap<>();
    metaData.put("localStartTime", newOnDate);
    List<Map<String, Object>> events = calendar.getMatchingEvents(metaData);
    String actualOutput = getViewCalendarOutput(events);
    return actualOutput;
  }


  @Test
  public void editSingleEventName() throws Exception {
    // Recurring Event creation with future until time
    ICalendar calendar = new Calendar();
    command = "event \"Event\" from 2025-03-01T09:00 to 2025-03-01T10:00";
    createCommand.execute(command, calendar);

    command = "event \"Hello\" from 2025-05-07T11:05 to 2025-05-09T12:00";
    createCommand.execute(command, calendar);

    command = "event \"Event\" from 2025-05-05T11:00 to 2025-05-06T12:00";
    createCommand.execute(command, calendar);

    //change events with name "Event" to "New Name"
    command = "events name \"Event\" \"New Name\"";
    editCommand.execute(command, calendar);


    String actualOutput = getViewCalendarOutput(calendar.getAllCalendarEvents());
    String event1 = "• Subject : New Name,Start date : 2025-03-01,Start time : 09:00," +
            "End date : 2025-03-01,End time : 10:00,isPublic : false\n";
    String event2 = "• Subject : Hello,Start date : 2025-05-07,Start time : 11:05," +
            "End date : 2025-05-09,End time : 12:00,isPublic : false\n";
    String event3 = "• Subject : New Name,Start date : 2025-05-05,Start time : 11:00," +
            "End date : 2025-05-06,End time : 12:00,isPublic : false\n";

    assertEquals(event1 + event2 + event3, actualOutput);

  }


  @Test
  public void editEventNameOnlyRecurring() throws Exception {
    // Recurring Event creation with future until time
    ICalendar calendar = new Calendar();

    command = "event \"no update\" from 2025-03-15T01:00 to 2025-03-15T02:00 " +
            "repeats S until 2025-03-15T06:00";
    createCommand.execute(command, calendar);


    command = "event \"Event\" from 2025-03-12T01:00 to 2025-03-12T02:00 " +
            "repeats W until 2025-03-12T06:00";
    createCommand.execute(command, calendar);

    command = "event \"Event\" from 2025-03-01T09:00 to 2025-03-01T10:00";
    createCommand.execute(command, calendar);

    command = "events name \"Event\" \"New Name\"";
    editCommand.execute(command, calendar);

    String actualOutput = getViewCalendarOutput(calendar.getAllCalendarEvents());
    String event1 = "• Subject : no update,Start date : 2025-03-15,Start time : 01:00," +
            "End date : 2025-03-15,End time : 02:00,isPublic : false\n";
    String event2 = "• Subject : New Name,Start date : 2025-03-12,Start time : 01:00," +
            "End date : 2025-03-12,End time : 02:00,isPublic : false\n";
    String event3 = "• Subject : New Name,Start date : 2025-03-01,Start time : 09:00," +
            "End date : 2025-03-01,End time : 10:00,isPublic : false\n";
    // check whether the first event is added properly

    assertEquals(event1 + event2 + event3, actualOutput);

  }

  @Test
  public void editSingleEvenLocation() throws Exception {
    // Recurring Event creation with future until time
    ICalendar calendar = new Calendar();
    command = "event \"Event\" from 2025-03-01T09:00 to 2025-03-01T10:00";
    createCommand.execute(command, calendar);

    command = "event \"Hello\" from 2025-05-07T11:05 to 2025-05-09T12:00";
    createCommand.execute(command, calendar);

    command = "event \"Event\" from 2025-05-05T11:00 to 2025-05-06T12:00";
    createCommand.execute(command, calendar);

    //change events with name "Event" to "New Name"
    command = "events location \"Event\" \"Snell Library\"";
    editCommand.execute(command, calendar);

    String actualOutput = getViewCalendarOutput(calendar.getAllCalendarEvents());
    String event1 = "• Subject : Event,Start date : 2025-03-01,Start time : 09:00," +
            "End date : 2025-03-01,End time : 10:00,location : Snell Library,isPublic : false\n";
    String event2 = "• Subject : Hello,Start date : 2025-05-07,Start time : 11:05," +
            "End date : 2025-05-09,End time : 12:00,isPublic : false\n";
    String event3 = "• Subject : Event,Start date : 2025-05-05,Start time : 11:00," +
            "End date : 2025-05-06,End time : 12:00,location : Snell Library,isPublic : false\n";

    assertEquals(event1 + event2 + event3, actualOutput);
  }

  @Test
  public void editLocation() throws Exception {
    // Recurring Event creation with future until time
    ICalendar calendar = new Calendar();

    command = "event \"no update\" from 2025-03-15T01:00 to 2025-03-15T02:00 " +
            "repeats S until 2025-03-15T06:00";
    createCommand.execute(command, calendar);

    command = "event \"Event\" from 2025-03-12T01:00 to 2025-03-12T02:00 " +
            "repeats W until 2025-03-12T06:00";
    createCommand.execute(command, calendar);

    command = "event \"Event\" from 2025-03-01T09:00 to 2025-03-01T10:00";
    createCommand.execute(command, calendar);

    command = "events name \"Event\" \"New Name\"";
    editCommand.execute(command, calendar);

    command = "events name \"New Name\" \"OLD Name\"";
    editCommand.execute(command, calendar);

    command = "events description \"no update\" \"this event has no update\"";
    editCommand.execute(command, calendar);

    view.viewEvents(calendar.getAllCalendarEvents());

    String actualOutput = getViewCalendarOutput(calendar.getAllCalendarEvents());
    String event1 = "• Subject : no update,Start date : 2025-03-15,Start time : 01:00," +
            "End date : 2025-03-15,End time : 02:00,description : this event has no update" +
            ",isPublic : false\n";
    String event2 = "• Subject : OLD Name,Start date : 2025-03-12,Start time : 01:00,End date " +
            ": 2025-03-12," +
            "End time : 02:00,isPublic : false\n";
    String event3 = "• Subject : OLD Name,Start date : 2025-03-01,Start time : 09:00,End date" +
            " : 2025-03-01," +
            "End time : 10:00,isPublic : false\n";

    assertEquals(event1 + event2 + event3, actualOutput);
  }


  @Test
  public void changeDatesOfRecurringEvent() throws Exception {
    // Recurring Event creation with future until time
    ICalendar calendar = new Calendar();

    command = "event \"Annaul Mee@13?\" from 2025-03-15T01:00 to 2025-03-15T02:00 " +
            "repeats M until 2025-03-31T06:00";
    createCommand.execute(command, calendar);

    command = "events name \"Annaul Mee@13?\" \"Annual Meeting\"";
    editCommand.execute(command, calendar);

    command = "events location \"Annual Meeting\" \"snell\"";
    editCommand.execute(command, calendar);

    command = "events description \"Annual Meeting\" \"empty event\"";
    editCommand.execute(command, calendar);

    command = "events isPublic \"Annual Meeting\" \"true\"";
    editCommand.execute(command, calendar);

    String event1 = "• Subject : Annual Meeting,Start date : 2025-03-17,Start time : 01:00," +
            "End date : 2025-03-17,End time : 02:00,location : snell,description : empty event," +
            "isPublic : true\n";
    String event2 = "• Subject : Annual Meeting,Start date : 2025-03-24,Start time : 01:00," +
            "End date : 2025-03-24," +
            "End time : 02:00,location : snell,description : empty event,isPublic : true\n";
    String event3 = "• Subject : Event 2,Start date : 2025-03-31,Start time : 01:00," +
            "End date : 2025-03-31,End time : 02:05,location : snell,description : empty event," +
            "isPublic : true\n";

    command = "events name \"Annual Meeting\" from 2025-03-31T01:00 to 2025-03-31T02:00  with " +
            "\"Event 2\"";
    editCommand.execute(command, calendar);

    command = "events endDate \"Event 2\" \"2025-03-31T02:05\"";
    editCommand.execute(command, calendar);

    view.viewEvents(calendar.getAllCalendarEvents());
    String actualOutput = getViewCalendarOutput(calendar.getAllCalendarEvents());

    assertEquals(event1 + event2 + event3, actualOutput);

  }


  @Test
  public void changePropertiesOfSingleEvent() throws Exception {
    // Recurring Event creation with future until time
    ICalendar calendar = new Calendar();

    command = "event \"Event 1\" from 2025-03-01T09:00 to 2025-03-05T10:00";
    createCommand.execute(command, calendar);

    command = "event \"Event 2\" from 2025-04-01T00:00 to 2025-04-01T23:59";
    createCommand.execute(command, calendar);

    command = "event \"Event 3\" from 2025-03-01T09:00 to 2025-03-01T10:00";
    createCommand.execute(command, calendar);

    command = "events startDate \"Event 1\" \"2025-03-01T06:00 \"";
    editCommand.execute(command, calendar);

    command = "events startDate \"Event 2\" \"2025-04-01T23:00\"";
    editCommand.execute(command, calendar);

    command = "events endDate \"Event 3\" \"2025-03-01T09:45\"";
    editCommand.execute(command, calendar);

    String actualOutput = getViewCalendarOutput(calendar.getAllCalendarEvents());
    String event1 = "• Subject : Event 1,Start date : 2025-03-01,Start time : 06:00," +
            "End date : 2025-03-05,End time : 10:00,isPublic : false\n";
    String event2 = "• Subject : Event 2,Start date : 2025-04-01,Start time : 23:00," +
            "End date : 2025-04-01,End time : 23:59,isPublic : false\n";
    String event3 = "• Subject : Event 3,Start date : 2025-03-01,Start time : 09:00," +
            "End date : 2025-03-01,End time : 09:45,isPublic : false\n";

    assertEquals(event1 + event2 + event3, actualOutput);
  }

  @Test
  public void changePropertiesOfRecurringEvent() throws Exception {
    // Recurring Event creation with future until time
    ICalendar calendar = new Calendar();
    command = "event \"Annual Meet\" from 2025-03-17T01:00 to 2025-03-17T06:00 " +
            "repeats M until 2025-03-17T06:00";

    createCommand.execute(command, calendar);

    command = "events startDate \"Annual Meet\" \"2025-03-17T03:00 \"";
    editCommand.execute(command, calendar);

    command = "events endDate \"Annual Meet\" \"2025-03-17T04:00 \"";
    editCommand.execute(command, calendar);

    String actualOutput = getViewCalendarOutput(calendar.getAllCalendarEvents());
    String event1 = "• Subject : Annual Meet,Start date : 2025-03-17,Start time : 03:00," +
            "End date : 2025-03-17,End time : 04:00,isPublic : false\n";

    assertEquals(event1, actualOutput);
  }


  @Test
  public void invalidStartDatesEditSingleEvent() throws Exception {

    thrown.expect(Exception.class);
    thrown.expectMessage("start date should be before end date");

    ICalendar calendar = new Calendar();

    //change start date  after end date
    command = "event \"Event 1\" from 2025-03-01T09:00 to 2025-03-05T10:00";
    createCommand.execute(command, calendar);

    command = "events startDate \"Event 1\" \"2025-03-05T10:01\"";
    editCommand.execute(command, calendar);

  }

  @Test
  public void invalidStartDatesEditSingleEventDifferentDay() throws Exception {

    thrown.expect(Exception.class);
    thrown.expectMessage("start date should be before end date");

    ICalendar calendar = new Calendar();

    //change start date  after end date
    command = "event \"Event 1\" from 2025-03-01T09:00 to 2025-03-05T10:00";
    createCommand.execute(command, calendar);

    command = "events startDate \"Event 1\" \"2025-03-06T10:01\"";
    editCommand.execute(command, calendar);

  }


  @Test
  public void invalidStartDatesEditRecurringEvent() throws Exception {
    thrown.expect(Exception.class);
    thrown.expectMessage("invalid date for recurring event");
    ICalendar calendar = new Calendar();

    //change start date  after end date
    command = "event \"Annual Meet\" from 2025-03-15T01:00 to 2025-03-15T02:00 " +
            "repeats M until 2025-03-31T06:00";
    createCommand.execute(command, calendar);

    command = "events startDate \"Annual Meet\" \"2025-03-15T01:01\"";
    editCommand.execute(command, calendar);
  }

  @Test
  public void invalidStartDatesEditRecurringEvent2() throws Exception {
    thrown.expect(Exception.class);
    thrown.expectMessage("invalid date for recurring event");
    ICalendar calendar = new Calendar();

    //change start date  after end date
    command = "event \"Annual Meet\" from 2025-03-15T01:00 to 2025-03-15T02:00 " +
            "repeats M until 2025-03-31T06:00";
    createCommand.execute(command, calendar);

    command = "events startDate \"Annual Meet\" \"2025-03-15T02:01\"";
    editCommand.execute(command, calendar);
  }


  @Test
  public void nomatchupdate() throws Exception {
    thrown.expect(Exception.class);
    thrown.expectMessage("no matching update");
    ICalendar calendar = new Calendar();

    //change start date  after end date
    command = "event \"Annual Meet\" from 2025-03-15T01:00 to 2025-03-15T02:00 " +
            "repeats M until 2025-03-31T06:00";
    createCommand.execute(command, calendar);

    command = "events startDate \"Annual\" \"2025-03-15T02:01\"";
    editCommand.execute(command, calendar);
  }


  @Test
  public void invalidEndDate() throws Exception {
    thrown.expect(Exception.class);
    thrown.expectMessage("Single Event End date should be before start date");

    ICalendar calendar = new Calendar();

    //change start date  after end date
    command = "event \"Event 1\" from 2025-03-01T09:00 to 2025-03-05T10:00";
    createCommand.execute(command, calendar);

    command = "events endDate \"Event 1\" \"2025-02-05T10:00\"";
    editCommand.execute(command, calendar);
  }


  @Test
  public void invalidEndDatesEditRecurringEvent() throws Exception {
    thrown.expect(Exception.class);
    thrown.expectMessage("Recurring event invalid dates");
    ICalendar calendar = new Calendar();

    //change start date  after end date
    command = "event \"Annual Meet\" from 2025-03-15T01:00 to 2025-03-15T02:00 " +
            "repeats M for 1 times";
    createCommand.execute(command, calendar);

    command = "events endDate \"Annual Meet\" \"2025-03-18T01:01\"";
    editCommand.execute(command, calendar);
  }

  @Test
  public void invalidEndDatesEditRecurringEvent2() throws Exception {
    thrown.expect(Exception.class);
    thrown.expectMessage("Recurring event invalid dates");
    ICalendar calendar = new Calendar();

    command = "event \"Annual Meet\" from 2025-03-15T01:00 to 2025-03-15T02:00 " +
            "repeats M for 1 times";
    createCommand.execute(command, calendar);

    command = "events endDate \"Annual Meet\" \"2025-03-17T00:59\"";
    editCommand.execute(command, calendar);
  }

  @Test
  public void editNonExistingPropertyName() throws Exception {
    ICalendar calendar = new Calendar();

    thrown.expect(Exception.class);
    thrown.expectMessage("invalid property");

    command = "event \"Annual Meet\" from 2025-03-15T01:00 to 2025-03-15T02:00 " +
            "repeats M for 1 times";
    createCommand.execute(command, calendar);

    command = "events xyz \"Annual Meet\" \"2025-03-17T00:59\"";
    editCommand.execute(command, calendar);
  }


  // TEST CASES FOR
  // edit events <property> <eventName> from <dateStringTtimeString> with <NewPropertyValue>

  @Test
  public void editUsingFrom() throws Exception {
    ICalendar calendar = new Calendar();

    command = "event \"Event 1\" from 2025-03-01T09:00 to 2025-03-05T10:00";
    createCommand.execute(command, calendar);

    command = "event \"Event 1\" from 2025-03-02T09:00 to 2025-03-02T10:00";
    createCommand.execute(command, calendar);

    command = "events name \"Event 1\" from 2025-03-02T09:00 with \"Event 2\"";
    editCommand.execute(command, calendar);

    String actualOutput = getViewCalendarOutput(calendar.getAllCalendarEvents());
    System.out.println(actualOutput);
    String event1 = "• Subject : Event 1,Start date : 2025-03-01,Start time : 09:00," +
            "End date : 2025-03-05,End time : 10:00,isPublic : false\n";
    String event2 = "• Subject : Event 2,Start date : 2025-03-02,Start time : 09:00," +
            "End date : 2025-03-02,End time : 10:00,isPublic : false\n";

    assertEquals(event1 + event2, actualOutput);
  }

  @Test
  public void errorEditUsingFrom() throws Exception {
    thrown.expect(Exception.class);
    thrown.expectMessage("start date should be before end date");

    ICalendar calendar = new Calendar();

    command = "event \"Event 1\" from 2025-03-01T09:00 to 2025-03-05T10:00";
    createCommand.execute(command, calendar);

    command = "event \"Event 1\" from 2025-03-02T09:00 to 2025-03-02T10:00";
    createCommand.execute(command, calendar);

    command = "events startDate \"Event 1\" from 2025-03-02T09:00 with \"2025-04-02T10:00\"";
    editCommand.execute(command, calendar);
  }

  // edit event <property> <eventName> from <dateStringTtimeString> to <dateStringTtimeString>
  // with <NewPropertyValue>

  @Test
  public void editUsingFromTo() throws Exception {
    ICalendar calendar = new Calendar();

    command = "event \"Event 1\" from 2025-03-01T09:00 to 2025-03-05T10:00";
    createCommand.execute(command, calendar);
    command = "event \"Event 1\" from 2025-03-02T09:00 to 2025-03-02T10:00";
    createCommand.execute(command, calendar);
    command = "event \"Event 2\" from 2025-03-02T09:00 to 2025-03-02T10:00";
    createCommand.execute(command, calendar);
    view.viewEvents(calendar.getAllCalendarEvents());
    command = "events startDate \"Event 1\" from 2025-03-02T09:00 to 2025-03-02T10:00 with " +
            "\"2025-03-02T09:15\"";
    editCommand.execute(command, calendar);

    String actualOutput = getViewCalendarOutput(calendar.getAllCalendarEvents());
    String event1 = "• Subject : Event 1,Start date : 2025-03-01,Start time : 09:00," +
            "End date : 2025-03-05,End time : 10:00,isPublic : false\n";
    String event2 = "• Subject : Event 1,Start date : 2025-03-02,Start time : 09:15,End date : " +
            "2025-03-02," + "End time : 10:00,isPublic : false\n";
    String event3 = "• Subject : Event 2,Start date : 2025-03-02,Start time : 09:00,End date : " +
            "2025-03-02," + "End time : 10:00,isPublic : false\n";
    assertEquals(event1 + event2 + event3, actualOutput);
  }

  @Test
  public void errorUsingUsingFromTo() throws Exception {
    thrown.expect(Exception.class);
    thrown.expectMessage("start date should be before end date");

    ICalendar calendar = new Calendar();

    command = "event \"Event 1\" from 2025-03-01T09:00 to 2025-03-05T10:00";
    createCommand.execute(command, calendar);
    command = "event \"Event 1\" from 2025-03-02T09:00 to 2025-03-02T10:00";
    createCommand.execute(command, calendar);
    command = "event \"Event 2\" from 2025-03-02T09:00 to 2025-03-02T10:00";
    createCommand.execute(command, calendar);
    view.viewEvents(calendar.getAllCalendarEvents());
    System.out.println("done");
    command = "events startDate \"Event 1\" from 2025-03-02T09:00 to 2025-03-02T10:00 with " +
            "\"2025-04-02T09:00\"";
    editCommand.execute(command, calendar);
  }

  @Test
  public void editLocationUsingFromTo() throws Exception {
    ICalendar calendar = new Calendar();

    command = "event \"Event 1\" from 2025-03-01T09:00 to 2025-03-05T10:00";
    createCommand.execute(command, calendar);
    command = "event \"Event 1\" from 2025-03-02T09:00 to 2025-03-02T10:00";
    createCommand.execute(command, calendar);
    command = "event \"Event 2\" from 2025-03-02T09:00 to 2025-03-02T10:00";
    createCommand.execute(command, calendar);
    view.viewEvents(calendar.getAllCalendarEvents());
    System.out.println("done");
    command = "events location \"Event 1\" from 2025-03-02T09:00 to 2025-03-02T10:00 with " +
            "\"white building\"";
    editCommand.execute(command, calendar);

    String actualOutput = getViewCalendarOutput(calendar.getAllCalendarEvents());
    String event1 = "• Subject : Event 1,Start date : 2025-03-01,Start time : 09:00," +
            "End date : 2025-03-05,End time : 10:00,isPublic : false\n";
    String event2 = "• Subject : Event 1,Start date : 2025-03-02,Start time : 09:00,End date : " +
            "2025-03-02,End time : 10:00," + "location : white building,isPublic : false\n";
    String event3 = "• Subject : Event 2,Start date : 2025-03-02,Start time : 09:00,End date :" +
            " 2025-03-02," + "End time : 10:00,isPublic : false\n";
    assertEquals(event1 + event2 + event3, actualOutput);
  }


  @Test
  public void v2calEditStartTimeOverlap() throws Exception {

    thrown.expect(Exception.class);
    thrown.expectMessage("the event conflicts with another event");

    ICalendarV2 calendar = new CalendarV2(ZoneId.of("Asia/Kolkata"));

    command = "event \"Event 1\" from 2025-03-01T09:00 to 2025-03-01T10:00";
    createCommand.execute(command, calendar);

    command = "event \"Event 2\" from 2025-03-01T11:00 to 2025-03-01T12:00";
    createCommand.execute(command, calendar);

    command = "events startDate \"Event 2\" \"2025-03-01T09:45\"";
    editCommand.execute(command, calendar);

  }

  @Test
  public void v2calEditStartTimeSuccess() throws Exception {

    ICalendarV2 calendar = new CalendarV2(ZoneId.of("Asia/Kolkata"));

    command = "event \"Event 1\" from 2025-03-01T09:00 to 2025-03-01T10:00";
    createCommand.execute(command, calendar);

    command = "event \"Event 2\" from 2025-03-01T11:00 to 2025-03-01T12:00";
    createCommand.execute(command, calendar);

    command = "events startDate \"Event 2\" \"2025-03-01T11:45\"";
    editCommand.execute(command, calendar);

    String actualOutput = getViewCalendarOutput(calendar.getAllCalendarEvents());
    String event1 = "• Subject : Event 1,Start date : 2025-03-01,Start time : 09:00," +
            "End date : 2025-03-01,End time : 10:00,isPublic : false\n" +
            "• Subject : Event 2,Start date : 2025-03-01,Start time : 11:45," +
            "End date : 2025-03-01,End time : 12:00,isPublic : false\n";

    assertEquals(event1, actualOutput);
  }


  @Test
  public void v2calEditEndTimeOverlap() throws Exception {
    thrown.expect(Exception.class);
    thrown.expectMessage("the event conflicts with another event");

    ICalendarV2 calendar = new CalendarV2(ZoneId.of("Asia/Kolkata"));
    command = "event \"Event 1\" from 2025-03-01T10:00 to 2025-03-01T11:00";
    createCommand.execute(command, calendar);

    command = "event \"Event 2\" from 2025-03-01T09:00 to 2025-03-01T09:45";
    createCommand.execute(command, calendar);

    command = "events endDate \"Event 2\" \"2025-03-01T10:45\"";
    editCommand.execute(command, calendar);

  }


  @Test
  public void v2calEditNoOverlap() throws Exception {

    ICalendarV2 calendar = new CalendarV2(ZoneId.of("Asia/Kolkata"));
    command = "event \"Event 1\" from 2025-03-01T10:00 to 2025-03-01T11:00";
    createCommand.execute(command, calendar);

    command = "event \"Event 2\" from 2025-03-01T09:00 to 2025-03-01T09:45";
    createCommand.execute(command, calendar);

    command = "events endDate \"Event 2\" \"2025-03-01T09:30\"";
    editCommand.execute(command, calendar);

    String actualOutput = getViewCalendarOutput(calendar.getAllCalendarEvents());
    String event1 = "• Subject : Event 1,Start date : 2025-03-01,Start time : 10:00," +
            "End date : 2025-03-01,End time : 11:00,isPublic : false\n" +
            "• Subject : Event 2,Start date : 2025-03-01,Start time : 09:00," +
            "End date : 2025-03-01,End time : 09:30,isPublic : false\n";
    assertEquals(event1, actualOutput);
  }


}