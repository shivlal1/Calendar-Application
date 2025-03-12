package view;

import java.util.List;
import java.util.Map;

public interface View {
  public void messageView(String message);

  public void eventsView(List<Map<String, Object>> events);

  public void showStatusInConsole(boolean status);
}
