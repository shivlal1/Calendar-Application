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

public class CreateCommandTest {
  ICommand createCommand;
  ICommand editCommand;
  ICommand printCommand;
  ICommand showStatusCommand;
  ICalendar cal;
  String command;
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
  public void missingEventKeyWord() throws Exception {
    try {
      command = "evnt --autoDecline \"Meeting\" from 2025-03-01T09:00 to 2025-03-01T10:00";
      createCommand.execute(command, cal);
    } catch (Exception e) {
      assertEquals(e.getMessage(), "error :Must start with create event");
    }
  }

  @Test
  public void autoDeclineMisplaced() throws Exception {
    try {
      command = "event \"Meeting\" from 2025-03-01T09:00 to 2025-03-01T10:00 --autoDecline";
      createCommand.execute(command, cal);
    } catch (Exception e) {
      assertEquals(e.getMessage(), "error :--autoDecline must appear immediately after create event");
    }
  }

  @Test
  public void fromMissing() throws Exception {
    try {
      command = "event \"Hello\" 2025-03-01T09:00 to 2025-03-01T10:00";
      createCommand.execute(command, cal);
    } catch (Exception e) {
      assertEquals(e.getMessage(), "error :Missing 'from' or 'on' keyword.");
    }
  }

  @Test
  public void toMissing() throws Exception {
    try {
      command = "event \"Hello\" from 2025-03-01T09:00 2025-03-01T10:00";
      createCommand.execute(command, cal);
    } catch (Exception e) {
      assertEquals(e.getMessage(), "error :from must be followed by to date");
    }
  }

  @Test
  public void missingFromDate() throws Exception {
    try {
      command = "event \"Hello\" from to 2025-03-01T10:00";
      createCommand.execute(command, cal);
    } catch (Exception e) {
      assertEquals(e.getMessage(), "error :Does not match expected format.");
    }
  }

  @Test
  public void missingToDate() throws Exception {
    try {
      command = "event \"Hello\" from 2025-03-01T10:00 to";
      createCommand.execute(command, cal);
    } catch (Exception e) {
      assertEquals(e.getMessage(), "error :from must be followed by to date");
    }
  }

  @Test
  public void invalidDate() throws Exception {
    try {
      command = "event \"Hello\" from 2025-03-01x10:00 to 2025-03-01T11:00";
      createCommand.execute(command, cal);
    } catch (Exception e) {
      assertEquals(e.getMessage(), "error :Does not match expected format.");
    }
  }

  @Test
  public void invalidDate2() throws Exception {
    try {
      command = "event \"Hello\" from 2025-03-01 to 2025-03-01T11:00";
      createCommand.execute(command, cal);
    } catch (Exception e) {
      assertEquals(e.getMessage(), "error :Does not match expected format.");
    }
  }

  @Test
  public void recurringEventcommand() throws Exception {
    try {
      command = "event \"Hello\" from 2025-03-01T10:00 to 2025-03-01T11:00 repeats MT for N times";
      createCommand.execute(command, cal);
    } catch (Exception e) {
      assertEquals(e.getMessage(), "error :'for' must be followed by a valid number of times.");
    }
  }

  @Test
  public void recurringEventcommand2() throws Exception {
    try {
      command = "event \"Hello\" from 2025-03-01T10:00 to 2025-03-01T11:00 repeats MT? for 5 times";
      createCommand.execute(command, cal);
    } catch (Exception e) {
      assertEquals(e.getMessage(), "error :Does not match expected format.");
    }
  }


  @Test
  public void recurringEventcommand3() throws Exception {
    try {
      command = "event \"Hello\"  2025-03-01T10:00 to 2025-03-01T11:00 repeats MT for 5 times";
      createCommand.execute(command, cal);
    } catch (Exception e) {
      assertEquals(e.getMessage(), "error :Missing 'from' or 'on' keyword.");
    }
  }

  @Test
  public void recurringEventcommand4() throws Exception {
    try {
      command = "event \"Hello\" from 2025-03-01T10:00 repeats MT for 5 times";
      createCommand.execute(command, cal);
    } catch (Exception e) {
      assertEquals(e.getMessage(), "error :from must be followed by to date");
    }
  }

  @Test
  public void recurringEventcommand5() throws Exception {
    try {
      command = "event \"Hello\"  from 2025-03-01T10:00 to 2025-03-01T11:00 repeats MT";
      createCommand.execute(command, cal);
    } catch (Exception e) {
      assertEquals(e.getMessage(), "error :'repeats' must be followed by 'for <N> times' or 'until <date>'.");
    }
  }

  @Test
  public void recurringEventcommand6() throws Exception {
    try {
      command = "event \"Hello\"  from 2025-03-01T10:00 to 2025-03-01T11:00 for 5 times";
      createCommand.execute(command, cal);
    } catch (Exception e) {
      assertEquals(e.getMessage(), "error :Does not match expected format.");
    }
  }


  @Test
  public void recurringEventcommand7() throws Exception {
    try {
      command = "event --autoDecline \"Hello\" from 2025-03-01T10:00 to 2025-03-01T11:00 " +
              "repeats M until";

      createCommand.execute(command, cal);
    } catch (Exception e) {
      assertEquals(e.getMessage(), "error :'until' must be followed by a valid date.");
    }
  }

  @Test
  public void recurringEventcommand8() throws Exception {
    try {
      command = "event --autoDecline \"Hello\" from 2025-03-01T10:00 to 2025-03-01T11:00 " +
              "repeats  until 2025-03-025T11:00";

      createCommand.execute(command, cal);
    } catch (Exception e) {
      assertEquals(e.getMessage(), "error :Does not match expected format.");
    }
  }


  @Test
  public void recurringEventcommand9() throws Exception {
    try {
      command = "event  \"Hello\" from 2025-03-01T10:00 to 2025-03-01T11:00 " +
              "repeats  MT until";

      createCommand.execute(command, cal);
    } catch (Exception e) {
      assertEquals(e.getMessage(), "error :'until' must be followed by a valid date.");
    }
  }

  @Test
  public void eventMissing() throws Exception {
    try {
      command = "--autoDecline \"Hello world\" on 2025-03-01T11:00";
      createCommand.execute(command, cal);
    } catch (Exception e) {
      assertEquals(e.getMessage(), "error :Must start with create event");
    }
  }

  @Test
  public void eventNameMissing() throws Exception {
    try {
      command = "event on 2025-03-01T11:00";
      createCommand.execute(command, cal);
    } catch (Exception e) {
      assertEquals(e.getMessage(), "error :Does not match expected format.");
    }
  }

  @Test
  public void emptyCommand() throws Exception {
    try {
      command = " ";
      createCommand.execute(command, cal);
    } catch (Exception e) {
      assertEquals(e.getMessage(), "error :Must start with create event");
    }
  }

  @Test
  public void recError() throws Exception {
    try {
      command = "event \"Hello\" on 2025-03-01 repeats MT\n";
      createCommand.execute(command, cal);
    } catch (Exception e) {
      assertEquals(e.getMessage(), "error :'repeats' must be followed by 'for <N> times' or 'until <date>'.");
    }
  }

  @Test
  public void missingT() throws Exception {
    try {
      command = "event \"Conference Call\" from 2025-03-10 14:30 to 2025-03-10T15:30";
      createCommand.execute(command, cal);
    } catch (Exception e) {
      assertEquals(e.getMessage(), "error :Does not match expected format.");
    }
  }

  @Test
  public void missingQuotesInEventName() throws Exception {
    try {
      command = "event Team Meeting from 2025-03-10T14:30 to 2025-03-10T15:30";
      createCommand.execute(command, cal);
    } catch (Exception e) {
      assertEquals(e.getMessage(), "error :Does not match expected format.");
    }
  }

  @Test
  public void incorrectDateFormat() throws Exception {
    try {
      command = "event \"Project Review\" from 03-10-2025T14:30 to 03-10-2025T15:30";
      createCommand.execute(command, cal);
    } catch (Exception e) {
      assertEquals(e.getMessage(), "error :Does not match expected format.");
    }
  }

  @Test
  public void invalidDAte() throws Exception {
    try {
      command = "event \"Dinner\" from 2025-04-20T19:00 to 2025-04-20";
      createCommand.execute(command, cal);
    } catch (Exception e) {
      assertEquals(e.getMessage(), "error :Does not match expected format.");
    }
  }

  @Test
  public void invalidHourInTime() throws Exception {
    try {
      command = "event \"Breakfast\" from 2025-04-20T8:00 to 2025-04-20T09:00";
      createCommand.execute(command, cal);
    } catch (Exception e) {
      assertEquals(e.getMessage(), "error :Does not match expected format.");
    }
  }

  @Test
  public void fromAndToSwapOrder() throws Exception {
    try {
      command = "event \"Sprint Planning\" to 2025-03-10T14:30 from 2025-03-10T15:30 ";
      createCommand.execute(command, cal);
    } catch (Exception e) {
      assertEquals(e.getMessage(), "error :Does not match expected format.");
    }
  }

  @Test
  public void startTimeAfterEndTime() throws Exception {
    try {
      command = "event \"Yoga Session\" from 2025-06-15T10:00 to 2025-06-15T09:00";
      createCommand.execute(command, cal);
    } catch (Exception e) {
      assertEquals(e.getMessage(), "end date cannot be before start date");
    }
  }

  @Test
  public void missingToKeyword() throws Exception {
    try {
      command = "event \"Call with Client\" from 2025-09-30T16:00 2025-09-30T17:00 ";
      createCommand.execute(command, cal);
    } catch (Exception e) {
      assertEquals(e.getMessage(), "error :from must be followed by to date");
    }
  }


  @Test
  public void extraWords() throws Exception {
    try {
      command = "event \"Game Night\" from 2025-10-10T18:30 to 2025-10-10T22:00 at John's Place";
      createCommand.execute(command, cal);
    } catch (Exception e) {
      assertEquals(e.getMessage(), "error :Does not match expected format.");
    }
  }

  @Test
  public void specialChar() throws Exception {
    try {
      command = "event Project@Review from 2025-06-05T13:00 to 2025-06-05T14:00";
      createCommand.execute(command, cal);
    } catch (Exception e) {
      assertEquals(e.getMessage(), "error :Does not match expected format.");
    }
  }

  @Test
  public void invalidMonth() throws Exception {
    try {
      command = "event \"Checkup\" from 2025-13-05T10:00 to 2025-13-05T11:00";
      createCommand.execute(command, cal);
    } catch (Exception e) {
      String msg = "Text '2025-13-05 10:00' could not be parsed: Invalid value for MonthOfYear (valid values 1 - 12): 13";
      assertEquals(e.getMessage(), msg);
    }
  }

  @Test
  public void invalidDay() throws Exception {
    try {
      command = "event \"Checkup\" from 2025-02-33T10:00 to 2025-02-31T11:00";
      createCommand.execute(command, cal);
    } catch (Exception e) {
      String msg = "Text '2025-02-33 10:00' could not be parsed: Invalid value for DayOfMonth (valid values 1 - 28/31): 33";
      assertEquals(e.getMessage(), msg);
    }
  }

  @Test
  public void nonNumericCharactersInDate() throws Exception {
    try {
      command = "event \"Training\" from 2025-0A-10T09:00 to 2025-0A-10T10:00";
      createCommand.execute(command, cal);
    } catch (Exception e) {
      String msg = "error :Does not match expected format.";
      assertEquals(e.getMessage(), msg);
    }
  }

  @Test
  public void eventNameError() throws Exception {
    try {
      command = "event from 2025-09-20T12:00 to 2025-09-20T13:00";
      createCommand.execute(command, cal);
    } catch (Exception e) {
      String msg = "error :Does not match expected format.";
      assertEquals(e.getMessage(), msg);
    }
  }

  @Test
  public void lowerCaseT() throws Exception {
    try {
      command = "event from 2025-09-20T12:00 to 2025-09-20T13:00";
      createCommand.execute(command, cal);
    } catch (Exception e) {
      String msg = "error :Does not match expected format.";
      assertEquals(e.getMessage(), msg);
    }
  }

  @Test
  public void weekdayError() throws Exception {
    try {
      command = "event \"Gym\" from 2025-06-01T07:00 to 2025-06-01T08:00 repeats Monday for 5 times";
      createCommand.execute(command, cal);
    } catch (Exception e) {
      String msg = "error :Does not match expected format.";
      assertEquals(e.getMessage(), msg);
    }
  }

  @Test
  public void incorrectUntilDate() throws Exception {
    try {
      command = "event \"Dance Class\" on 2025-07-15 repeats TWR until 15-10-2025";
      createCommand.execute(command, cal);
    } catch (Exception e) {
      String msg = "error :'until' must be followed by a valid date.";
      assertEquals(e.getMessage(), msg);
    }
  }

  @Test
  public void nonNumericValuesInDate() throws Exception {
    try {
      command = "event \"Meeting\" from 2025-0X-15T14:00 to 2025-0X-15T15:00";
      createCommand.execute(command, cal);
    } catch (Exception e) {
      String msg = "error :Does not match expected format.";
      assertEquals(e.getMessage(), msg);
    }
  }


  // CREATE EVENTS TESTING

  @Test
  public void createAsimpleEvent() throws Exception {
    ICalendar calendar = new Calendar();
    command = "event \"Hello\" from 2025-09-20T12:00 to 2025-09-20T13:00";
    createCommand.execute(command, calendar);
    calendar.printEvents();

    String onDate = DateUtils.changeDateToDateTime("2025-09-20");
    LocalDateTime newOnDate = DateUtils.stringToLocalDateTime(onDate);

    Map<String, Object> metaData = new HashMap<>();
    metaData.put("localStartTime", newOnDate);

    List<Map<String, Object>> events = calendar.getMatchingEvents(metaData);

    assertEquals(events.size(), 1);
    Map<String, Object> firstEvent = events.get(0);
    assertEquals((String) firstEvent.get("subject"), "Hello");
    assertEquals((LocalDateTime) firstEvent.get("startDate"),
            pareStringToLocalDateTime("2025-09-20T12:00"));
    assertEquals((LocalDateTime) firstEvent.get("endDate"),
            pareStringToLocalDateTime("2025-09-20T13:00"));

  }


  @Test
  public void simpleEventWithAutoDecline() throws Exception {
    ICalendar calendar = new Calendar();
    command = "event --autoDecline \"Hello\" from 2025-09-20T12:00 to 2025-09-20T13:00";
    createCommand.execute(command, calendar);
    calendar.printEvents();

    String onDate = DateUtils.changeDateToDateTime("2025-09-20");
    LocalDateTime newOnDate = DateUtils.stringToLocalDateTime(onDate);

    Map<String, Object> metaData = new HashMap<>();
    metaData.put("localStartTime", newOnDate);

    List<Map<String, Object>> events = calendar.getMatchingEvents(metaData);

    assertEquals(events.size(), 1);
    Map<String, Object> firstEvent = events.get(0);
    assertEquals((String) firstEvent.get("subject"), "Hello");
    assertEquals((LocalDateTime) firstEvent.get("startDate"),
            pareStringToLocalDateTime("2025-09-20T12:00"));
    assertEquals((LocalDateTime) firstEvent.get("endDate"),
            pareStringToLocalDateTime("2025-09-20T13:00"));

  }

  @Test
  public void autoDeclineSimpleEvent() throws Exception {
    ICalendar calendar = new Calendar();
    command = "event  \"Hello\" from 2025-09-20T12:00 to 2025-09-20T13:00";
    createCommand.execute(command, calendar);
    calendar.printEvents();

    String onDate = DateUtils.changeDateToDateTime("2025-09-20");
    LocalDateTime newOnDate = DateUtils.stringToLocalDateTime(onDate);

    Map<String, Object> metaData = new HashMap<>();
    metaData.put("localStartTime", newOnDate);

    List<Map<String, Object>> events = calendar.getMatchingEvents(metaData);

    assertEquals(events.size(), 1);
    Map<String, Object> firstEvent = events.get(0);
    assertEquals((String) firstEvent.get("subject"), "Hello");
    assertEquals((LocalDateTime) firstEvent.get("startDate"),
            pareStringToLocalDateTime("2025-09-20T12:00"));
    assertEquals((LocalDateTime) firstEvent.get("endDate"),
            pareStringToLocalDateTime("2025-09-20T13:00"));

    command = "event --autoDecline \"Hello 123\" from 2025-09-20T12:00 to 2025-09-20T13:00";

    try {
      createCommand.execute(command, calendar);
    } catch (Exception e) {
      assertEquals(e.getMessage(), "the event conflicts with another event");

      List<Map<String, Object>> events1 = calendar.getMatchingEvents(metaData);
      assertEquals(events1.size(), 1);
    }
  }


  @Test
  public void multiDaySimpleEvent() throws Exception {
    ICalendar calendar = new Calendar();
    command = "event  \"multi Day@123 Event\" from 2025-09-20T12:00 to 2025-09-25T13:00";
    createCommand.execute(command, calendar);
    calendar.printEvents();

    String onDate = DateUtils.changeDateToDateTime("2025-09-20");
    LocalDateTime newOnDate = DateUtils.stringToLocalDateTime(onDate);

    Map<String, Object> metaData = new HashMap<>();
    metaData.put("localStartTime", newOnDate);

    List<Map<String, Object>> events = calendar.getMatchingEvents(metaData);

    assertEquals(events.size(), 1);
    Map<String, Object> firstEvent = events.get(0);
    assertEquals((String) firstEvent.get("subject"), "multi Day@123 Event");
    assertEquals((LocalDateTime) firstEvent.get("startDate"),
            pareStringToLocalDateTime("2025-09-20T12:00"));
    assertEquals((LocalDateTime) firstEvent.get("endDate"),
            pareStringToLocalDateTime("2025-09-25T13:00"));
  }

  @Test
  public void TwomultiDaySimpleEventsOverlap() throws Exception {
    ICalendar calendar = new Calendar();
    command = "event  \"Event 1\" from 2025-09-20T12:00 to 2025-09-25T13:00";
    createCommand.execute(command, calendar);

    command = "event  \"Event 2\" from 2025-09-20T12:45 to 2025-09-25T13:45";
    createCommand.execute(command, calendar);

    command = "event  \"Event 3\" from 2025-09-19T12:45 to 2025-09-23T13:45";
    createCommand.execute(command, calendar);

    String onDate = DateUtils.changeDateToDateTime("2025-09-20");
    LocalDateTime newOnDate = DateUtils.stringToLocalDateTime(onDate);

    Map<String, Object> metaData = new HashMap<>();
    metaData.put("localStartTime", newOnDate);

    calendar.printEvents();
    List<Map<String, Object>> events = calendar.getMatchingEvents(metaData);

    assertEquals(events.size(), 3);
    Map<String, Object> firstEvent = events.get(0);
    assertEquals((String) firstEvent.get("subject"), "Event 1");
    assertEquals((LocalDateTime) firstEvent.get("startDate"),
            pareStringToLocalDateTime("2025-09-20T12:00"));
    assertEquals((LocalDateTime) firstEvent.get("endDate"),
            pareStringToLocalDateTime("2025-09-25T13:00"));

    Map<String, Object> secondEvent = events.get(1);
    assertEquals((String) secondEvent.get("subject"), "Event 2");
    assertEquals((LocalDateTime) secondEvent.get("startDate"),
            pareStringToLocalDateTime("2025-09-20T12:45"));
    assertEquals((LocalDateTime) secondEvent.get("endDate"),
            pareStringToLocalDateTime("2025-09-25T13:45"));


    Map<String, Object> thirdEvent = events.get(2);
    assertEquals((String) thirdEvent.get("subject"), "Event 3");
    assertEquals((LocalDateTime) thirdEvent.get("startDate"),
            pareStringToLocalDateTime("2025-09-19T12:45"));
    assertEquals((LocalDateTime) thirdEvent.get("endDate"),
            pareStringToLocalDateTime("2025-09-23T13:45"));
  }

  @Test
  public void noEventOnDate() throws Exception {
    ICalendar calendar = new Calendar();
    command = "event  \"multi Day@123 Event\" from 2025-09-20T12:00 to 2025-09-25T13:00";
    createCommand.execute(command, calendar);
    calendar.printEvents();

    String onDate = DateUtils.changeDateToDateTime("2025-09-26");
    LocalDateTime newOnDate = DateUtils.stringToLocalDateTime(onDate);

    Map<String, Object> metaData = new HashMap<>();
    metaData.put("localStartTime", newOnDate);

    List<Map<String, Object>> events = calendar.getMatchingEvents(metaData);

    assertEquals(events.size(), 0);
  }

  @Test
  public void allDayEvent() throws Exception {
    ICalendar calendar = new Calendar();
    command = "event \"All Day Event\" on 2025-09-20T12:00";
    createCommand.execute(command, calendar);
    calendar.printEvents();

    String onDate = DateUtils.changeDateToDateTime("2025-09-20");
    LocalDateTime newOnDate = DateUtils.stringToLocalDateTime(onDate);

    Map<String, Object> metaData = new HashMap<>();
    metaData.put("localStartTime", newOnDate);

    List<Map<String, Object>> events = calendar.getMatchingEvents(metaData);

    assertEquals(events.size(), 1);
    Map<String, Object> firstEvent = events.get(0);
    assertEquals((String) firstEvent.get("subject"), "All Day Event");
    assertEquals((LocalDateTime) firstEvent.get("startDate"),
            pareStringToLocalDateTime("2025-09-20T00:00"));
    assertEquals((LocalDateTime) firstEvent.get("endDate"),
            pareStringToLocalDateTime("2025-09-20T23:59"));
  }


  @Test
  public void simpleRecEvent() throws Exception {
    ICalendar calendar = new Calendar();
    command = "event \"Recurring Event\" from 2025-03-11T01:00 to 2025-03-11T02:00 " +
            "repeats TW for 2 times";
    createCommand.execute(command, calendar);
    calendar.printEvents();

    String onDate = DateUtils.changeDateToDateTime("2025-03-11");
    LocalDateTime newOnDate = DateUtils.stringToLocalDateTime(onDate);

    Map<String, Object> metaData = new HashMap<>();
    metaData.put("localStartTime", newOnDate);

    List<Map<String, Object>> events = calendar.getMatchingEvents(metaData);

    assertEquals(events.size(), 1);
    Map<String, Object> firstEvent = events.get(0);
    assertEquals((String) firstEvent.get("subject"), "Recurring Event");
    assertEquals((LocalDateTime) firstEvent.get("startDate"),
            pareStringToLocalDateTime("2025-03-11T01:00"));
    assertEquals((LocalDateTime) firstEvent.get("endDate"),
            pareStringToLocalDateTime("2025-03-11T02:00"));
  }

  @Test
  public void recEventMultipleMonths() throws Exception {
    ICalendar calendar = new Calendar();
    command = "event \"Recurring Event\" from 2025-03-11T01:00 to 2025-03-11T02:00 " +
            "repeats TW for 2 times";
    createCommand.execute(command, calendar);
    calendar.printEvents();

    String onDate = DateUtils.changeDateToDateTime("2025-03-11");
    LocalDateTime newOnDate = DateUtils.stringToLocalDateTime(onDate);

    Map<String, Object> metaData = new HashMap<>();
    metaData.put("localStartTime", newOnDate);

    List<Map<String, Object>> events = calendar.getMatchingEvents(metaData);

    //_______


    PrintStream originalOut = System.out;
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    PrintStream customOut = new PrintStream(outputStream);
    System.setOut(customOut);
    customOut.flush();
    int captureStartIndex = outputStream.size();


    cal.printEvents();

    System.setOut(originalOut);
    String capturedOutput = outputStream.toString();
    String filteredOutput = capturedOutput.substring(captureStartIndex);

    // Print the captured output
    System.out.println("Captured Output After Specific Line:");
    System.out.println(filteredOutput);

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

  @Test
  public void consecutiveRecurringEvents() throws Exception {
    ICalendar calendar = new Calendar();
    command = "event \"Recurring Event\" from 2025-03-11T01:00 to 2025-03-11T02:00 " +
            "repeats TW for 2 times";
    createCommand.execute(command, calendar);

    calendar.printEvents();

    // check whether the first event is added properly
    String onDate = DateUtils.changeDateToDateTime("2025-03-11");
    LocalDateTime newOnDate = DateUtils.stringToLocalDateTime(onDate);
    Map<String, Object> metaData = new HashMap<>();
    metaData.put("localStartTime", newOnDate);
    List<Map<String, Object>> events = calendar.getMatchingEvents(metaData);
    String actualOutput = getViewCalendarOutput(events);
    String expectedEvent = "• Subject : Recurring Event,Start date : 2025-03-11,Start time : 01:00," +
            "End date : 2025-03-11,End time : 02:00\n";
    assertEquals(expectedEvent, actualOutput);


    // check whether the first event is added properly
    onDate = DateUtils.changeDateToDateTime("2025-03-12");
    newOnDate = DateUtils.stringToLocalDateTime(onDate);
    metaData.put("localStartTime", newOnDate);
    events = calendar.getMatchingEvents(metaData);
    actualOutput = getViewCalendarOutput(events);
    expectedEvent = "• Subject : Recurring Event,Start date : 2025-03-12,Start time : 01:00," +
            "End date : 2025-03-12,End time : 02:00\n";
    assertEquals(expectedEvent, actualOutput);

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

  @Test
  public void recurringDayForTheWholeWeek() throws Exception {
    ICalendar calendar = new Calendar();
    command = "event \"Recurring Event\" from 2025-03-12T01:00 to 2025-03-12T02:00 " +
            "repeats WRFSUMT for 7 times";
    createCommand.execute(command, calendar);

    // check whether the first event is added properly
    String actualOutput = getEventStringOnADate("2025-03-12", calendar);
    String expectedEvent = "• Subject : Recurring Event,Start date : 2025-03-12,Start time : 01:00," +
            "End date : 2025-03-12,End time : 02:00\n";
    assertEquals(expectedEvent, actualOutput);
    assertEquals(calendar.isUserBusy(pareStringToLocalDateTime("2025-03-12T01:05")), true);

    // check whether the second event is added properly
    actualOutput = getEventStringOnADate("2025-03-13", calendar);
    expectedEvent = "• Subject : Recurring Event,Start date : 2025-03-13,Start time : 01:00," +
            "End date : 2025-03-13,End time : 02:00\n";
    assertEquals(expectedEvent, actualOutput);
    assertEquals(calendar.isUserBusy(pareStringToLocalDateTime("2025-03-13T01:00")), true);


    // check whether the third event is added properly
    actualOutput = getEventStringOnADate("2025-03-14", calendar);
    expectedEvent = "• Subject : Recurring Event,Start date : 2025-03-14,Start time : 01:00," +
            "End date : 2025-03-14,End time : 02:00\n";
    assertEquals(expectedEvent, actualOutput);
    assertEquals(calendar.isUserBusy(pareStringToLocalDateTime("2025-03-14T01:30")), true);


    // check whether the fourth event is added properly
    actualOutput = getEventStringOnADate("2025-03-15", calendar);
    expectedEvent = "• Subject : Recurring Event,Start date : 2025-03-15,Start time : 01:00," +
            "End date : 2025-03-15,End time : 02:00\n";
    assertEquals(expectedEvent, actualOutput);
    assertEquals(calendar.isUserBusy(pareStringToLocalDateTime("2025-03-15T02:05")), false);


    // check whether the fifth event is added properly
    actualOutput = getEventStringOnADate("2025-03-16", calendar);
    expectedEvent = "• Subject : Recurring Event,Start date : 2025-03-16,Start time : 01:00," +
            "End date : 2025-03-16,End time : 02:00\n";
    assertEquals(expectedEvent, actualOutput);
    assertEquals(calendar.isUserBusy(pareStringToLocalDateTime("2025-03-16T02:00")), true);


    // check whether the sixth event is added properly
    actualOutput = getEventStringOnADate("2025-03-17", calendar);
    expectedEvent = "• Subject : Recurring Event,Start date : 2025-03-17,Start time : 01:00," +
            "End date : 2025-03-17,End time : 02:00\n";
    assertEquals(expectedEvent, actualOutput);
    assertEquals(calendar.isUserBusy(pareStringToLocalDateTime("2025-03-17T01:00")), true);


    // check whether the seventh event is added properly
    actualOutput = getEventStringOnADate("2025-03-18", calendar);
    expectedEvent = "• Subject : Recurring Event,Start date : 2025-03-18,Start time : 01:00," +
            "End date : 2025-03-18,End time : 02:00\n";
    assertEquals(expectedEvent, actualOutput);
    assertEquals(calendar.isUserBusy(pareStringToLocalDateTime("2025-03-16T02:00")), true);

    assertEquals(calendar.getAllCalendarEvents().size(), 7);

  }

  @Test
  public void recEventWithInvalidDay() throws Exception {
    try {
      ICalendar calendar = new Calendar();
      command = "event \"Recurring Event\" from 2025-03-12T01:00 to 2025-03-12T02:00 " +
              "repeats WRFSUMT for -1 times";
      createCommand.execute(command, calendar);
    } catch (Exception e) {
      assertEquals(e.getMessage(), "error :'for' must be followed by a valid number of times.");
    }
  }

  @Test
  public void recEventWithInvalidWeek() throws Exception {
    try {
      ICalendar calendar = new Calendar();
      command = "event \"Recurring Event\" from 2025-03-12T01:00 to 2025-03-12T02:00 " +
              "repeats M?T for 2 times";
      createCommand.execute(command, calendar);
    } catch (Exception e) {
      assertEquals(e.getMessage(), "error :Does not match expected format.");
    }
  }

  @Test
  public void recurringEventWithAutoDecline() throws Exception {

    // Recurring Event with autoDecline and no single event in calendar and no conflict.
    ICalendar calendar = new Calendar();
    command = "event --autoDecline \"Recurring Event\" from 2025-03-12T01:00 to 2025-03-12T02:00 " +
            "repeats WR for 7 times";
    createCommand.execute(command, calendar);

    // check whether the first event is added properly
    String actualOutput = getEventStringOnADate("2025-03-12", calendar);
    String expectedEvent = "• Subject : Recurring Event,Start date : 2025-03-12,Start time : 01:00," +
            "End date : 2025-03-12,End time : 02:00\n";
    assertEquals(expectedEvent, actualOutput);
    assertEquals(calendar.isUserBusy(pareStringToLocalDateTime("2025-03-12T01:05")), true);

    // check whether the second event is added properly
    actualOutput = getEventStringOnADate("2025-03-13", calendar);
    expectedEvent = "• Subject : Recurring Event,Start date : 2025-03-13,Start time : 01:00," +
            "End date : 2025-03-13,End time : 02:00\n";
    assertEquals(expectedEvent, actualOutput);
    assertEquals(calendar.isUserBusy(pareStringToLocalDateTime("2025-03-13T01:00")), true);

  }

  @Test
  public void recEventCreatingUsingUntilTime() throws Exception {
    // Recurring Event creation with future until time
    ICalendar calendar = new Calendar();
    command = "event \"Recurring Test\" from 2025-03-12T01:00 to 2025-03-12T02:00 " +
            "repeats R until 2025-03-22T06:00";
    createCommand.execute(command, calendar);

    // check whether the first event is added properly
    String actualOutput = getEventStringOnADate("2025-03-13", calendar);
    String expectedEvent = "• Subject : Recurring Test,Start date : 2025-03-13,Start time : 01:00," +
            "End date : 2025-03-13,End time : 02:00\n";
    assertEquals(expectedEvent, actualOutput);
    assertEquals(calendar.isUserBusy(pareStringToLocalDateTime("2025-03-13T01:05")), true);

    // check whether the second event is added properly
    actualOutput = getEventStringOnADate("2025-03-20", calendar);
    expectedEvent = "• Subject : Recurring Test,Start date : 2025-03-20,Start time : 01:00," +
            "End date : 2025-03-20,End time : 02:00\n";
    assertEquals(expectedEvent, actualOutput);
    assertEquals(calendar.isUserBusy(pareStringToLocalDateTime("2025-03-20T01:00")), true);

    assertEquals(calendar.getAllCalendarEvents().size(), 2);

  }

  @Test
  public void recEventStartsAndEndsOnUntilTime() throws Exception {
    ICalendar calendar = new Calendar();
    command = "event \"Recurring Test\" from 2025-03-13T01:00 to 2025-03-13T02:00 " +
            "repeats R until 2025-03-13T06:00";
    createCommand.execute(command, calendar);

    // check whether the first event is added properly
    String actualOutput = getEventStringOnADate("2025-03-13", calendar);
    String expectedEvent = "• Subject : Recurring Test,Start date : 2025-03-13,Start time : 01:00," +
            "End date : 2025-03-13,End time : 02:00\n";
    assertEquals(expectedEvent, actualOutput);
    assertEquals(calendar.isUserBusy(pareStringToLocalDateTime("2025-03-13T01:05")), true);
  }

  @Test
  public void noMatchingWeekDayInUntilTime() throws Exception {
    ICalendar calendar = new Calendar();
    command = "event \"Recurring Test\" from 2025-03-12T01:00 to 2025-03-12T02:00 " +
            "repeats T until 2025-03-17T06:00";
    createCommand.execute(command, calendar);
    // The next tuesday is on 2025-03-18 , so it will never be added.
    assertEquals(calendar.isUserBusy(pareStringToLocalDateTime("2025-03-12T01:05")), false);
    assertEquals(calendar.isUserBusy(pareStringToLocalDateTime("2025-03-13T01:05")), false);
    assertEquals(calendar.isUserBusy(pareStringToLocalDateTime("2025-03-14T01:05")), false);
    assertEquals(calendar.isUserBusy(pareStringToLocalDateTime("2025-03-15T01:05")), false);
    assertEquals(calendar.isUserBusy(pareStringToLocalDateTime("2025-03-16T01:05")), false);
    assertEquals(calendar.isUserBusy(pareStringToLocalDateTime("2025-03-17T01:05")), false);
    assertEquals(calendar.getAllCalendarEvents().size(), 0);

  }

  @Test
  public void createAsingleDayAllDay() throws Exception {
    ICalendar calendar = new Calendar();
    command = "event \"Single Event\" on 2025-05-12T01:00";
    createCommand.execute(command, calendar);
    assertEquals(calendar.isUserBusy(pareStringToLocalDateTime("2025-05-12T01:05")), true);
    String actualOutput = getEventStringOnADate("2025-05-12", calendar);
    String expectedEvent = "• Subject : Single Event,Start date : 2025-05-12,Start time : 00:00," +
            "End date : 2025-05-12,End time : 23:59\n";
    assertEquals(expectedEvent, actualOutput);
    assertEquals(calendar.getAllCalendarEvents().size(), 1);

  }

  @Test
  public void createMultipleAllDayRecurringEvents() throws Exception {
    ICalendar calendar = new Calendar();
    command = "event \"All Day\" on 2025-03-17 repeats MT for 2 times";
    createCommand.execute(command, calendar);
    assertEquals(calendar.isUserBusy(pareStringToLocalDateTime("2025-03-17T01:05")), true);
    assertEquals(calendar.isUserBusy(pareStringToLocalDateTime("2025-03-18T01:05")), true);

    String actualOutput = getEventStringOnADate("2025-03-17", calendar);
    String expectedEvent = "• Subject : All Day,Start date : 2025-03-17,Start time : 00:00," +
            "End date : 2025-03-17,End time : 23:59\n";
    assertEquals(expectedEvent, actualOutput);

    actualOutput = getEventStringOnADate("2025-03-18", calendar);
    expectedEvent = "• Subject : All Day,Start date : 2025-03-18,Start time : 00:00," +
            "End date : 2025-03-18,End time : 23:59\n";
    assertEquals(expectedEvent, actualOutput);

    assertEquals(calendar.getAllCalendarEvents().size(), 2);
  }


  @Test
  public void createMultipleAllDayUntilTime() throws Exception {
    ICalendar calendar = new Calendar();
    command = "event \"All Day\" on 2025-03-17 repeats MT until  2025-03-19";
    createCommand.execute(command, calendar);
    assertEquals(calendar.isUserBusy(pareStringToLocalDateTime("2025-03-17T01:05")), true);
    assertEquals(calendar.isUserBusy(pareStringToLocalDateTime("2025-03-18T01:05")), true);

    String actualOutput = getEventStringOnADate("2025-03-17", calendar);
    String expectedEvent = "• Subject : All Day,Start date : 2025-03-17,Start time : 00:00," +
            "End date : 2025-03-17,End time : 23:59\n";
    assertEquals(expectedEvent, actualOutput);

    actualOutput = getEventStringOnADate("2025-03-18", calendar);
    expectedEvent = "• Subject : All Day,Start date : 2025-03-18,Start time : 00:00," +
            "End date : 2025-03-18,End time : 23:59\n";
    assertEquals(expectedEvent, actualOutput);

    assertEquals(calendar.getAllCalendarEvents().size(), 2);
  }


  @Test
  public void singleEventConflictWithSingleEvent() throws Exception {
    // a single event conflicts with another single event
    ICalendar calendar = new Calendar();
    command = "event \"SingleEvent\" from 2025-03-01T09:00 to 2025-03-01T10:00";
    createCommand.execute(command, calendar);
    assertEquals(calendar.getAllCalendarEvents().size(), 1);

    String actualOutput = getEventStringOnADate("2025-03-01", calendar);
    String expectedEvent = "• Subject : SingleEvent,Start date : 2025-03-01,Start time : 09:00," +
            "End date : 2025-03-01,End time : 10:00\n";
    assertEquals(expectedEvent, actualOutput);

    // The below event will not be added as it conflicts with the above added event
    command = "event --autoDecline \"SingleEvent2\" from 2025-03-01T09:05 to 2025-03-01T10:00";
    try {
      createCommand.execute(command, calendar);
    } catch (Exception e) {
      assertEquals("the event conflicts with another event", e.getMessage());
    }
    assertEquals(calendar.getAllCalendarEvents().size(), 1);

    // the same event can be added when autoDecline is not present
    command = "event \"SingleEvent2\" from 2025-03-01T09:05 to 2025-03-01T10:00";
    createCommand.execute(command, calendar);
    actualOutput = getEventStringOnADate("2025-03-01", calendar);
     expectedEvent = expectedEvent +"• Subject : SingleEvent2,Start date : 2025-03-01,Start time : 09:05," +
            "End date : 2025-03-01,End time : 10:00\n";
    assertEquals(expectedEvent, actualOutput);
    assertEquals(calendar.getAllCalendarEvents().size(), 2);

  }

  @Test
  public void recurringEventAutoDecline() throws Exception {
    // Recurring event creation is declined because of conflict with single event
    ICalendar calendar = new Calendar();
    command = "event \"SingleEvent\" from 2025-03-01T09:00 to 2025-03-01T10:00";
    createCommand.execute(command, calendar);
    assertEquals(calendar.getAllCalendarEvents().size(), 1);

    String actualOutput = getEventStringOnADate("2025-03-01", calendar);
    String expectedEvent = "• Subject : SingleEvent,Start date : 2025-03-01,Start time : 09:00," +
            "End date : 2025-03-01,End time : 10:00\n";
    assertEquals(expectedEvent, actualOutput);

    command = "event --autoDecline \"Recurring Event\" from 2025-03-01T09:00 to 2025-03-01T10:00 " +
            "repeats SU for 7 times";
    try{
      createCommand.execute(command, calendar);
    }catch (Exception e){
      assertEquals(e.getMessage(),"the event conflicts with another event");
    }

    // check calendar size again to ensure no new events are added
    assertEquals(calendar.getAllCalendarEvents().size(), 1);
  }


  @Test
  public void singleEventAutoDeclineWithRecEvent() throws Exception {
    // Recurring event creation is declined because of conflict with single event
    ICalendar calendar = new Calendar();
    command = "event --autoDecline \"Recurring Event\" from 2025-03-01T09:00 to 2025-03-01T10:00 " +
            "repeats SU for 7 times";

    createCommand.execute(command, calendar);
    assertEquals(calendar.getAllCalendarEvents().size(), 7);

    String actualOutput = getEventStringOnADate("2025-03-01", calendar);
    String expectedEvent = "• Subject : Recurring Event,Start date : 2025-03-01,Start time : 09:00," +
            "End date : 2025-03-01,End time : 10:00\n";
    assertEquals(expectedEvent, actualOutput);

    command = "event --autoDecline \"SingleEvent\" from 2025-03-01T09:00 to 2025-03-01T10:00";
    try{
      createCommand.execute(command, calendar);
    }catch (Exception e){
      assertEquals(e.getMessage(),"the event conflicts with another event");
    }

    // check calendar size again to ensure the single event is not adde
    assertEquals(calendar.getAllCalendarEvents().size(), 7);
  }

  @Test
  public void untilDayisBeforeStartDay() throws Exception {
    ICalendar calendar = new Calendar();
    command = "event \"All Day\" on 2025-03-17 repeats MT until  2025-03-16";
    createCommand.execute(command, calendar);
    calendar.printEvents();
    assertEquals(calendar.isUserBusy(pareStringToLocalDateTime("2025-03-17T01:05")), false);
    assertEquals(calendar.getAllCalendarEvents().size(), 0);
  }


}