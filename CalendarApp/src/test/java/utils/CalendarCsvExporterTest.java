package utils;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import view.ConsoleView;
import view.View;

import static org.junit.Assert.assertEquals;

/**
 * Test class to check the functionality of the export csv file.
 */
public class CalendarCsvExporterTest {

  View view;

  @Before
  public void init() {
    view = new ConsoleView();
  }

  @Test
  public void testCSV() throws Exception {

    Map<String, Object> mapEvent = new HashMap<>();
    mapEvent.put("subject", "hello");
    mapEvent.put("startDate", LocalDateTime.of(2025, 3, 01, 10, 0));
    mapEvent.put("endDate", LocalDateTime.of(2025, 3, 02, 10, 0));
    mapEvent.put("description", "annual meeting");
    mapEvent.put("location", "snell");
    mapEvent.put("isPublic", true);

    List<Map<String, Object>> events = new ArrayList<>();
    events.add(mapEvent);

    String fileName = "testFile.csv";
    CalendarCsvExporter export = new CalendarCsvExporter();
    String absolutePath = export.export(events, fileName);
    String expected = getViewCalendarOutput(absolutePath);
    String actual = "Absolute Path" + absolutePath + "\n";
    assertEquals(actual, expected);

    String pathToRead = Paths.get(fileName).toAbsolutePath().toString();

    List<String> readingEvents = Files.readAllLines(Paths.get(pathToRead));

    String expectedHeader = "Subject, Start Date, Start Time, End Date, End Time, " +
            "All Day Event, Description, Location, Private";
    assertEquals(expectedHeader, readingEvents.get(0));

    String expectedValue = "\"hello\",03/01/2025,10:00 AM,\"03/02/2025\",\"10:00 AM\"," +
            "false,\"annual meeting\",\"snell\",true";
    assertEquals(expectedValue, readingEvents.get(1));

  }

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Test
  public void invalidFile() throws Exception {

    thrown.expect(Exception.class);
    thrown.expectMessage("Not able to create file");

    Map<String, Object> mapEvent = new HashMap<>();
    List<Map<String, Object>> events = new ArrayList<>();
    events.add(mapEvent);

    CalendarCsvExporter export = new CalendarCsvExporter();
    String absolutePath = export.export(events, "..csv");

  }

  /**
   * Function to caputure console value of absolute path.
   *
   * @param absolutePath absolute path as a parameter.
   * @return string value with absolute path.
   */
  private String getViewCalendarOutput(String absolutePath) {
    PrintStream originalOut = System.out;
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    PrintStream customOut = new PrintStream(outputStream);
    System.setOut(customOut);
    customOut.flush();
    int captureStartIndex = outputStream.size();
    view.viewMessage("Absolute Path" + absolutePath);
    System.setOut(originalOut);
    String capturedOutput = outputStream.toString();
    String filteredOutput = capturedOutput.substring(captureStartIndex);
    return filteredOutput;
  }

  @Test
  public void testCSVAllDay() throws Exception {

    Map<String, Object> mapEvent = new HashMap<>();
    mapEvent.put("subject", "hello");
    mapEvent.put("startDate", LocalDateTime.of(2025, 3, 01, 00, 0));
    mapEvent.put("endDate", LocalDateTime.of(2025, 3, 01, 23, 59));
    mapEvent.put("description", "annual meeting");
    mapEvent.put("location", "snell");
    mapEvent.put("isPublic", true);

    List<Map<String, Object>> events = new ArrayList<>();
    events.add(mapEvent);

    String fileName = "testFile.csv";
    CalendarCsvExporter export = new CalendarCsvExporter();
    String absolutePath = export.export(events, fileName);
    String expected = getViewCalendarOutput(absolutePath);
    String actual = "Absolute Path" + absolutePath + "\n";
    assertEquals(actual, expected);

    String pathToRead = Paths.get(fileName).toAbsolutePath().toString();

    List<String> readingEvents = Files.readAllLines(Paths.get(pathToRead));

    String expectedHeader = "Subject, Start Date, Start Time, End Date, End Time, " +
            "All Day Event, Description, Location, Private";
    assertEquals(expectedHeader, readingEvents.get(0));

    String expectedValue = "\"hello\",03/01/2025,12:00 AM,\"03/01/2025\",\"11:59 PM\"," +
            "true,\"annual meeting\",\"snell\",true";
    assertEquals(expectedValue, readingEvents.get(1));

  }


}