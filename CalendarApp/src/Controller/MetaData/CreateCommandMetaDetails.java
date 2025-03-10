package Controller.MetaData;


public class CreateCommandMetaDetails {

  private String addUntilDateTime;
  private String forTimes, weekdays;
  private boolean isRecurring, autoDecline, isAllDay;

  private CreateCommandMetaDetails(EventMetaDetailsBuilder builder) {
    this.weekdays = builder.weekdays;
    this.forTimes = builder.forTimes;
    this.isRecurring = builder.isRecurring;
    this.isAllDay = builder.isAllDay;
    this.addUntilDateTime = builder.addUntilDateTime;
    this.autoDecline = builder.autoDecline;
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

  public boolean getAutoDecline() {
    return autoDecline;
  }


  public static class EventMetaDetailsBuilder {

    private String addUntilDateTime;
    private String forTimes, weekdays;
    private boolean isRecurring, autoDecline, isAllDay;

    public EventMetaDetailsBuilder addWeekdays(String weekdays) {
      this.weekdays = weekdays;
      return this;
    }

    public EventMetaDetailsBuilder addForTimes(String forTimes) {
      this.forTimes = forTimes;
      return this;
    }

    public EventMetaDetailsBuilder addAutoDecline(boolean autoDecline) {
      this.autoDecline = autoDecline;
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


    public CreateCommandMetaDetails build() {
      return new CreateCommandMetaDetails(this);
    }
  }
}

