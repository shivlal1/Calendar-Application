package controller;

import model.ICalendarExtended;

public interface ICalendarManager {

  public ICalendarExtended getActiveCalendar();

  public void execute(String commandArgs) throws Exception;

}
