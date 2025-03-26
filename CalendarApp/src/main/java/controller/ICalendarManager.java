package controller;

import model.ICalendar;
import model.ICalendarExtended;

public interface ICalendarManager {

  public ICalendarExtended getActiveCalendar();

  public ICalendar getCalendarByName(String calendarName);

  public void execute(String commandArgs) throws Exception;

}
