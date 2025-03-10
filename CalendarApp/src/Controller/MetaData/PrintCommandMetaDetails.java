package Controller.MetaData;

import java.time.LocalDateTime;

public class PrintCommandMetaDetails {

  private LocalDateTime localStartDate;
  private LocalDateTime localEndDate;

  private PrintCommandMetaDetails(PrintEventMetaDetailsBuilder builder) {
    this.localStartDate = builder.localStartDate;
    this.localEndDate = builder.localEndDate;
  }

  public LocalDateTime getLocalStartDate() {
    return localStartDate;
  }

  public LocalDateTime getLocalEndDate() {
    return localEndDate;
  }

  public static class PrintEventMetaDetailsBuilder {

    private LocalDateTime localStartDate;
    private LocalDateTime localEndDate;

    public PrintEventMetaDetailsBuilder addLocalStartTime(LocalDateTime localStartTime) {
      this.localStartDate = localStartTime;
      return this;
    }

    public PrintEventMetaDetailsBuilder addLocalEndTime(LocalDateTime localEndTime) {
      this.localEndDate = localEndTime;
      return this;
    }

    public PrintCommandMetaDetails build() {
      return new PrintCommandMetaDetails(this);
    }
  }
}

