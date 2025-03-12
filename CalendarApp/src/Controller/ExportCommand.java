package Controller;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Model.ICalendar;
import Utils.CalendarCsvExporter;

public class ExportCommand implements ICommand {
  private String fileName;
  private Pattern pattern;
  private Matcher matcher;

  private String diagnoseCommandError(String command) {
    if (!command.startsWith("cal")) {
      return "Missing/Misplaced cal keyword";
    }
    if (!command.endsWith(".csv")) {
      return "Error in fileName";
    }
    return "Invalid Command";
  }

  private void commandParser(String commandArgs) throws Exception {
    String regex = "cal (.+\\.csv)";
    pattern = Pattern.compile(regex);
    matcher = pattern.matcher(commandArgs);
    if (!matcher.matches()) {
      throw new Exception("Invalid Command " + diagnoseCommandError(commandArgs));
    }
    String fileName = matcher.group(1);
    System.out.println("filename " + fileName);
  }

  private void exportCommandUtil(ICalendar calendar) throws Exception {
    // String filePath = calendar.exportCalendarAndGetFilePath();
    List<Map<String, Object>> events = calendar.getAllCalendarEvents();
    CalendarCsvExporter exporter = new CalendarCsvExporter();
    String filePath = exporter.export(events, fileName);

  }

  @Override
  public void execute(String commandArgs, ICalendar calendar) throws Exception {
    commandParser(commandArgs);
    exportCommandUtil(calendar);
  }

}