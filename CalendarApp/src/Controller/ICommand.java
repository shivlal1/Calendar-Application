package controller;

import model.ICalendar;

/**
 * This interface sets a base for the classes that implement commands
 * in a calendar system. It has a method for executing commands with given
 * arguments on a calendar object.
 */
public interface ICommand extends ICommandExtend {

  /**
   * Executes a command with the provided arguments on the specified calendar.
   *
   * @param commandArgs the command arguments as a string.
   * @param calendar    the ICalendar object on which the command will be executed.
   * @throws Exception if an error occurs during command execution.
   */
  public void execute(String commandArgs, ICalendar calendar) throws Exception;
}
