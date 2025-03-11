package Controller;

import org.junit.Before;
import org.junit.Test;

import Controller.CommandHandler.CreateCommand;
import Controller.CommandHandler.EditCommand;
import Controller.CommandHandler.ICommand;
import Controller.CommandHandler.PrintCommand;
import Controller.CommandHandler.ShowStatusCommand;
import Model.Calendar.ACalendar;
import Model.Calendar.Calendar;

import static org.junit.Assert.assertEquals;

public class CreateCommandTest {
  ICommand createCommand;
  ICommand editCommand;
  ICommand printCommand;
  ICommand showStatusCommand;
  ACalendar cal = new Calendar();
  String command;

  @Before
  public void init() {
    createCommand = new CreateCommand();
    editCommand = new EditCommand();
    printCommand = new PrintCommand();
    showStatusCommand = new ShowStatusCommand();
  }


//
//  @Test
//  public void testSimpleCommand() throws Exception {
//
//    ACalendar cal = new Calendar();
//
//    String command = "event \"International Conference\" from 2025-03-01T09:00 to 2025-03-03T13:00";
//    createCommand.execute(command, cal);
//
//    LocalDateTime start = DateUtils.stringToLocalDateTime("2025-03-01 09:00");
//    LocalDateTime end = DateUtils.stringToLocalDateTime("2025-03-03 13:00");
//
//    PrintCommandMetaDetails print = new PrintCommandMetaDetails.PrintEventMetaDetailsBuilder()
//            .addLocalEndTime(end)
//            .addLocalStartTime(start)
//            .build();
//
//    List<Event> list = cal.getMatchingEvents(print);
//    assertEquals(list.get(0).getSubject(), "International Conference");
//    assertEquals(list.get(0).getStartDate(), start);
//    assertEquals(list.get(0).getEndDate(), end);
//    assertEquals(list.size(), 1);
//
//
//    start = DateUtils.stringToLocalDateTime("2025-03-01 09:00");
//
//    PrintCommandMetaDetails print2 = new PrintCommandMetaDetails.PrintEventMetaDetailsBuilder()
//            .addLocalStartTime(start)
//            .build();
//
//    list = cal.getMatchingEvents(print2);
//
//    assertEquals(list.get(0).getSubject(), "International Conference");
//    assertEquals(list.get(0).getStartDate(), start);
//    assertEquals(list.size(), 1);
//    cal.printEvents();
//  }
//
//  @Test
//  public void testSimpleCommand2() throws Exception {
//    ACalendar cal = new Calendar();
//    String command = "event  \"RE\" from 2025-03-01T09:00  to " +
//            "2025-03-01T10:00  repeats MTW for 5 times";
//    createCommand.execute(command, cal);
//
//    LocalDateTime start = DateUtils.stringToLocalDateTime("2025-03-03 09:00");
//    LocalDateTime end = DateUtils.stringToLocalDateTime("2025-03-03 10:00");
//
//    PrintCommandMetaDetails print = new PrintCommandMetaDetails.PrintEventMetaDetailsBuilder()
//            .addLocalEndTime(end)
//            .addLocalStartTime(start)
//            .build();
//
//    List<Event> list = cal.getMatchingEvents(print);
//    assertEquals(list.get(0).getSubject(), "RE");
//    assertEquals(list.get(0).getStartDate(), start);
//    assertEquals(list.size(), 1);
//  }
//
//  @Test
//  public void testSimpleCommand3() throws Exception {
//    ACalendar cal = new Calendar();
//    String command = "event  \"RE\" from 2025-03-01T09:00  to " +
//            "2025-03-01T10:00  repeats MTW for 5 times";
//    createCommand.execute(command, cal);
//
//    command = "event \"International Conference\" from 2025-03-01T09:00 to 2025-03-03T13:00";
//    createCommand.execute(command, cal);
//
//    cal.printEvents();
//    /* LocalDateTime start = DateUtils.stringToLocalDateTime("2025-03-03 09:00");
//    LocalDateTime end = DateUtils.stringToLocalDateTime("2025-03-03 10:00");
//
//    PrintEventMetaDetails print = new PrintEventMetaDetails.PrintEventMetaDetailsBuilder()
//            .addLocalEndTime(end)
//            .addLocalStartTime(start)
//            .build();
//
//    List<AEvent> list = cal.getMatchingEvents(print);
//    assertEquals(list.get(0).getSubject(),"RE");
//    assertEquals(list.get(0).getStartDate(),start);
//    assertEquals(list.size(),1); */
//  }
//
//  @Test
//  public void dateUtil() {
//    System.out.println(DateUtils.stringToLocalDateTime("2025-03-04 00:00"));
//
//  }

  // Command Errors

  @Test
  public void missingEventKeyWord() throws Exception {
    try {
      command = "evnt --autoDecline \"Meeting\" from 2025-03-01T09:00 to 2025-03-01T10:00";
      createCommand.execute(command, cal);
    }
    catch (Exception e){
      assertEquals(e.getMessage(),"error :Must start with create event");
    }
  }

  @Test
  public void autoDeclineMisplaced() throws Exception {
    try {
      command = "event \"Meeting\" from 2025-03-01T09:00 to 2025-03-01T10:00 --autoDecline";
      createCommand.execute(command, cal);
    }
    catch (Exception e){
      assertEquals(e.getMessage(),"error :--autoDecline must appear immediately after create event");
    }
  }

  @Test
  public void fromMissing() throws Exception {
    try {
      command = "event \"Hello\" 2025-03-01T09:00 to 2025-03-01T10:00";
      createCommand.execute(command, cal);
    }
    catch (Exception e){
      assertEquals(e.getMessage(),"error :Missing 'from' or 'on' keyword.");
    }
  }
  @Test
  public void toMissing() throws Exception {
    try {
      command = "event \"Hello\" from 2025-03-01T09:00 2025-03-01T10:00";
      createCommand.execute(command, cal);
    }
    catch (Exception e){
      assertEquals(e.getMessage(),"error :from must be followed by to date");
    }
  }
  @Test
  public void missingFromDate() throws Exception {
    try {
      command = "event \"Hello\" from to 2025-03-01T10:00";
      createCommand.execute(command, cal);
    }
    catch (Exception e){
      assertEquals(e.getMessage(),"error :Does not match expected format.");
    }
  }
  @Test
  public void missingToDate() throws Exception {
    try {
      command = "event \"Hello\" from 2025-03-01T10:00 to";
      createCommand.execute(command, cal);
    }
    catch (Exception e){
      assertEquals(e.getMessage(),"error :from must be followed by to date");
    }
  }

  @Test
  public void invalidDate() throws Exception {
    try {
      command = "event \"Hello\" from 2025-03-01x10:00 to 2025-03-01T11:00";
      createCommand.execute(command, cal);
    }
    catch (Exception e){
      assertEquals(e.getMessage(),"error :Does not match expected format.");
    }
  }
  @Test
  public void invalidDate2() throws Exception {
    try {
      command = "event \"Hello\" from 2025-03-01 to 2025-03-01T11:00";
      createCommand.execute(command, cal);
    }
    catch (Exception e){
      assertEquals(e.getMessage(),"error :Does not match expected format.");
    }
  }

  @Test
  public void recurringEventcommand() throws Exception {
    try {
      command = "event \"Hello\" from 2025-03-01T10:00 to 2025-03-01T11:00 repeats MT for N times";
      createCommand.execute(command, cal);
    }
    catch (Exception e){
      assertEquals(e.getMessage(),"error :'for' must be followed by a valid number of times.");
    }
  }

  @Test
  public void recurringEventcommand2() throws Exception {
    try {
      command = "event \"Hello\" from 2025-03-01T10:00 to 2025-03-01T11:00 repeats MT? for 5 times";
      createCommand.execute(command, cal);
    }
    catch (Exception e){
      assertEquals(e.getMessage(),"error :Does not match expected format.");
    }
  }


  @Test
  public void recurringEventcommand3() throws Exception {
    try {
      command = "event \"Hello\"  2025-03-01T10:00 to 2025-03-01T11:00 repeats MT for 5 times";
      createCommand.execute(command, cal);
    }
    catch (Exception e){
      assertEquals(e.getMessage(),"error :Missing 'from' or 'on' keyword.");
    }
  }

  @Test
  public void recurringEventcommand4() throws Exception {
    try {
      command = "event \"Hello\" from 2025-03-01T10:00 repeats MT for 5 times";
      createCommand.execute(command, cal);
    }
    catch (Exception e){
      assertEquals(e.getMessage(),"error :from must be followed by to date");
    }
  }

  @Test
  public void recurringEventcommand5() throws Exception {
    try {
      command = "event \"Hello\"  from 2025-03-01T10:00 to 2025-03-01T11:00 repeats MT";
      createCommand.execute(command, cal);
    }
    catch (Exception e){
      assertEquals(e.getMessage(),"error :'repeats' must be followed by 'for <N> times' or 'until <date>'.");
    }
  }

  @Test
  public void recurringEventcommand6() throws Exception {
    try {
      command = "event \"Hello\"  from 2025-03-01T10:00 to 2025-03-01T11:00 for 5 times";
      createCommand.execute(command, cal);
    }
    catch (Exception e){
      assertEquals(e.getMessage(),"error :Does not match expected format.");
    }
  }


  @Test
  public void recurringEventcommand7() throws Exception {
    try {
      command = "event --autoDecline \"Hello\" from 2025-03-01T10:00 to 2025-03-01T11:00 " +
              "repeats M until";

      createCommand.execute(command, cal);
    }
    catch (Exception e){
      assertEquals(e.getMessage(),"error :'until' must be followed by a valid date.");
    }
  }

  @Test
  public void recurringEventcommand8() throws Exception {
    try {
      command = "event --autoDecline \"Hello\" from 2025-03-01T10:00 to 2025-03-01T11:00 " +
              "repeats  until 2025-03-025T11:00";

      createCommand.execute(command, cal);
    }
    catch (Exception e){
      assertEquals(e.getMessage(),"error :Does not match expected format.");
    }
  }


  @Test
  public void recurringEventcommand9() throws Exception {
    try {
      command = "event  \"Hello\" from 2025-03-01T10:00 to 2025-03-01T11:00 " +
              "repeats  MT until";

      createCommand.execute(command, cal);
    }
    catch (Exception e){
      assertEquals(e.getMessage(),"error :'until' must be followed by a valid date.");
    }
  }

  @Test
  public void eventMissing() throws Exception {
    try {
      command = "--autoDecline \"Hello world\" on 2025-03-01T11:00";
      createCommand.execute(command, cal);
    }
    catch (Exception e){
      assertEquals(e.getMessage(),"error :Must start with create event");
    }
  }

  @Test
  public void eventNameMissing() throws Exception {
    try {
      command = "event on 2025-03-01T11:00";
      createCommand.execute(command, cal);
    }
    catch (Exception e){
      assertEquals(e.getMessage(),"error :Does not match expected format.");
    }
  }

  @Test
  public void emptyCommand() throws Exception {
    try {
      command = " ";
      createCommand.execute(command, cal);
    }
    catch (Exception e){
      assertEquals(e.getMessage(),"error :Must start with create event");
    }
  }
  @Test
  public void recError() throws Exception {
    try {
      command = "event \"Hello\" on 2025-03-01 repeats MT\n";
      createCommand.execute(command, cal);
    }
    catch (Exception e){
      assertEquals(e.getMessage(),"error :'repeats' must be followed by 'for <N> times' or 'until <date>'.");
    }
  }

  @Test
  public void missingT() throws Exception {
    try {
      command = "event \"Conference Call\" from 2025-03-10 14:30 to 2025-03-10T15:30";
      createCommand.execute(command, cal);
    }
    catch (Exception e){
      assertEquals(e.getMessage(),"error :Does not match expected format.");
    }
  }

  @Test
  public void missingQuotesInEventName() throws Exception {
    try {
      command = "event Team Meeting from 2025-03-10T14:30 to 2025-03-10T15:30";
      createCommand.execute(command, cal);
    }
    catch (Exception e){
      assertEquals(e.getMessage(),"error :Does not match expected format.");
    }
  }
  @Test
  public void incorrectDateFormat() throws Exception {
    try {
      command = "event \"Project Review\" from 03-10-2025T14:30 to 03-10-2025T15:30";
      createCommand.execute(command, cal);
    }
    catch (Exception e){
      assertEquals(e.getMessage(),"error :Does not match expected format.");
    }
  }
  @Test
  public void invalidDAte() throws Exception {
    try {
      command = "event \"Dinner\" from 2025-04-20T19:00 to 2025-04-20";
      createCommand.execute(command, cal);
    }
    catch (Exception e){
      assertEquals(e.getMessage(),"error :Does not match expected format.");
    }
  }
  @Test
  public void invalidHourInTime() throws Exception {
    try {
      command = "event \"Breakfast\" from 2025-04-20T8:00 to 2025-04-20T09:00";
      createCommand.execute(command, cal);
    }
    catch (Exception e){
      assertEquals(e.getMessage(),"error :Does not match expected format.");
    }
  }
  @Test
  public void fromAndToSwapOrder() throws Exception {
    try {
      command = "event \"Sprint Planning\" to 2025-03-10T14:30 from 2025-03-10T15:30 ";
      createCommand.execute(command, cal);
    }
    catch (Exception e){
      assertEquals(e.getMessage(),"error :Does not match expected format.");
    }
  }
  @Test
  public void startTimeAfterEndTime() throws Exception {
    try {
      command = "event \"Yoga Session\" from 2025-06-15T10:00 to 2025-06-15T09:00";
      createCommand.execute(command, cal);
    }
    catch (Exception e){
      assertEquals(e.getMessage(),"end date cannot be before start date");
    }
  }

  @Test
  public void missingToKeyword() throws Exception {
    try {
      command = "event \"Call with Client\" from 2025-09-30T16:00 2025-09-30T17:00 ";
      createCommand.execute(command, cal);
    }
    catch (Exception e){
      assertEquals(e.getMessage(),"error :from must be followed by to date");
    }
  }


  @Test
  public void extraWords() throws Exception {
    try {
      command = "event \"Game Night\" from 2025-10-10T18:30 to 2025-10-10T22:00 at John's Place";
      createCommand.execute(command, cal);
    }
    catch (Exception e){
      assertEquals(e.getMessage(),"error :Does not match expected format.");
    }
  }
  @Test
  public void specialChar() throws Exception {
    try {
      command = "event Project@Review from 2025-06-05T13:00 to 2025-06-05T14:00";
      createCommand.execute(command, cal);
    }
    catch (Exception e){
      assertEquals(e.getMessage(),"error :Does not match expected format.");
    }
  }
  @Test
  public void invalidMonth() throws Exception {
    try {
      command = "event \"Checkup\" from 2025-13-05T10:00 to 2025-13-05T11:00";
      createCommand.execute(command, cal);
    }
    catch (Exception e){
      String msg = "Text '2025-13-05 10:00' could not be parsed: Invalid value for MonthOfYear (valid values 1 - 12): 13";
      assertEquals(e.getMessage(),msg);
    }
  }
  @Test
  public void invalidDay() throws Exception {
    try {
      command = "event \"Checkup\" from 2025-02-33T10:00 to 2025-02-31T11:00";
      createCommand.execute(command, cal);
    }
    catch (Exception e){
      String msg = "Text '2025-02-33 10:00' could not be parsed: Invalid value for DayOfMonth (valid values 1 - 28/31): 33";
      assertEquals(e.getMessage(),msg);
    }
  }

  @Test
  public void nonNumericCharactersInDate() throws Exception {
    try {
      command = "event \"Training\" from 2025-0A-10T09:00 to 2025-0A-10T10:00";
      createCommand.execute(command, cal);
    }
    catch (Exception e){
      String msg = "error :Does not match expected format.";
      assertEquals(e.getMessage(),msg);
    }
  }

  @Test
  public void eventNameError() throws Exception {
    try {
      command = "event from 2025-09-20T12:00 to 2025-09-20T13:00";
      createCommand.execute(command, cal);
    }
    catch (Exception e){
      String msg = "error :Does not match expected format.";
      assertEquals(e.getMessage(),msg);
    }
  }
  @Test
  public void lowerCaseT() throws Exception {
    try {
      command = "event from 2025-09-20T12:00 to 2025-09-20T13:00";
      createCommand.execute(command, cal);
    }
    catch (Exception e){
      String msg = "error :Does not match expected format.";
      assertEquals(e.getMessage(),msg);
    }
  }
  @Test
  public void weekdayError() throws Exception {
    try {
      command = "event \"Gym\" from 2025-06-01T07:00 to 2025-06-01T08:00 repeats Monday for 5 times";
      createCommand.execute(command, cal);
    }
    catch (Exception e){
      String msg = "error :Does not match expected format.";
      assertEquals(e.getMessage(),msg);
    }
  }
  @Test
  public void incorrectUntilDate() throws Exception {
    try {
      command = "event \"Dance Class\" on 2025-07-15 repeats TWR until 15-10-2025";
      createCommand.execute(command, cal);
    }
    catch (Exception e){
      String msg = "error :'until' must be followed by a valid date.";
      assertEquals(e.getMessage(),msg);
    }
  }
  @Test
  public void nonNumericValuesInDate() throws Exception {
    try {
      command = "event \"Meeting\" from 2025-0X-15T14:00 to 2025-0X-15T15:00";
      createCommand.execute(command, cal);
    }
    catch (Exception e){
      String msg = "error :Does not match expected format.";
      assertEquals(e.getMessage(),msg);
    }
  }
  @Test
  public void add() throws Exception {
    try {
      command = "event \"Hello\" from 2025-09-20T12:00 to 2025-09-20T13:00";
      createCommand.execute(command, cal);
      cal.printEvents();
    }
    catch (Exception e){
      String msg = "error :Does not match expected format.";
      assertEquals(e.getMessage(),msg);
    }
  }




}