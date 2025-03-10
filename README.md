# Calendar Application

## Introduction

This project implements a virtual calendar application inspired by popular applications like Google Calendar and Apple's iCalendar. It allows users to create, modify, query, and export calendar events with features tailored for user-friendliness and ease of use.

## Feature Overview

- **Single Calendar Support**: Operates on EST timezone, capable of handling events spanning multiple days.
- **Event Conflict Management**: Automatic conflict detection with existing events.
- **Recurring events** support with customizable repetition.
- **Event editing** capabilities.
- **Calendar querying** for events or busy status.
- **Export functionality** to CSV compatible with Google Calendar.
- Text-based commands through interactive or headless mode.

## Supported Commands

| Command                                                                                                 | Description                                                                                    |
| ------------------------------------------------------------------------------------------------------- | ---------------------------------------------------------------------------------------------- |
| `create event --autoDecline <eventName> from <dateTime> to <dateTime>`                                  | Create an event with optional conflict auto-decline.                                           |
| `create event <eventName> from <dateTime> to <dateTime> repeats <weekdays> until <dateTime>`            | Creates a recurring event until specified date.                                                | 
| `create event <eventName> on <date>`                                                                    | Creates an all-day event.                                                                      |
| `create event --autoDecline <eventName> from <dateTime> to <dateTime> repeats <weekdays> for N times`   | Creates a recurring event on the specified weekdays for N times                                |
| `create event <eventName> on <date> repeats <weekdays> for <N> times`                                   | Creates an event on the specified date repeating on the given days for N times.                |
| `create event <eventName> on <date> repeats <weekdays> until <date>`                                    | Creates an event on the specified date which repeats on the specified days until a given date. |
| `edit event <property> <eventName> from <dateTime> to <dateTime> with <newPropertyValue>`               | Edits properties of an event with the given new property value.                                |
| `edit events <property> <eventName> from <dateStringTtimeString> with <NewPropertyValue>`               | Edits the property of the event that starts from the given date and time.                      |
| `edit events <property> <eventName> <NewPropertyValue>`                                                 | Edits the property of all events with the given name to the new property value.                |
| `print events on <date>`                                                                                | Lists events on a specific day.                                                                |
| `print events from <date> to <date>`                                                                    | Lists events within a specified range of dates.                                                |
| `export calendar <filename.csv>`                                                                        | Exports calendar to CSV and prints file location.                                              |
| `show status on <dateTime>`                                                                             | Shows busy status at a specified date and time if an event is already scheduled.               |
| `exit`                                                                                                  | Exits the application.                                                                         |

### Weekday Abbreviations:

- **M**: Monday
- **T**: Tuesday
- **W**: Wednesday
- **R**: Thursday
- **F**: Friday
- **S**: Saturday
- **U**: Sunday

## Running the Application

### Interactive Mode

```
java CalendarApp --mode interactive
```

This mode enables interactive command entry.

### Headless Mode

```bash
java CalendarApp --headless <commands.txt>
```

This mode executes commands listed in a provided text file sequentially.

## Exporting Calendar

The exported CSV file is compatible with Google Calendar. Upload the generated file to visualize events online.

## Project Structure

- `src/`: Source code files.
- `test/`: Contains all unit tests.
- `README.md`: Project description and user guide.

## Testing and Quality Assurance

The project adheres to MVC architecture and applies SOLID principles, ensuring modular, maintainable, and scalable code. Comprehensive test cases using JUnit verify the application's robustness and correctness.

