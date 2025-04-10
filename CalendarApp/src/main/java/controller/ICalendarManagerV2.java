package controller;

import model.ICalendarV2;

public interface ICalendarManagerV2 extends ICalendarManager {

  public void createCalendar(String calendarName, String timeZone);

}
