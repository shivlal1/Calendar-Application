package view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.swing.*;

public class JFrameView extends JFrame implements UiView {
  private JFrame frame;
  private JPanel calendarPanel;
  private JLabel monthLabel;
  private JComboBox<String> calendarDropdown;
  private Map<String, Color> calendars;
  private Map<LocalDate, List<String>> events;
  private YearMonth currentMonth;
  private String selectedCalendar;
  private JButton searchButton;
  private JLabel nameLabel;
  private JPanel topPanel;
  private JButton prevButton;
  private JButton nextButton;
  private JButton editAcrossCalendar;
  private String newCalendarName;
  private String newCalendarTimeZone;
  private JButton dayButton;
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

  public JFrameView() {
    rand = new Random();
    frame = new JFrame("Calendar App");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(600, 600);
    frame.setLocationRelativeTo(null);
    frame.setLayout(new BorderLayout());
    currentMonth = YearMonth.now();
    events = new HashMap<>();
    frame.add(getNorthPanel(), BorderLayout.NORTH);
    calendarPanel = new JPanel();
    updateButton = new JButton("Update All Matches");
    updateButton.setActionCommand("update value");
    frame.add(calendarPanel, BorderLayout.CENTER);
    frame.add(getBottomPanel(), BorderLayout.SOUTH);
  }

  private JPanel getNorthPanel() {
    JPanel wrapper = new JPanel();
    wrapper.setLayout(new BorderLayout());
    wrapper.add(getTopPanel(), BorderLayout.NORTH);
    wrapper.add(getCalendarMetaPanel(), BorderLayout.SOUTH);
    return wrapper;
  }

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

  private JPanel getBottomPanel() {
    JPanel bottomPanel = new JPanel();
    bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
    bottomPanel.add(getErrorPanel());
    bottomPanel.add(getImportExportPanel());
    return bottomPanel;
  }

  private JPanel getImportExportPanel() {
    JPanel panel = new JPanel();
    importButton = new JButton("Import into current calendar with csv");
    importButton.setActionCommand("Import CSV");
    panel.add(importButton);
    exportButton = new JButton(" Export currenr cal csv");
    exportButton.setActionCommand("Export CSV");
    panel.add(exportButton);
    return panel;
  }

  private JScrollPane getErrorPanel() {
    messageArea = new JTextArea(3, 20);
    messageArea.setEditable(false);
    messageArea.setLineWrap(true);
    messageArea.setWrapStyleWord(true);
    messageArea.setForeground(Color.RED); // Set color for error messages
    JScrollPane scroll = new JScrollPane(messageArea);
    return scroll;
  }

  public String[] getSelectedDate(ActionEvent e) {
    String day = ((JButton) e.getSource()).getText();
    String currentMonthInString = currentMonth.toString();
    String spliIntoYearAndMonth[] = currentMonthInString.split("-");
    String finalDate[] = {day, spliIntoYearAndMonth[1], spliIntoYearAndMonth[0]};
    return finalDate;
  }

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

  @Override
  public void goToNextMonth(ActionListener actionEvent) {
    changeMonth(1, actionEvent);
  }

  public void goToPreviousMonth(ActionListener actionEvent) {
    changeMonth(-1, actionEvent);
  }

  @Override
  public void display() {
    frame.setVisible(true);
  }

  @Override
  public void changeCalendarInDropDown(ActionListener actionListener) {
    changeCalendar(actionListener);
  }

  @Override
  public void setCalendarForGUI(String calendarName, String newCalendarTimeZone) {
    selectedCalendar = calendarName;
    calendarValueLabel.setText(calendarName);
    timezoneValueLabel.setText(newCalendarTimeZone);
    calendarPanel.setBackground(calendars.get(selectedCalendar));
  }

  @Override
  public String[] getCalendarDetails() {
    String[] details = {newCalendarName, newCalendarTimeZone};
    return details;
  }

  private void updateCalendar(ActionListener actionListener) {
    calendarPanel.removeAll();
    calendarPanel.setLayout(new GridLayout(0, 7));
    monthLabel.setText(currentMonth.getMonth() + " " + currentMonth.getYear());
    calendarPanel.setBackground(calendars.get(selectedCalendar));
    for (int day = 1; day <= currentMonth.lengthOfMonth(); day++) {
      dayButton = new JButton(String.valueOf(day));
      dayButton.setActionCommand("Add Event");
      dayButton.addActionListener(actionListener);
      calendarPanel.add(dayButton);
    }
    frame.revalidate();
    frame.repaint();
  }

  private void changeMonth(int offset, ActionListener listener) {
    currentMonth = currentMonth.plusMonths(offset);
    updateCalendar(listener);
  }

  public boolean shouldAddCalendar() {
    String selected = (String) calendarDropdown.getSelectedItem();
    if (selected.equals("+")) {
      return true;
    }
    return false;
  }

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
        //calendarValueLabel.setText(newCalendarName);
        //timezoneValueLabel.setText(newCalendarTimeZone);
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

  @Override
  public Map<String, Object> showAddEventDialog(LocalDate date, List<Map<String, Object>> dayEvents, String eventAsString) {
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
      metaDeta = showEventForm(date, dayEvents);
    }
    return metaDeta;
  }


  public String getChangedCalName() {
    selectedCalendar = (String) calendarDropdown.getSelectedItem();
    return selectedCalendar;
  }

  private void changeCalendar(ActionListener listener) {
    updateCalendar(listener);
  }

  public void showMessage(String errorMessage) {
    clearMessage();
    messageArea.setText(errorMessage);
  }

  public void clearMessage() {
    messageArea.setText("");
  }

  public HashMap<String, Object> getEditPropertyValuesFromGUI() {
    HashMap<String, Object> metaDeta = new HashMap<>();
    String property = properToBeEdited.getText().trim();
    String newValue = newPropertyValue.getText().trim();
    metaDeta.put("property", property);
    metaDeta.put("newValue", newValue);
    return metaDeta;
  }

  public void clearSearchPanel() {
    eventToBeEditedName.setText("");
    eventToBeEditedStartDate.setText("");
    eventToBeEditedEndDate.setText("");
    properToBeEdited.setText("");
    newPropertyValue.setText("");
  }

  public void closeSearchPanel() {
    Window window = SwingUtilities.getWindowAncestor(updateButton);
    if (window != null) {
      window.dispose();
    }
  }

  private JPanel getSearchEventsToEditPanel() {
    JPanel inputPanel = new JPanel(new GridLayout(5, 2, 10, 10));
    inputPanel.setBorder(BorderFactory.createTitledBorder(""));
    nameLabel = new JLabel("Name:");
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

  private JLabel getDateInfo() {
    JLabel infoLabel = new JLabel("Date format Eg: 2025-04-31T01:00");
    infoLabel.setHorizontalAlignment(SwingConstants.CENTER);
    return infoLabel;
  }

  private Map<String, Object> showEventForm(LocalDate date, List<Map<String, Object>> dayEvents) {
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
      String eventName = nameField.getText().trim().equals("") ? null : nameField.getText().trim();
      String startDate = startDateField.getText().trim().equals("") ? null : startDateField.getText().trim();
      String endDate = endDateField.getText().trim().equals("") ? null : endDateField.getText().trim();
      String repeats = repeatsField.getText().trim().equals("") ? null : repeatsField.getText().trim();
      String forValue = forField.getText().trim().equals("") ? null : forField.getText().trim();
      String untilValue = untilField.getText().trim().equals("") ? null : untilField.getText().trim();
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

  private JPanel getTopPanel() {
    calendars = new HashMap<>();
    calendars.put("default", Color.BLUE);
    selectedCalendar = "default";
    newCalendarTimeZone = ZoneId.systemDefault().toString();
    topPanel = new JPanel();
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

  public void displayEvents(String eventString) {
    resultArea.setText(eventString);
  }

  public void removeCalendarFromDropdown(String newCalendarName) {
    calendarDropdown.removeItem(newCalendarName);
    calendars.remove(newCalendarName);
  }
}
