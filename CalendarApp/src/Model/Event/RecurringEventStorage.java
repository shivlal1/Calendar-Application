package Model.Event;

import java.util.List;

public class RecurringEventStorage {

  EventDetails originalEventDetails;
  List<EventDetails> recurringEventList;

  RecurringEventStorage(EventDetails event, List<EventDetails> recurringEventList) {
    this.originalEventDetails = event;
    this.recurringEventList = recurringEventList;
  }

}
