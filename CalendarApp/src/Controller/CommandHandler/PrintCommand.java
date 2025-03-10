package Controller.CommandHandler;


import java.time.LocalDateTime;
import java.util.List;

import Controller.MetaData.PrintCommandMetaDetails;
import Model.Calendar.ACalendar;
import Model.Event.Event;
import Model.Utils.DateUtils;
import view.ConsoleView;

public class PrintCommand extends AbstractCommand {

  private String startDate, endDate;
  private LocalDateTime localStart, localEnd;
  private PrintCommandMetaDetails.PrintEventMetaDetailsBuilder metaData;
  private PrintCommandMetaDetails allMetaDeta;

  private static final String regex = "^events (?:from \"(\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2})\" to \"(\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2})\"|on \"(\\d{4}-\\d{2}-\\d{2})\")$";
  // private static final String regex = "^events (?:from \"(\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2})\" to \"(\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2})\"|on \"(\\d{4}-\\d{2}-\\d{2})\")$

  public PrintCommand() {
//    this.calendar = calendar;
  }

  private String diagnoseCommandError(String command) {

    if (!command.startsWith("events"))
      return "Missing events";


    boolean hasFrom = command.contains("from");
    boolean hasTo = command.contains("to");
    boolean hasOn = command.contains("on");

    if (!hasFrom && !hasOn) {
      return ("Missing From and On");
    }

    if (hasFrom && !hasTo) {
      return "Missing To";
    }

    if (!hasFrom && hasTo) {
      return "Missing From";
    }

    if (!hasFrom && !hasTo && !hasOn) {
      return "Missing On";
    }
    return "Invalid command: Does not match expected format.";

  }

  public void commandParser(String commandArgs) throws Exception {
    initRegexPatter(regex, commandArgs);

    if (!matcher.matches()) {
      throw new Exception("Invalid Command " + diagnoseCommandError(commandArgs));
    }

    startDate = matcher.group(1) != null ? matcher.group(1) : matcher.group(3);
    endDate = matcher.group(2);

    startDate = DateUtils.removeTinDateTime(startDate);
    endDate = DateUtils.removeTinDateTime(endDate);

    if (startDate.indexOf(":") == -1) {
      startDate = DateUtils.changeDateToDateTime(startDate);
    }
    //System.out.println("startDate " + startDate);

    localStart = DateUtils.stringToLocalDateTime(startDate);
    if (endDate != null) {
      localEnd = DateUtils.stringToLocalDateTime(endDate);
    }

    addValuesInMetaDataObject();
  }

  private void addValuesInMetaDataObject() {
    //System.out.println(localEnd + " " + localEnd);
    metaData = new PrintCommandMetaDetails.PrintEventMetaDetailsBuilder();

    metaData.addLocalStartTime(localStart);
    metaData.addLocalEndTime(localEnd);
  }

  private void printCommandUtil(ACalendar calendar) {
    allMetaDeta = metaData.build();
    List<Event> eventDetails = calendar.getMatchingEvents(allMetaDeta);

    ConsoleView v = new ConsoleView();
    v.printInConsole(eventDetails);

  }

  private void printCommandProcess(String commandArgs, ACalendar calendar) throws Exception {
    commandParser(commandArgs);
    printCommandUtil(calendar);
  }

  @Override
  public void execute(String commandArgs, ACalendar calendar) throws Exception {
    printCommandProcess(commandArgs, calendar);
  }
}