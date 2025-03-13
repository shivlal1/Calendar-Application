package Controller;

import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Model.ICalendar;
import Utils.DateUtils;
import view.ConsoleView;

/**
 * This class implements the ICommand interface and is responsible for
 * displaying the user's status (busy or not) at a specified time.
 */
public class ShowStatusCommand implements ICommand {
  private static final String regex = "status on (\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2})";
  private LocalDateTime localOnDate;
  private String onDate;
  private Pattern pattern;
  private Matcher matcher;

  /**
   * This method validates errors in the show status command format and provides specific
   * error messages.
   *
   * @param command The command string to diagnose.
   * @return A string describing the error in the command format.
   */
  private String diagnoseCommandError(String command) {
    if (!command.startsWith("status")) {
      return "Status Missing";
    }
    if (!command.contains("status on")) {
      return "Missing or misplaced On";
    }
    return "Invalid command";
  }

  /**
   * This method parses the command arguments to extract the date and time for checking
   * the user's status.
   *
   * @param commandArgs the command arguments as a string.
   * @throws Exception if the command format is invalid or missing required fields.
   */
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

  /**
   * This method is used to handle displaying the user's status at the specified time.
   *
   * @param calendar The ICalendar object used to check if the user is busy.
   */
  private void printCommandUtil(ICalendar calendar) {
    boolean isBusy = calendar.isUserBusy(localOnDate);
    ConsoleView view = new ConsoleView();
    view.showStatusInConsole(isBusy);
  }

  /**
   * This method executes the show status command by parsing the arguments and displaying
   * the user's status at the specified time.
   *
   * @param commandArgs The command arguments as a string, including the date and time.
   * @param calendar    The ICalendar object used to check the user's status.
   * @throws Exception If there's an error in parsing or displaying the status.
   */
  @Override
  public void execute(String commandArgs, ICalendar calendar) throws Exception {
    commandParser(commandArgs);
    printCommandUtil(calendar);
  }
}