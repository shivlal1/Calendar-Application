package controller;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import model.Calendar;
import model.ICalendar;

/**
 * A unit test for the ExportCommand class.
 */
public class ExportCommandTest {
  ICommand exportCommand;
  ICalendar cal = new Calendar();
  String command;

  @Before
  public void init() {
    exportCommand = new ExportCommand();
  }

  @Test(expected = Exception.class)
  public void testProperFileName() throws Exception {
    command = "cal filename.csv";
    exportCommand.execute(command, cal);
  }

  @Test(expected = Exception.class)
  public void testProperFileName2() throws Exception {
    command = "cal thisisaverylongfilenamethatistobeadded.csv";
    exportCommand.execute(command, cal);
  }

  @Test(expected = Exception.class)
  public void testMissingExportCal() throws Exception {
    command = "filename.csv";
    exportCommand.execute(command, cal);
  }

  @Test(expected = Exception.class)
  public void testMissingFileName() throws Exception {
    command = "cal";
    exportCommand.execute(command, cal);
  }

  @Test(expected = Exception.class)
  public void testInvalidFileType() throws Exception {
    command = "cal filename.png";
    exportCommand.execute(command, cal);
  }

  @Test(expected = Exception.class)
  public void testIncorrectFileNameWithExtraWords() throws Exception {
    command = "cal filename.csv hello there";
    exportCommand.execute(command, cal);
  }

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Test
  public void emptyCommand() throws Exception {
    CalendarController controler = new CalendarController();

    thrown.expect(Exception.class);
    thrown.expectMessage("Invalid Command");
    ICalendar calendar = new Calendar();

    String command = "export cal";
    controler.execute(command, calendar);
  }

  @Test
  public void emptyCommand2() throws Exception {
    CalendarController controler = new CalendarController();

    thrown.expect(Exception.class);
    thrown.expectMessage("Invalid Command");
    ICalendar calendar = new Calendar();

    String command = "print events";
    controler.execute(command, calendar);
  }


}