package controller;

import model.ICalendarV2;

/**
 * This interface represents a manager for handling multiple calendars, handling operations
 * such as retrieving the active calendar, accessing calendars by name, and executing commands.
 */
public interface ICalendarManager {

  /**
   * This method gets the currently active calendar.
   *
   * @return The active ICalendarV2 instance.
   */
  public ICalendarV2 getActiveCalendar();

  /**
   * This method gets the calendar instance when given the name of it.
   *
   * @param calendarName the name of the calendar provided.
   * @return a ICalendarV2 instance.
   */
  public ICalendarV2 getCalendarByName(String calendarName);

  /**
   * This method executes a given calendar command based on the given arguments.
   *
   * @param commandArgs the arguments provided to the method of the command.
   * @throws Exception if the command format is invalid or if the execution fails.
   */
  public void execute(String commandArgs) throws Exception;

}
