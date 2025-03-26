package controller;

import org.junit.Test;

import model.CalendarExtended;
import model.ICalendar;

public class CopyCommandTest {


  @Test
  public void test() throws Exception {
    String command = "copy event \"Event\" on 2025-03-02T09:00 --target MyCal to 2025-03-02T10:00";
    ICalendar cal = new CalendarExtended("cal", "Asia/Kolkata");
    CopyCommand cmd = new CopyCommand();
    cmd.execute(command, cal);

    command = "copy events on 2025-03-02 --target <calendarName> to 2025-03-05";
    cmd.execute(command, cal);

    command = "copy events between 2025-03-02 and 2025-03-03 --target <calendarName> to 2025-03-05";
    cmd.execute(command, cal);

  }
}