package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface UiView {
  public void setActionListener(ActionListener actionListener);

  public void goToNextMonth(ActionListener actionListener);

  public void display();

  public void goToPreviousMonth(ActionListener actionListener);

  public void changeCalendarInDropDown(ActionListener actionListener);

  public boolean shouldAddCalendar();

  public void setCalendarForGUI(String calendar, String timeZone);

  public String[] getCalendarDetails();

  public void showAddCalendarDialog();

  public Map<String, Object> showAddEventDialog(LocalDate date, List<Map<String, Object>> dayEvents, String eventAsString);

  public String[] getSelectedDate(ActionEvent e);

  public void searchEventsToEdit();

  public Map<String, Object> getEditEventValuesFromGUI();

  public Map<String, Object> getEditPropertyValuesFromGUI();

  public void clearSearchPanel();

  public void closeSearchPanel();

  public void displayEvents(String eventAsString);

  public String getChangedCalName();

  public void removeCalendarFromDropdown(String calendarName);

  public void showMessage(String message);

  public void clearMessage();
}
