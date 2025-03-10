package Controller.CommandHandler;

import java.time.LocalDateTime;

import Model.Calendar.ACalendar;
import Model.Utils.DateUtils;
import view.ConsoleView;

public class ShowStatusCommand extends AbstractCommand {
  private static String regex = "status on (\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2})";
  private LocalDateTime localOnDate;
  private String onDate;

  public ShowStatusCommand() {
  }

  public void commandParser(String commandArgs) {

    initRegexPatter(regex, commandArgs);

    if (!matcher.matches()) {
      System.out.println("Invalid command format!");
    }

    onDate = matcher.group(1);
    onDate = DateUtils.removeTinDateTime(onDate);
    localOnDate = DateUtils.stringToLocalDateTime(onDate);
  }

  private void printCommandUtil(ACalendar calendar) {
    boolean isBusy = calendar.isBusyOnDay(localOnDate);
    ConsoleView view = new ConsoleView();
    view.showStatusInConsole(isBusy);
  }

  private void showCommandProcess(String commandArgs, ACalendar calendar) {
    commandParser(commandArgs);
    printCommandUtil(calendar);
  }

  @Override
  public void execute(String commandArgs, ACalendar calendar) {
    showCommandProcess(commandArgs, calendar);
  }
}