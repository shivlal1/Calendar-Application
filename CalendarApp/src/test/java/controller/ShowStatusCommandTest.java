package controller;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import model.Calendar;
import model.ICalendar;
import view.ConsoleView;
import view.View;

import static org.junit.Assert.assertEquals;
import static utils.DateUtils.pareStringToLocalDateTime;

/**
 * Test class to check the functionality of show status.
 */
public class ShowStatusCommandTest {
  ICommand showStatusCommand;
  ICommand createCommand;
  ICalendar cal = new Calendar();
  String command;
  View view;

  @Before
  public void init() {
    showStatusCommand = new ShowStatusCommand();
    createCommand = new CreateCommand();
    view = new ConsoleView();
  }

  @Rule
  public ExpectedException thrown = ExpectedException.none();


  @Test(expected = Exception.class)
  public void testInvalidStatusCommandFormat() throws Exception {
    command = "status at 2025-03-12 15:30";
    showStatusCommand.execute(command, cal);
  }

  @Test
  public void testInvalidStatusMissingDate() throws Exception {
    thrown.expect(Exception.class);
    thrown.expectMessage("Invalid Command Missing Date/invalid");
    command = "status on ";
    showStatusCommand.execute(command, cal);
  }

  @Test(expected = Exception.class)
  public void testDoesntStartWithStatus() throws Exception {
    command = "on 2025-03-12 15:30";
    showStatusCommand.execute(command, cal);
  }

  @Test(expected = Exception.class)
  public void testMismatchKeywords() throws Exception {
    command = "status 2025-03-12 15:30 at";
    showStatusCommand.execute(command, cal);
  }

  @Test(expected = Exception.class)
  public void testInvalidDateFormatCommand() throws Exception {
    command = "status on 2025-03-12 15:30";
    showStatusCommand.execute(command, cal);
  }

  @Test(expected = Exception.class)
  public void testExtraWordsAfterCommand() throws Exception {
    command = "status on 2025-03-12T15:30 hello there";
    showStatusCommand.execute(command, cal);
  }

  @Test(expected = Exception.class)
  public void testExtraWordsBeforeCommand() throws Exception {
    command = "hello there status on 2025-03-12T15:30";
    showStatusCommand.execute(command, cal);
  }

  @Test(expected = Exception.class)
  public void testInvalidDateFormat() throws Exception {
    command = "status on 12-03-2025T15:30";
    showStatusCommand.execute(command, cal);
  }

  @Test(expected = Exception.class)
  public void testInvalidTimeFormat() throws Exception {
    command = "status on 2025-03-12T15:30:09";
    showStatusCommand.execute(command, cal);
  }

  /**
   * Function to caputre the print statement from console.
   *
   * @param status boolean value of the status.
   * @return string value of the captured boolean.
   */
  public String captureStatusOutput(boolean status) {
    PrintStream originalOut = System.out;
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    PrintStream customOut = new PrintStream(outputStream);
    System.setOut(customOut);
    customOut.flush();
    int captureStartIndex = outputStream.size();
    view.viewStatus(status);
    System.setOut(originalOut);
    String capturedOutput = outputStream.toString();
    String filteredOutput = capturedOutput.substring(captureStartIndex);
    return filteredOutput;
  }

  @Test
  public void singleEventBusy() throws Exception {
    ICalendar calendar = new Calendar();
    command = "event \"Single Event\" on 2025-05-12T01:00";
    createCommand.execute(command, calendar);

    boolean expectedStatus = calendar.isUserBusy(pareStringToLocalDateTime("2025-05-12T01:05"));
    String expectedMessge = captureStatusOutput(expectedStatus);
    assertEquals(expectedMessge, "status : busy\n");
  }

  @Test
  public void singleEventSingleDayBusy() throws Exception {
    ICalendar calendar = new Calendar();
    command = "event \"SingleEvent\" from 2025-03-01T09:00 to 2025-03-01T10:00";
    createCommand.execute(command, calendar);

    boolean expectedStatus = calendar.isUserBusy(pareStringToLocalDateTime("2025-03-01T09:05"));
    String expectedMessge = captureStatusOutput(expectedStatus);
    assertEquals(expectedMessge, "status : busy\n");
  }

  @Test
  public void multiEventSingleDayBusy() throws Exception {
    ICalendar calendar = new Calendar();
    command = "event \"SingleEvent\" from 2025-03-01T09:00 to 2025-03-05T10:00";
    createCommand.execute(command, calendar);

    boolean expectedStatus = calendar.isUserBusy(pareStringToLocalDateTime("2025-03-04T09:05"));
    String expectedMessge = captureStatusOutput(expectedStatus);
    assertEquals(expectedMessge, "status : busy\n");
  }

  @Test
  public void recEventBusy() throws Exception {
    ICalendar calendar = new Calendar();
    command = "event --autoDecline \"Recurring Event\" from 2025-03-01T09:00 to 2025-03-01T10:00 " +
            "repeats SU for 7 times";
    createCommand.execute(command, calendar);

    boolean expectedStatus = calendar.isUserBusy(pareStringToLocalDateTime("2025-03-01T09:05"));
    String expectedMessge = captureStatusOutput(expectedStatus);
    assertEquals(expectedMessge, "status : busy\n");
  }

  @Test
  public void recEventNotBusy() throws Exception {
    ICalendar calendar = new Calendar();
    command = "event --autoDecline \"Recurring Event\" from 2025-03-01T09:00 to 2025-03-01T10:00 " +
            "repeats SU for 7 times";
    createCommand.execute(command, calendar);

    boolean expectedStatus = calendar.isUserBusy(pareStringToLocalDateTime("2025-03-01T10:05"));
    String expectedMessge = captureStatusOutput(expectedStatus);
    assertEquals(expectedMessge, "status : available\n");
  }

  @Test
  public void singleEventAvailable() throws Exception {
    ICalendar calendar = new Calendar();
    command = "event \"SingleEvent\" from 2025-03-01T09:00 to 2025-03-01T10:00";
    createCommand.execute(command, calendar);

    boolean expectedStatus = calendar.isUserBusy(pareStringToLocalDateTime("2025-03-01T12:05"));
    String expectedMessge = captureStatusOutput(expectedStatus);
    assertEquals(expectedMessge, "status : available\n");
  }

  @Test
  public void noEventOnCalendar() throws Exception {
    ICalendar calendar = new Calendar();

    boolean expectedStatus = calendar.isUserBusy(pareStringToLocalDateTime("2025-03-01T12:05"));
    String expectedMessge = captureStatusOutput(expectedStatus);
    assertEquals(expectedMessge, "status : available\n");
  }

  @Test
  public void statusMiss() throws Exception {
    thrown.expect(Exception.class);
    thrown.expectMessage("Invalid Command Status Missing");
    command = "on";
    showStatusCommand.execute(command, cal);
  }

  @Test
  public void onMissing() throws Exception {
    thrown.expect(Exception.class);
    thrown.expectMessage("Invalid Command Missing or misplaced On");
    command = "status 2025-02-01T:05:00";
    showStatusCommand.execute(command, cal);
  }

  @Test
  public void statustest() throws Exception {
    ICalendar calendar = new Calendar();
    command = "event --autoDecline \"Recurring Event\" from 2025-03-01T09:00 to 2025-03-01T10:00 " +
            "repeats SU for 7 times";
    createCommand.execute(command, calendar);


    command = "status on 2025-03-01T09:00";

    String expectedMessge = captureStatusOutputOfPrintCommand(command, calendar);


    assertEquals(expectedMessge, "status : busy\n");
  }

  public String captureStatusOutputOfPrintCommand(String command, ICalendar cal) throws Exception {

    PrintStream originalOut = System.out;
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    PrintStream customOut = new PrintStream(outputStream);
    System.setOut(customOut);
    customOut.flush();
    int captureStartIndex = outputStream.size();
    showStatusCommand.execute(command, cal);
    System.setOut(originalOut);
    String capturedOutput = outputStream.toString();
    String filteredOutput = capturedOutput.substring(captureStartIndex);
    return filteredOutput;

  }


}