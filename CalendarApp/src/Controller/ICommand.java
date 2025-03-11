package Controller;

import Model.Calendar.ACalendar;

public interface ICommand {
  public void execute(String commandArgs, ACalendar calendar) throws Exception;
}
