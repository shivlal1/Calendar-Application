import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.assertEquals;


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
    String output2 = "• Subject : SingleEvent,Start date : 2025-03-01," +
            "Start time : 09:00,End date : 2025-03-01,End time : 10:00,isPublic : false\n";

    assertEquals(output1 + output2, getOutput());
  }


  @Rule
  public ExpectedException thrown = ExpectedException.none();


  @Test
  public void invalidCalCommand() throws Exception {
    thrown.expect(Exception.class);
    thrown.expectMessage("start with commad+calendar+name");

    String createCal = "calendar --name myCalendar --timezone Asia/Kolkata";
    String[] inputs = {createCal, "exit"};

    String simulatedInput = String.join("\n", inputs) + "\n";

    simulateUserInput(simulatedInput);
    CalendarApp.main(new String[]{"--mode", "interactive"});
  }


  @Test
  public void invalidCalCommand2() throws Exception {
    thrown.expect(Exception.class);
    thrown.expectMessage("start with commad+calendar+name");

    String createCal = "create --name myCalendar --timezone Asia/Kolkata";
    String[] inputs = {createCal, "exit"};

    String simulatedInput = String.join("\n", inputs) + "\n";

    simulateUserInput(simulatedInput);
    CalendarApp.main(new String[]{"--mode", "interactive"});
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
    thrown.expect(Exception.class);
    thrown.expectMessage("Missing --timezone flag");

    String createCal = "create calendar --name myCalendar  Asia/Kolkata";
    String[] inputs = {createCal, "exit"};

    String simulatedInput = String.join("\n", inputs) + "\n";

    simulateUserInput(simulatedInput);
    CalendarApp.main(new String[]{"--mode", "interactive"});
  }

  @Test
  public void invalidCalCommand5() throws Exception {
    thrown.expect(Exception.class);
    thrown.expectMessage("Missing timeZone value");

    String createCal = "create calendar --name myCalendar --timezone";
    String[] inputs = {createCal, "exit"};

    String simulatedInput = String.join("\n", inputs) + "\n";

    simulateUserInput(simulatedInput);
    CalendarApp.main(new String[]{"--mode", "interactive"});
  }


  @Test
  public void invalidCalCommand6() throws Exception {
    thrown.expect(Exception.class);
    thrown.expectMessage("start with commad+calendar+name");

    String createCal = "create calendar --name myCalendar --timezone Asia/Kolkata";
    String edit = "edit --name myCalendar --property name myCalendar2";
    String[] inputs = {createCal, edit, "exit"};

    String simulatedInput = String.join("\n", inputs) + "\n";

    simulateUserInput(simulatedInput);
    CalendarApp.main(new String[]{"--mode", "interactive"});
  }

  @Test
  public void invalidCalCommand7() throws Exception {
    thrown.expect(Exception.class);
    thrown.expectMessage("No such calendar exists");

    String edit = "edit calendar --name myCalendar --property name myCalendar2";
    String[] inputs = {edit, "exit"};

    String simulatedInput = String.join("\n", inputs) + "\n";

    simulateUserInput(simulatedInput);
    CalendarApp.main(new String[]{"--mode", "interactive"});
  }

  @Test
  public void invalidCalCommand8() throws Exception {
    thrown.expect(Exception.class);
    thrown.expectMessage("property name/value is not present");

    String createCal = "create calendar --name myCalendar --timezone Asia/Kolkata";
    String edit = "edit calendar --name myCalendar --property myCalendar2";
    String[] inputs = {createCal, edit, "exit"};

    String simulatedInput = String.join("\n", inputs) + "\n";

    simulateUserInput(simulatedInput);
    CalendarApp.main(new String[]{"--mode", "interactive"});
  }


  private void simulateUserInput(String input) {
    System.setIn(new ByteArrayInputStream(input.getBytes()));
    outputStream.reset(); // Clear previous output
  }

  private String getOutput() {
    return outputStream.toString().replace("\r", ""); // Normalize newlines
  }


  @Test
  public void basicCalendar2() throws Exception {

    thrown.expect(Exception.class);
    thrown.expectMessage("the event conflicts with another event");

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

  }


  @Test
  public void invalidEdit() throws Exception {
    thrown.expect(Exception.class);
    thrown.expectMessage("--property missing");

    String createCal = "create calendar --name myCalendar --timezone Asia/Kolkata";
    String useCal = "use calendar --name myCalendar";
    String editCal = "edit calendar --name myCalendar name myCalendar2";

    String[] inputs = {createCal, useCal, editCal, "exit"};

    String simulatedInput = String.join("\n", inputs) + "\n";

    simulateUserInput(simulatedInput);
    CalendarApp.main(new String[]{"--mode", "interactive"});

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
  }

  @Test
  public void editTimeZoneOfCalendar() throws Exception {

    String createCal = "create calendar --name myCalendar --timezone Asia/Kolkata";
    String useCal = "use calendar --name myCalendar";
    String editCal = "edit calendar --name myCalendar --property timezone Australia/Sydney";

    String[] inputs = {createCal, useCal, editCal, "exit"};
    String simulatedInput = String.join("\n", inputs) + "\n";

    simulateUserInput(simulatedInput);
    CalendarApp.main(new String[]{"--mode", "interactive"});
  }


  @Test
  public void editTimeZoneOfCalendarInvalid() throws Exception {
    thrown.expect(Exception.class);
    thrown.expectMessage("invalid calendarProperty timezone");

    String createCal = "create calendar --name myCalendar --timezone Asia/Kolkata";
    String useCal = "use calendar --name myCalendar";
    String editCal = "edit calendar --name myCalendar --property timezone Austral/Sydney";

    String[] inputs = {createCal, useCal, editCal, "exit"};
    String simulatedInput = String.join("\n", inputs) + "\n";

    simulateUserInput(simulatedInput);
    CalendarApp.main(new String[]{"--mode", "interactive"});
  }

  @Test
  public void editNotExistingCal() throws Exception {
    thrown.expect(Exception.class);
    thrown.expectMessage("No such calendar exists");

    String createCal = "create calendar --name myCalendar --timezone Asia/Kolkata";
    String useCal = "use calendar --name myCalendar";
    String editCal = "edit calendar --name NotExistingCal --property timezone Australia/Sydney";

    String[] inputs = {createCal, useCal, editCal, "exit"};
    String simulatedInput = String.join("\n", inputs) + "\n";

    simulateUserInput(simulatedInput);
    CalendarApp.main(new String[]{"--mode", "interactive"});
  }


  @Test
  public void invalidUse() throws Exception {
    thrown.expect(Exception.class);
    thrown.expectMessage("calendar Doesn't exists to Use");

    String createCal = "create calendar --name myCalendar --timezone Asia/Kolkata";
    String useCal = "use calendar --name notExistingCal";

    String[] inputs = {createCal, useCal, "exit"};
    String simulatedInput = String.join("\n", inputs) + "\n";

    simulateUserInput(simulatedInput);
    CalendarApp.main(new String[]{"--mode", "interactive"});
  }
}
