package controller;

import java.util.HashMap;
import java.util.Map;

import model.ICalendar;

/**
 * The CalendarController class implements the ICommand interface and manages
 * the execution of various calendar-related commands.
 */
public class CalendarController implements ICommand {

  Map<String, ICommand> commandMap;

  /**
   * Constructs a new CalendarController and initializes the commandMap with
   * various calendar-related commands.
   */
  public CalendarController() {
    commandMap = new HashMap<>();
    commandMap.put("create", new CreateCommand());
    commandMap.put("edit", new EditCommand());
    commandMap.put("print", new PrintCommand());
    commandMap.put("show", new ShowStatusCommand());
    commandMap.put("export", new ExportCommand());
  }


  /**
   * This method splits a given string into two parts, the first word and the rest of the string.
   *
   * @param s The input string to be split.
   * @return An array of two strings.
   */
  private String[] splitString(String s) {
    int firstSpaceIndex = s.indexOf(" ");
    if (firstSpaceIndex == -1) {
      return new String[]{s, ""};
    }
    String firstPart = s.substring(0, firstSpaceIndex);
    String secondPart = s.substring(firstSpaceIndex + 1);
    return new String[]{firstPart, secondPart};
  }

  /**
   * This method executes the given command with the provided arguments on the specified calendar.
   *
   * @param commandArgs the command and its arguments as a single string.
   * @param calendar    the object on which the command will be executed.
   * @throws Exception if at all an error occurs during the command execution.
   */
  @Override
  public void execute(String commandArgs, ICalendar calendar) throws Exception {
    String[] result = splitString(commandArgs);
    ICommand command = commandMap.get(result[0]);
    if (command != null) {
      command.execute(result[1], calendar);
    } else {
      throw new Exception("command should start with only create,edit,print,show,status");

    }
  }
}
