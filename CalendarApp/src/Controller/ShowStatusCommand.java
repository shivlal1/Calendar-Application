package Controller;

import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Model.ICalendar;
import Utils.DateUtils;
import view.ConsoleView;

public class ShowStatusCommand implements ICommand {
  private static final String regex = "status on (\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2})";
  private LocalDateTime localOnDate;
  private String onDate;
  private Pattern pattern;
  private Matcher matcher;

  private String diagnoseCommandError(String command) {
    if (!command.startsWith("status")) {
      return "Status Missing";
    }
    if (!command.contains("status on")) {
      return "Missing or misplaced On";
    }
    return "Invalid command";
  }

  private void commandParser(String commandArgs) throws Exception {
    pattern = Pattern.compile(regex);
    matcher = pattern.matcher(commandArgs);
    if (!matcher.matches()) {
      throw new Exception("Invalid Command " + diagnoseCommandError(commandArgs));
    }
    onDate = matcher.group(1);
    onDate = DateUtils.removeTinDateTime(onDate);
    localOnDate = DateUtils.stringToLocalDateTime(onDate);
  }

  private void printCommandUtil(ICalendar calendar) {
    boolean isBusy = calendar.isUserBusy(localOnDate);
    ConsoleView view = new ConsoleView();
    view.showStatusInConsole(isBusy);
  }

  @Override
  public void execute(String commandArgs, ICalendar calendar) throws Exception {
    commandParser(commandArgs);
    printCommandUtil(calendar);
  }
}