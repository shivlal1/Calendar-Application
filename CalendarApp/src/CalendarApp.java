import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

import Controller.CalendarController;
import Model.Calendar;
import Model.ICalendar;
import view.ConsoleView;

public class CalendarApp {
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
        view.messageView("Using interactive mode.To quit use exit");
        while (true) {
          String commandArgs = scanner.nextLine();
          if (commandArgs.equals("exit")) {
            break;
          }
          controller.execute(commandArgs, calendar);
        }
      } else if (isHeadlessMode(args)) {
        view.messageView("Headless mode");
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

  private static boolean isArgsStartingWithMode(String args[]) {
    return args[0].equalsIgnoreCase("--mode");
  }

  private static boolean isInteractiveMode(String args[]) {
    return args[1].toLowerCase().equals("interactive");
  }

  private static boolean isHeadlessMode(String args[]) {
    return args[1].toLowerCase().equals("headless");
  }

  private static boolean isLastCommandExit(List<String> lines) {
    return lines.get(lines.size() - 1).trim().equalsIgnoreCase("exit");
  }
}
