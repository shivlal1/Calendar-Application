package controller;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import model.ICalendar;
import utils.CalendarCsvExporter;
import view.ConsoleView;
import view.View;

/**
 * This class implements the ICommand interface and performs
 * exporting of the calendar events to a CSV file.
 */
public class ExportCommand implements ICommand {
  private String fileName;
  private Pattern pattern;
  private Matcher matcher;

  /**
   * This method validates the  errors in the export command format and provides the
   * corresponding specific error messages.
   *
   * @param command the command string to diagnose.
   * @return a string describing the error in the command format.
   */
  private String diagnoseCommandError(String command) {
    if (!command.startsWith("cal")) {
      return "Missing/Misplaced cal keyword";
    }
    if (!command.endsWith(".csv")) {
      return "Error in fileName";
    }
    return "Invalid Command";
  }

  /**
   * This method parses the command arguments to extract the CSV file name.
   *
   * @param commandArgs the command arguments in String format.
   * @throws Exception if the command format is invalid.
   */
  private void commandParser(String commandArgs) throws Exception {
    String regex = "cal (.+\\.csv)";
    pattern = Pattern.compile(regex);
    matcher = pattern.matcher(commandArgs);
    if (!matcher.matches()) {
      throw new Exception("Invalid Command " + diagnoseCommandError(commandArgs));
    }
    fileName = matcher.group(1);
    System.out.println("filename " + fileName);
  }

  /**
   * This method handles exporting the calendar events to a CSV file.
   *
   * @param calendar the ICalendar object that has the events to be exported.
   * @throws Exception if an error occurs during the export process.
   */
  private void exportCommandUtil(ICalendar calendar) throws Exception {
    List<Map<String, Object>> events = calendar.getAllCalendarEvents();
    CalendarCsvExporter exporter = new CalendarCsvExporter();
    String filePath = exporter.export(events, fileName);
    View displayView = new ConsoleView();
    displayView.viewMessage("filePath " + filePath);

  }

  /**
   * THis method executes the export command by parsing the arguments and exporting
   * calendar events to a specified CSV file.
   *
   * @param commandArgs the command arguments as a string, including the file name.
   * @param calendar    the ICalendar object containing events to be exported.
   * @throws Exception if there's an error in parsing or exporting events.
   */
  @Override
  public void execute(String commandArgs, ICalendar calendar) throws Exception {
    commandParser(commandArgs);
    exportCommandUtil(calendar);
  }

}