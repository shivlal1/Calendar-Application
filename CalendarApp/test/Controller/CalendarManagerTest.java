package controller;

import org.junit.Test;

import model.ICalendar;
import model.ICalendarExtended;

import static org.junit.Assert.*;

public class CalendarManagerTest {


  @Test
  public void createTest() throws Exception {
    String command = "create calendar --name myCal --timezone Asia/Kolkata";

    ICalendarManager cal = new CalendarManager();
    cal.execute(command);

    command = "use calendar --name myCal";
    cal.execute(command);

    System.out.println("active cal "+cal.getActiveCalendar());

    ICommand cmd = new CreateCommand();
    cmd.execute("",cal.getActiveCalendar());

  }
}