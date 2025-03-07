package Model.Event;

public class EventFactory {

  private AEvent event;

  public AEvent getEvent(EventDetails eventDetails, EventMetaDetails allMetaDeta) {

    if (allMetaDeta.getIsRecurring()) {
      event = new RecurringEvent();
    } else {
      event = new SingleEvent();
    }
    return event;
  }

}
