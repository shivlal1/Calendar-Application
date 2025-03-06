package Controller;

import java.util.HashMap;
import java.util.Map;

import Model.Calendar.ACalendar;
import Model.Calendar.Calendar;


public class CalendarApp {
  public static void main(String[] args) {

    ACalendar calendar = new Calendar();

    Map<String, ICommand> commandMap = new HashMap<>();
    commandMap.put("create", new CreateCommand());
    commandMap.put("edit", new EditCommand());
    commandMap.put("print", new PrintCommand());
    commandMap.put("showStatus", new ShowStatusCommand());
    commandMap.put("export", new ExportCommand());

    String fromDate = "2025-03-03T00:00:00";
    String toDate = "2025-03-03T10:30:00";
    String userCommand = "create event \"Today Meeting\" from " + fromDate + " to " + toDate;

    String[] commands = {
//            "create event --autoDecline \"International Conference\" from 2025-03-01T09:00:00 to 2025-03-03T17:00:00",
            "create event --autoDecline \"Annual Meeting\" on 2025-03-01T09:00:00",
//            "create event --autoDecline \"Project Kickoff\" on 2025-05-10 repeats MRU until 2025-05-20",
//            "create event --autoDecline \"International Conference\" from 2025-03-01T09:00:00 to 2025-03-01T10:00:00 repeats MU until 2025-03-10T09:00:00"
//            "create event --autoDecline \"International Conference\" from 2025-03-01T09:00:00 to 2025-03-01T17:00:00 repeats MR for 5 times"
//            "create event --autoDecline \"Annual Meeting\" on 2025-04-15 repeats MR for 5 times"
//            "create event --autoDecline \"Annual Meeting\" on 2025-04-15 repeats M until 2025-06-01"

//            "edit event name \"Annual Meeting\" from 2025-03-01T09:00:00 to 2025-03-01T10:00:00 with \"Weekly Meeting\"",
            "edit events name \"Annual Meeting\" from 2025-03-01T09:00:00 with \"Weekly Meet\"",
//            "edit events name \"Annual Meeting\" \"Weekly Meeting\""
//            "edit event name \"International Conference\" from \"2025-03-01T09:00:00\" to \"2025-03-03T17:00:00\" with \"NewConferenceName\""
    };

    for (int i = 0; i < commands.length; i++) {
      System.out.println("Command being processed: " + commands[i]);
      String[] result = splitString(commands[i]);
//      System.out.println("result " + result[0]);
      ICommand command = commandMap.get(result[0]);
      if (command != null) {
        command.execute(result[1], calendar);  // Pass the remaining command as an argument
      } else {
        System.out.println("Invalid command: " + result[0]);
      }

    }


  }

  public static String[] splitString(String s) {
    int firstSpaceIndex = s.indexOf(" ");
    if (firstSpaceIndex == -1) {
      return new String[]{s, ""};
    }
    String firstPart = s.substring(0, firstSpaceIndex);
    String secondPart = s.substring(firstSpaceIndex + 1);
    return new String[]{firstPart, secondPart};
  }
}
