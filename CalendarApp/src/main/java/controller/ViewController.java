package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.*;

import model.ICalendarV2;
import utils.DateUtils;
import view.UiView;

public class ViewController implements ActionListener {

  private ICalendarManagerV2 calendarManager;
  private UiView uiView;
  private ICalendarV2 calendarV2;
  private String editEventName;
  private LocalDateTime editEventStartDate;
  private LocalDateTime editEventEndDate;
  private String activeCalendarName;

  public ViewController(ICalendarManagerV2 calendarManager, UiView uiView) {
    this.calendarManager = calendarManager;
    this.uiView = uiView;
    uiView.display();
    uiView.setListener(this);
    activeCalendarName = "default";
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    calendarV2 = calendarManager.getCalendarByName(activeCalendarName);

    switch (e.getActionCommand()) {
      case "Next Month":
        uiView.goToNextMonth(this);
        break;
      case "Previous Month":
        uiView.goToPreviousMonth(this);
        break;
      case "Drop down":
        handleDropDown();
        break;
      case "Add Event":
        handleAddEvent(e);
        break;
      case "Edit across calendar":
        System.out.println("search events to edit");
        uiView.searchEventsToEdit();
        break;
      case "update value":
        handleUpdateEvent();
        break;
      case "Search":
        handleSearch();
        break;
      case "Import CSV":
        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
          File selectedFile = fileChooser.getSelectedFile();

          try (BufferedReader br = new BufferedReader(new FileReader(selectedFile))) {
            String line;
            br.readLine(); // Skip header

            while ((line = br.readLine()) != null) {
              // Split using regex to handle quoted commas
              String[] values = line.split("\\s*,\\s*(?=([^\"]*\"[^\"]*\")*[^\"]*$)", -1);

              // Clean up quotes and spaces
              for (int i = 0; i < values.length; i++) {
                values[i] = values[i].replaceAll("^\"|\"$", "").trim();
              }

              // Print each value
              System.out.println("Subject: " + values[0]);
              System.out.println("Start Date: " + values[1]);
              System.out.println("Start Time: " + values[2]);
              System.out.println("End Date: " + values[3]);
              System.out.println("End Time: " + values[4]);
              System.out.println("All Day Event: " + values[5]);
              System.out.println("Description: " + values[6]);
              System.out.println("Location: " + values[7]);
              System.out.println("Private: " + values[8]);
              System.out.println("----------------------------------");
            }

          } catch (IOException exception) {
            exception.printStackTrace();
          }

        }
        break;

      case "Export CSV" :
        break;
      case "Exit Button":
        System.exit(0);
        break;
    }
  }


  private Map<String, Object> getDataForCreateEvent(Map<String, Object> event) {
    event.put("autoDecline", true);
    String startDateAfterT = DateUtils.removeTinDateTime(event.get("startDate").toString());
    LocalDateTime startDate = DateUtils.stringToLocalDateTime(startDateAfterT);
    event.put("startDate", startDate);
    LocalDateTime endDate = null;
    if (event.get("endDate") != null) {
      String endDateAfterT = DateUtils.removeTinDateTime(event.get("endDate").toString());
      endDate = DateUtils.stringToLocalDateTime(endDateAfterT);
      event.put("isAllDay", false);
      event.put("endDate", endDate);
    } else {
      event.put("isAllDay", true);
      LocalDate currentDay = startDate.toLocalDate();
      LocalDateTime localStartDateTime = currentDay.atStartOfDay();
      LocalDateTime localEndDateTime = currentDay.atTime(23, 59);
      event.put("startDate", localStartDateTime);
      event.put("endDate", localEndDateTime);
    }
    return event;
  }

  private void handleDropDown() {

    if (uiView.isAddCalendar()) {
      uiView.showAddCalendarinUI();
      String calendarDetails[] = uiView.getCalendarDetails();
      System.out.println(calendarDetails[0] + " " + calendarDetails[1]);
      calendarManager.createCalendar(calendarDetails[0], calendarDetails[1]);
      uiView.setCalendar(calendarDetails[0]);
      activeCalendarName = calendarDetails[0];
    } else {
      activeCalendarName = uiView.getChangedCalName();
      uiView.changeCalendarInDropDown(this);
      System.out.println("New Cal Name" + uiView.getChangedCalName());
    }
    calendarV2 = calendarManager.getCalendarByName(activeCalendarName);
  }

  private void handleAddEvent(ActionEvent e) {
    String ans[] = uiView.getSelectedDate(e);
    int d = Integer.parseInt(ans[0]);
    int m = Integer.parseInt(ans[1]);
    int y = Integer.parseInt(ans[2]);
    LocalDate date = LocalDate.of(y, m, d);
    LocalDateTime dateTime = LocalDateTime.of(y, m, d, 0, 0);
    Map<String, Object> metaData = new HashMap<>();
    metaData.put("localStartTime", dateTime);
    //calendarV2 = calendarManager.getCalendarByName(activeCalendarName);
    List<Map<String, Object>> eventDetails = calendarV2.getMatchingEvents(metaData);
    Map<String, Object> event = uiView.getUserShowEventChoice(date, eventDetails, getPrintEventsAsString(eventDetails));
    Map<String, Object> dataForCreateEvent = getDataForCreateEvent(event);
    try {
      String subject = dataForCreateEvent.get("subject").toString();
      LocalDateTime startDate = (LocalDateTime) dataForCreateEvent.get("startDate");
      LocalDateTime endDate = (LocalDateTime) dataForCreateEvent.get("endDate");
      calendarV2.createEvent(subject, startDate, endDate, event);
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }

  private void handleUpdateEvent() {
    Map<String, Object> metaDeta = uiView.getNewPropertyAndValue();
    Map<String, Object> updateMap = new HashMap<>();
    updateMap.put("property", metaDeta.get("property"));
    updateMap.put("eventName", editEventName);
    updateMap.put("newValue", metaDeta.get("newValue"));
    updateMap.put("localStartTime", editEventStartDate);
    updateMap.put("localEndTime", editEventEndDate);
    try {
      calendarV2.editEvent(updateMap);
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }
    uiView.clearSearchPanel();
  }

  private void handleSearch() {
    refreshEditValues();
    Map<String, Object> data = uiView.getEventsToBeEditedValues();
    editEventName = data.get("eventName").toString();
    calendarV2 = calendarManager.getCalendarByName(activeCalendarName);
    List<Map<String, Object>> match = new ArrayList<>();
    if (data.get("StartTime") == null && data.get("EndTime") == null) {
      match = getEventsToShowInUIByName(data);
    } else {
      String newDate = data.get("StartTime").toString();
      newDate = DateUtils.removeTinDateTime(newDate);
      editEventStartDate = DateUtils.stringToLocalDateTime(newDate);
      Map<String, Object> editData = new HashMap<>();
      editData.put("localStartTime", editEventStartDate);
      if (data.get("endDate") != null) {
        newDate = data.get("endTime").toString();
        newDate = DateUtils.removeTinDateTime(newDate);
        editEventEndDate = DateUtils.stringToLocalDateTime(newDate);
        editData.put("localEndTime", editEventEndDate);
      }
      match = getEventsToShowInUI(data);
    }
    uiView.showMatchingEventsForEdit(getPrintEventsAsString(match));
    uiView.clearSearchPanel();
  }

  private void refreshEditValues() {
    editEventEndDate = null;
    editEventName = null;
    editEventStartDate = null;
  }

  private List<Map<String, Object>> getEventsToShowInUI(
          Map<String, Object> data) {
    List<Map<String, Object>> match = new ArrayList<>();
    List<Map<String, Object>> allEvents = calendarV2.getAllCalendarEvents();
    for (Map<String, Object> eventDetail : allEvents) {
      LocalDateTime eventStartDate = (LocalDateTime) eventDetail.get("startDate");
      if (eventDetail.get("subject").equals(data.get("eventName"))) {
        if (editEventEndDate == null && (eventStartDate.isAfter(editEventStartDate)
                || eventStartDate.equals(editEventStartDate))) {
          match.add(eventDetail);

        } else if (editEventStartDate.equals(eventStartDate) &&
                editEventEndDate.equals(eventStartDate)) {
          match.add(eventDetail);
        }
      }
    }
    return match;
  }

  private List<Map<String, Object>> getEventsToShowInUIByName(Map<String, Object> data) {
    List<Map<String, Object>> match = new ArrayList<>();
    List<Map<String, Object>> allEvents = calendarV2.getAllCalendarEvents();
    for (Map<String, Object> eventDetail : allEvents) {
      if (eventDetail.get("subject").equals(data.get("eventName"))) {
        match.add(eventDetail);
      }
    }
    return match;
  }

  private String getPrintEventsAsString(List<Map<String, Object>> dayEvents) {
    StringBuilder eventListBuilder = new StringBuilder();
    if (dayEvents.size() == 0) {
      return "No Matching Events";
    }
    for (Map<String, Object> event : dayEvents) {
      StringBuilder bulletEvent = new StringBuilder();
      bulletEvent.append(" Subject : " + event.get("subject") + ",");
      LocalDateTime startDateTime = (LocalDateTime) event.get("startDate");
      bulletEvent.append("Start date : " + startDateTime.toLocalDate() + ",");
      bulletEvent.append("Start time : " + startDateTime.toLocalTime() + ",");
      if (event.get("endDate") != null) {
        LocalDateTime endDateTime = (LocalDateTime) event.get("endDate");
        bulletEvent.append("End date : " + endDateTime.toLocalDate() + ",");
        bulletEvent.append("End time : " + endDateTime.toLocalTime() + ",");
      }
      if (event.get("location") != null) {
        bulletEvent.append("location : " + event.get("location") + ",");
      }
      if (event.get("description") != null) {
        bulletEvent.append("description : " + event.get("description") + ",");
      }
      if (event.get("isPublic") != null) {
        bulletEvent.append("isPublic : " + (Boolean) event.get("isPublic") + ",");
      }
      bulletEvent.deleteCharAt(bulletEvent.length() - 1);
      eventListBuilder.append(bulletEvent + "\n");
    }
    return eventListBuilder.toString();
  }
}
