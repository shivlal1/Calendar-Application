package Controller.CommandHandler;

import Model.Calendar.ACalendar;

// Command interface
public interface ICommand {
  public void execute(String commandArgs, ACalendar calendar) throws Exception;

  public void commandParser(String commandArgs) throws Exception;
}
