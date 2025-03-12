package view;

import java.util.List;
import java.util.Map;

public interface View {
  public void viewMessage(String message);

  public void viewEvents(List<Map<String, Object>> events);

  public void viewStatus(boolean status);
}
