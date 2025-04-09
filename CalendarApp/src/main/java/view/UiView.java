package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface UiView {
  void setListener(ActionListener actionListener);

  public void goToNextMonth(ActionListener actionListener);

  public void display();

  public void goToPreviousMonth(ActionListener actionListener);

  public void changeCalendarInDropDown(ActionListener actionListener);

  public boolean isAddCalendar();

  public void setCalendar(String calendar, String timeZone);

  public String[] getCalendarDetails();

  public void showAddCalendarinUI();

  public Map<String, Object> getUserShowEventChoice(LocalDate date, List<Map<String, Object>> dayEvents);

  public String[] getSelectedDate(ActionEvent e);

  public void searchEventsToEdit();

  public Map<String, Object> getEventsToBeEditedValues();

  public Map<String, Object> getNewPropertyAndValue();
}
