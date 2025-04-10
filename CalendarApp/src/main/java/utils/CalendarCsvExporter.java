package utils;

import java.io.FileWriter;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * The CalendarCsvExporter class is responsible for exporting calendar events to a CSV file.
 * It formats event details into a CSV-compatible format and writes them to the specified file.
 */
public class CalendarCsvExporter {

  private static final String header = "Subject, Start Date, Start Time, End Date, End Time, " +
          "All Day Event, Description, Location, Private\n";

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

    try (FileWriter writer = new FileWriter(absolutePath)) {
      writer.write(header);

      for (Map<String, Object> event : eventList) {
        String subject = (String) event.get("subject");
        LocalDateTime startDateTime = (LocalDateTime) event.get("startDate");
        LocalDateTime endDateTime = (LocalDateTime) event.get("endDate");
        String location = (String) event.get("location");
        if (location == null) {
          location = "";
        }

        String description = (String) event.get("description");
        if (description == null) {
          description = "";
        }
        Boolean isPublic = (Boolean) event.get("isPublic");
        String startDateString = DateUtils.getCsvDate(startDateTime);
        String startTimeString = DateUtils.getCsvTime(startDateTime);
        String endDateString = DateUtils.getCsvDate(endDateTime);
        String endTimeString = DateUtils.getCsvTime(endDateTime);
        Boolean isAllDay = isAllDayEvent(startDateString, endDateString, startTimeString,
                endTimeString);


        writer.write(String.format("\"%s\",%s,%s,\"%s\",\"%s\",%b,\"%s\",\"%s\",%b\n",
                subject, startDateString, startTimeString,
                endDateString, endTimeString,
                isAllDay, description, location, isPublic));
      }
      return absolutePath;

    } catch (Exception e) {
      throw new Exception("Not able to create file");
    }
  }

  /**
   * Determines whether an event should be classified as an "all-day" event.
   *
   * @param startDateString The event's start date as a string.
   * @param endDateString   The event's end date as a string.
   * @param startTimeString The event's start time as a string.
   * @param endTimeString   The event's end time as a string.
   * @return true if the event spans an entire day; false otherwise.
   */
  private boolean isAllDayEvent(String startDateString, String endDateString,
                                String startTimeString, String endTimeString) {

    return (startDateString.equals(endDateString) && startTimeString.equals("12:00 AM")
            && endTimeString.equals("11:59 PM"));
  }

}
