package Controller.CommandHandler;

import Model.Calendar.ACalendar;

// Command interface
public interface ICommand {
  public void commandParser(String commandArgs) throws Exception;

  public void execute(String commandArgs, ACalendar calendar) throws Exception;
}
