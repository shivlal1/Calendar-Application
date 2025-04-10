1. Steps to run the GUI :
   - Go to the CalendarApp. 
   - Click the Run option in the top right corner.
   - An interactive calendar gui will pop up in your screen.

2. Main GUI Interface :
   ![guifront.jpeg](guifront.jpeg)
   - This is the main screen of the GUI that pops up.
   - The top panel shows current month and year.
   - By clicking the '<' and '>' arrows, we can move to previous or next month. 
   - In the next panel we have :
        * Dropdown to switch between calendars.
        * Create Calendar button.
        * Create event button.

        * Edit Across Calendar to change the properties of event.
        * Show events.
   - The calendar shows April month of year 2025.
   - If an event is created the box of specific date gets coloured.
   - In the bottom panel we have:
        * The name and timezone of calendar we are currently in.
        * Edit calendar to change the name or timezone of calendar.
        * Export calendar button to export the calendar to a csv file.
        * Import calendar button to import events to the calendar as a csv file.

3. Creating a Calendar : 
    - Click on create calendar.
    - Enter the new calendar name.
    - Enter the timezone of new calendar.
    - Click OK.
    - A message "Calendar created successfully" appears.
    - If you give wrong timezone you get the message Invalid Zone Id : Error creating calendar.
    - If you create a calendar with the name that is already taken; you get the message - Error creating Calendar : Calendar already exists with same name.

4. Switching between Calendars :
     - Click the dropdown next to Select Calendar.
     - Choose the calendar you want to view or work on.
     - Once you choose a calendar, notice the bottom panel changing to the calendar name with timezone you selected.

5. Creating an event : 
    - Click on Create Event.
    - Select event type. Choose what event you want to create - Single or Recurring.
    - Creating a Single Event :
      * Choose if it's an event with a specific start and end date time (timed) or all day event (All-day). 
      * All day event :
        - Enter the subject of the event.
        - Enter the date-time in the format yyyy-mm-ddThh:mm
        - Event created successfully. 
      * Timed event :
        - Enter the subject of the event.
        - Enter the start date-time in the format yyyy-mm-ddThh:mm
        - Enter the start date-time in the format yyyy-mm-ddThh:mm
        - Event Created Successfully.
    - Creating a Recurring Event :
      * Choose if it's an event with a specific start and end date time (timed) or all day event (All-day).
      * All day event :
          - Choose if you want the event to repeat until a specific date (Until date) or Repeat for number of times.
          - Repeat Until date :
              * Enter the subject of the event.
              * Enter the start date in the format yyyy-mm-dd.
              * Enter the weekdays you want the event to repeat on. 
              * Enter the end date until which you want the event to repeat in the format yyyy-mm-dd.
              * Event Created Successfully.
          - Repeat for N number of times : 
              * Enter the subject of the event.
              * Enter the start date in the format yyyy-mm-dd.
              * Enter the weekdays you want the event to repeat on.
              * Enter the number of occurrences.
              * Event Created Successfully.
      * Timed event :
          - Choose if you want the event to repeat until a specific date (Until date) or repeat for number of times.
          - Repeat Until date :
              * Enter the subject of the event.
              * Enter the start datetime in the format yyyy-mm-ddThh:mm. ex : 2025-04-01T10:00
              * Enter the end time in the format yyyy-mm-ddThh:mm.   ex : 2025-04-01T12:00
              * Enter the weekdays you want the event to repeat on.
              * Enter the end datetimestring until which you want the event to repeat in the format yyyy-mm-ddThh:mm.
              * Event Created Successfully.
          - Repeat for N number of times :
              * Enter the subject of the event.
              * Enter the start datetime in the format yyyy-mm-ddThh:mm. 
              * Enter the end time in the format yyyy-mm-ddThh:mm.
              * Enter the weekdays you want the event to repeat on.
              * Enter the number of occurrences.
              * Event Created Successfully.
    - If the input is invalid you get the error message - Invalid datetime or property.
    - if you try to create two events that conflict with each other you get a message - Event conflict occurred, and it is not created.
   
6. Editing a calendar :
    - Click on edit calendar in the bottom panel.
    - This edits the current calendar being used.
    - Choose which property to edit : Timezone or Name.
    - Edit Timezone of a calendar : 
      - Enter the timezone in IANA format.
      - Calendar Updated Successfully.
      - If you give the wrong timezone you get the message that the timezone is invalid.
   - Edit Name of a calendar :
       - Enter the new name of calendar.
       - Calendar Updated Successfully.

7. Showing the events :
    - Enter the start datetime in the format yyyy-mm-ddThh:mm.
    - Enter the end datetime in the format yyyy-mm-ddThh:mm.
    - If the events are present, they are printed in the bulletin format.
    - If no events present you get message No events.
   * Showing the events on a specific date : 
    - If you click the date on the calendar, you get the events on the date.

8. You can view the events by clicking on a specific day.

9. Editing events :
    * Edit property of all events with the same event name :
      - Click on Edit Across Calendar button.
      - Enter the name of the event you want to edit.
      - Select the property you want to edit.
      - Edit name of event: 
        - Enter new name.
        - Events updated successfully across calendar.
      - Edit start date time of event: 
        - Enter new start date time in the format yyyy-mm-ddThh:mm, else you get invalid date time property error.
        - Events updated successfully across calendar.
      - Edit end date time of event:
        - Enter new end date time in the format yyyy-mm-ddThh:mm, else you get invalid date time property error.
        - Events updated successfully across calendar.
      - Edit description of event:
          - Enter new description.
          - Events updated successfully across calendar.
      - Edit location of event:
          - Enter new location.
          - Events updated successfully across calendar.
      - Edit whether event is public or private:
        - Enter true or false.
        - Events updated successfully across calendar.
    * Edit property of events starting at a specific date/time and have the same event name: 
      - Click on the day with the event, you want to edit.
      - Select Edit Events After the Currentday.
      - Enter Event Name.
      - Select property you want to edit.
        - Edit name of event:
            - Enter new name.
            - Events updated successfully across calendar.
        - Edit start date time of event:
            - Enter new start date time in the format yyyy-mm-ddThh:mm, else you get invalid date time property error.
            - Events updated successfully across calendar.
        - Edit end date time of event:
            - Enter new end date time in the format yyyy-mm-ddThh:mm, else you get invalid date time property error.
            - Events updated successfully across calendar.
        - Edit description of event:
            - Enter new description.
            - Events updated successfully across calendar.
        - Edit location of event:
            - Enter new location.
            - Events updated successfully across calendar.
        - Edit whether event is public or private:
            - Enter true or false.
            - Events updated successfully across calendar.
   * Edit property of given specific event :
    - Click on the day with the event, you want to edit.
    - Select Edit Specific Event.
    - Select property you want to edit.
        - Edit name of event:
            - Enter new name.
            - Events updated successfully across calendar.
        - Edit start date time of event:
            - Enter new start date time in the format yyyy-mm-ddThh:mm, else you get invalid date time property error.
            - Events updated successfully across calendar.
        - Edit end date time of event:
            - Enter new end date time in the format yyyy-mm-ddThh:mm, else you get invalid date time property error.
            - Events updated successfully across calendar.
        - Edit description of event:
            - Enter new description.
            - Events updated successfully across calendar.
        - Edit location of event:
            - Enter new location.
            - Events updated successfully across calendar.
        - Edit whether event is public or private:
            - Enter true or false.
            - Events updated successfully across calendar.
    * If you enter an invalid property, you get error message stating Invalid Datetime or property.

10. Exporting the calendar :
    - Click on the export calendar button in the bottom.
    - This exports the current calendar we are using.
    - Message pops up :  Export Successful. File created as CalendarNameEvents.csv.
    - The exported calendar is found in the project file.

11. Importing a calendar :
    - Click on the import calendar button in the bottom.
    - Enter the path to the csv file you want to import.
    - Import Successful.
    - Click OK. Now your calendar will have all the imported events from the csv file.
    - If you give an invalid file path, error message pops up saying error importing calendar.