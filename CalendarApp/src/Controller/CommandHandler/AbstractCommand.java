package Controller.CommandHandler;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class AbstractCommand implements ICommand {
  protected Pattern pattern;
  protected Matcher matcher;

  protected void initRegexPatter(String regex, String commandArgs) {
    pattern = Pattern.compile(regex);
    matcher = pattern.matcher(commandArgs);
  }
}
