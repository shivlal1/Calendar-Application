package Controller;

import Model.Calendar.ACalendar;

// Command interface
public interface ICommand {
  public void execute(String commandArgs, ACalendar calendar) throws Exception;
}
