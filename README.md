# Calendar Application

## Introduction

This project implements a comprehensive virtual calendar application inspired by Google Calendar and Apple's iCalendar. It simplifies event management, allowing users to seamlessly create, modify, query, and export calendar events through intuitive command-line interactions.

## Features

### Original Features
- **Event Conflict Management** (Automatic conflict detection)
- **Recurring Events** (Customizable repetitions)
- **Event Editing**
- **Event Querying** (View events or check busy status)
- **Printing events** on a given date
- **Checking busy status** on a particular date
- **CSV Export** (Compatible with Google Calendar)
- **Interactive and Headless Modes**

### New Extended Features
- **Multiple Calendars** (Support creation and management of multiple named calendars)
- **Timezone Support** (Calendars have individual timezones, events inherit calendar timezone)
- **Event Copying** (Support copying events between calendars, respecting timezone and conflict rules)
- **Clarified Command Set** (Conflicts automatically declined, enhanced editing commands)

## Usage

### Interactive Mode

Start interactive command entry:

```bash
java CalendarApp --mode interactive
```

### Headless Mode

Execute commands from a file (provide absolute file path after program starts):

```bash
java CalendarApp --mode headless
```

## Supported Commands

### Event Commands

| Command                                                                                                 | Description                                                                                    |
| ------------------------------------------------------------------------------------------------------- | ---------------------------------------------------------------------------------------------- |
| `create event <eventName> from <dateTime> to <dateTime>`                                  | Creates an event. Automatically declines if there is a conflict.                               |
| `create event <eventName> from <dateTime> to <dateTime> repeats <weekdays> until <dateTime>`            | Creates a recurring event until specified date. Automatically declines conflicts.              | 
| `create event <eventName> on <date>`                                                                    | Creates an all-day event. Automatically declines conflicts.                                    |
| `create event <eventName> from <dateTime> to <dateTime> repeats <weekdays> for N times`   | Creates a recurring event on specified weekdays for N times. Automatically declines conflicts. |
| `create event <eventName> on <date> repeats <weekdays> for <N> times`                                   | Creates a recurring all-day event. Automatically declines conflicts.                           |
| `edit event <property> <eventName> from <dateTime> to <dateTime> with <newPropertyValue>`               | Edits properties of an event, preventing conflicts.                                            |
| `edit events <property> <eventName> from <dateTime> with <NewPropertyValue>`               | Edits properties of events starting at or after the specified time, preventing conflicts.      |
| `edit events <property> <eventName> <NewPropertyValue>`                                                 | Edits properties of all events with a given name, preventing conflicts.                        |
| `print events on <date>`                                                                                | Lists events on a specific day.                                                                |
| `print events from <date> to <date>`                                                                    | Lists events within a specified date range.                                                    |
| `export calendar <filename.csv>`                                                                        | Exports calendar events to CSV and prints file location.                                       |
| `show status on <dateTime>`                                                                             | Shows busy status at specified date and time.                                                  |
| `copy event <eventName> on <dateTime> --target <calendarName> to <dateTime>`                            | Copies a specific event to a target calendar at specified date/time, preventing conflicts.     |
| `copy events on <date> --target <calendarName> to <date>`                                               | Copies all events on a specific date to another calendar, adjusting for timezone.              |
| `copy events between <date> and <date> --target <calendarName> to <date>`                               | Copies events within a range to a target calendar, adjusting times and preventing conflicts.   |
| `exit`                                                                                                  | Exits the application.                                                                         |

### Calendar Management Commands

| Command | Description |
|---------|-------------|
| `create calendar --name <calName> --timezone <area/location>` | Creates a new calendar with a unique name and specified timezone. |
| `edit calendar --name <calendarName> --property <propertyName> <newPropertyValue>` | Edits properties (name or timezone) of an existing calendar. |
| `use calendar --name <calendarName>` | Sets the active calendar context for subsequent commands. |

## Weekday Abbreviations

| Abbreviation | Day       |
|--------------|-----------|
| **M**        | Monday    |
| **T**        | Tuesday   |
| **W**        | Wednesday |
| **R**        | Thursday  |
| **F**        | Friday    |
| **S**        | Saturday  |
| **U**        | Sunday    |

## Project Structure

### `src/`
- **`controller`**: Command parsing, delegation, execution logic.
- **`model`**: Event and calendar logic, timezone handling, conflict checks.
- **`view`**: User interface and output logic.
- **`utils`**: Utility functions (date/time conversions, regex utilities).
- **`CalendarApp.java`**: Application entry point.

### `test/`
- **Unit tests**: Comprehensive tests covering events, calendars, and commands.

### `res/`
- **Resources**: Screenshots, diagrams, and demonstration files.

## Testing and Quality Assurance

The application follows MVC architecture and SOLID principles, ensuring modularity, maintainability, and scalability. Comprehensive JUnit tests verify correctness and robustness.

## CSV Integration with Google Calendar

Exported CSV files can be imported into Google Calendar, facilitating easy visual management of events.

## Contributions
 
| Team Member | Contributions                                             |
|-------------|-----------------------------------------------------------|
| **Ronit**   | Controller logic, command parsing, and execution. |
| **Siva**    | Model logic, event representation, data storage, and execution flow. |
| **Ronit & Siva** | Collaborative testing and code reviews. |

