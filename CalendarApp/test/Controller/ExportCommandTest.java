package Controller;

import org.junit.Before;
import org.junit.Test;

import Model.Calendar.ACalendar;
import Model.Calendar.Calendar;

import static org.junit.Assert.assertEquals;

public class ExportCommandTest {
  ICommand exportCommand;
  ACalendar cal = new Calendar();
  String command;

  @Before
  public void init() {
    exportCommand = new ExportCommand();
  }

  @Test
  public void testProperFileName() throws Exception {
    try {
      command = "cal filename.csv";
      exportCommand.execute(command, cal);
    } catch (Exception e) {
      assertEquals(e.getMessage(), "Invalid Command");
    }
  }

  @Test
  public void testProperFileName2() throws Exception {
    try {
      command = "cal thisisaverylongfilenamethatistobeadded.csv";
      exportCommand.execute(command, cal);
    } catch (Exception e) {
      assertEquals(e.getMessage(), "Invalid Command");
    }
  }

  @Test
  public void testMissingExportCal() throws Exception {
    try {
      command = "filename.csv";
      exportCommand.execute(command, cal);
    } catch (Exception e) {
      assertEquals(e.getMessage(), "Invalid Command Missing/Misplaced cal keyword");
    }
  }

  @Test
  public void testMissingFileName() throws Exception {
    try {
      command = "cal";
      exportCommand.execute(command, cal);
    } catch (Exception e) {
      assertEquals(e.getMessage(), "Invalid Command Error in fileName");
    }
  }

  @Test
  public void testInvalidFileType() throws Exception {
    try {
      command = "cal filename.png";
      exportCommand.execute(command, cal);
    } catch (Exception e) {
      assertEquals(e.getMessage(), "Invalid Command Error in fileName");
    }
  }

  @Test
  public void testIncorrectFileNameWithExtraWords() throws Exception {
    try {
      command = "cal filename.csv hello there";
      exportCommand.execute(command, cal);
    } catch (Exception e) {
      assertEquals(e.getMessage(), "Invalid Command Error in fileName");
    }
  }

}