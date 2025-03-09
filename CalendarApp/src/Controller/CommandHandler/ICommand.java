package Controller.CommandHandler;

import Model.Calendar.ACalendar;

// Command interface
public interface ICommand {
  public void execute(String commandArgs, ACalendar calendar);
  public void commandParser(String commandArgs);
}
