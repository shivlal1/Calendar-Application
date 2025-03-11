package Controller;

import Model.ACalendar;

public interface ICommand {
  public void execute(String commandArgs, ACalendar calendar) throws Exception;
}
