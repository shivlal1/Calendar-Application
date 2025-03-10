package Controller;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.List;

import Controller.CommandHandler.CreateCommand;
import Controller.CommandHandler.EditCommand;
import Controller.CommandHandler.ICommand;
import Controller.CommandHandler.PrintCommand;
import Controller.CommandHandler.ShowStatusCommand;
import Controller.MetaData.PrintCommandMetaDetails;
import Model.Calendar.ACalendar;
import Model.Calendar.Calendar;
import Model.Event.Event;
import Model.Utils.DateUtils;

import static org.junit.Assert.assertEquals;

public class CreateCommandTest {
  ICommand createCommand;
  ICommand editCommand;
  ICommand printCommand;
  ICommand showStatusCommand;

  @Before
  public void init() {
    createCommand = new CreateCommand();
    editCommand = new EditCommand();
    printCommand = new PrintCommand();
    showStatusCommand = new ShowStatusCommand();
  }

  @Test
  public void testSimpleCommand() throws Exception {

    ACalendar cal = new Calendar();

    String command = "event \"International Conference\" from 2025-03-01T09:00 to 2025-03-03T13:00";
    createCommand.execute(command, cal);

    LocalDateTime start = DateUtils.stringToLocalDateTime("2025-03-01 09:00");
    LocalDateTime end = DateUtils.stringToLocalDateTime("2025-03-03 13:00");

    PrintCommandMetaDetails print = new PrintCommandMetaDetails.PrintEventMetaDetailsBuilder()
            .addLocalEndTime(end)
            .addLocalStartTime(start)
            .build();

    List<Event> list = cal.getMatchingEvents(print);
    assertEquals(list.get(0).getSubject(), "International Conference");
    assertEquals(list.get(0).getStartDate(), start);
    assertEquals(list.get(0).getEndDate(), end);
    assertEquals(list.size(), 1);


    start = DateUtils.stringToLocalDateTime("2025-03-01 09:00");

    PrintCommandMetaDetails print2 = new PrintCommandMetaDetails.PrintEventMetaDetailsBuilder()
            .addLocalStartTime(start)
            .build();

    list = cal.getMatchingEvents(print2);

    assertEquals(list.get(0).getSubject(), "International Conference");
    assertEquals(list.get(0).getStartDate(), start);
    assertEquals(list.size(), 1);
    cal.printEvents();
  }

  @Test
  public void testSimpleCommand2() throws Exception {
    ACalendar cal = new Calendar();
    String command = "event  \"RE\" from 2025-03-01T09:00  to " +
            "2025-03-01T10:00  repeats MTW for 5 times";
    createCommand.execute(command, cal);

    LocalDateTime start = DateUtils.stringToLocalDateTime("2025-03-03 09:00");
    LocalDateTime end = DateUtils.stringToLocalDateTime("2025-03-03 10:00");

    PrintCommandMetaDetails print = new PrintCommandMetaDetails.PrintEventMetaDetailsBuilder()
            .addLocalEndTime(end)
            .addLocalStartTime(start)
            .build();

    List<Event> list = cal.getMatchingEvents(print);
    assertEquals(list.get(0).getSubject(), "RE");
    assertEquals(list.get(0).getStartDate(), start);
    assertEquals(list.size(), 1);
  }

  @Test
  public void testSimpleCommand3() throws Exception {
    ACalendar cal = new Calendar();
    String command = "event  \"RE\" from 2025-03-01T09:00  to " +
            "2025-03-01T10:00  repeats MTW for 5 times";
    createCommand.execute(command, cal);

    command = "event \"International Conference\" from 2025-03-01T09:00 to 2025-03-03T13:00";
    createCommand.execute(command, cal);

    cal.printEvents();
    /* LocalDateTime start = DateUtils.stringToLocalDateTime("2025-03-03 09:00");
    LocalDateTime end = DateUtils.stringToLocalDateTime("2025-03-03 10:00");

    PrintEventMetaDetails print = new PrintEventMetaDetails.PrintEventMetaDetailsBuilder()
            .addLocalEndTime(end)
            .addLocalStartTime(start)
            .build();

    List<AEvent> list = cal.getMatchingEvents(print);
    assertEquals(list.get(0).getSubject(),"RE");
    assertEquals(list.get(0).getStartDate(),start);
    assertEquals(list.size(),1); */
  }

  @Test
  public void dateUtil() {
    System.out.println(DateUtils.stringToLocalDateTime("2025-03-04 00:00"));

  }
}