# Virtual Calendar Application

## Introduction

This project implements a comprehensive virtual calendar application inspired by Google Calendar and Apple's iCalendar. It simplifies event management, allowing users to seamlessly create, modify, query, and export calendar events through intuitive command-line interactions.

## Features

- **Single Calendar Support** (EST timezone)
- **Event Conflict Management** (Automatic conflict detection and optional auto-decline)
- **Recurring Events** (Customizable repetitions)
- **Event Editing**
- **Event Querying** (View events or check busy status)
- **CSV Export** (Compatible with Google Calendar)
- **Interactive and Headless Modes**

## Usage

### Interactive Mode

Start interactive command entry:

```bash
java CalendarApp --mode interactive
```

### Headless Mode

Execute commands from a file:

```bash
java CalendarApp --headless commands.txt
```

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

## Sample Command Variations

### Create Events

```bash
create event --autoDecline TeamMeeting from 2024-04-01T09:00 to 2024-04-01T10:00
create event YogaSession from 2024-03-15T18:00 to 2024-03-15T19:00 repeats TR until 2024-05-30
create event CompanyHoliday on 2024-07-04
```

### Edit Events

```bash
edit event location TeamMeeting from 2024-04-01T09:00 to 2024-04-01T10:00 with ConferenceRoomA
edit events title YogaSession from 2024-03-15T18:00 with EveningYoga
edit events location CompanyHoliday Remote
```

### Print Events

```bash
print events on 2024-04-01
print events from 2024-03-01 to 2024-03-31
```

### Show Status

```bash
show status on 2024-04-01T09:30
```

### Export Calendar

```bash
export calendar april_calendar.csv
```

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

- `src/`: Source code
- `test/`: Unit tests
- `res/`: Resources (screenshots, command files, diagrams)
- `README.md`: Project documentation

## Testing and Quality Assurance

The application follows MVC architecture and SOLID principles, ensuring modularity and maintainability. Comprehensive JUnit tests verify application robustness and correctness.

## CSV Integration with Google Calendar

Exported CSV files can be uploaded to Google Calendar for visual event management.

