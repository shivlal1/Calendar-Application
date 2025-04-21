# Design Critique

## Strengths
- The project tries to follow MVC principles.
- Separate classes are used for each command, thus allowing extending the code to support new commands with minimal code changes in existing code.
- The classes for different views are separated properly and GUI classes are in the `view.gui` package and CLI interface classes are in `view.text`.
- The `Model` interface is created properly and it provides a good abstraction.
- Helper methods are private in their respective classes.

## Limitations

### 1. MVC Architecture Only in Structure
- The project has packages named `model` and `controller`, but does not strictly follow MVC principles.
- Multiple controller classes interact with multiple model classes, creating high coupling.

### 2. Controller-Model Coupling
- `CreateCommandParser` interacts with: `CalendarManager`, `EventStorage`, `RecurringEventStorage`, `CalendarEvent`.
- `EditCommandParser` interacts with: `CalendarEvent`, `EditEvent`, `EventStorage`.
- `CommandParser` interacts with: `CalendarManager`, `EventStorage`.

**Suggestion:** Introduce a single class to interact with the model to reduce coupling.

### 3. OOP Violation - Use of Static Methods
- `EditEvent` mixes static and instance-based design.
- Static methods like `getEventPropertyFromString`, `createTempEventWithNewTime`, etc., should be in utility classes.

**Suggestion:** Avoid static methods in classes that can be instantiated.

### 4. Weak Interface Usage for `Event`
- The `Event` interface lacks most of the methods needed to represent an event.
- Most methods are defined only in concrete classes (`CalendarEvent`, `RecurringEventStorage`).

**Suggestion:** Declare all necessary methods in the `Event` interface.

### 5. Variables Typed as Concrete Classes
- Instances of `CalendarEvent`, `RecurringEventStorage`, `Calendar` are created without using their interfaces.

### 6. View-Model Coupling
- `CalendarPrinter` is used by controller classes and depends on model classes.
- Suggest using a structure like `List<Map<String, String>>` for loose coupling.

### 7. GUI Depends on CLI View
- GUI builds CLI commands and captures console output for GUI display, violating independence between views.

### 8. Class Placement Issues
- `CalendarCSVImporter` and `CSVCalendarExporter` are placed in inappropriate packages without proper interfaces.

### 9. Test-Only Methods in Production Code
- `conflictsWith` is used only for test purposes.

### 10. Constructor Overload
- `CalendarEvent` has 4 constructors.
- `RecurringEventStorage` has 3 constructors.

**Suggestion:** Use Builder Pattern.

### 11. Assignment 5 Refactor Instead of Extension
- Complete rewrite instead of extending Assignment 4 code led to test duplication.

### 12. GUI Instantiates Model Classes
- Direct model use in GUI makes it tightly coupled.

---

# Implementation Critique

## Strengths
- Efficient use of `Map<LocalDateTime, List<CalendarEvent>>` for event retrieval.
- Functions are modular and reused.

## Limitations

### 1. DateTime Duplication
- No centralized utility for date-time parsing (duplicate methods in multiple classes).

### 2. Static Method Abuse
- `CreateCommandParser` has several unnecessary static helper methods.

### 3. Incomplete Command Design Pattern
- `CommandParser` uses switch-case statements instead of extensible structure.

**Suggestion:** Replace with command registry (e.g., `Map<String, Command>`).

### 4. Code Duplication in `CommandParser`
- Functions `createCommand` and `extractMainCommand` duplicate switch-cases.

### 5. Two-Class Pattern for Every Command
- Example: `CreateCommand` just wraps `CreateCalendarParser`.

**Suggestion:** Merge into single classes to avoid redundancy.

### 6. Misplaced Export Logic
- `CSVCalendarExporter` is located in view package though it handles business logic.

### 7. Non-Used Functions in Model
- Example: `conflictsWith` exists only for test use.

### 8. Excessive Constructors
- Suggest Builder Pattern for flexibility.

### 9. GUI Dependency
- GUI imports and directly uses `CalendarEvent`, `CalendarManager`.

---

# Documentation

## Strengths
- README gives clear setup steps and known limitations.

## Weaknesses

### 1. Missing Method Documentation
- Many public methods lack comments (e.g., `executeCommand` in multiple parsers).

### 2. Weak Comments
- Example: `getEventsInRange` only states: “Gets events within date range for calendar.”

### 3. Unexplained Refactoring
- No justification in README for redesign between Assignment 4 and 5.

### 4. Hidden Project Size
- Original codebase had 125 files — not mentioned in README or during handoff.

---

## Summary of Suggestions
- Minimize model-controller and model-view coupling.
- Favor interfaces over concrete types.
- Refactor static utilities into dedicated helpers.
- Implement Builder Pattern for object construction.
- Improve documentation quality and coverage.
- Use centralized utilities for repeated logic.
- Avoid view-to-view dependency (e.g., GUI relying on CLI).
