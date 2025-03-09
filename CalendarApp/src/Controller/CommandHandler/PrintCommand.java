package Controller.CommandHandler;


import java.time.LocalDateTime;

import Model.Calendar.ACalendar;
import Controller.MetaData.PrintEventMetaDetails;
import Model.Utils.DateUtils;

public class PrintCommand extends AbstractCommand {

  private String startDate, endDate;
  private LocalDateTime localStart, localEnd;
  private PrintEventMetaDetails.PrintEventMetaDetailsBuilder metaData;
  private PrintEventMetaDetails allMetaDeta;

  private static final String regex = "^events (?:from \"(\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2})\" to \"(\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2})\"|on \"(\\d{4}-\\d{2}-\\d{2})\")$";

  public PrintCommand() {
//    this.calendar = calendar;
  }

  void commandParser(String commandArgs) {

    initRegexPatter(regex, commandArgs);

    if (!matcher.matches()) {
      System.out.println("  Command did not match the pattern.");
    }

    startDate = matcher.group(1) != null ? matcher.group(1) : matcher.group(3);
    endDate = matcher.group(2);

    startDate = DateUtils.removeTinDateTime(startDate);
    endDate = DateUtils.removeTinDateTime(endDate);


    localStart = DateUtils.stringToLocalDateTime(startDate);
    if (endDate != null) {
      localEnd = DateUtils.stringToLocalDateTime(endDate);
    }

    addValuesInMetaDataObject();
  }

  public void addValuesInMetaDataObject() {
    metaData.addLocalStartTime(localStart);
    metaData.addLocalEndTime(localEnd);
  }

  private void printCommandUtil(ACalendar calendar) {

    allMetaDeta = metaData.build();

    calendar.getMatchingEvents(allMetaDeta);

    //.....
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