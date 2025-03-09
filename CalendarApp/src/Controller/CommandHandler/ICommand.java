package Controller.CommandHandler;

import Model.Calendar.ACalendar;

// Command interface
public interface ICommand {
  void execute(String commandArgs, ACalendar calendar);
}
