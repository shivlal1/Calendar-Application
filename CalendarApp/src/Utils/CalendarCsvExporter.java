package Utils;

import java.io.FileWriter;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

/**
 * The CalendarCsvExporter class is responsible for exporting calendar events to a CSV file.
 * It formats event details into a CSV-compatible format and writes them to the specified file.
 */
public class CalendarCsvExporter {

  /**
   * Exports a list of events to a CSV file with the specified file name.
   *
   * @param eventList The list of events to export, where each event is represented as a map.
   * @param fileName  The name of the CSV file to create.
   * @return The absolute path of the exported CSV file.
   * @throws Exception If an error occurs during file writing.
   */
  public String export(List<Map<String, Object>> eventList, String fileName) throws Exception {

    String absolutePath = Paths.get(fileName).toAbsolutePath().toString();
    DateTimeFormatter csvDateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    DateTimeFormatter csvTimeFormatter = DateTimeFormatter.ofPattern("hh:mm a");

    try (FileWriter writer = new FileWriter(absolutePath)) {
      writer.write("Subject, Start Date, Start Time, End Date, End Time, " +
              "All Day Event, Description, Location, Private\n");


      for (Map<String, Object> event : eventList) {
        String subject = (String) event.get("subject");
        LocalDateTime startDateTime = (LocalDateTime) event.get("startDate");
        LocalDateTime endDateTime = (LocalDateTime) event.get("endDate");
        String location = (String) event.get("location");
        String description = (String) event.get("description");
        Boolean isPublic = (Boolean) event.get("isPublic");
        Boolean isAllDay = setIsAllDayValue();

        String startDateStr = (startDateTime != null) ? startDateTime.format(csvDateFormatter) : "";
        String startTimeStr = (startDateTime != null) ? startDateTime.format(csvTimeFormatter) : "";
        String endDateStr = (endDateTime != null) ? endDateTime.format(csvDateFormatter) : "";
        String endTimeStr = (endDateTime != null) ? endDateTime.format(csvTimeFormatter) : "";

        writer.write(String.format("\"%s\",%s,%s,\"%s\",\"%s\",%b,\"%s\",\"%s\",%b\n",
                subject,
                startDateStr,
                startTimeStr,
                endDateStr,
                endTimeStr,
                isAllDay,
                description,
                location,
                isPublic));
      }
      return absolutePath;
    } catch (Exception e) {
      throw new Exception(e);
    }
  }

  /**
   * Sets the "All Day Event" value for events. Currently always returns false.
   *
   * @return false, indicating that events are not all-day by default.
   */
  private boolean setIsAllDayValue() {
    return false;
  }
}
