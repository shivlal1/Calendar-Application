package Controller;

import java.time.LocalDateTime;

import Model.Calendar.ACalendar;
import Model.Utils.DateUtils;
import view.ConsoleView;

public class ShowStatusCommand extends AbstractCommand {
  private static String regex = "status on (\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2})";
  private LocalDateTime localOnDate;
  private String onDate;

  private String diagnoseCommandError(String command) {

    if (!command.startsWith("status")) {
      return "Status Missing";
    }

    if (!command.contains("status on")) {
      return "Missing or misplaced On";
    }
    return "Invalid command";
  }

  public void commandParser(String commandArgs) throws Exception {

    initRegexPatter(regex, commandArgs);

    if (!matcher.matches()) {
      throw new Exception("Invalid Command " + diagnoseCommandError(commandArgs));
    }

    onDate = matcher.group(1);
    onDate = DateUtils.removeTinDateTime(onDate);
    localOnDate = DateUtils.stringToLocalDateTime(onDate);
  }

  private void printCommandUtil(ACalendar calendar) {
    boolean isBusy = calendar.isUserBusy(localOnDate);

    ConsoleView view = new ConsoleView();
    view.showStatusInConsole(isBusy);
  }

  private void showCommandProcess(String commandArgs, ACalendar calendar) throws Exception {
    commandParser(commandArgs);
    printCommandUtil(calendar);
  }

  @Override
  public void execute(String commandArgs, ACalendar calendar) throws Exception {
    showCommandProcess(commandArgs, calendar);
  }
}