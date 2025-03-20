package controller;

import java.util.HashMap;
import java.util.Map;


public class CalendarManager implements ICalendarManager {


  Map<String, ICommand> calendarMap;

  public CalendarManager() {
    calendarMap = new HashMap<>();
  }


  public void execute(String commandArgs) throws Exception {

  }

}
