package controller;

import org.junit.Test;

import model.ICalendar;

public class CalendarManagerTest {


  @Test
  public void createTest() throws Exception {

    String command = "create calendar --name myCal --timezone Asia/Kolkata";


    command = "edit calendar --name myCal --property name myCalendar";

    command = "use calendar --name myCal";



  }
}