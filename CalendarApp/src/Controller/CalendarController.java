package Controller;

import java.util.HashMap;
import java.util.Map;

import Model.ICalendar;


public class CalendarController implements ICommand {

  Map<String, ICommand> commandMap;

  public CalendarController() {
    commandMap = new HashMap<>();
    commandMap.put("create", new CreateCommand());
    commandMap.put("edit", new EditCommand());
    commandMap.put("print", new PrintCommand());
    commandMap.put("show", new ShowStatusCommand());
    commandMap.put("export", new ExportCommand());
  }


  private String[] splitString(String s) {
    int firstSpaceIndex = s.indexOf(" ");
    if (firstSpaceIndex == -1) {
      return new String[]{s, ""};
    }
    String firstPart = s.substring(0, firstSpaceIndex);
    String secondPart = s.substring(firstSpaceIndex + 1);
    return new String[]{firstPart, secondPart};
  }

  @Override
  public void execute(String commandArgs, ICalendar calendar) throws Exception {
    System.out.println("Command being processed: " + commandArgs);
    String[] result = splitString(commandArgs);
    ICommand command = commandMap.get(result[0]);
    if (command != null) {
      command.execute(result[1], calendar);
    } else {
      System.out.println("Invalid command: " + result[0]);
    }
  }
}
