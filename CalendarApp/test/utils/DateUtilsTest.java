package utils;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.*;

public class DateUtilsTest {


  @Rule
  public ExpectedException thrown = ExpectedException.none();



  @Test()
  public void objectCreation(){
      thrown.expect(Exception.class);
      thrown.expectMessage("Date Utility cannot be instantiated");

    DateUtils date = new DateUtils();

  }
}