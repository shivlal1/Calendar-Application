package Model.Calendar;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Model.Event.CalendarEvent;

public abstract class ACalendar implements ICalendar, CalendarCreate, CalendarEdit {

  Map<Integer, Map<Integer, Map<Integer, List<CalendarEvent>>>> yearMonthDayData = new HashMap<>();

}
