import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.ZoneId;
import java.util.List;
import java.util.Scanner;

import controller.CalendarController;
import controller.CalendarManager;
import controller.CalendarManagerV2;
import controller.ICalendarManager;
import controller.ICalendarManagerV2;
import controller.ICommand;
import controller.ViewController;
import model.ICalendarV2;
import view.ConsoleView;
import view.JFrameView;
import view.UiView;
import view.View;

/**
 * This class is the main driving class of the program. This class handles the initialization
 * and execution of the calendar application in either interactive or headless mode,
 * based on command-line arguments.
 */
public class CalendarApp {

  /**
   * Main method that initializes the application and selects the operation mode based on arguments.
   *
   * @param args Command-line arguments specifying operation mode and command file.
   * @throws Exception If arguments are invalid or unsupported mode is specified.
   */
  public static void main(String[] args) throws Exception {

    if (args.length == 0) {
      ICalendarManagerV2 manager = new CalendarManagerV2("default",
              ZoneId.systemDefault().toString());
      UiView uiView = new JFrameView();
      ViewController viewController = new ViewController(manager, uiView);
      return;
    }
    if (args.length < 2) {
      throw new Exception("Use: --mode interactive OR --mode headless <commandFile>");
    }

    View view = new ConsoleView();
    ICalendarManager calendarManager = new CalendarManager();
    ICommand controller = new CalendarController(calendarManager);
    Scanner scanner = new Scanner(System.in);

    if (isArgsStartingWithMode(args)) {
      if (isInteractiveMode(args)) {
        runInteractiveMode(view, controller, calendarManager, scanner);
      } else if (isHeadlessMode(args)) {
        runHeadlessMode(view, controller, calendarManager, scanner, args[2]);
      } else {
        throw new Exception("Unsupported mode. Use only 'interactive' or 'headless'");
      }
    } else {
      throw new Exception("Command should start with '--mode' keyword");
    }
  }

  /**
   * Runs the application in interactive mode, accepting commands from the user via the console.
   *
   * @param view            The console view for displaying messages and outputs.
   * @param controller      The calendar controller responsible for executing commands.
   * @param calendarManager The manager handling calendar-specific commands and actions.
   * @param scanner         Scanner object for reading user input from console.
   * @throws Exception If command execution encounters an issue.
   */
  private static void runInteractiveMode(View view, ICommand controller,
                                         ICalendarManager calendarManager,
                                         Scanner scanner) throws Exception {

    view.viewMessage("Using interactive mode. To quit, use 'exit'");
    while (true) {
      String commandArgs = scanner.nextLine();
      if (commandArgs.equals("exit")) {
        break;
      }
      try {
        executeCommand(commandArgs, view, controller, calendarManager);
      } catch (Exception e) {
        view.viewMessage(e.getMessage());
      }
    }
  }

  /**
   * Runs the application in headless mode, reading commands sequentially from a provided file.
   *
   * @param view            The console view for displaying messages and outputs.
   * @param controller      The calendar controller responsible for executing commands.
   * @param calendarManager The manager handling calendar-specific commands and actions.
   * @param scanner         Scanner object used to read the file path from the console.
   * @throws Exception If the file path is invalid or the file doesn't end with 'exit'.
   */
  private static void runHeadlessMode(View view, ICommand controller,
                                      ICalendarManager calendarManager,
                                      Scanner scanner, String filePath) throws Exception {
    view.viewMessage("Headless mode");
    List<String> lines;
    try {
      lines = Files.readAllLines(Paths.get(filePath));
    } catch (Exception e) {
      throw new Exception("Path is invalid: " + e.getMessage());
    }

    if (!isLastCommandExit(lines)) {
      throw new Exception("Last command should be 'exit' in file");
    }

    for (String commandArgs : lines) {
      if (commandArgs.equals("exit")) {
        break;
      }
      executeCommand(commandArgs, view, controller, calendarManager);
    }
  }

  /**
   * Determines the appropriate execution path for a given command and delegates execution
   * accordingly.
   *
   * @param commandArgs     The input command string to execute.
   * @param view            The console view for displaying messages and outputs.
   * @param controller      The calendar controller for general command execution.
   * @param calendarManager Manager for calendar-specific commands.
   * @throws Exception If execution of the command encounters an error.
   */
  private static void executeCommand(String commandArgs, View view,
                                     ICommand controller,
                                     ICalendarManager calendarManager) throws Exception {
    if (commandArgs.contains("--name")) {
      calendarManager.execute(commandArgs);
    } else {
      ICalendarV2 activeCalendar = calendarManager.getActiveCalendar();
      if (activeCalendar == null) {
        view.viewMessage("No active calendar selected'");
        return;
      }
      controller.execute(commandArgs, activeCalendar);
    }
  }

  /**
   * Checks if the provided arguments array starts with the '--mode' keyword.
   *
   * @param args The command-line arguments to validate.
   * @return true if the first argument is '--mode'; false otherwise.
   */
  private static boolean isArgsStartingWithMode(String[] args) {
    return args[0].equalsIgnoreCase("--mode");
  }

  /**
   * Checks if the provided mode is 'interactive'.
   *
   * @param args The command-line arguments to evaluate.
   * @return true if mode is 'interactive'; false otherwise.
   */
  private static boolean isInteractiveMode(String[] args) {
    return args[1].equalsIgnoreCase("interactive");
  }

  /**
   * Checks if the provided mode is 'headless'.
   *
   * @param args The command-line arguments to evaluate.
   * @return true if mode is 'headless'; false otherwise.
   */
  private static boolean isHeadlessMode(String[] args) {
    return args[1].equalsIgnoreCase("headless");
  }

  /**
   * Verifies that the last command in a list of commands is 'exit'.
   *
   * @param lines The list of command lines from the input file.
   * @return true if the last command is 'exit'; false otherwise.
   */
  private static boolean isLastCommandExit(List<String> lines) {
    return lines.get(lines.size() - 1).trim().equalsIgnoreCase("exit");
  }
}
