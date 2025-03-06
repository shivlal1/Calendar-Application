package Model.Event;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import Model.Calendar.ACalendar;

public class RecurringEvent extends AEvent {

  public void addEvent(EventInformation info, ACalendar calendar, String weekdays, String forTimes,LocalDateTime finalUntilDateTime) {
    LocalDateTime start = info.startDate;
    LocalDateTime end = info.info.endDate;

    LocalDateTime currDate = start;
//    System.out.println("Start date: " + currDate);
//    System.out.println("End date: " + end);

    while (!currDate.isAfter(finalUntilDateTime)) {
      DayOfWeek day = currDate.getDayOfWeek();
      char  d = getDayAbbreviation(day.name());

      if(weekdays.indexOf(d)!=-1){
//       System.out.println("Matched day "+d);
//       System.out.println("that day start "+currDate);
//        System.out.println("that day end "+end);

        info.info.endDate =end;
        info.startDate = currDate;
        EventToBeAdded eventToBeAdded = new EventToBeAdded(info,currDate,end);
        int year = currDate.getYear();
        int month = currDate.getMonthValue();
        int day_ = currDate.getDayOfMonth();
        System.out.println("adding day "+day_);
//        System.out.println("New Recurring Event "+eventToBeAdded);
        calendar.createEvent(year, month, day_, eventToBeAdded);

      }

      currDate =  currDate.plusDays(1);
      end = end.plusDays(1);
    }
  }

  @Override
  public void breakEvents() {

  }

  public  char getDayAbbreviation(String day) {
    // Define the map for day abbreviations
    Map<String, Character> dayMap = new HashMap<>();
    dayMap.put("MONDAY", 'M');
    dayMap.put("TUESDAY", 'T');
    dayMap.put("WEDNESDAY", 'W');
    dayMap.put("THURSDAY", 'R');
    dayMap.put("FRIDAY", 'F');
    dayMap.put("SATURDAY", 'S');
    dayMap.put("SUNDAY", 'U');

    // Return the abbreviation or a default value
    return dayMap.getOrDefault(day, '?'); // '?' indicates an invalid input
  }

}
