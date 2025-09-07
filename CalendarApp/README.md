# Virtual Calendar Application

## Introduction
This project is a virtual calendar application designed to mimic the functionality of popular calendar apps such as Google Calendar or Apple iCalendar. The application supports multiple calendars, timezone management, recurring events, conflict handling, and exporting events in Google Calendar–compatible format.

The project follows the **MVC architecture** and is built using **SOLID principles** to ensure scalability and maintainability.

---

## Features

### Multiple Calendars
- Create and maintain several calendars, each with a unique name.
- Change calendar properties such as name and timezone.
- Switch between calendars using `use calendar`.

### Timezone Support
- Each calendar is associated with a specific timezone (IANA format, e.g., `America/New_York`).
- Events inherit the timezone of their calendar.
- Timezone changes automatically affect interpretation of events.

### Event Management
- Create single events with subject, start/end time, description, location, and privacy flag.
- Create all-day events (if no end time specified).
- Support for multi-day events.
- Support recurring events that repeat weekly for a fixed count or until a specific end date.
- Editing of events, including:
  - Single instances
  - All future instances
  - Entire series

### Conflict Handling
- Events cannot overlap within the same calendar.
- Any command that would create or edit a conflicting event is rejected.

### Copying Events
- Copy a single event to another calendar at a specified date/time.
- Copy all events on a given day to another calendar.
- Copy all events in a date range to another calendar.
- Events copied across calendars are converted to the target calendar’s timezone.

### Querying
- List all events scheduled on a specific date.
- List all events within a given date range.
- Check if the user is busy at a specific date and time.

### Export
- Export a calendar as a **CSV file**.
- Format is compatible with Google Calendar import.

### Command-Based Interface
- Users interact through text-based commands.
- Supports both **interactive mode** and **headless (batch execution) mode**.
