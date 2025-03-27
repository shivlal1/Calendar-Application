package controller;

import model.ICalendarExtended;

public interface ICalendarManager {

  public ICalendarExtended getActiveCalendar();

  public ICalendarExtended getCalendarByName(String calendarName);

  public void execute(String commandArgs) throws Exception;

}
