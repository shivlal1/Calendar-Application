package Controller.MetaData;


import java.time.LocalDateTime;

public class EditEventMetaDetails {
  private LocalDateTime localStartTime, localEndTime;
  private String startTime, endTime;
  private String eventName;
  private String newValue;
  private String property;

  private EditEventMetaDetails(EditEventMetaDetailsBuilder builder) {
    this.eventName = builder.eventName;
    this.newValue = builder.newValue;
    this.property = builder.property;
    this.localStartTime = builder.localStartTime;
    this.localEndTime = builder.localEndTime;
    this.startTime = builder.startTime;
    this.endTime = builder.endTime;
  }

  public String getStartTime() {
    return startTime;
  }

  public String getEndTime() {
    return endTime;
  }

  public LocalDateTime getLocalStartTime() {
    return localStartTime;
  }

  public LocalDateTime getLocalEndTime() {
    return localEndTime;
  }

  public String getEventName() {
    return eventName;
  }

  public String getProperty() {
    return property;
  }

  public String getNewValue() {
    return newValue;
  }

  public static class EditEventMetaDetailsBuilder {

    private LocalDateTime localStartTime, localEndTime;
    private String startTime, endTime;
    private String eventName;
    private String newValue;
    private String property;


    public EditEventMetaDetailsBuilder addLocalStartTime(LocalDateTime localStartTime) {
      this.localStartTime = localStartTime;
      return this;
    }

    public EditEventMetaDetailsBuilder addLocalEndTime(LocalDateTime localEndTime) {
      this.localEndTime = localEndTime;
      return this;
    }


    public EditEventMetaDetailsBuilder addStartTime(String startTime) {
      this.startTime = startTime;
      return this;
    }

    public EditEventMetaDetailsBuilder addEndTime(String endTime) {
      this.endTime = endTime;
      return this;
    }

    public EditEventMetaDetailsBuilder addEventName(String eventName) {
      this.eventName = eventName;
      return this;
    }

    public EditEventMetaDetailsBuilder addNewValue(String newValue) {
      this.newValue = newValue;
      return this;
    }

    public EditEventMetaDetailsBuilder addProperty(String property) {
      this.property = property;
      return this;
    }

    public EditEventMetaDetails build() {
      return new EditEventMetaDetails(this);
    }
  }
}

