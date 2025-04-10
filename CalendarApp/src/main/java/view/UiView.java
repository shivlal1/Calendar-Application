package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * The UiView interface defines the set of methods that any calendar UI view
 * should implement to support interaction with the user and communicate with
 * the controller in an MVC architecture.
 */
public interface UiView {

  /**
   * Sets the ActionListener for handling UI actions.
   *
   * @param actionListener the ActionListener to be set.
   */
  public void setActionListener(ActionListener actionListener);

  /**
   * Navigates to the next month in the calendar view.
   *
   * @param actionListener the ActionListener to handle the event.
   */
  public void goToNextMonth(ActionListener actionListener);

  /**
   * Makes the UI visible and ready for interaction.
   */
  public void display();

  /**
   * Navigates to the previous month in the calendar view.
   *
   * @param actionListener the ActionListener to handle the event.
   */
  public void goToPreviousMonth(ActionListener actionListener);

  /**
   * Sets the ActionListener for handling calendar selection from the dropdown.
   *
   * @param actionListener the ActionListener to handle the event.
   */
  public void changeCalendarInDropDown(ActionListener actionListener);

  /**
   * Determines whether the user intends to add a new calendar.
   *
   * @return true if a calendar should be added, false otherwise.
   */
  public boolean shouldAddCalendar();

  /**
   * Sets the calendar name and time zone to be displayed in the UI.
   *
   * @param calendar the name of the calendar.
   * @param timeZone the time zone associated with the calendar.
   */
  public void setCalendarForGUI(String calendar, String timeZone);

  /**
   * Retrieves calendar details entered by the user.
   *
   * @return an array of Strings containing calendar details such as name and time zone.
   */
  public String[] getCalendarDetails();

  /**
   * Displays a dialog to allow the user to add a new calendar.
   */
  public void showAddCalendarDialog();

  /**
   * Displays a dialog for adding an event and retrieves user input.
   *
   * @param date          the date for which the event is being added.
   * @param dayEvents     the list of existing events for that date.
   * @param eventAsString a string representation of the current events.
   * @return a Map containing the values for the new event.
   */
  public Map<String, Object> showAddEventDialog(LocalDate date,
                                                List<Map<String, Object>> dayEvents,
                                                String eventAsString);

  /**
   * Retrieves the selected date from the UI when an event is triggered.
   *
   * @param e the ActionEvent that triggered the date selection.
   * @return a String array containing the selected date information.
   */
  public String[] getSelectedDate(ActionEvent e);

  /**
   * Initiates a search for events that the user wants to edit.
   */
  public void searchEventsToEdit();

  /**
   * Retrieves the updated event values from the GUI.
   *
   * @return a Map containing the edited event values.
   */
  public Map<String, Object> getEditEventValuesFromGUI();

  /**
   * Retrieves updated property values for an event.
   * The property could be name, location, description, startDate,endDate,isPublic.
   *
   * @return a Map containing the edited event property values.
   */
  public Map<String, Object> getEditPropertyValuesFromGUI();


  /**
   * Clears the search panel values in UI component.
   */
  public void clearSearchPanel();

  /**
   * Closes the search panel UI component.
   */
  public void closeSearchPanel();

  /**
   * Displays events in the UI.
   *
   * @param eventAsString the string representation of the events to be displayed.
   */
  public void displayEvents(String eventAsString);


  /**
   * Retrieves the new calendar name if it has been changed by the user.
   *
   * @return the changed calendar name.
   */
  public String getChangedCalName();


  /**
   * Removes a calendar from the dropdown UI component.
   *
   * @param calendarName the name of the calendar to be removed.
   */
  public void removeCalendarFromDropdown(String calendarName);

  /**
   * Shows a message to the user.
   *
   * @param message the message to be displayed.
   */
  public void showMessage(String message);


  /**
   * Clears any currently displayed messages in the UI.
   */
  public void clearMessage();
}
