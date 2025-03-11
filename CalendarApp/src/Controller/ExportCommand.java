package Controller;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Model.Calendar.ACalendar;

public class ExportCommand implements ICommand {
  private String fileName;
  private Pattern pattern;
  private Matcher matcher;

  protected String diagnoseCommandError(String command) {
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

  private void exportCommandUtil(ACalendar calendar) {
    String filePath = calendar.exportCalendarAndGetFilePath();
  }

  @Override
  public void execute(String commandArgs, ACalendar calendar) throws Exception {
    commandParser(commandArgs);
    exportCommandUtil(calendar);
  }

}