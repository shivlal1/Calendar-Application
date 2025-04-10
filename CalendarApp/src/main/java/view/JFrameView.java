package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JTextArea;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.BorderFactory;
import java.awt.Font;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JScrollPane;
import java.awt.GridLayout;
import javax.swing.JOptionPane;
import java.awt.Window;
import javax.swing.JDialog;
import javax.swing.SwingConstants;
import java.awt.Dimension;

/**
 * JFrameView implements the UiView interface to provide a Swing-based GUI for application.
 * It supports multiple calendars, navigating between months, and editing properties.
 * It also supports viewing events on particular day.
 */
public class JFrameView extends JFrame implements UiView {
  private JFrame frame;
  private JPanel calendarPanel;
  private JLabel monthLabel;
  private JComboBox<String> calendarDropdown;
  private Map<String, Color> calendars;
  private YearMonth currentMonth;
  private String selectedCalendar;
  private JButton searchButton;
  private JButton prevButton;
  private JButton nextButton;
  private JButton editAcrossCalendar;
  private String newCalendarName;
  private String newCalendarTimeZone;
  private JTextField eventToBeEditedName;
  private JTextField eventToBeEditedStartDate;
  private JTextField eventToBeEditedEndDate;
  private JTextField properToBeEdited;
  private JTextField newPropertyValue;
  private JButton updateButton;
  private JTextField untilField;
  private JTextField repeatsField;
  private JTextField forField;
  private JTextField nameField;
  private JTextField startDateField;
  private JTextField endDateField;
  private JTextArea resultArea;
  private JCheckBox recurringCheck;
  private Random rand;
  private JTextArea messageArea;
  private JButton importButton;
  private JButton exportButton;
  private JLabel calendarValueLabel;
  private JLabel timezoneValueLabel;

  /**
   * Constructs the JFrameView, sets up the main frame layout, size, realtive position.
   */
  public JFrameView() {
    rand = new Random();
    frame = new JFrame("Calendar App");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(600, 600);
    frame.setLocationRelativeTo(null);
    frame.setLayout(new BorderLayout());
    currentMonth = YearMonth.now();
    frame.add(getNorthPanel(), BorderLayout.NORTH);
    calendarPanel = new JPanel();
    updateButton = new JButton("Update All Matches");
    updateButton.setActionCommand("update value");
    frame.add(calendarPanel, BorderLayout.CENTER);
    frame.add(getBottomPanel(), BorderLayout.SOUTH);
  }

  /**
   * Returns the north panel of the frame which contains top controls and calendar meta info.
   * The calendar meta info is used to display the calendar name and timezone.
   *
   * @return panel with GUI top panel and Calendar details panel.
   */
  private JPanel getNorthPanel() {
    JPanel wrapper = new JPanel();
    wrapper.setLayout(new BorderLayout());
    wrapper.add(getTopPanel(), BorderLayout.NORTH);
    wrapper.add(getCalendarMetaPanel(), BorderLayout.SOUTH);
    return wrapper;
  }

  /**
   * Creates the panel displaying calendar name and timezone.
   *
   * @return a panel bold font and white background.
   */
  private JPanel getCalendarMetaPanel() {
    JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    panel.setBackground(Color.WHITE);
    panel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
    JLabel calendarLabel = new JLabel("Calendar:");
    calendarLabel.setFont(calendarLabel.getFont().deriveFont(Font.BOLD));
    calendarLabel.setForeground(new Color(0, 102, 204));
    calendarValueLabel = new JLabel(selectedCalendar);
    JLabel timezoneLabel = new JLabel("Timezone:");
    timezoneLabel.setFont(timezoneLabel.getFont().deriveFont(Font.BOLD));
    timezoneLabel.setForeground(new Color(0, 102, 204));
    timezoneValueLabel = new JLabel(newCalendarTimeZone != null ? newCalendarTimeZone : "");
    panel.add(calendarLabel);
    panel.add(calendarValueLabel);
    panel.add(Box.createHorizontalStrut(30));
    panel.add(timezoneLabel);
    panel.add(timezoneValueLabel);
    return panel;
  }

  /**
   * Constructs the bottom panel that contains the message panel and import/export options.
   *
   * @return panel which contains import and export buttons.
   */
  private JPanel getBottomPanel() {
    JPanel bottomPanel = new JPanel();
    bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
    bottomPanel.add(messagePanel());
    bottomPanel.add(getImportExportPanel());
    return bottomPanel;
  }

  /**
   * Creating the import/export panel with relevant buttons and it's action command.
   *
   * @return a panel with import and export button.
   */
  private JPanel getImportExportPanel() {
    JPanel panel = new JPanel();
    importButton = new JButton("Import into current calendar with csv");
    importButton.setActionCommand("Import CSV");
    panel.add(importButton);
    exportButton = new JButton(" Export current cal csv");
    exportButton.setActionCommand("Export CSV");
    panel.add(exportButton);
    return panel;
  }

  /**
   * Constructs and returns the panel used for displaying messages in the GUI.
   *
   * @return a panel with red background.
   */
  private JScrollPane messagePanel() {
    messageArea = new JTextArea(3, 20);
    messageArea.setEditable(false);
    messageArea.setLineWrap(true);
    messageArea.setWrapStyleWord(true);
    messageArea.setForeground(Color.RED);
    JScrollPane scroll = new JScrollPane(messageArea);
    return scroll;
  }


  /**
   * Extracts and returns the selected date from a button click event.
   *
   * @return an array with first day, year and month values in string.
   */
  public String[] getSelectedDate(ActionEvent e) {
    String day = ((JButton) e.getSource()).getText();
    String currentMonthInString = currentMonth.toString();
    String[] spliIntoYearAndMonth = currentMonthInString.split("-");
    String[] finalDate = {day, spliIntoYearAndMonth[1], spliIntoYearAndMonth[0]};
    return finalDate;
  }

  /**
   * Binds action listeners to all interactive UI components.
   */
  @Override
  public void setActionListener(ActionListener actionListener) {
    nextButton.addActionListener(actionListener);
    prevButton.addActionListener(actionListener);
    calendarDropdown.addActionListener(actionListener);
    updateCalendar(actionListener);
    editAcrossCalendar.addActionListener(actionListener);
    updateButton.addActionListener(actionListener);
    searchButton.addActionListener(actionListener);
    importButton.addActionListener(actionListener);
    exportButton.addActionListener(actionListener);
  }

  /**
   * Navigates to the next month and updates the view.
   */
  @Override
  public void goToNextMonth(ActionListener actionEvent) {
    changeMonth(1, actionEvent);
  }

  /**
   * Navigates to the previous month and updates the view.
   */
  public void goToPreviousMonth(ActionListener actionEvent) {
    changeMonth(-1, actionEvent);
  }

  /**
   * Makes the application frame visible.
   */
  @Override
  public void display() {
    frame.setVisible(true);
  }

  /**
   * Binds action listener for calendar dropdown changes.
   */
  @Override
  public void changeCalendarInDropDown(ActionListener actionListener) {
    changeCalendar(actionListener);
  }

  /**
   * Updates the current selected calendar and its timezone display.
   */
  @Override
  public void setCalendarForGUI(String calendarName, String newCalendarTimeZone) {
    selectedCalendar = calendarName;
    calendarValueLabel.setText(calendarName);
    timezoneValueLabel.setText(newCalendarTimeZone);
    calendarPanel.setBackground(calendars.get(selectedCalendar));
  }


  /**
   * Returns the latest calendar details entered via dialog.
   *
   * @return string array with clendar name and timezone.
   */
  @Override
  public String[] getCalendarDetails() {
    String[] details = {newCalendarName, newCalendarTimeZone};
    return details;
  }

  /**
   * Updates the calendar panel with buttons representing days in the current month.
   */
  private void updateCalendar(ActionListener actionListener) {
    calendarPanel.removeAll();
    calendarPanel.setLayout(new GridLayout(0, 7));
    monthLabel.setText(currentMonth.getMonth() + " " + currentMonth.getYear());
    calendarPanel.setBackground(calendars.get(selectedCalendar));
    for (int day = 1; day <= currentMonth.lengthOfMonth(); day++) {
      JButton dayButton = new JButton(String.valueOf(day));
      dayButton.setActionCommand("Add Event");
      dayButton.addActionListener(actionListener);
      calendarPanel.add(dayButton);
    }
    frame.revalidate();
    frame.repaint();
  }

  /**
   * Changes the current view month by a given offset and refreshes UI.
   */
  private void changeMonth(int offset, ActionListener listener) {
    currentMonth = currentMonth.plusMonths(offset);
    updateCalendar(listener);
  }

  /**
   * Checks if the '+' calendar option is selected, indicating the user wants to add a new calendar.
   *
   * @return boolean value if + is selected or false.
   */
  public boolean shouldAddCalendar() {
    String selected = (String) calendarDropdown.getSelectedItem();
    return selected.equals("+");
  }

  /**
   * Displays a dialog to add a new calendar and updates the dropdown if added.
   */
  public void showAddCalendarDialog() {
    JTextField nameField = new JTextField();
    JTextField timezoneField = new JTextField();
    JPanel panel = new JPanel(new GridLayout(0, 1));
    panel.add(new JLabel("Calendar Name:"));
    panel.add(nameField);
    panel.add(new JLabel("TimeZone (e.g. America/New_York):"));
    panel.add(timezoneField);
    int result = JOptionPane.showConfirmDialog(frame, panel, "New Calendar",
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

    if (result == JOptionPane.OK_OPTION) {
      newCalendarName = nameField.getText().trim();
      newCalendarTimeZone = timezoneField.getText().trim();
      if (!newCalendarName.isEmpty() && !calendars.containsKey(newCalendarName)) {
        int r = rand.nextInt(256);
        int g = rand.nextInt(256);
        int b = rand.nextInt(256);
        Color calColor = new Color(r, g, b);
        calendars.put(newCalendarName, calColor);
        calendarDropdown.addItem(newCalendarName);
        selectedCalendar = newCalendarName;
        calendarDropdown.setSelectedItem(newCalendarName);
      } else {
        calendarDropdown.setSelectedItem(selectedCalendar);
      }
    } else {
      calendarValueLabel.setText(newCalendarName);
      timezoneValueLabel.setText(newCalendarTimeZone);
      calendarDropdown.setSelectedItem(selectedCalendar);
    }
  }

  /**
   * Displays the event dialog for a specific day, optionally showing existing events.
   *
   * @return map with key value pairs about the event details.
   */
  @Override
  public Map<String, Object> showAddEventDialog(LocalDate date,
                                                List<Map<String, Object>> dayEvents,
                                                String eventAsString) {
    Map<String, Object> metaDeta = null;
    Object[] options = {"Add New Event", "Cancel"};
    int choice = JOptionPane.showOptionDialog(
            frame,
            eventAsString,
            "Events on " + date,
            JOptionPane.YES_NO_CANCEL_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            options,
            options[0]
    );

    if (choice == 0) {
      metaDeta = showEventForm();
    }
    return metaDeta;
  }


  /**
   * Retrieves the calendar name currently selected in the dropdown.
   *
   * @return string value of the calendar.
   */
  public String getChangedCalName() {
    selectedCalendar = (String) calendarDropdown.getSelectedItem();
    return selectedCalendar;
  }

  /**
   * Triggers the logic to update calendar when dropdown is changed.
   */
  private void changeCalendar(ActionListener listener) {
    updateCalendar(listener);
  }

  /**
   * Displays a message in the message area.
   */
  public void showMessage(String errorMessage) {
    clearMessage();
    messageArea.setText(errorMessage);
  }

  /**
   * Clears any message from the message area.
   */
  public void clearMessage() {
    messageArea.setText("");
  }

  /**
   * Gets edited property and its new value from the GUI.
   *
   * @return hashmap with key value pairs containing the property and new value.
   */
  public Map<String, Object> getEditPropertyValuesFromGUI() {
    HashMap<String, Object> metaDeta = new HashMap<>();
    String property = properToBeEdited.getText().trim();
    String newValue = newPropertyValue.getText().trim();
    metaDeta.put("property", property);
    metaDeta.put("newValue", newValue);
    return metaDeta;
  }

  /**
   * Clears search/edit text fields in the search dialog box.
   */
  @Override
  public void clearSearchPanel() {
    eventToBeEditedName.setText("");
    eventToBeEditedStartDate.setText("");
    eventToBeEditedEndDate.setText("");
    properToBeEdited.setText("");
    newPropertyValue.setText("");
  }

  /**
   * Closes the search/edit dialog.
   */
  @Override
  public void closeSearchPanel() {
    Window window = SwingUtilities.getWindowAncestor(updateButton);
    if (window != null) {
      window.dispose();
    }
  }

  /**
   * Constructs the panel for entering search parameters to find events.
   *
   * @return a JPanel with name, start & end date with search button.
   */
  private JPanel getSearchEventsToEditPanel() {
    JPanel inputPanel = new JPanel(new GridLayout(5, 2, 10, 10));
    inputPanel.setBorder(BorderFactory.createTitledBorder(""));
    JLabel nameLabel = new JLabel("Name:");
    eventToBeEditedName = new JTextField(12);
    JLabel startDate = new JLabel("Start Date:");
    eventToBeEditedStartDate = new JTextField(12);
    JLabel endDate = new JLabel("End Date:");
    eventToBeEditedEndDate = new JTextField(12);
    inputPanel.add(nameLabel);
    inputPanel.add(eventToBeEditedName);
    inputPanel.add(startDate);
    inputPanel.add(eventToBeEditedStartDate);
    inputPanel.add(endDate);
    inputPanel.add(eventToBeEditedEndDate);
    inputPanel.add(searchButton);
    inputPanel.add(getDateInfo());
    return inputPanel;
  }

  /**
   * Gets event name, start, and end date from the edit panel.
   *
   * @return key value pairs which contains properties to search for edit events.
   */
  @Override
  public Map<String, Object> getEditEventValuesFromGUI() {
    Map<String, Object> metaData = new HashMap<>();
    String eventName = eventToBeEditedName.getText().trim();
    String startDate = eventToBeEditedStartDate.getText().trim();
    String endTime = eventToBeEditedEndDate.getText().trim();
    if (eventName.equals("")) {
      eventName = null;
    }
    if (startDate.equals("")) {
      startDate = null;
    }
    if (endTime.equals("")) {
      endTime = null;
    }
    metaData.put("eventName", eventName);
    metaData.put("StartTime", startDate);
    metaData.put("endTime", endTime);
    return metaData;
  }


  /**
   * Opens a dialog that allows the user to search for and edit events.
   */
  @Override
  public void searchEventsToEdit() {
    JDialog dialog = new JDialog(frame, "Search and edit events", true);
    dialog.setSize(600, 500);
    dialog.setLayout(new BorderLayout());
    dialog.add(getSearchEventsToEditPanel(), BorderLayout.NORTH);
    resultArea = new JTextArea();
    resultArea.setEditable(false);
    resultArea.setLineWrap(true);
    resultArea.setWrapStyleWord(true);
    JScrollPane scrollPane = new JScrollPane(resultArea);
    dialog.add(scrollPane, BorderLayout.CENTER);
    JPanel updatePanel = new JPanel(new GridLayout(2, 2, 5, 5));
    properToBeEdited = new JTextField(); // name / description / location
    newPropertyValue = new JTextField();
    updatePanel.add(new JLabel("Property Name"));
    updatePanel.add(properToBeEdited);
    updatePanel.add(new JLabel("New value:"));
    updatePanel.add(newPropertyValue);
    JPanel bottomPanel = new JPanel(new BorderLayout());
    bottomPanel.add(updatePanel, BorderLayout.CENTER);
    bottomPanel.add(updateButton, BorderLayout.SOUTH);
    dialog.add(bottomPanel, BorderLayout.SOUTH);
    dialog.setLocationRelativeTo(frame);
    dialog.getRootPane().setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
    dialog.setVisible(true);
  }

  /**
   * Returns a panel containing recurring event options.
   *
   * @return JPanel with recurring values input fields.
   */
  private JPanel getRecurringPanel() {
    untilField = new JTextField(12);
    repeatsField = new JTextField(12);
    forField = new JTextField(12);
    JPanel recurringPanel = new JPanel(new GridLayout(0, 2, 5, 5));
    recurringPanel.setBorder(BorderFactory.createTitledBorder("Recurring Options"));
    recurringPanel.add(new JLabel("Until Date"));
    recurringPanel.add(untilField);
    recurringPanel.add(new JLabel("Repeats (weekdays eg. MT):"));
    recurringPanel.add(repeatsField);
    recurringPanel.add(new JLabel("For (number of times):"));
    recurringPanel.add(forField);
    recurringPanel.setVisible(false);
    return recurringPanel;
  }


  /**
   * Returns the main event input panel with name, start, and end fields.
   *
   * @return JPanel with basic event details input field.
   */
  private JPanel getMainPanel() {
    JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));
    panel.setBorder(BorderFactory.createTitledBorder("Event Details"));
    panel.add(new JLabel("Event Name:"));
    panel.add(nameField);
    panel.add(new JLabel("Start Date:"));
    panel.add(startDateField);
    panel.add(new JLabel("End Date:"));
    panel.add(endDateField);
    panel.add(new JLabel("Recurring:"));
    panel.add(recurringCheck);
    return panel;
  }

  /**
   * Returns a label with formatting guidance for date input fields.
   *
   * @return JLabel which info text data.
   */
  private JLabel getDateInfo() {
    JLabel infoLabel = new JLabel("Date format Eg: 2025-04-31T01:00");
    infoLabel.setHorizontalAlignment(SwingConstants.CENTER);
    return infoLabel;
  }

  /**
   * Opens a form to collect event data from the user, including recurrence.
   *
   * @return Map with values to form the event.
   */
  private Map<String, Object> showEventForm() {
    Map<String, Object> metaData = new HashMap<>();
    nameField = new JTextField(12);
    startDateField = new JTextField(12);
    endDateField = new JTextField(12);
    untilField = new JTextField(12);
    repeatsField = new JTextField(12);
    forField = new JTextField(12);
    recurringCheck = new JCheckBox("Recurring Event");
    JPanel recurringPanel = getRecurringPanel();
    recurringCheck.addItemListener(e -> recurringPanel.setVisible(recurringCheck.isSelected()));
    JPanel panel = getMainPanel();
    JPanel container = new JPanel(new BorderLayout());
    container.add(panel, BorderLayout.NORTH);
    container.add(recurringPanel, BorderLayout.CENTER);
    container.setPreferredSize(new Dimension(400, 320)); // compact!
    container.add(getDateInfo(), BorderLayout.SOUTH);

    int result = JOptionPane.showConfirmDialog(
            frame,
            container,
            "Add New Event",
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE
    );

    if (result == JOptionPane.OK_OPTION) {
      String eventName = nameField.getText().trim().equals("") ? null :
              nameField.getText().trim();
      String startDate = startDateField.getText().trim().equals("") ? null :
              startDateField.getText().trim();
      String endDate = endDateField.getText().trim().equals("") ? null :
              endDateField.getText().trim();
      String repeats = repeatsField.getText().trim().equals("") ? null :
              repeatsField.getText().trim();
      String forValue = forField.getText().trim().equals("") ? null :
              forField.getText().trim();
      String untilValue = untilField.getText().trim().equals("") ? null :
              untilField.getText().trim();
      metaData.put("subject", eventName);
      metaData.put("startDate", startDate);
      metaData.put("endDate", endDate);
      metaData.put("untilTime", untilValue);
      metaData.put("weekdays", repeats);
      metaData.put("forTimes", forValue);
      metaData.put("isRecurring", recurringCheck.isSelected());
    }
    return metaData;
  }


  /**
   * Creates and returns the top panel with navigation buttons and calendar selector.
   *
   * @return JPanel which contains <,> and cal drop down.
   */
  private JPanel getTopPanel() {
    calendars = new HashMap<>();
    calendars.put("default", Color.BLUE);
    selectedCalendar = "default";
    newCalendarTimeZone = ZoneId.systemDefault().toString();
    JPanel topPanel = new JPanel();
    prevButton = new JButton("<");
    nextButton = new JButton(">");
    nextButton.setActionCommand("Next Month");
    prevButton.setActionCommand("Previous Month");
    searchButton = new JButton("Search");
    searchButton.setActionCommand("Search");
    monthLabel = new JLabel();
    calendarDropdown = new JComboBox<>();
    calendarDropdown.setActionCommand("Drop down");
    calendarDropdown.addItem("+");
    for (String calendarName : calendars.keySet()) {
      calendarDropdown.addItem(calendarName);
    }
    calendarDropdown.setSelectedItem(selectedCalendar);
    editAcrossCalendar = new JButton("Edit Events across calendar");
    editAcrossCalendar.setActionCommand("Edit across calendar");
    topPanel.add(prevButton);
    topPanel.add(monthLabel);
    topPanel.add(nextButton);
    topPanel.add(calendarDropdown);
    topPanel.add(editAcrossCalendar);
    return topPanel;
  }

  /**
   * Displays events as text in the result area of the GUI.
   */
  @Override
  public void displayEvents(String eventString) {
    resultArea.setText(eventString);
  }

  /**
   * Removes a calendar entry from the dropdown and internal map.
   */
  @Override
  public void removeCalendarFromDropdown(String newCalendarName) {
    calendarDropdown.removeItem(newCalendarName);
    calendars.remove(newCalendarName);
  }
}
