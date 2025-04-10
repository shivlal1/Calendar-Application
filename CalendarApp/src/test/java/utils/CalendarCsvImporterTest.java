package utils;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import utils.CalendarCsvImporter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class CalendarCsvImporterTest {
  private File tempCsvFile;



  @Test
  public void testImportCSV() throws IOException {
    tempCsvFile = File.createTempFile("test_file", ".csv");
    FileWriter writer = new FileWriter(tempCsvFile);
    writer.write("Subject, Start Date, Start Time, End Date, End Time, All Day Event, Description, Location, Private\n");
    writer.write("\"Single Event\",05/12/2025,12:00 AM,\"05/12/2025\",\"11:59 PM\",true,\"\",\"\",false");
   // writer.write("\"Event\",04/01/2025,01:00 AM,\"04/01/2025\",\"02:00 AM\",false,\"Annaul Event\",\"snell\",false");
    writer.close();

    CalendarCsvImporter importer = new CalendarCsvImporter();
    List<Map<String, Object>> result = importer.importCSV(tempCsvFile);

    //assertEquals(2, result.size());

    Map<String, Object> event1 = result.get(0);
    assertEquals("Single Event", event1.get("subject"));
    assertEquals(LocalDateTime.of(2025, 5, 12, 00, 0), event1.get("startDate"));
    assertEquals(LocalDateTime.of(2025, 5, 12, 23, 59), event1.get("endDate"));
    assertEquals("", event1.get("description"));
    assertEquals("", event1.get("location"));
    assertEquals(false, event1.get("isPublic"));
    assertEquals(true, event1.get("isAllDay"));

//    event1 = result.get(1);
//    assertEquals("Event", event1.get("subject"));
//    assertEquals(LocalDateTime.of(2025, 4, 1, 13, 0), event1.get("startDate"));
//    assertEquals(LocalDateTime.of(2025, 4, 1, 14, 0), event1.get("endDate"));
//    assertEquals("Annaul Event", event1.get("description"));
//    assertEquals("snell", event1.get("location"));
//    assertEquals(false, event1.get("isPublic"));
//    assertEquals(false, event1.get("isAllDay"));

    if (tempCsvFile != null && tempCsvFile.exists()) {
      tempCsvFile.delete();
    }
  }


  @Test
  public void testImportCSV2() throws IOException {
    tempCsvFile = File.createTempFile("test_file", ".csv");
    FileWriter writer = new FileWriter(tempCsvFile);
    writer.write("Subject, Start Date, Start Time, End Date, End Time, All Day Event, Description, Location, Private\n");
    //writer.write("\"Single Event\",05/12/2025,12:00 AM,\"05/12/2025\",\"11:59 PM\",true,\"\",\"\",false");
     writer.write("\"Event\",04/01/2025,01:00 AM,\"04/01/2025\",\"02:00 AM\",false,\"Annaul Event\",\"snell\",false");
    writer.close();

    CalendarCsvImporter importer = new CalendarCsvImporter();
    List<Map<String, Object>> result = importer.importCSV(tempCsvFile);

    Map<String, Object> event1 = result.get(0);
    assertEquals("Event", event1.get("subject"));
    assertEquals(LocalDateTime.of(2025, 4, 1, 01, 0), event1.get("startDate"));
    assertEquals(LocalDateTime.of(2025, 4, 1, 02, 0), event1.get("endDate"));
    assertEquals("Annaul Event", event1.get("description"));
    assertEquals("snell", event1.get("location"));
    assertEquals(false, event1.get("isPublic"));
    assertEquals(false, event1.get("isAllDay"));

    if (tempCsvFile != null && tempCsvFile.exists()) {
      tempCsvFile.delete();
    }
  }
}



