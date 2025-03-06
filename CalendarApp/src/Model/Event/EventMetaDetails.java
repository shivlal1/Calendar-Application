package Model.Event;


public class EventMetaDetails {
  private String weekdays;
  private String forTimes;
  private boolean isRecurring;
  private boolean isAllDay;
  private String addUntilDateTime;

  private EventMetaDetails(EventMetaDetailsBuilder builder) {
    this.weekdays = builder.weekdays;
    this.forTimes = builder.forTimes;
    this.isRecurring = builder.isRecurring;
    this.isAllDay = builder.isAllDay;
    this.addUntilDateTime = builder.addUntilDateTime;
  }

  public String getWeekdays() {
    return weekdays;
  }

  public boolean getIsAllDay() {
    return isAllDay;
  }

  public boolean getIsRecurring() {
    return isRecurring;
  }

  public String getForTimes() {
    return forTimes;
  }

  public String getAddUntilDateTime() {
    return addUntilDateTime;
  }

  public static class EventMetaDetailsBuilder {

    private String weekdays;
    private String forTimes;
    private boolean isRecurring;
    private boolean isAllDay;
    private String addUntilDateTime;

    public EventMetaDetailsBuilder addWeekdays(String weekdays) {
      this.weekdays = weekdays;
      return this;
    }

    public EventMetaDetailsBuilder addForTimes(String forTimes) {
      this.forTimes = forTimes;
      return this;
    }

    public EventMetaDetailsBuilder addIsRecurring(boolean isRecurring) {
      this.isRecurring = isRecurring;
      return this;
    }

    public EventMetaDetailsBuilder addIsAllDay(boolean isAllDay) {
      this.isAllDay = isAllDay;
      return this;
    }

    public EventMetaDetailsBuilder addUntilDateTime(String addUntilDateTime) {
      this.addUntilDateTime = addUntilDateTime;
      return this;
    }

    public EventMetaDetails build() {
      return new EventMetaDetails(this);
    }
  }
}

