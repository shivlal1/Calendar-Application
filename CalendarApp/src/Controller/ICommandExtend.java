package controller;

import model.ICalendarExtended;

public interface ICommandExtend  {
 public void execute(String commandArgs, ICalendarExtended calendar) throws Exception;
}
