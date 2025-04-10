import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.ZoneId;
import java.util.ArrayList;

import controller.CreateCommand;
import controller.EditCommand;
import controller.ICommand;
import model.CalendarV2;
import model.ICalendar;
import view.ConsoleView;
import view.View;

import static org.junit.Assert.assertEquals;

/**
 * A unit test for the calendar app class.
 */
public class CalendarAppTest {

  private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
  private final PrintStream originalOut = System.out;

  @Before
  public void setUpStreams() {
    System.setOut(new PrintStream(outputStream)); // Capture console output
  }

  @After
  public void restoreStreams() {
    System.setOut(originalOut);
    System.setIn(System.in);
  }

  @Test
  public void basicCalendar() throws Exception {

    String createCal = "create calendar --name myCalendar --timezone Asia/Kolkata";
    String useCal = "use calendar --name myCalendar";
    String editCal = "edit calendar --name myCalendar --property name myCalendar2";
    String createEvent = "create event \"SingleEvent\" from 2025-03-01T09:00 to 2025-03-01T10:00";
    String printEvent = "print events on \"2025-03-01\"";
    String[] inputs = {createCal, useCal, editCal, createEvent, printEvent, "exit"};

    String simulatedInput = String.join("\n", inputs) + "\n";

    simulateUserInput(simulatedInput);
    CalendarApp.main(new String[]{"--mode", "interactive"});

    String output1 = "Using interactive mode. To quit, use 'exit'\n";
    String output2 = "Name Change Success: myCalendar2\n";
    String output3 = "• Subject : SingleEvent,Start date : 2025-03-01," +
            "Start time : 09:00,End date : 2025-03-01,End time : 10:00,isPublic : false\n";

    assertEquals(output1 + output2 + output3, getOutput());
  }


  @Rule
  public ExpectedException thrown = ExpectedException.none();


  @Test
  public void invalidCalCommand() throws Exception {
    String createCal = "calendar --name myCalendar --timezone Asia/Kolkata";
    String[] inputs = {createCal, "exit"};
    String simulatedInput = String.join("\n", inputs) + "\n";
    simulateUserInput(simulatedInput);
    CalendarApp.main(new String[]{"--mode", "interactive"});
    String output = "Using interactive mode. To quit, use 'exit'\n";
    output += "start with commad+calendar+name\n";
    assertEquals(output, getOutput());
  }


  @Test
  public void invalidCalCommand2() throws Exception {

    String createCal = "create --name myCalendar --timezone Asia/Kolkata";
    String[] inputs = {createCal, "exit"};
    String simulatedInput = String.join("\n", inputs) + "\n";
    simulateUserInput(simulatedInput);
    CalendarApp.main(new String[]{"--mode", "interactive"});

    String output1 = "Using interactive mode. To quit, use 'exit'\n";
    String output2 = "start with commad+calendar+name\n";
    assertEquals(output1 + output2, getOutput());
  }


  @Test
  public void invalidCalCommand3() throws Exception {
    //thrown.expect(Exception.class);
    //thrown.expectMessage("Command must start with 'calendar --name'");

    String createCal = "create myCalendar --timezone Asia/Kolkata";
    String[] inputs = {createCal, "exit"};

    String simulatedInput = String.join("\n", inputs) + "\n";

    simulateUserInput(simulatedInput);
    CalendarApp.main(new String[]{"--mode", "interactive"});
    String output1 = "Using interactive mode. To quit, use 'exit'\n";
    String output2 = "No active calendar selected'\n";
    assertEquals(output1 + output2, getOutput());

  }

  @Test
  public void invalidCalCommand4() throws Exception {

    String createCal = "create calendar --name myCalendar  Asia/Kolkata";
    String[] inputs = {createCal, "exit"};
    String simulatedInput = String.join("\n", inputs) + "\n";
    simulateUserInput(simulatedInput);
    CalendarApp.main(new String[]{"--mode", "interactive"});

    String output1 = "Using interactive mode. To quit, use 'exit'\n";
    String output2 = "Missing --timezone flag\n";
    assertEquals(output1 + output2, getOutput());
  }

  @Test
  public void invalidCalCommand5() throws Exception {

    String createCal = "create calendar --name myCalendar --timezone";
    String[] inputs = {createCal, "exit"};

    String simulatedInput = String.join("\n", inputs) + "\n";

    simulateUserInput(simulatedInput);
    CalendarApp.main(new String[]{"--mode", "interactive"});

    String output1 = "Using interactive mode. To quit, use 'exit'\n";
    String output2 = "Missing timeZone value\n";
    assertEquals(output1 + output2, getOutput());
  }


  @Test
  public void invalidCalCommand6() throws Exception {

    String createCal = "create calendar --name myCalendar --timezone Asia/Kolkata";
    String edit = "edit --name myCalendar --property name myCalendar2";
    String[] inputs = {createCal, edit, "exit"};

    String simulatedInput = String.join("\n", inputs) + "\n";

    simulateUserInput(simulatedInput);
    CalendarApp.main(new String[]{"--mode", "interactive"});

    String output1 = "Using interactive mode. To quit, use 'exit'\n";
    String output2 = "start with commad+calendar+name\n";
    assertEquals(output1 + output2, getOutput());
  }

  @Test
  public void invalidCalCommand7() throws Exception {
    String edit = "edit calendar --name myCalendar --property name myCalendar2";
    String[] inputs = {edit, "exit"};
    String simulatedInput = String.join("\n", inputs) + "\n";
    simulateUserInput(simulatedInput);
    CalendarApp.main(new String[]{"--mode", "interactive"});
    String output = "Using interactive mode. To quit, use 'exit'\n";
    output += "No such calendar exists\n";
    assertEquals(output, getOutput());
  }

  @Test
  public void invalidCalCommand8() throws Exception {

    String createCal = "create calendar --name myCalendar --timezone Asia/Kolkata";
    String edit = "edit calendar --name myCalendar --property myCalendar2";
    String[] inputs = {createCal, edit, "exit"};

    String simulatedInput = String.join("\n", inputs) + "\n";

    simulateUserInput(simulatedInput);
    CalendarApp.main(new String[]{"--mode", "interactive"});

    String output = "Using interactive mode. To quit, use 'exit'\n";
    output += "property name/value is not present\n";
    assertEquals(output, getOutput());
  }

  /**
   * Simulates user input by temporarily redirecting the standard input stream.
   *
   * @param input The string representing user input to simulate.
   */
  private void simulateUserInput(String input) {
    System.setIn(new ByteArrayInputStream(input.getBytes()));
    outputStream.reset(); // Clear previous output
  }

  /**
   * This method prints captured output from the redirected output stream.
   *
   * @return the normalized string output captured during command execution.
   */
  private String getOutput() {
    return outputStream.toString().replace("\r", ""); // Normalize newlines
  }

  @Test
  public void basicCalendar2() throws Exception {

    String createCal = "create calendar --name myCalendar --timezone Asia/Kolkata";
    String useCal = "use calendar --name myCalendar";
    String editCal = "edit calendar --name myCalendar --property name myCalendar2";
    String createEvent1 = "create event \"SingleEvent\" from 2025-03-01T09:00 to 2025-03-01T10:00";
    String createEvent2 = "create event \"SingleEvent\" from 2025-03-01T09:00 to 2025-03-01T10:00";
    String edit = "edit events name \"SingleEvent\" from 25-03-01T09:00  with New";
    String[] inputs = {createCal, useCal, editCal, createEvent1, createEvent2, edit, "exit"};

    String simulatedInput = String.join("\n", inputs) + "\n";

    simulateUserInput(simulatedInput);
    CalendarApp.main(new String[]{"--mode", "interactive"});

    String output = "Using interactive mode. To quit, use 'exit'\n";
    output += "Name Change Success: myCalendar2\n";
    output += "the event conflicts with another event\n";
    output += "Invalid Command Missing To\n";
    assertEquals(output, getOutput());
  }

  @Test
  public void invalidEdit() throws Exception {
//    thrown.expect(Exception.class);
//    thrown.expectMessage("--property missing");

    String createCal = "create calendar --name myCalendar --timezone Asia/Kolkata";
    String useCal = "use calendar --name myCalendar";
    String editCal = "edit calendar --name myCalendar name myCalendar2";

    String[] inputs = {createCal, useCal, editCal, "exit"};

    String simulatedInput = String.join("\n", inputs) + "\n";

    simulateUserInput(simulatedInput);
    CalendarApp.main(new String[]{"--mode", "interactive"});

    String output = "Using interactive mode. To quit, use 'exit'\n";
    output += "--property missing\n";
    assertEquals(output, getOutput());

  }

  @Test
  public void editNotExistingCal() throws Exception {
    String createCal = "create calendar --name myCalendar --timezone Asia/Kolkata";
    String useCal = "use calendar --name myCalendar";
    String editCal = "edit calendar --name NotExistingCal --property timezone Australia/Sydney";

    String[] inputs = {createCal, useCal, editCal, "exit"};
    String simulatedInput = String.join("\n", inputs) + "\n";

    simulateUserInput(simulatedInput);
    CalendarApp.main(new String[]{"--mode", "interactive"});

    String output = "Using interactive mode. To quit, use 'exit'\n";
    output += "No such calendar exists\n";
    assertEquals(output, getOutput());
  }


  @Test
  public void invalidUse() throws Exception {
    //thrown.expect(Exception.class);
    //thrown.expectMessage("calendar Doesn't exists to Use");

    String createCal = "create calendar --name myCalendar --timezone Asia/Kolkata";
    String useCal = "use calendar --name notExistingCal";

    String[] inputs = {createCal, useCal, "exit"};
    String simulatedInput = String.join("\n", inputs) + "\n";

    simulateUserInput(simulatedInput);
    CalendarApp.main(new String[]{"--mode", "interactive"});
    String output = "Using interactive mode. To quit, use 'exit'\n";
    output += "calendar Doesn't exists to Use\n";
    assertEquals(output, getOutput());

  }


  @Test
  public void basicCopy() throws Exception {
    String createCal = "create calendar --name myCalendar1 --timezone Asia/Kolkata";
    String createCal2 = "create calendar --name myCalendar2 --timezone Asia/Kolkata";
    String useCal = "use calendar --name myCalendar1";
    String createEvent = "create event \"SingleEvent\" from 2025-03-01T09:00 to 2025-03-01T10:00";
    String copy = "copy events on 2025-03-01 --target myCalendar2 to 2025-04-01";
    String useCal2 = "use calendar --name myCalendar2";
    String printEvent = "print events on \"2025-04-01\"";

    ArrayList<String> inputs = new ArrayList<>();
    inputs.add(createCal);
    inputs.add(createCal2);
    inputs.add(useCal);
    inputs.add(createEvent);
    inputs.add(copy);
    inputs.add(useCal2);
    inputs.add(printEvent);
    inputs.add("exit");

    String simulatedInput = String.join("\n", inputs) + "\n";
    simulateUserInput(simulatedInput);
    CalendarApp.main(new String[]{"--mode", "interactive"});

    String output1 = "Using interactive mode. To quit, use 'exit'\n";
    String output2 = "• Subject : SingleEvent,Start date : 2025-04-01," +
            "Start time : 09:00,End date : 2025-04-01,End time : 10:00,isPublic : false\n";

    assertEquals(output1 + output2, getOutput());
  }

  @Test
  public void basicCopy2() throws Exception {
    String createCal = "create calendar --name myCalendar1 --timezone America/New_York";
    String createCal2 = "create calendar --name myCalendar2 --timezone Australia/Sydney";
    String useCal = "use calendar --name myCalendar1";
    String createEvent = "create event \"SingleEvent\" from 2025-03-01T02:00 to 2025-03-01T03:00";
    String copy = "copy events on 2025-03-01 --target myCalendar2 to 2025-04-01";
    String useCal2 = "use calendar --name myCalendar2";
    String printEvent = "print events on \"2025-04-01\"";

    ArrayList<String> inputs = new ArrayList<>();
    inputs.add(createCal);
    inputs.add(createCal2);
    inputs.add(useCal);
    inputs.add(createEvent);
    inputs.add(copy);
    inputs.add(useCal2);
    inputs.add(printEvent);
    inputs.add("exit");

    String simulatedInput = String.join("\n", inputs) + "\n";

    simulateUserInput(simulatedInput);
    CalendarApp.main(new String[]{"--mode", "interactive"});

    String output1 = "Using interactive mode. To quit, use 'exit'\n";
    String output2 = "• Subject : SingleEvent,Start date : 2025-04-01," +
            "Start time : 17:00,End date : 2025-04-01,End time : 18:00,isPublic : false\n";

    assertEquals(output1 + output2, getOutput());
  }

  @Test
  public void basicCopy3() throws Exception {
    String createCal = "create calendar --name myCalendar1 --timezone America/New_York";
    String createCal2 = "create calendar --name myCalendar2 --timezone Australia/Sydney";
    String useCal = "use calendar --name myCalendar1";
    String createEvent = "create event \"SingleEvent\" from 2025-03-01T02:00 to 2025-03-01T03:00";
    String createEvent2 = "create event \"SingleEvent\" from 2025-03-02T02:00 to 2025-03-02T03:00";
    String createEvent3 = "create event \"SingleEvent\" from 2025-03-03T02:00 to 2025-03-03T03:00";


    String copy = "copy events between 2025-03-01 and 2025-03-03 --target myCalendar2 to " +
            "2025-04-01";
    String useCal2 = "use calendar --name myCalendar2";
    String printEvent1 = "print events on \"2025-04-01\"";
    String printEvent2 = "print events on \"2025-04-02\"";
    String printEvent3 = "print events on \"2025-04-03\"";

    ArrayList<String> inputs = new ArrayList<>();

    // Adding elements dynamically
    inputs.add(createCal);
    inputs.add(createCal2);
    inputs.add(useCal);
    inputs.add(createEvent);
    inputs.add(createEvent2);
    inputs.add(createEvent3);
    inputs.add(copy);
    inputs.add(useCal2);
    inputs.add(printEvent1);
    inputs.add(printEvent2);
    inputs.add(printEvent3);
    inputs.add("exit");
    String simulatedInput = String.join("\n", inputs) + "\n";

    simulateUserInput(simulatedInput);
    CalendarApp.main(new String[]{"--mode", "interactive"});

    String output1 = "Using interactive mode. To quit, use 'exit'\n";
    String output2 = "• Subject : SingleEvent,Start date : 2025-04-01," +
            "Start time : 17:00,End date : 2025-04-01,End time : 18:00,isPublic : false\n";

    String output3 = "• Subject : SingleEvent,Start date : 2025-04-02,Start time : 17:00," +
            "End date : 2025-04-02,End time : 18:00,isPublic : false\n";
    String output4 = "• Subject : SingleEvent,Start date : 2025-04-03,Start time : 17:00," +
            "End date : 2025-04-03,End time : 18:00,isPublic : false\n";

    assertEquals(output1 + output2 + output3 + output4, getOutput());
  }

  @Test
  public void newEdit() throws Exception {
    String createCal = "create calendar --name myCalendar1 --timezone America/New_York";
    String createCal2 = "create calendar --name myCalendar2 --timezone Australia/Sydney";
    String useCal = "use calendar --name myCalendar1";
    String diffEvent = "create event \"SingleEvent\" from 2025-02-01T02:00 to 2025-02-01T03:00";
    String createEvent = "create event \"SingleEvent\" from 2025-03-01T02:00 to 2025-03-01T03:00";
    String createEvent2 = "create event \"SingleEvent\" from 2025-03-02T02:00 to 2025-03-02T03:00";
    String createEvent3 = "create event \"SingleEvent\" from 2025-03-03T02:00 to 2025-03-03T03:00";
    String edit = "edit events name \"SingleEvent\" from 2025-03-01T02:00 with \"Event 2\"";
    String diffPrint = "print events on \"2025-02-01\"";
    String printEvent1 = "print events on \"2025-03-01\"";
    String printEvent2 = "print events on \"2025-03-02\"";
    String printEvent3 = "print events on \"2025-03-03\"";

    ArrayList<String> inputs = new ArrayList<>();
    inputs.add(createCal);
    inputs.add(createCal2);
    inputs.add(useCal);
    inputs.add(diffEvent);
    inputs.add(createEvent);
    inputs.add(createEvent2);
    inputs.add(createEvent3);
    inputs.add(edit);
    inputs.add(diffPrint);
    inputs.add(printEvent1);
    inputs.add(printEvent2);
    inputs.add(printEvent3);
    inputs.add("exit");
    String simulatedInput = String.join("\n", inputs) + "\n";

    simulateUserInput(simulatedInput);
    CalendarApp.main(new String[]{"--mode", "interactive"});

    String output1 = "Using interactive mode. To quit, use 'exit'\n";
    String diffEvent1 = "• Subject : SingleEvent,Start date : 2025-02-01,Start time : 02:00," +
            "End date : 2025-02-01,End time : 03:00,isPublic : false\n";
    String output2 = "• Subject : Event 2,Start date : 2025-03-01," +
            "Start time : 02:00,End date : 2025-03-01,End time : 03:00,isPublic : false\n";

    String output3 = "• Subject : Event 2,Start date : 2025-03-02,Start time : 02:00," +
            "End date : 2025-03-02,End time : 03:00,isPublic : false\n";
    String output4 = "• Subject : Event 2,Start date : 2025-03-03,Start time : 02:00," +
            "End date : 2025-03-03,End time : 03:00,isPublic : false\n";

    assertEquals(output1 + diffEvent1 + output2 + output3 + output4, getOutput());
  }

  @Test
  public void basicCopyWithName() throws Exception {
    String createCal = "create calendar --name myCalendar1 --timezone Asia/Kolkata";
    String createCal2 = "create calendar --name myCalendar2 --timezone Asia/Kolkata";
    String useCal = "use calendar --name myCalendar1";
    String createEvent = "create event \"SingleEvent\" from 2025-03-01T09:00 to 2025-03-01T10:00";
    String copy = "copy event SingleEvent on 2025-03-01T09:00 --target myCalendar2 " +
            "to 2025-03-01T09:00";
    String useCal2 = "use calendar --name myCalendar2";
    String printEvent = "print events on \"2025-03-01\"";

    ArrayList<String> inputs = new ArrayList<>();
    inputs.add(createCal);
    inputs.add(createCal2);
    inputs.add(useCal);
    inputs.add(createEvent);
    inputs.add(copy);
    inputs.add(useCal2);
    inputs.add(printEvent);
    inputs.add("exit");

    String simulatedInput = String.join("\n", inputs) + "\n";

    simulateUserInput(simulatedInput);
    CalendarApp.main(new String[]{"--mode", "interactive"});

    String output1 = "Using interactive mode. To quit, use 'exit'\n";
    String output2 = "• Subject : SingleEvent,Start date : 2025-03-01," +
            "Start time : 09:00,End date : 2025-03-01,End time : 10:00,isPublic : false\n";

    assertEquals(output1 + output2, getOutput());
  }


  @Test
  public void editNameOfCalendar() throws Exception {

    String createCal = "create calendar --name myCalendar --timezone Asia/Kolkata";
    String useCal = "use calendar --name myCalendar";
    String editCal = "edit calendar --name myCalendar --property name myCalendar2";
    String[] inputs = {createCal, useCal, editCal, "exit"};
    String simulatedInput = String.join("\n", inputs) + "\n";

    simulateUserInput(simulatedInput);
    CalendarApp.main(new String[]{"--mode", "interactive"});

    String output = "Using interactive mode. To quit, use 'exit'\n";
    assertEquals(output + "Name Change Success: myCalendar2\n", getOutput());

  }


  @Test
  public void newCalAutoDeclineCheck() throws Exception {
    String command;
    ICommand createCommand = new CreateCommand();
    ICommand editCommand = new EditCommand();
    View view = new ConsoleView();

    ICalendar cal = new CalendarV2(ZoneId.of("Asia/Kolkata"));
    command = "event \"SingleEvent\" from 2025-03-01T09:00 to 2025-03-01T10:00";
    createCommand.execute(command, cal);
    command = "event \"SingleEvent\" from 2025-03-02T09:45 to 2025-03-02T10:15";
    createCommand.execute(command, cal);
    command = "events name \"SingleEvent\" from 2025-03-01T09:00  with \"Event 2\"";
    editCommand.execute(command, cal);
    view.viewEvents(cal.getAllCalendarEvents());

    String output1 = "• Subject : Event 2,Start date : 2025-03-01,Start time : 09:00," +
            "End date : 2025-03-01,End time : 10:00,isPublic : false\n";

    String output2 = "• Subject : Event 2,Start date : 2025-03-02," +
            "Start time : 09:45,End date : 2025-03-02,End time : 10:15,isPublic : false\n";

    assertEquals(output1 + output2, getOutput());

  }


  @Test
  public void changeSelfTimeZone() throws Exception {

    String createCal = "create calendar --name myCalendar1 --timezone America/New_York";
    String useCal = "use calendar --name myCalendar1";
    String diffEvent = "create event \"Event 1\" from 2025-02-01T02:00 to 2025-02-01T03:00";
    String createEvent = "create event \"Event 2 \" from 2025-03-01T02:00 to 2025-03-01T03:00";
    String zoneChange = "edit calendar --name myCalendar1 --property timezone Asia/Kolkata";
    String diffPrint = "print events on \"2025-02-01\"";
    String printEvent1 = "print events on \"2025-03-01\"";

    ArrayList<String> inputs = new ArrayList<>();
    inputs.add(createCal);
    inputs.add(useCal);
    inputs.add(diffEvent);
    inputs.add(createEvent);
    inputs.add(zoneChange);
    inputs.add(diffPrint);
    inputs.add(printEvent1);
    inputs.add("exit");

    String simulatedInput = String.join("\n", inputs) + "\n";
    simulateUserInput(simulatedInput);
    CalendarApp.main(new String[]{"--mode", "interactive"});
    String output1 = "Using interactive mode. To quit, use 'exit'\n";
    String evet1 = "• Subject : Event 1,Start date : 2025-02-01,Start time : 12:30," +
            "End date : 2025-02-01,End time : 13:30,isPublic : false\n";
    String event2 = "• Subject : Event 2,Start date : 2025-03-01,Start time : 12:30," +
            "End date : 2025-03-01,End time : 13:30,isPublic : false\n";

    assertEquals(output1 + evet1 + event2, getOutput());
  }


  @Test
  public void eventNotCopiedBecauseOfConflictInTargetCal() throws Exception {

    String createCal2 = "create calendar --name myCalendar2 --timezone Asia/Kolkata";
    String useCal2 = "use calendar --name myCalendar2";
    String diffEvent = "create event \"Event 1\" from 2025-02-01T12:30 to 2025-02-01T13:30";

    String createCal = "create calendar --name myCalendar1 --timezone America/New_York";
    String useCal = "use calendar --name myCalendar1";
    String event = "create event \"Event 1\" from 2025-02-01T02:00 to 2025-02-01T03:00";
    // this event willl not be copied as it creats conflict with the target calendar
    String createEvent = "create event \"Event 2 \" from 2025-03-01T02:00 to 2025-03-01T03:00";
    String copy = "copy events on 2025-03-01 --target myCalendar2 to 2025-02-01";

    String calUse = "use calendar --name myCalendar2";
    String diffPrint = "print events on \"2025-02-01\"";
    String printEvent1 = "print events on \"2025-03-01\"";


    ArrayList<String> inputs = new ArrayList<>();
    inputs.add(createCal2);
    inputs.add(useCal2);
    inputs.add(diffEvent);
    inputs.add(createCal);
    inputs.add(useCal);
    inputs.add(event);
    inputs.add(createEvent);
    inputs.add(copy);
    inputs.add(calUse);
    inputs.add(diffPrint);
    inputs.add(printEvent1);
    inputs.add("exit");

    String simulatedInput = String.join("\n", inputs) + "\n";
    simulateUserInput(simulatedInput);
    CalendarApp.main(new String[]{"--mode", "interactive"});
    String output1 = "Using interactive mode. To quit, use 'exit'\n";
    String evet1 = "• Subject : Event 1,Start date : 2025-02-01,Start time : 12:30," +
            "End date : 2025-02-01,End time : 13:30,isPublic : false\n";

    assertEquals(output1 + evet1, getOutput());
  }


  @Test
  public void recurringEventsCopiedToAnotherCalendar() throws Exception {

    // Create calendar
    String createCal2 = "create calendar --name myCalendar2 --timezone Asia/Kolkata";
    String useCal2 = "use calendar --name myCalendar2";

    //create another calendar and add single and recurring events
    String createCal = "create calendar --name myCalendar1 --timezone America/New_York";
    String useCal = "use calendar --name myCalendar1";
    String singleEvent = "create event \"Event 1\" from 2025-02-01T12:30 to 2025-02-01T13:30";
    String recEvent = "create event --autoDecline \"Recurring\" from 2025-04-01T09:00" +
            " to 2025-04-01T10:00 " +
            "repeats T for 1 times";

    // copy to myCalendar2
    String copy = "copy events between 2025-02-01 and 2025-05-01 --target myCalendar2 to " +
            "2025-04-01";

    String calUse = "use calendar --name myCalendar2";

    String diffPrint = "print events on \"2025-04-01\"";
    String printEvent1 = "print events on \"2025-05-30\"";

    ArrayList<String> inputs = new ArrayList<>();
    inputs.add(createCal2);
    inputs.add(useCal2);
    inputs.add(createCal);
    inputs.add(useCal);
    inputs.add(singleEvent);
    inputs.add(recEvent);
    inputs.add(copy);
    inputs.add(calUse);
    inputs.add(diffPrint);
    inputs.add(printEvent1);
    inputs.add("exit");

    String simulatedInput = String.join("\n", inputs) + "\n";
    simulateUserInput(simulatedInput);
    CalendarApp.main(new String[]{"--mode", "interactive"});
    String output1 = "Using interactive mode. To quit, use 'exit'\n";
    String evet1 = "• Subject : Event 1,Start date : 2025-04-01,Start time : 22:00," +
            "End date : 2025-04-01,End time : 23:00,isPublic : false\n";
    String event2 = "• Subject : Recurring,Start date : 2025-05-30," +
            "Start time : 18:30,End date : 2025-05-30,End time : 19:30,isPublic : false\n";


    assertEquals(output1 + evet1 + event2, getOutput());
  }

}
