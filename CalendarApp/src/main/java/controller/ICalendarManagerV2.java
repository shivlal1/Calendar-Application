package controller;

import model.ICalendarV2;

public interface ICalendarManagerV2 extends ICalendarManager {

  public ICalendarV2 getDefaultCalendar();

  public void createCalendar(String calendarName, String timeZone);
}
