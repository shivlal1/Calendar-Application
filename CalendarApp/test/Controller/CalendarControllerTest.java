package controller;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.List;
import java.util.Map;

import model.Calendar;
import model.ICalendar;
import view.ConsoleView;
import view.View;

import static org.junit.Assert.*;

public class CalendarControllerTest {

  CalendarController controler;

  View view;

  @Before
  public void init(){
    controler = new CalendarController();
    view = new ConsoleView();

  }


  @Rule
  public ExpectedException thrown = ExpectedException.none();



  @Test
  public void validCommands() throws Exception {

    thrown.expect(Exception.class);
    thrown.expectMessage("command should start with only create,edit,print,show,status");

    ICalendar calendar = new Calendar();

    String command = "create event  \"multi Day@123 Event\" from 2025-09-20T12:00 to 2025-09-25T13:00";
    controler.execute(command,calendar);

    command = "edit events name  \"multi Day@123 Event\" \"Hello\"";
    controler.execute(command,calendar);

    command = "print events on \"2025-09-20\"";
    controler.execute(command,calendar);

    command = "show status on 2025-09-20T12:00";
    controler.execute(command,calendar);

    command = "InvalidStart status on 2025-09-20T12:00";
    controler.execute(command,calendar);

  }


  @Test
  public void emptyCommand() throws Exception {

    thrown.expect(Exception.class);
    thrown.expectMessage("command should start with only create,edit,print,show,status");
    ICalendar calendar = new Calendar();

    String command = "";
    controler.execute(command,calendar);
  }
  @Test
  public void emptyCreate() throws Exception {

    thrown.expect(Exception.class);
    thrown.expectMessage("error :Must start with create event");
    ICalendar calendar = new Calendar();

    String command = "create";
    controler.execute(command,calendar);
  }

//  @Test
//  public void emptyEdit() throws Exception {
//
//    thrown.expect(Exception.class);
//    thrown.expectMessage("error :Must start with edit event");
//    ICalendar calendar = new Calendar();
//
//    String command = "Ed";
//    controler.execute(command,calendar);
//  }
}