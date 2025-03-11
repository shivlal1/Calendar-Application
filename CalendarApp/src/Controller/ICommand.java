package Controller;

import Model.ICalendar;

public interface ICommand {
  public void execute(String commandArgs, ICalendar calendar) throws Exception;
}
