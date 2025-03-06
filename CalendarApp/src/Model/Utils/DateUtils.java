package Model.Utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtils {

  private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
  private LocalDateTime localDate;
  private String date;

  public DateUtils(String date) {
    this.date = date;
  }

  public LocalDateTime stringToLocalDateTime() {
    return LocalDateTime.parse(date, formatter);
  }

}
