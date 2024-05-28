// Most of this code is boilerplate taken from:
// https://developers.google.com/calendar/quickstart/java

// Main runner class for creating/deleting Google Calendar events.
// tinaozhu, winter 2021.

// Google API imports.

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.Events;

// Java library imports.
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.FileReader;
import java.security.GeneralSecurityException;
import java.util.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit; // for `sleep`


public class ClassTimes {
    private static final String TINAS_CALENDAR_ID = "tzhu@greenwichacademy.org";
    private static final String TEST_CALENDAR_ID = "c_k5ih2v866kvl19bikes80u10sk@group.calendar.google.com";
    private static final String APPLICATION_NAME = "Google Calendar API Demo";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";

    /**
     * Global instance of the scopes required by this quickstart.
     * If modifying these scopes, delete your previously saved tokens/ folder.
     * CalendarScopes.CALENDAR_READONLY by default!
     */
    private static final List<String> SCOPES = Collections.singletonList(CalendarScopes.CALENDAR);
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";

    private static final String TIMEZONE = "America/New_York";

    public static void main(String... args) throws IOException, GeneralSecurityException {
        String mode = args[0];
        if (mode.equals("delete")) {
            deleteAllEvents();
        } else if (mode.equals("ms")) {
            createMSEventsFromForm();
        } else if (mode.equals("us")) {
            createUSEventsFromForm();
        } else if (mode.equals("days")) {
            createDayEvents();
        } else {
            System.out.println("Invalid mode given.");
        }
    }

    /**
     * Helper function for sleep functionality.
     */
    private static void sleep(int ms) {
        try {
            TimeUnit.MILLISECONDS.sleep(ms);
        } catch (InterruptedException ex) {
            System.out.println("ERROR: sleep call failed - why?");
        }
    }

    /**
     * Creates an authorized Credential object.
     *
     * @param HTTP_TRANSPORT The network HTTP Transport.
     * @return An authorized Credential object.
     * @throws IOException If the credentials.json file cannot be found.
     */
    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        // Load client secrets.
        InputStream in = ClassTimes.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    private static Event setUpClassEvent(LocalDateTime unzonedStartTime, LocalDateTime unzonedEndTime, String className, String colorId) {
        Instant startInstant = unzonedStartTime.atZone(ZoneId.of(TIMEZONE)).toInstant();

        // Make a calendar event at that time.
        Event event = new Event()
                .setSummary(className)
                .setDescription("");

        DateTime startDateTime = new DateTime(startInstant.toString());
        EventDateTime start = new EventDateTime()
                .setDateTime(startDateTime)
                .setTimeZone(TIMEZONE);
        event.setStart(start);

        // TODO: Have the event end based on classMinutes duration.
        Instant endInstant = unzonedEndTime.atZone(
                ZoneId.of(TIMEZONE)).toInstant();
        DateTime endDateTime = new DateTime(endInstant.toString());
        EventDateTime end = new EventDateTime()
                .setDateTime(endDateTime)
                .setTimeZone(TIMEZONE);
        event.setEnd(end);
        if (!colorId.equals("")) {
            event.setColorId(colorId);
        }

        return event;
    }

    private static void actuallyCreateEvents(
            Calendar service, ArrayList<Event> allEvents,
            String calendarId) throws IOException {
        // Get this from Settings > Integrate calendar.
//        Scanner sc = new Scanner(System.in);
//        System.out.println("Are you sure you want to create these " + allEvents.size() + " events? y for yes.");
        int counter = 0;
        if (true) { // (sc.nextLine().equals("y")) {
            for (int i = 0; i < allEvents.size(); i++) {
                if (counter % 10 == 1) {
                    System.out.println(
                            "Sleeping for 1 second to prevent exceeding rate limit.");
                    // Note: If we create events too quickly in succession,
                    // Google Calendar will interrupt our requests
                    // (anti-spam mechanism probably?).
                    sleep(1);
                }
                Event event = allEvents.get(i);
                event = service.events().insert(calendarId, event).execute();
                System.out.printf("Event created: %s\n", event.getHtmlLink());
                counter++;
            }
        } else {
            System.out.println("Okay, never mind.");
        }
    }

    private static void deleteAllEvents() throws IOException, GeneralSecurityException {
        Scanner sc = new Scanner(System.in);
        System.out.println("** DELETING ALL EVENTS. **");

        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Calendar service = new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();

        System.out.println("Calendar ID?");
        String calendarId = sc.nextLine();

        String pageToken = null;
        do {
            Events events = service.events().list(calendarId).setPageToken(pageToken).execute();
            List<Event> items = events.getItems();
            int counter = 0;
            for (Event event : items) {
                if (counter % 10 == 1) {
                    System.out.println(
                            "Sleeping for .5 second to prevent exceeding rate limit.");
                    // Note: If we delete events too quickly in succession,
                    // Google Calendar will interrupt our requests
                    // (anti-spam mechanism probably?).
                    sleep(1000);
                }
                String eventId = event.getId();
                service.events().delete(calendarId, eventId).execute();
                System.out.println("Deleted event: " + event.getSummary());
                counter++;
            }
            pageToken = events.getNextPageToken();
        } while (pageToken != null);
    }

    private static ArrayList<ArrayList<String>> getDaysToBlocksMapping(String pathToCsv, boolean sevenEight) throws IOException {
        BufferedReader csvReader = new BufferedReader(new FileReader(pathToCsv));
        ArrayList<ArrayList<String>> all = new ArrayList<>();
        // Grades 5-6 have 8 total periods, grades 7-8 have 5.
        int numPeriods;
        if (sevenEight) {
            numPeriods = 5;
        } else {
            numPeriods = 8;
        }
        int numDays = 6;
        for (int i = 0; i < numDays; i++) {
            all.add(new ArrayList<>());
        }
        String row = csvReader.readLine();
        while (row != null) {
            String[] tmp = row.split(",", -1);
            if (tmp.length != numDays) {
                System.out.println("!!!! invalid input schedule, should have " + numDays + " columns !!!!");
            }
            for (int i = 0; i < tmp.length; i++) {
                all.get(i).add(tmp[i]);
            }
            row = csvReader.readLine();
        }
        for (int i = 0; i < numDays; i++) {
            if (all.get(i).size() != numPeriods) {
                System.out.println("!!!! invalid input schedule, should have " + numPeriods + "rows !!!!");
                System.out.println(all.get(i));
            }
        }
        csvReader.close();
        System.out.println(all);
        return all;
    }

    private static ArrayList<LocalDate> getSchoolDates(String division) throws IOException {
        ArrayList<LocalDate> schoolDates = new ArrayList<>();
        LocalDate thisDate, endDate;
        if (division.equals("ms")) {
            thisDate = MSSchedule.firstDayOfSchool;
            endDate = MSSchedule.lastDayOfSchool;
        } else {
            thisDate = USSchedule.firstDayOfSchool;
            endDate = USSchedule.lastDayOfSchool;
        }
        Schedule sch;
        while (!thisDate.isAfter(endDate)) {  // Incl. of last day.
            if (division.equals("us")) {
                sch = getUSScheduleForDate(thisDate, true);
            } else {
                // TODO: Extract method.
                ArrayList<ArrayList<String>> mapping = getDaysToBlocksMapping("schedules/example.csv", true);
                sch = getMSScheduleForDate(thisDate, mapping, true);
            }
            if (sch != null) {  // This is a real school day.
                schoolDates.add(thisDate);
            }
            thisDate = thisDate.plusDays(1);
        }

        System.out.println(
                "Total number of school days in this semester: " + schoolDates.size());
        return schoolDates;
    }

    public static void createUSEventsFromForm() throws IOException, GeneralSecurityException {
        // Build a new authorized API client service.
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Calendar service = new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();

        Scanner sc = new Scanner(System.in);
        System.out.println("What is your name?");
        String username = sc.nextLine();
        System.out.println("Hi there, " + username + "!");

        String calendarId;
        if (username.equals("test")) {
            calendarId = TEST_CALENDAR_ID;
        } else if (username.equals("tina")) {
            calendarId = TINAS_CALENDAR_ID;
        } else {
            System.out.println("What's your calendar ID?");
            calendarId = sc.nextLine();
        }

        // Get list of school days.
        ArrayList<LocalDate> schoolDates = getSchoolDates("us");

        ArrayList<String> courseNames = new ArrayList<>();
        ArrayList<String> colors = new ArrayList<>();
        ArrayList<Integer> upperOrUnder = new ArrayList<>();
        ArrayList<String> blocks = new ArrayList<>();

        String pathToCsv = "schedules/" + username + ".csv";
        BufferedReader csvReader = new BufferedReader(new FileReader(pathToCsv));
        String row = csvReader.readLine();  // Skip the header.
        while ((row = csvReader.readLine()) != null) {
            String[] data = row.split(",");
            blocks.add(data[0].toUpperCase());
            courseNames.add(data[1]);
            if (data[2].equals("yes") || data[2].equals("Yes")) {
                upperOrUnder.add(2);
            } else {
                upperOrUnder.add(1);
            }
            colors.add(data[3]);
        }
        csvReader.close();

        for (int i = 0; i < courseNames.size(); i++) {  // Each course.
            String block = blocks.get(i);
            String className = courseNames.get(i);
            boolean upperclassmen = upperOrUnder.get(i) == 2;
            String colorId = colors.get(i);

            // Generate list of events (classes) to create for this course.
            ArrayList<Event> allEvents = generateUSEvents(schoolDates, block, className, upperclassmen, colorId);

            actuallyCreateEvents(service, allEvents, calendarId);
        }
    }

    public static void createMSEventsFromForm() throws IOException, GeneralSecurityException {
        // Build a new authorized API client service.
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Calendar service = new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();

        Scanner sc = new Scanner(System.in);
        System.out.println("What is your name?");
        String username = sc.nextLine();
        System.out.println("Hi there, " + username + "!");

        String calendarId;
        if (username.equals("tina")) {
            calendarId = "c_l3apeufd2fjk3rskl9dmn2p3hs@group.calendar.google.com";
        } else {
            System.out.println("What's your calendar ID?");
            calendarId = sc.nextLine();
        }

        System.out.println("Grades 7/8? (y/n - n defaults to grades 5/6):");
        boolean sevenEight = sc.nextLine().equals("y");
        System.out.println("Please enter filename where schedule is stored:");
        String form = sc.nextLine();
        ArrayList<ArrayList<String>> daysToBlocks = getDaysToBlocksMapping(form, sevenEight);

        ArrayList<Event> allEvents = new ArrayList<>();
        // Loop through each school date.
        for (LocalDate date : getSchoolDates("ms")) {
            // TODO: Add color coding?
            // Generate list of events (classes/blocks) to create for this day.
            allEvents.addAll(generateMSEvents(date, sevenEight, daysToBlocks));
        }
        actuallyCreateEvents(service, allEvents, calendarId);
    }

    // TODO: Update this.
    public static void createEventsByCommand() throws IOException, GeneralSecurityException {
        // Build a new authorized API client service.
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Calendar service = new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();

        Scanner sc = new Scanner(System.in);
        System.out.println("What is your name?");
        String username = sc.nextLine();
        System.out.println("Hi there, " + username + "!");

        String calendarId;
        if (username.equals("tina")) {
            calendarId = TINAS_CALENDAR_ID;
        } else {
            System.out.println("What's your calendar ID?");
            calendarId = sc.nextLine();
        }

        // Get list of school days.
        ArrayList<LocalDate> schoolDates = getSchoolDates("us");

        while (true) {
            System.out.println("Block? e.g. `A` [or type 'Q' to quit]");
            String block = sc.nextLine();
            if (block.equals("Q")) {
                System.out.println("Bye!");
                break;
            }
            System.out.println("Class name? This will be the name of the event in Calendar.");
            String className = sc.nextLine();
            System.out.println("1 for underclassmen schedule, 2 for upperclassmen.");
            boolean upperclassmen = sc.nextInt() == 2;
            sc.nextLine();
            String colorId = "";
            if (!username.equals("tina")) {
                // These are just hard-coded values for my classes.
                System.out.println("Color ID (int). Press enter to skip and use default calendar color.");
                colorId = sc.nextLine();
                System.out.println("Chosen color ID:" + colorId);
            } else {
                if (block.equals("A")) {
                    colorId = "6";
                } else if (block.equals("B")) {
                    colorId = "7";
                } else if (block.equals("E")) {
                    colorId = "10";
                } else if (block.equals("F")) {
                    colorId = "4";
                }
            }

            // Generate list of events to create.
            ArrayList<Event> allEvents = generateUSEvents(schoolDates, block, className, upperclassmen, colorId);

            actuallyCreateEvents(service, allEvents, calendarId);
        }
    }

    // Only for MS.
    public static void createDayEvents() throws IOException, GeneralSecurityException {
        // Build a new authorized API client service.
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Calendar service = new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();

        Scanner sc = new Scanner(System.in);
        System.out.println("What's your calendar ID?");
        String calendarId = sc.nextLine();

        // Get list of school days.
        ArrayList<LocalDate> schoolDates = getSchoolDates("ms");
        System.out.println("School dates:");
        System.out.println(schoolDates);

        // Generate list of events to create.
        ArrayList<Event> allEvents = new ArrayList<>();
        for (int i = 0; i < schoolDates.size(); i++) {
            LocalDate targetDate = schoolDates.get(i);
            Schedule sch = getMSScheduleForDate(targetDate, getDaysToBlocksMapping("schedules/example.csv", true), true);
            if (sch != null) {
                System.out.println("That date is a Day " + sch.getDayType());
                // Make a calendar event for that day.
                Event event = new Event()
                        .setSummary("MS Day " + sch.getDayType())
                        .setDescription("");

                String dateAsString = targetDate.toString();
                event.setStart(new EventDateTime().setDate(new DateTime(dateAsString)));
                event.setEnd(new EventDateTime().setDate(new DateTime(dateAsString)));
                allEvents.add(event);

            } else {
                System.out.println("Not a school day");
            }
        }

        System.out.println("Generated " + allEvents.size() + " total events.");
        actuallyCreateEvents(service, allEvents, calendarId);
    }

    private static ArrayList<Event> generateMSEvents(LocalDate targetDate, boolean sevenEight, ArrayList<ArrayList<String>> daysToBlocks) throws IOException {
        ArrayList<Event> allEvents = new ArrayList<Event>();
        MSSchedule sch = getMSScheduleForDate(targetDate, daysToBlocks, sevenEight);
        System.out.println("That date is a Day " + sch.getDayType());

        // Loop through all the blocks for that day.
        for (int i = 0; i < sch.blocksForDayType().size(); i++) {
            String className = sch.blocksForDayType().get(i);
            if (className.length() > 0) {  // Could be empty string or null.
                LocalDateTime unzonedStartTime = sch.getStart(targetDate, i);
                LocalDateTime unzonedEndTime = sch.getEnd(targetDate, i);
                String colorId = "";
                allEvents.add(setUpClassEvent(unzonedStartTime, unzonedEndTime, className, colorId));
            }
        }

        System.out.println("Generated " + allEvents.size() + " total events.");
        return allEvents;
    }

    private static ArrayList<Event> generateUSEvents(ArrayList<LocalDate> schoolDates, String block, String className, boolean upperclassmen, String colorId) {
        ArrayList<Event> allEvents = new ArrayList<Event>();
        for (int i = 0; i < schoolDates.size(); i++) {
            LocalDate targetDate = schoolDates.get(i);
            USSchedule sch = getUSScheduleForDate(targetDate, upperclassmen);
            System.out.println("That date is a Day " + sch.getDayType());
            if (sch.isThereBlockToday(block)) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/YY");
                System.out.println(targetDate.format(formatter));
                LocalDateTime unzonedStartTime = sch.getStart(targetDate, block);
                LocalDateTime unzonedEndTime = sch.getEnd(targetDate, block);
                allEvents.add(setUpClassEvent(unzonedStartTime, unzonedEndTime, className, colorId));
            } else {
                System.out.println("No " + block + " block class that day.");
            }
        }

        System.out.println("Generated " + allEvents.size() + " total events.");
        return allEvents;
    }

    public static USSchedule getUSScheduleForDate(LocalDate date, boolean upperclassmen) {
        DayOfWeek day = date.getDayOfWeek();

        USSchedule schedule;

        // Determine if this is an adjusted schedule day.
        if (USSchedule.adjustedSchedules.containsKey(date)) {
            String adjustedType = USSchedule.adjustedSchedules.get(date);
            if (adjustedType.equals("Adjusted1")) {
                schedule = new USAdjusted1(date, upperclassmen);
            } else if (adjustedType.equals("Adjusted2")) {
                schedule = new USAdjusted2(date, upperclassmen);
            } else if (adjustedType.equals("USAdjustedArtsAssembly")) {
                schedule = new USAdjustedArtsAssembly(date, upperclassmen);
            } else if (adjustedType.equals("USAdjustedCommencement")) {
                schedule = new USAdjustedCommencement(date, upperclassmen);
            } else if (adjustedType.equals("USAdjustedHonors")) {
                schedule = new USAdjustedHonors(date, upperclassmen);
            } else if (adjustedType.equals("USAdjustedIngathering")) {
                schedule = new USAdjustedIngathering(date, upperclassmen);
            } else if (adjustedType.equals("USAdjustedMLK")) {
                schedule = new USAdjustedMLK(date, upperclassmen);
            } else if (adjustedType.equals("USAdjustedRehearsal")) {
                schedule = new USAdjustedRehearsal(date, upperclassmen);
            } else if (adjustedType.equals("USAdjustedWinterHoliday")) {
                schedule = new USAdjustedWinterHoliday(date, upperclassmen);
            } else {  // Treat as default.
                schedule = new USAdjusted1(date, upperclassmen);
            }
        } else {
            boolean flexDay = day == DayOfWeek.MONDAY || day == DayOfWeek.TUESDAY || day == DayOfWeek.THURSDAY;
            if (flexDay) {
                schedule = new USFlexDay(date, upperclassmen);
            } else if (day == DayOfWeek.FRIDAY) {
                schedule = new USFriday(date, upperclassmen);
            } else {  // Wed schedule
                schedule = new USWednesday(date, upperclassmen);
            }
            if (schedule.getDayType() == -1) {
                // Not a real school day.
                return null;
            }
        }
        return schedule;
    }

    // sevenEight == true for grades 7-8 schedule, false for grades 5-6
    public static MSSchedule getMSScheduleForDate(LocalDate date, ArrayList<ArrayList<String>> daysToBlocks, boolean sevenEight) {
        MSSchedule schedule;
        DayOfWeek day = date.getDayOfWeek();

        if (sevenEight) {
            if (day == DayOfWeek.MONDAY || day == DayOfWeek.TUESDAY || day == DayOfWeek.THURSDAY) {
                schedule = new MSMTTh78(date, daysToBlocks);
            } else if (day == DayOfWeek.FRIDAY) {
                schedule = new MSF78(date, daysToBlocks);
            } else {  // Wed schedule
                schedule = new MSW78(date, daysToBlocks);
            }
        } else {
            if (day == DayOfWeek.MONDAY || day == DayOfWeek.TUESDAY || day == DayOfWeek.THURSDAY) {
                schedule = new MSMTTh56(date, daysToBlocks);
            } else if (day == DayOfWeek.FRIDAY) {
                schedule = new MSF56(date, daysToBlocks);
            } else {  // Wed schedule
                schedule = new MSW56(date, daysToBlocks);
            }
        }
        if (schedule.getDayType() == -1) {
            // Not a real school day.
            return null;
        }
        return schedule;
    }
}
