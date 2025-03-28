# Calendar Application

## Introduction

This project implements a comprehensive virtual calendar application inspired by Google Calendar and Apple's iCalendar. It simplifies event management, allowing users to seamlessly create, modify, query, and export calendar events through intuitive command-line interactions.

## Features

- **Single Calendar Support** (EST timezone)
- **Event Conflict Management** (Automatic conflict detection and optional auto-decline)
- **Recurring Events** (Customizable repetitions)
- **Event Editing**
- **Event Querying** (View events or check busy status)
- Printing events on a given date
- Checking if the user is busy on a particular date
- **CSV Export** (Compatible with Google Calendar)
- **Interactive and Headless Modes**

## New Extended Features
- **Multiple Calendars** (Support creation and management of multiple named calendars)
- **Timezone Support** (Calendars have individual timezones, events inherit calendar timezone)
- **Event Copying** (Support copying events between calendars, respecting timezone and conflict rules)
- **Clarified Command Set** (Conflicts automatically declined, enhanced editing commands)

### New Functionalities Implemented
- **Support for Time Zones** – The system now accommodates time zones for event scheduling and management.
- **Auto-Decline Policy** – By default, event conflicts are automatically declined, and editing events that create conflicts is not allowed.
- **Copy Events to Another Calendar** – Users can now duplicate events across different calendars.
- **Calendar Management Commands** – New commands for creating, editing, and switching between calendars have been introduced.

### Code Design Approach
The existing design was **NOT CHANGED** in any way to integrate these new functionalities. Instead of modifying the existing design, the implementation was extended by introducing new classes and interfaces. Changes to existing code primarily enhanced mutation testing strength and improved live coverage, addressing gaps from the previous assignment.

### Key Code Changes

#### 1. Copy Command in the Controller
- Introduced a new class, `CopyCommand`, adhering to the existing command design pattern to support event copying.

#### 2. Calendar Management Commands in the Controller
- Created a new interface `ICalendarManager` and its concrete implementation `CalendarManager` to handle multiple calendars and track the active calendar context.
- `ICalendarManager` provides an `execute()` method to handle calendar-specific commands.

#### 3. Copy Command in the Model
- **Previous Implementation (Assignment 4)**:
  - `Calendar` implemented `ICalendar`, providing basic event management functionalities.

- **Updated Implementation (Assignment 5)**:
  - Created a new class, `CalendarV2`, extending `Calendar` and implementing `ICalendarV2`.
  - `ICalendarV2` extends `ICalendar` by introducing methods for copying events.
  - Methods previously marked private in `Calendar` were changed to protected to facilitate reuse in `CalendarV2`.

#### 4. Auto-Decline Feature
- Enabled auto-decline by default in the `CalendarV2` implementation.
- Invoked the existing `createEvent` method from `Calendar` using `super()` to avoid code duplication.
- Event editing triggers auto-decline if changes to event times cause conflicts. This was managed using `super()` in `CalendarV2` for methods like `updateStartDate` and `updateEndDate`.
- Enhanced edit command syntax to support auto-decline when modifying event properties.
- Introduced new logic in `CalendarV2` to filter and edit all events starting from a specified date using an enhanced implementation of `isMatchingEvent`.

#### 5. Time Zone Support
- Added a time zone parameter to the constructor of `CalendarV2`.
- Developed new utility methods in `DateUtils` for handling time zone conversions.
## Usage

### Interactive Mode

Start interactive command entry:

java CalendarApp --mode interactive


### Headless Mode


java CalendarApp --mode headless

( please enter the absolute file path after program runs after the program display Headless mode)


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
| `edit events <property> <eventName> from <dateStringTtimeString> with <NewPropertyValue>`              | Edits the property of the event that starts from the given date and time.                      |
| `edit events <property> <eventName> <NewPropertyValue>`                                                 | Edits the property of all events with the given name to the new property value.                |
| `print events on <date>`                                                                                | Lists events on a specific day.                                                                |
| `print events from <date> to <date>`                                                                    | Lists events within a specified range of dates.                                                |
| `export calendar <filename.csv>`                                                                        | Exports calendar to CSV and prints file location.                                              |
| `show status on <dateTime>`                                                                             | Shows busy status at a specified date and time if an event is already scheduled.               |
| `copy event <eventName> on <dateTime> --target <calendarName> to <dateTime>`                            | Copies a specific event to a target calendar at specified date/time, preventing conflicts.     |
| `copy events on <date> --target <calendarName> to <date>`                                               | Copies all events on a specific date to another calendar, adjusting for timezone.              |
| `copy events between <date> and <date> --target <calendarName> to <date>`                               | Copies events within a range to a target calendar, adjusting times and preventing conflicts.   |
| `exit`                                                                                                  | Exits the application.                                                                         |


## Calendar Management Commands

| Command | Description |
|---------|-------------|
| `create calendar --name <calName> --timezone <area/location>` | Creates a new calendar with a unique name and specified timezone. |
| `edit calendar --name <calendarName> --property <propertyName> <newPropertyValue>` | Edits properties (name or timezone) of an existing calendar. |
| `use calendar --name <calendarName>` | Sets the active calendar context for subsequent commands. |

## Sample Command Variations

### Create Events

```
create event --autoDecline TeamMeeting from 2024-04-01T09:00 to 2024-04-01T10:00
create event YogaSession from 2024-03-15T18:00 to 2024-03-15T19:00 repeats TR until 2024-05-30
create event CompanyHoliday on 2024-07-04
```

### Edit Events

```
edit event location TeamMeeting from 2024-04-01T09:00 to 2024-04-01T10:00 with ConferenceRoomA
edit events title YogaSession from 2024-03-15T18:00 with EveningYoga
edit events location CompanyHoliday Remote
```
### Copy Events

```
copy event TeamMeeting on 2024-04-01T09:00 --target WorkCalendar to 2024-05-01T09:00
copy events on 2024-04-01 --target PersonalCalendar to 2024-06-01
copy events between 2024-04-01 and 2024-04-30 --target ProjectCalendar to 2024-07-01
```
### Print Events

```
print events on 2024-04-01
print events from 2024-03-01 to 2024-03-31
```

### Show Status

```
show status on 2024-04-01T09:30
```

### Export Calendar

```
export calendar april_calendar.csv
```

### Calendar Commands

```
create calendar --name WorkCalendar --timezone America/New_York
edit calendar --name WorkCalendar --property timezone America/Los_Angeles
use calendar --name WorkCalendar
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
This project is organized clearly to enhance readability, maintainability, and ease of collaboration. The directory structure and contents are as follows:

### src/
This directory contains the main source code organized using the MVC (Model-View-Controller) architectural pattern, along with utility classes:

- **controller**: Contains command-related classes responsible for handling user commands such as Create, Edit, Show, Print, and Export.
- **model**: Includes the logic behind the creation and editing of events. It also contains the logic of exporting and checking if the user is busy at a given time slot or not.
- **view**: Handles all the user interface logic, including outputting to the console.
- **utils**: Provides common helper functions like date conversions and regex utilities.
- **CalendarApp.java**: The main entry point of the application, which is responsible for initializing and running the application logic.

### test/
This directory hosts all unit tests to ensure the correctness and reliability of the application:

#### controller/
- **CreateCommandTest.java**: Tests functionalities related to creating new events.
- **ShowCommandTest.java**: Tests functionalities for displaying event details and status.
- **EditCommandTest.java**: Covers tests for editing event details.
- **PrintCommandTest.java**: Verifies functionalities for printing events or schedules.
- **ExportCommandTest.java**: Ensures correctness of event export functionalities.
- **CopyCommandTest.java**: Ensures the correctness of event copy functionalities.

#### utils/
- **CalendarCsvExport.java**: Tests functionalities related to exporting the calendar to csv.
- **DateUtilsTest.java**: Tests functionalities related to creating new events.

**CalendarAppTest.java**: Tests functionalities of the main file that runs the program.
### res/
This directory holds various resources useful for documentation, reference, or illustrative purposes, including:

- Screenshots demonstrating application functionality.
- Command files used for testing or demonstration.
- Diagrams illustrating project architecture and design.
- README.md: Project documentation

## Testing and Quality Assurance

The application follows MVC architecture and SOLID principles, ensuring modularity and maintainability. Comprehensive JUnit tests verify application robustness and correctness.

## CSV Integration with Google Calendar

The events being created by our program can be exported into a csv file and the exported CSV files can be uploaded to Google Calendar for visual event management.

## Contributions
 
| Team Member | Contributions                                             |
|-------------|-----------------------------------------------------------|
| **Ronit**   | Implemented the **Controller** component, including command parsing, delegation, and execution logic. |
| **Siva**    | Developed the **Model** component, including calendar event representation, data storage, and the flow of the execution of the program. |
| **Ronit & Siva** | Collaboratively wrote and reviewed **unit test cases** to ensure application correctness and reliability. |
