package Model.Event;

public class EventFactory {

  private AEvent event;

  public AEvent getEvent(EventDetails eventDetails, EventMetaDetails allMetaDeta) {

    if (allMetaDeta.getIsRecurring()) {
      event = new RecurringEvent(eventDetails, allMetaDeta);
    } else {
      event = new SingleEvent(eventDetails, allMetaDeta);
    }
    return event;
  }

}
