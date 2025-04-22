
**Design Critique**:

**Strengths**:

1. The project tries to follow MVC principles.  
2. Separate classes are used for each command, thus allowing extending the code to support new commands with minimal code changes in existing code.  
3. The classes for different views are separated properly and GUI classes are in the `view.gui` package and CLI interface classes are in `view.text`.  
4. The Model interface is created properly and it provides a good abstraction.  
5. Helper methods are private in their respective classes.  

**Limitations**:

1. **MVC architecture exists only in folder structure**  

The project has packages with names `model` and `controller`, but it doesn't follow the MVC principles. There exists high coupling between the model and the controller.  
As per MVC design, a single class from the controller should be used to interact with the model.  
Multiple classes (or components) from controllers should not interact with multiple classes (or components) in the model. This leads to high coupling between the model and the controller.  

`CreateCommandParser` from controller interacts with `CalendarManager`, `EventStorage`, `RecurringEventStorage`, `CalendarEvent` in model.  
`EditCommandParser` from controller interacts with `CalendarEvent`, `EditEvent`, `EventStorage` in model.  
`CommandParser` from controller interacts with `CalendarManager`, `EventStorage` in model.  

This creates high coupling between the model and the controller.  

**Suggestion**: Creating a single class to interact with the model will reduce the coupling between the model and the controller.  

2. **Deviating from OOP and blurring the line between functional and OOP**  

The `EditEvent` class also defines the properties to be edited, `EventProperty`, as an `enum` and provides a public static function `getEventPropertyFromString`. This is not a good practice. Static methods can live in utility/helper classes that are not tied to any object.  
But this is violated here. This functionality could have been in a separate class. Other static methods in `EditEvent` are `executeMultipleEdits`, `createTempEventWithNewTime`, `parseDateTime`.  

The `EditEvent` class has been used by creating objects and also as a static class by invoking its static functions.  

**Suggestion**: Avoid using static methods for classes for which objects can be created.  

3. **No proper interface to represent an Event**  

There's no abstract representation for the `Event` available.  
The interface `Event` has only two public methods: `getSubject()` and `getStartDateTime()`.  
The concrete implementations of interface `Event` are `CalendarEvent` and `RecurringEventStorage`.  

`CalendarEvent` has the public methods `setStartDateTime`, `setEndDateTime`, `setSubject`,  
`getEndDateTime`, `getDescription`, `setDescription`, `getLocation`,  
`setLocation`, `getEventType`, `setEventType`, `isRecurring`, `conflictsWith`, `isAllDayEvent`, `getDuration`.  

All these public methods could have been declared in the `Event` interface to represent the `Event`.  
The interface `Event` is just namesake and serves no purpose as all the functions are declared and defined in the concrete class `CalendarEvent`.  

The same goes for the concrete implementation `RecurringEventStorage` which also extends `Event`.  
Methods like `getOccurrences`, `setOccurrences`, `generateOccurrences`, `addRecurringEvents`, `printOccurrences` are all public methods in the concrete class.  

All public methods must have a declaration in the interface but it's not followed in the design.  

Other concrete class implementations which have the same flaw of declaring and defining public methods in the concrete implementation are:  
- `EventStorage` has `addEvent`, `updateEventStartTime`, `findEvent`, `getEventsOnDate`, `getEventsInRange`, `getAllEvents`, `removeEvent`.  
- `EventCopier` has `copyEvent`, `copyEventsOnDate`, `copyEventsBetween`.  
- `EditEvent` has `executeEdit`, `executeMultipleEdits`.  

A proper analysis should have been done, and if these functions were really needed to be public, then an interface should have been created and they should have been declared there.  
If there was no reason for them to be public, then these methods should have been made private as helper methods.  
Having public functions in a concrete class without an interface is not recommended.  

**Suggestion**: Creating proper interfaces is recommended.  

4. **Variables are concrete classes, not interfaces when they can be**  

The concrete class `CalendarEvent` is instantiated multiple times and its objects are not of its interface type `Event`.  
The same applies to the class `RecurringEventStorage` and class `Calendar`.  

5. **The violation of MVC architecture**  
`ShowStatusParser` and `PrintCommandParser` from controller invoke the `CalendarPrinter` functions.  
`CalendarEvent` and `EventStorage` classes are used in the `CalendarPrinter` functions.  
The view has dependency with the model.  
A single controller class could have been used to interact with the view.  
And events from the model could be sent as a `List<Map>` where each map entry represents a key-value pair.  
A single event will be represented as a map as `{ "subject": "Event", "location": "online", ... }`.  
This could have removed the dependency of model classes from the view.  

6. **The concrete class `CalendarPrinter` used in test-based view (for Assignment 4 and 5) has no interface**  
All the methods inside the class `CalendarPrinter`—`printEventsOnDate`, `printEventsInRange`, `showStatusOnDateTime`—are public.  

7. **High coupling between GUI and text-based view**  

The GUI indirectly uses the text view to show events on the GUI.  
The GUI gets input from the user and builds the commands.  
If the user enters date "2025-04-01" to view the events on date, then the GUI view will create the command  
"Print events on 2025-04-01" and the GUI will invoke the `PrintCommandParser` which will use `CalendarPrinter` to print events on the console.  
The GUI view will capture `System.out.print` values as a string and will display it on the GUI.  
GUI cannot exist independently.  
It's good design that different views should be independent and there should not be any coupling between different views, which is violated here.  

**Suggestion**: If one view depends on another view then it creates a high coupling scenario which should be avoided.  

8. **The class `CalendarCSVImporter` to support import functionality is in the Controller package and it has a public method `importFromCSV()` and no interface is provided to access this public method**  

**Suggestion**: Creating objects of type interface.  

---

**Implementation Critique**:

**Strengths**:

1. The model uses `Map<LocalDateTime, List<CalendarEvent>>`, thus making searching and retrieving events more efficient.  
2. The code tries to avoid heavy duplication by modularizing functions and reusing them properly.  

**Limitations**:

1. **No centralized class for DateTime functionality**  

There's no separate class to handle the date and time functionality, which is leading to code duplication in multiple places.  
One instance is `parseDateTime` in `CreateCommandParser` and `parseDateTime` in `EditCommandParser`, which serve the same functionality but the code is duplicated.  
The format `yyyy-MM-dd'T'HH:mm` is defined at multiple places in view, model, and also in controller.  

2. **Use of static methods in `CreateCommandParser`**  
The class has private static methods: `extractRepeatDays`, `extractRepeatTimes`, `extractEventName`, `extractDateTime`, `extractField`, `parseDateTime`.  
The use of static here serves no purpose as they are private and used only inside the `CreateCommandParser` class.  

3. **Command Design Pattern**  

According to SOLID principles, code should be Open for extension and Closed for modification.  
Even though each command is handled by a separate class,  
if we need to implement a new command, there's no way to do it unless we edit the existing class `CommandParser`.  
The class `CommandParser` needs to be edited as switch-case is used to map commands to classes instead of a Map.  

4. **Code duplication in `CommandParser`**  

In `CommandParser`, the two functions `createCommand`, `extractMainCommand` have the same cases: `create calendar`, `create`, `edit calendar`, etc.  
This could be combined into a single function.  
In the future, if we need to support new commands, both the functions now will need to be edited with the same case.  
This could be avoided by combining them into a single function; both the functions `createCommand` and `extractMainCommand` serve the same purpose and are in the same class.  

5. **Requiring 2 classes for a single command and code duplication in package `controller.command`**  

The classes inside the folder `controller/command` such as `CopyCommand`, `CreateCalendarCommand`, `CreateCommand`, `EditCalendarCommand`, `EditCommand`,  
`ExportCommand`, `PrintCommand`, `ShowStatusCommand`, `UseCalendarCommand` have the same code with the exception of a single variable in all the classes.  

All these in-turn call another in their `execute` method.  

- `CreateCommand` invokes `CreateCalendarParser`'s `executeCommand`  
- `EditCommand` invokes `EditCommandParser`'s `executeCommand` and so on.  

This applies for all the classes which leads to using 2 classes for a single command with heavy duplication across the whole `controller.command` package.  

The `CreateCalendarParser`'s `executeCommand` could have been implemented in the `CreateCommand` itself.  

6. **Even though class `CSVCalendarExporter` implements an interface to support functionality, it is nested inside the View package**  
The view package should have classes only related to view and it's not a good place for the export functionality to reside in.  

7. **The method `conflictsWith` in `CalendarEvent` is used only for writing test cases and it's not used anywhere in the model**  
It's not recommended to have methods which are used only in test cases.  

8. **Implementing too many constructors**  
The `CalendarEvent` has 4 constructors with variable parameters, and  
the `RecurringEventStorage` has 3 constructors with variable parameters.  
Design patterns such as the Builder Pattern could have been used to eliminate this.  

9. **No code extension was done when implementing Assignment 5**  

To support additional functionalities requested in Assignment 5, the code from Assignment 4 for model was completely rewritten.  
New classes and interfaces were not created to support new functionalities.  
This also led to rewriting all the existing test cases since now creating a Calendar requires calendar name and time zone values.  

**Suggestion**: Implementation should be done in such a way that existing test cases should not have to be rewritten again and again when new functionality is requested.  

10. **GUI dependency on the Model**  

The GUI depends on the model classes and imports `CalendarEvent`, `CalendarManager`.  
Any change in these classes will result in changes in the GUI view also.  

11. **Additionally, the main method instantiates and calls the view directly (`CalendarFrame` in `Main` method), which is not ideal as it breaks the proper flow of MVC architecture**.  

---

**Documentation**:

**Strengths**:  
1. The README has proper instructions on how to use the program.  
2. The README also talks about features which are not implemented.  

**Weaknesses**:  

1. Some public methods don't have comments.  
E.g.,  
- `CopyEventParser` has public method `executeCommand` with no comments.  
- `EditCalendarParser` has public method `executeCommand` with no comments.  
- `EditCommandParser` has public method `executeCommand` with no comments.  
- `ShowStatusParser` has public method `executeCommand` with no comments.  
- `CalendarEvent` has public methods `setStartDateTime`, `setEndDateTime`, `getDuration` with no comments.  
- `RecurringEventStorage` has public methods `getEventStorage`, `setEventStorage` with no comments.  
- `CalendarFrame` has multiple public methods with no comments.  
- `GUIViewImpl` has multiple public methods with no comments.  

2. Some private methods also don't have comments.  
- All private methods in `EditCommandParser` have no comments.  
- All private methods in `CalendarManager` have no comments.  
- All private methods in `EditEvent` class have no comments.  
- All GUI-related classes (`CreateCalendarDialog`, `CreateEventDialog`, `EditCalendarDialog`, `EditEventDialog`) have many private methods with no comments.  
- `CalendarFrame` has multiple private methods with no comments.  
- `GUIViewImpl` has multiple private methods with no comments.  

3. **Comments are not detailed enough**  

The function signature of `getEventsInRange` in `MultiCalendarEventStorage` is:  

```java
public List<CalendarEvent> getEventsInRange(String calendarName, LocalDateTime start, LocalDateTime end)
```  

The comment for this method is: "Gets events within date range for calendar."  
The comment has no details about the input parameters or return type.  

There are multiple methods like these in the codebase with non-detailed descriptions and no mention of return types and input parameters.  

4. **The README has no mention of why the model design completely changed from Assignment 4 to Assignment 5**  
The Assignment 4 design was not extended and new classes and interfaces were not created.  
Instead, the existing classes were edited and the code was not extended using SOLID principles, and this is not mentioned in the README.  

5. **The codebase had exactly 125 files when it was given to us and this critical detail was not mentioned anywhere in the README or even informed verbally.**


### Final Suggestions and Conclusion

It would be better if:

1. The project follows the MVC pattern and SOLID principles more strictly.  
2. Design patterns such as the Command Pattern and Builder Pattern are used wherever applicable.  
3. Static functions are avoided unless absolutely necessary.  
4. Code duplication is minimized.  
5. All public functions are documented with descriptions, parameter details, and return type information.  
6. Critical information about the project is clearly mentioned in the README file.
