package controller;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import static org.junit.Assert.assertEquals;


public class CalendarAppTest {

  @Test
 public void testMainWithArguments() {
    // Capture System.out output
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    PrintStream originalOut = System.out;
    System.setOut(new PrintStream(outputStream));

    // Run main method with arguments
    String[] args = {"Hello", "World"};
   // MainProgram.main(args);

    // Restore System.out
    System.setOut(originalOut);

    // Verify output
    assertEquals("Received: Hello, World" + System.lineSeparator(), outputStream.toString());
  }
}