# Calendar Application

## Introduction

This project implements a comprehensive virtual calendar application inspired by Google Calendar and Apple's iCalendar. It simplifies event management, allowing users to seamlessly create, modify, query, and export calendar events through intuitive command-line interactions.

## ALL the Features and New Extended Features below work

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

## Additional Changes for Assignment 6 (GUI Support)

### Expected Feature Set
The following features were implemented and are usable through the graphical user interface as part of Assignment 6:

- **Create a Calendar with Timezone:** Users can create a new calendar by specifying a name and a valid timezone.
- **Default Calendar:** The GUI provides a default calendar using the system's current timezone, allowing users to begin using the app immediately without needing to create a calendar.
- **Select and Work on a Calendar:** Users can choose an existing calendar and then create, view, and edit events within it. Conflict checks are enforced to prevent overlapping events.
- **Calendar Identification:** Users can visually distinguish calendars. For example, different calendars are color-coded in the GUI.
- **View Events on a Selected Day:** Clicking on a day displays all the events scheduled on that date, adjusted to the calendar's timezone.
- **Create New Events:** Events can be created for a selected day. The GUI supports both single and recurring events. Users can specify the recurrence rules via weekdays and either a number of occurrences or an end date.
- **Edit Events on a Specific Day:** The GUI supports editing existing events scheduled on a selected day.

To support the GUI, no changes were made to the existing view and controller code. Instead, new classes were added specifically for the GUI functionality.

### New Classes and Interfaces
- `controller/ViewController`: Acts as a bridge between the GUI and the model. Implements `ActionListener` and handles GUI events via `actionPerformed()`.
- `controller/ICalendarManagerV2` and `controller/CalendarManagerV2`: Enhanced calendar manager interface and its implementation to support calendar creation from the GUI.
- `view/UiView`: Interface defining methods for GUI interaction.
- `view/JFrameView`: Concrete implementation of `UiView` that extends `JFrame` and builds the full GUI experience.

### ViewController
The `ViewController` coordinates all UI-triggered actions and interacts with the model accordingly. It responds to user actions like calendar switching, event creation/editing, CSV importing/exporting, etc.

### Additional Utility
- `utils/CalendarCsvImporter`: A new class that allows users to import events from a CSV file into the calendar model via the GUI.

The updated `ICalendarManagerV2` and its implementation `CalendarManagerV2` extend the functionality of the original `ICalendarManager`, enabling GUI-driven calendar creation with timezone support.
- `utils/CalendarCsvImporter`: A new class to support importing events from a CSV file into the model.

## Mutation Testing Exclusions

The following classes are excluded from mutation testing due to their nature and role in GUI handling or interface definitions:

1. `controller/ViewController` – Implements `ActionListener` and primarily serves as a bridge between GUI actions and model logic. GUI triggers are difficult to simulate in mutation testing.
2. `view/UiView` – Interface used for defining methods for GUI interaction. Interfaces don't have method bodies to mutate.
3. `view/JFrameView` – Concrete implementation of GUI code, heavily dependent on Swing components and user interaction, which are not well-suited to automated mutation testing.

To support the GUI, no changes were made to the existing view and controller code. Instead, new classes were added specifically for the GUI functionality.

## Additional Features for Assignment 5

### Extended Functionalities
- **Multiple Calendars** (Support creation and management of multiple named calendars)
- **Timezone Support** (Calendars have individual timezones, events inherit calendar timezone)
- **Event Copying** (Support copying events between calendars, respecting timezone and conflict rules)
- **Clarified Command Set** (Conflicts automatically declined, enhanced editing commands)

### Newly Implemented
- **Support for Time Zones** – The system now accommodates time zones for event scheduling and management.
- **Auto-Decline Policy** – By default, event conflicts are automatically declined, and editing events that create conflicts is not allowed.
- **Copy Events to Another Calendar** – Users can now duplicate events across different calendars.
- **Calendar Management Commands** – New commands for creating, editing, and switching between calendars have been introduced.

### Code Design Approach
The existing design was **not changed** to implement these new features. Instead, we extended functionality via new classes and interfaces. Minimal changes were made to enhance mutation testing and coverage.

### Key Code Additions for Assignment 5

- **Controller Changes**:
  - Added `CopyCommand` to support event copying.
  - Introduced `ICalendarManager` and `CalendarManager` to manage multiple calendars.
- **Model Changes**:
  - Added `CalendarV2`, extending `Calendar` and implementing new interface `ICalendarV2`.
  - Enabled auto-decline and enhanced recurring event logic.
  - Time zone support added through constructor changes and `DateUtils` enhancements.

### New Classes and Interfaces
- `controller/ViewController`: Acts as a bridge between the GUI and the model. Implements `ActionListener` and handles GUI events via `actionPerformed()`.
- `controller/ICalendarManagerV2` and `controller/CalendarManagerV2`: Enhanced calendar manager interface and its implementation to support calendar creation from the GUI.
- `view/UiView`: Interface defining methods for GUI interaction.
- `view/JFrameView`: Concrete implementation of `UiView` that extends `JFrame` and builds the full GUI experience.

### ViewController
The `ViewController` coordinates all UI-triggered actions and interacts with the model accordingly. It responds to user actions like calendar switching, event creation/editing, CSV importing/exporting, etc.

### Additional Utility
- `utils/CalendarCsvImporter`: A new class that allows users to import events from a CSV file into the calendar model via the GUI.

The updated `ICalendarManagerV2` and its implementation `CalendarManagerV2` extend the functionality of the original `ICalendarManager`, enabling GUI-driven calendar creation with timezone support.
- `utils/CalendarCsvImporter`: A new class to support importing events from a CSV file into the model.

## Application Modes and Command-Line Arguments

The application now supports 3 modes depending on how it is launched:

| Command | Description |
|---------|-------------|
| `java -jar Program.jar` | Launches the **Graphical User Interface (GUI)**. |
| `java -jar Program.jar --mode interactive` | Launches the app in **interactive command-line mode** where users can enter commands one at a time. |
| `java -jar Program.jar --mode headless path-of-script-file` | Launches in **headless mode**, reads and executes commands from the script file, then exits. |


## Supported Commands

| Command | Description |
|---------|-------------|
| `create event --autoDecline <eventName> from <dateTime> to <dateTime>` | Create an event with optional conflict auto-decline. |
| `create event <eventName> from <dateTime> to <dateTime> repeats <weekdays> until <dateTime>` | Creates a recurring event until specified date. |
| `create event <eventName> on <date>` | Creates an all-day event. |
| `create event --autoDecline <eventName> from <dateTime> to <dateTime> repeats <weekdays> for N times` | Creates a recurring event on weekdays for N times. |
| `create event <eventName> on <date> repeats <weekdays> for <N> times` | All-day recurring event for N times. |
| `create event <eventName> on <date> repeats <weekdays> until <date>` | All-day recurring event until a date. |
| `edit event <property> <eventName> from <dateTime> to <dateTime> with <newPropertyValue>` | Edits a property of a specific event. |
| `edit events <property> <eventName> from <dateTime> with <newPropertyValue>` | Edits the property for events starting at a specific date. |
| `edit events <property> <eventName> <newPropertyValue>` | Edits the property for all matching events. |
| `print events on <date>` | Lists events on a specific day. |
| `print events from <date> to <date>` | Lists events within a date range. |
| `export calendar <filename.csv>` | Exports calendar to CSV. |
| `show status on <dateTime>` | Checks busy status at a specific time. |
| `copy event <eventName> on <dateTime> --target <calendarName> to <dateTime>` | Copies an event to another calendar. |
| `copy events on <date> --target <calendarName> to <date>` | Copies all events from one day to another calendar. |
| `copy events between <date> and <date> --target <calendarName> to <date>` | Copies events in a range to another calendar. |
| `exit` | Exits the application. |

## Calendar Management Commands

| Command | Description |
|---------|-------------|
| `create calendar --name <calName> --timezone <area/location>` | Creates a new calendar with a timezone. |
| `edit calendar --name <calendarName> --property <propertyName> <newPropertyValue>` | Edits name or timezone of a calendar. |
| `use calendar --name <calendarName>` | Sets the active calendar context. |

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

### Copy Events
```bash
copy event TeamMeeting on 2024-04-01T09:00 --target WorkCalendar to 2024-05-01T09:00
copy events on 2024-04-01 --target PersonalCalendar to 2024-06-01
copy events between 2024-04-01 and 2024-04-30 --target ProjectCalendar to 2024-07-01
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

### Calendar Commands
```bash
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

### src/
Contains the source code following MVC architecture:

- **controller/**: Command parsing and dispatching (Create, Edit, Show, Copy, etc.)
- **model/**: Core logic and business rules for event/calendar handling
- **view/**: Console and GUI user interfaces
- **utils/**: Utilities like date/time parsing and CSV handling
- **CalendarApp.java**: Entry point of the application

### test/
Includes unit test coverage:

- **controller/**: Tests for commands (Create, Edit, Print, etc.)
- **utils/**: Tests for CSV and DateUtils
- **CalendarAppTest.java**: Tests the overall app execution logic

### res/
- Screenshots, command files, and diagrams
- README documentation

## Testing and Quality Assurance
Follows MVC and SOLID principles. Unit tests written with JUnit. Mutations and edge cases addressed.

## CSV Integration with Google Calendar
Events exported via CSV are compatible with Google Calendar import.

## Contributions

| Team Member     | Contributions                                                                 |
|------------------|------------------------------------------------------------------------------|
| **Ronit**        | Controller logic, CopyCommand, CalendarManager, command parsing and the ViewController.       |
| **Siva**         | Model implementation, CalendarV2, recurring events, timezone support and the UI part for the implementation of other functionalities.      |
| **Ronit & Siva** | Jointly wrote unit tests, verified complete system functionality and worked together on the integration of the import and export function.         |

