package Controller.CommandHandler;

import java.util.List;

import Model.Calendar.ACalendar;
import Model.Event.EventDetails;

public class ExportCommand extends AbstractCommand {
  private String fileName;

  public ExportCommand() {
    // this.calendar = calendar;
  }

  @Override
  public void commandParser(String commandArgs) {
    String regex = "cal (.+\\.csv)";

    initRegexPatter(regex, commandArgs);

    if (!matcher.matches()) {
      System.out.println("  Command did not match the pattern.");
    }
    String fileName = matcher.group(1);
    System.out.println("filename "+fileName);
  }

  public void exportCommandUtil(ACalendar calendar){
    String filePath =  calendar.exportCalendarAndGetFilePath();
  }

  @Override
  public void execute(String commandArgs, ACalendar calendar) {
    commandParser(commandArgs);
    exportCommandUtil(calendar);
  }


}