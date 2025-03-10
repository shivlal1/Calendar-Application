package Model.Calendar;

import java.util.ArrayList;
import java.util.List;

import Model.Event.Event;

public abstract class ACalendar implements ICalendar {


  protected List<Event> calendarStorage = new ArrayList();

}
