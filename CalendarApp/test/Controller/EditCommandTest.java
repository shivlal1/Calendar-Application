package Controller;

import org.junit.Before;
import org.junit.Test;

import Model.Calendar;
import Model.ICalendar;

import static org.junit.Assert.assertEquals;

public class EditCommandTest {

  ICommand editCommand;
  ICalendar cal = new Calendar();
  String command;

  @Before
  public void init() {
    editCommand = new EditCommand();
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
      assertEquals(e.getMessage(), "Should start with 'edit event(s)' or it is Missing 'edit event(s)'");
    }
  }

  @Test
  public void missingNameOrProperty() throws Exception {
    try {
      command = "event \"Meeting\" from 2025-03-01T09:00 to 2025-03-01T10:00 with \"Weekly Meeting\"";
      editCommand.execute(command, cal);
    } catch (Exception e) {
      assertEquals(e.getMessage(), "Invalid Command Missing 'eventName' or Property is missing");
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

}