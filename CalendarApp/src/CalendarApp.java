import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

import Controller.CalendarController;
import Model.Calendar;
import Model.ICalendar;
import view.ConsoleView;

/**
 * The CalendarApp class is the main entry point for the calendar application.
 * It handles command-line arguments to determine the operating mode (interactive or headless)
 * and executes commands accordingly.
 */
public class CalendarApp {

  /**
   * The main method that starts the application.
   *
   * @param args Command-line arguments.
   * @throws Exception If there's an error in processing arguments or executing commands.
   */
  public static void main(String[] args) throws Exception {

    if (args.length < 2) {
      new Exception("use: --mode interactive " +
              "OR --mode headless <commandFile>");
      System.exit(1);
    }

    ICalendar calendar = new Calendar();
    ConsoleView view = new ConsoleView();
    CalendarController controller = new CalendarController();
    Scanner scanner = new Scanner(System.in);

    if (isArgsStartingWithMode(args)) {
      if (isInteractiveMode(args)) {
        view.viewMessage("Using interactive mode.To quit use exit");
        while (true) {
          String commandArgs = scanner.nextLine();
          if (commandArgs.equals("exit")) {
            break;
          }
          controller.execute(commandArgs, calendar);
        }
      } else if (isHeadlessMode(args)) {
        view.viewMessage("Headless mode");
        String filePath = scanner.nextLine();
        List<String> lines;
        try {
          lines = Files.readAllLines(Paths.get(filePath));
        } catch (Exception e) {
          throw new Exception("Path is invalid " + e.getMessage());
        }
        if (isLastCommandExit(lines)) {
          for (String commandArgs : lines) {
            controller.execute(commandArgs, calendar);
          }
        } else {
          throw new Exception("Last command should be exit in file");
        }
      } else {
        new Exception("unsupported mode. use only interactive/headless ");
      }
    } else {
      new Exception("command should start with --mode keyword");
    }
  }

  /**
   * Checks if the command-line arguments start with the '--mode' keyword.
   *
   * @param args Command-line arguments.
   * @return true if the arguments start with '--mode' else, false.
   */
  private static boolean isArgsStartingWithMode(String args[]) {
    return args[0].equalsIgnoreCase("--mode");
  }

  /**
   * Checks if the operating mode is 'interactive'.
   *
   * @param args Command-line arguments.
   * @return true if the mode is 'interactive' else, false.
   */
  private static boolean isInteractiveMode(String args[]) {
    return args[1].toLowerCase().equals("interactive");
  }

  /**
   * Checks if the operating mode is 'headless'.
   *
   * @param args Command-line arguments.
   * @return true if the mode is 'headless' else, false.
   */
  private static boolean isHeadlessMode(String args[]) {
    return args[1].toLowerCase().equals("headless");
  }

  /**
   * Checks if the last command in a file is 'exit'.
   *
   * @param lines List of commands read from a file.
   * @return true if the last command is 'exit' else, false.
   */
  private static boolean isLastCommandExit(List<String> lines) {
    return lines.get(lines.size() - 1).trim().equalsIgnoreCase("exit");
  }
}
