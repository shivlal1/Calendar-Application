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

  public void commandParser(String commandArgs) {
    initRegexPatter(regex, commandArgs);

    if (!matcher.matches()) {
      System.out.println("  Command did not match the pattern.");
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

  public void addValuesInMetaDataObject() {
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

  private void printCommandProcess(String commandArgs, ACalendar calendar) {
    commandParser(commandArgs);
    printCommandUtil(calendar);
  }

  @Override
  public void execute(String commandArgs, ACalendar calendar) {
    printCommandProcess(commandArgs, calendar);
  }
}