package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.ICalendarV2;
import utils.DateUtils;
import view.UiView;

public class ViewController implements ActionListener {

  private ICalendarManagerV2 calendarManager;
  private UiView uiView;
  ICalendarV2 currentCalendar;

  public ViewController(ICalendarManagerV2 calendarManager, UiView uiView) {
    this.calendarManager = calendarManager;
    this.uiView = uiView;
    uiView.display();
    uiView.setListener(this);
  }

  @Override
  public void actionPerformed(ActionEvent e) {

    switch (e.getActionCommand()) {
      case "Next Month":
        uiView.goToNextMonth(this);
        break;

      case "Previous Month":
        uiView.goToPreviousMonth(this);
        break;

      case "Drop down":
        if (uiView.isAddCalendar()) {
          uiView.showAddCalendarinUI();
          String calendarDetails[] = uiView.getCalendarDetails();
          System.out.println(calendarDetails[0] + " " + calendarDetails[1]);
        } else {
          uiView.changeCalendarInDropDown(this);
        }
        break;

      case "Add Event":
        String ans[] = uiView.getSelectedDate(e);
        int d = Integer.parseInt(ans[0]);
        int m = Integer.parseInt(ans[1]);
        int y = Integer.parseInt(ans[2]);
        LocalDate date = LocalDate.of(y, m, d);
        LocalDateTime dateTime = LocalDateTime.of(y, m, d, 0, 0);

        ICalendarV2 calendarV2 = calendarManager.getActiveCalendar();
        Map<String, Object> metaData = new HashMap<>();
        metaData.put("localStartTime", dateTime);
        List<Map<String, Object>> eventDetails = calendarV2.getMatchingEvents(metaData);
        Map<String, Object> event = uiView.getUserShowEventChoice(date, eventDetails);

        event.put("isAllDay", false);
        event.put("autoDecline", true);

        StringBuilder bulletEvent = new StringBuilder();
        bulletEvent.append(" Subject : " + event.get("subject") + ",");
        bulletEvent.append("Start date : " + event.get("startDate") + ",");
        bulletEvent.append("Start date : " + event.get("endDate") + ",");
        bulletEvent.append("location : " + event.get("location") + ",");
        bulletEvent.append("description : " + event.get("description") + ",");
        bulletEvent.append("isPublic : " + event.get("isPublic") + ",");

        bulletEvent.deleteCharAt(bulletEvent.length() - 1);
        System.out.println(bulletEvent.toString());
        String subject = event.get("subject").toString();

        String startDateAfterT = DateUtils.removeTinDateTime(event.get("startDate").toString());
        LocalDateTime startDate = DateUtils.stringToLocalDateTime(startDateAfterT);

        LocalDateTime endDate = null;
        if (event.get("endDate") != null) {
          String endDateAfterT = DateUtils.removeTinDateTime(event.get("endDate").toString());
          endDate = DateUtils.stringToLocalDateTime(endDateAfterT);
        }

        try {
          calendarV2.createEvent(subject, startDate, endDate, event);
        } catch (Exception ex) {
          throw new RuntimeException(ex);
        }

        break;

      case "Edit across calendar":
        System.out.println("search events to edit");
        uiView.searchEventsToEdit();
        Map<String, Object> data = uiView.getEventsToBeEditedValues();
        break;

      case "update value":
        Map<String, Object> metaDeta = uiView.getNewPropertyAndValue();
        System.out.println("update value" + metaDeta.get("name"));
        break;

      case "Exit Button":
        System.exit(0);
        break;
    }
  }

  public void initiateDefaultCalendar() {
    calendarManager.createCalendar("default", "America/New_York");
    currentCalendar = calendarManager.getDefaultCalendar();
  }
}
