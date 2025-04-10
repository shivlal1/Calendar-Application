package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CalendarCsvImporter {
  public List<Map<String, Object>> importCSV(File selectedFile) {
    List<Map<String, Object>> metaData = new ArrayList<>();
    try (BufferedReader br = new BufferedReader(new FileReader(selectedFile))) {
      String line;
      br.readLine();
      while ((line = br.readLine()) != null) {
        Map<String, Object> meta = new HashMap<>();
        String[] values = line.split("\\s*,\\s*(?=([^\"]*\"[^\"]*\")*[^\"]*$)", -1);
        for (int i = 0; i < values.length; i++) {
          values[i] = values[i].replaceAll("^\"|\"$", "").trim();
        }
        LocalDateTime startDate = DateUtils.getDateFromCSVFile(values[1], values[2]);
        LocalDateTime endDate;
        if (values[5].equals("true")) {  // if all day event set start and end date time
          LocalDateTime newStartDate = startDate.toLocalDate().atStartOfDay();
          LocalDateTime newEndDate = startDate.toLocalDate().atTime(23, 59);
          meta.put("startDate", newStartDate);
          meta.put("endDate", newEndDate);
          meta.put("isAllDay", true);
        } else {
          endDate = DateUtils.getDateFromCSVFile(values[3], values[4]);
          meta.put("startDate", startDate);
          meta.put("endDate", endDate);
          meta.put("isAllDay", false);
        }
        meta.put("subject", values[0]);
        meta.put("description", values[6]);
        meta.put("location", values[7]);
        meta.put("isPublic", Boolean.parseBoolean(values[8]));
        meta.put("isRecurring", false);
        metaData.add(meta);
      }
    } catch (IOException exception) {
      exception.printStackTrace();
    }

    return metaData;
  }


}
