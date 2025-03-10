package Controller.CommandHandler;

import java.time.LocalDateTime;

import Controller.MetaData.EditCommandMetaDetails;
import Model.Calendar.ACalendar;
import Model.Utils.DateUtils;

public class EditCommand extends AbstractCommand {

  private String eventName;
  private String start, end;
  private String property, newValue;
  private LocalDateTime localStartTime, localEndTime;
  private EditCommandMetaDetails.EditEventMetaDetailsBuilder metaData;
  private EditCommandMetaDetails allMetaDeta;

  private static final String regex = "^event(?:s)?\\s+" +
          "(?<property>\\S+)\\s+" +
          "\"(?<eventName>.*?)\"\\s+" +
          "(?:" +
          "from\\s+(?<start>\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}(?::\\d{2})?)\\s+" +
          "(?:to\\s+(?<end>\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}(?::\\d{2})?)\\s+)?" +
          "with\\s+\"(?<newValue>.*?)\"" +
          "|" +
          "\"(?<newValue2>.*?)\"" +
          ")$";

  public EditCommand() {
  }

  public void commandParser(String commandArgs) {

    initRegexPatter(regex, commandArgs);
    metaData = new EditCommandMetaDetails.EditEventMetaDetailsBuilder();

    if (!matcher.matches()) {
      System.out.println("  Command did not match the pattern.");
    }

    property = matcher.group("property");
    eventName = matcher.group("eventName").trim();
    start = matcher.group("start");
    end = matcher.group("end");
    newValue = matcher.group("newValue") != null
            ? matcher.group("newValue").trim()
            : matcher.group("newValue2").trim();


    if (start != null) {
      start = DateUtils.removeTinDateTime(start);
      localStartTime = DateUtils.stringToLocalDateTime(start);
    }
    if (end != null) {
      end = DateUtils.removeTinDateTime(end);
      localEndTime = DateUtils.stringToLocalDateTime(end);
    }
    addValuesInMetaDataObject();
  }

  private void addValuesInMetaDataObject() {
    metaData.addProperty(property);
    metaData.addEventName(eventName);
    metaData.addNewValue(newValue);
    metaData.addEndTime(end);
    metaData.addStartTime(start);
    metaData.addLocalStartTime(localStartTime);
    metaData.addLocalEndTime(localEndTime);
  }

  private void editCommandUtil(ACalendar calendar) {
    allMetaDeta = metaData.build();
    calendar.editEvent(allMetaDeta);
  }

  private void editCommandProcess(String commandArgs, ACalendar calendar) {
    commandParser(commandArgs);
    editCommandUtil(calendar);
  }

  @Override
  public void execute(String commandArgs, ACalendar calendar) {
    editCommandProcess(commandArgs, calendar);
  }
}