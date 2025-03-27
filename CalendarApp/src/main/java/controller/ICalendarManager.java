package controller;

import model.ICalendarV2;

public interface ICalendarManager {

  public ICalendarV2 getActiveCalendar();

  public ICalendarV2 getCalendarByName(String calendarName);

  public void execute(String commandArgs) throws Exception;

}
