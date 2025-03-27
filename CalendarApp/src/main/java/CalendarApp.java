import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

import controller.CalendarController;
import controller.CalendarManager;
import controller.ICalendarManager;
import model.ICalendarV2;
import view.ConsoleView;

public class CalendarApp {

  public static void main(String[] args) throws Exception {
    if (args.length < 2) {
      throw new Exception("Use: --mode interactive OR --mode headless <commandFile>");
    }

    ConsoleView view = new ConsoleView();
    ICalendarManager calendarManager = new CalendarManager();
    CalendarController controller = new CalendarController(calendarManager);
    Scanner scanner = new Scanner(System.in);

    if (isArgsStartingWithMode(args)) {
      if (isInteractiveMode(args)) {
        runInteractiveMode(view, controller, calendarManager, scanner);
      } else if (isHeadlessMode(args)) {
        runHeadlessMode(view, controller, calendarManager, scanner);
      } else {
        throw new Exception("Unsupported mode. Use only 'interactive' or 'headless'");
      }
    } else {
      throw new Exception("Command should start with '--mode' keyword");
    }
  }

  private static void runInteractiveMode(ConsoleView view, CalendarController controller,
                                         ICalendarManager calendarManager,
                                         Scanner scanner) throws Exception {

    view.viewMessage("Using interactive mode. To quit, use 'exit'");

    while (true) {
      String commandArgs = scanner.nextLine();
      if (commandArgs.equals("exit")) {
        break;
      }
      executeCommand(commandArgs, view, controller, calendarManager);
    }
  }

  private static void runHeadlessMode(ConsoleView view, CalendarController controller,
                                      ICalendarManager calendarManager,
                                      Scanner scanner) throws Exception {
    view.viewMessage("Headless mode");
    String filePath = scanner.nextLine();
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

  private static void executeCommand(String commandArgs, ConsoleView view,
                                     CalendarController controller,
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

  private static boolean isArgsStartingWithMode(String[] args) {
    return args[0].equalsIgnoreCase("--mode");
  }

  private static boolean isInteractiveMode(String[] args) {
    return args[1].equalsIgnoreCase("interactive");
  }

  private static boolean isHeadlessMode(String[] args) {
    return args[1].equalsIgnoreCase("headless");
  }

  private static boolean isLastCommandExit(List<String> lines) {
    return lines.get(lines.size() - 1).trim().equalsIgnoreCase("exit");
  }
}
