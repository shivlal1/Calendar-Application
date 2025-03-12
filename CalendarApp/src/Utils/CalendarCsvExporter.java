package Utils;

import java.io.FileWriter;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public class CalendarCsvExporter {

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

  private boolean setIsAllDayValue() {
    return false;
  }
}
