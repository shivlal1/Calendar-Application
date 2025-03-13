package Utils;

import java.io.FileWriter;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class CalendarCsvExporter {

  public String export(List<Map<String, Object>> eventList, String fileName) throws Exception {
    System.out.println("file " + fileName);
    String absolutePath = Paths.get(fileName).toAbsolutePath().toString();

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
        String startDateString = DateUtils.getCsvDate(startDateTime);
        String startTimeString = DateUtils.getCsvTime(startDateTime);
        String endDateString = DateUtils.getCsvDate(endDateTime);
        String endTimeString = DateUtils.getCsvTime(endDateTime);

        writer.write(String.format("\"%s\",%s,%s,\"%s\",\"%s\",%b,\"%s\",\"%s\",%b\n",
                subject, startDateString, startTimeString,
                endDateString, endTimeString,
                isAllDay, description, location, isPublic));
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
