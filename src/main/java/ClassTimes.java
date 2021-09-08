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
import com.google.api.client.json.jackson2.JacksonFactory;
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
import java.util.concurrent.TimeUnit; // for `sleep`


public class ClassTimes {
    private static final String TINAS_CALENDAR_ID = "c_3u311s0vrhfpfubgs9p5jjhheg@group.calendar.google.com";

    private static final String APPLICATION_NAME = "Google Calendar API Demo";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";


    /**
     * Global instance of the scopes required by this quickstart.
     * If modifying these scopes, delete your previously saved tokens/ folder.
     * CalendarScopes.CALENDAR_READONLY by default!
     */
    private static final List<String> SCOPES = Collections.singletonList(CalendarScopes.CALENDAR);
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";

    /**
     * Helper function for sleep functionality.
     */
    private static void sleep(int s) {
        try {
            TimeUnit.SECONDS.sleep(s);
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
        InputStream in = CalendarQuickstart.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
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

    private static Event setUpClassEvent(
            LocalDate targetDate,
            String block,
            String className,
            boolean upperclassmen,
            String colorId) {

        String timezone = "America/New_York";

        // Time zone wrangling.
        LocalDateTime unzonedDateTime = GAPlanner21_22.dateTimeForBlock(targetDate, block, upperclassmen);
        System.out.println("Start time for the block:" + unzonedDateTime);
        Instant startInstant = unzonedDateTime.atZone(ZoneId.of(timezone)).toInstant();

        // Make a calendar event at that time.
        Event event = new Event()
                .setSummary(className)
                .setDescription("");

        DateTime startDateTime = new DateTime(startInstant.toString());
        EventDateTime start = new EventDateTime()
                .setDateTime(startDateTime)
                .setTimeZone(timezone);
        event.setStart(start);

        // Have the event end based on classMinutes duration.
        int classMinutes = GAPlanner21_22.getClassDuration(targetDate, block);
        Instant endInstant = unzonedDateTime.plusMinutes(classMinutes).atZone(ZoneId.of(timezone)).toInstant();
        DateTime endDateTime = new DateTime(endInstant.toString());
        EventDateTime end = new EventDateTime()
                .setDateTime(endDateTime)
                .setTimeZone(timezone);
        event.setEnd(end);
        if (colorId != "") {
            event.setColorId(colorId);
        }

        System.out.println("Finished setting up event:");
        return event;
    }

    private static void actuallyCreateEvents(
            Calendar service, ArrayList<Event> allEvents,
            String calendarId) throws IOException, GeneralSecurityException {
        // Get this from Settings > Integrate calendar.
        Scanner sc = new Scanner(System.in);
        System.out.println("Are you sure you want to create these " + allEvents.size() + " events? Y for yes.");
        int counter = 0;
        if (sc.nextLine().equals("Y")) {
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

    private static void deleteAllEvents(Calendar service) throws IOException, GeneralSecurityException {
        Scanner sc = new Scanner(System.in);
        System.out.println("What's your calendar ID?");
        String calendarId = sc.nextLine();
        System.out.println("Deleting all events.");
        String pageToken = null;
        do {
            Events events = service.events().list(calendarId).setPageToken(pageToken).execute();
            List<Event> items = events.getItems();
            int counter = 0;
            for (Event event : items) {
                if (counter % 10 == 1) {
                    System.out.println(
                            "Sleeping for 1 second to prevent exceeding rate limit.");
                    // Note: If we delete events too quickly in succession,
                    // Google Calendar will interrupt our requests
                    // (anti-spam mechanism probably?).
                    sleep(1);
                }
                String eventId = event.getId();
                service.events().delete(calendarId, eventId).execute();
                System.out.println("Deleted event: " + event.getSummary());
                counter++;
            }
            pageToken = events.getNextPageToken();
        } while (pageToken != null);
    }

    public static void parseForm() throws IOException, GeneralSecurityException {
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
        // Be careful with this!
        // deleteAllEvents(service);

        // Get list of school days.
        TreeMap schoolDays = GAPlanner21_22.getSchoolDaysMap();
        ArrayList<String> schoolDates = new ArrayList<String>(schoolDays.keySet());
        System.out.println(
                "Total number of school days in this semester: " + schoolDates.size());

        ArrayList<String> courseNames = new ArrayList<>();
        ArrayList<String> colors = new ArrayList<>();
        ArrayList<Integer> upperOrUnder = new ArrayList<>();
        ArrayList<String> blocks = new ArrayList<>();

        String pathToCsv = "courses.csv";
        BufferedReader csvReader = new BufferedReader(new FileReader(pathToCsv));
        String row = csvReader.readLine();  // Skip the header.
        while ((row = csvReader.readLine()) != null) {
            String[] data = row.split(",");
            blocks.add(data[0].toUpperCase());
            courseNames.add(data[1]);
            if (data[2].equals("yes")) {
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
            ArrayList<Event> allEvents = new ArrayList<Event>();
            for (int j = 0; j < schoolDays.size(); j++) {
                // This is iffy. Shouldn't index into a set like this.
                String dateString = schoolDates.get(j);
                LocalDate targetDate = LocalDate.parse(dateString);
                System.out.println("That date is a Day " + schoolDays.get(dateString));
                if (GAPlanner21_22.isThereClassToday(dateString, block, schoolDays)) {
                    Event event = setUpClassEvent(targetDate, block, className, upperclassmen, colorId);
                    allEvents.add(event);
                } else {
                    System.out.println("No " + block + " block class that day.");
                }
            }

            System.out.println("Generated " + allEvents.size() + " total events.");
            actuallyCreateEvents(service, allEvents, calendarId);
        }


    }

    public static void main(String... args) throws IOException, GeneralSecurityException {
        parseForm();
    }

    public static void promptUser() throws IOException, GeneralSecurityException {
        // Build a new authorized API client service.
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Calendar service = new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();

        // Be careful with this!
        // deleteAllEvents(service);

        // Get list of school days.
        TreeMap schoolDays = GAPlanner21_22.getSchoolDaysMap();
        ArrayList<String> schoolDates = new ArrayList<String>(schoolDays.keySet());
        System.out.println(
                "Total number of school days in this semester: " + schoolDates.size());

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
            ArrayList<Event> allEvents = new ArrayList<Event>();
            for (int i = 0; i < schoolDays.size(); i++) {
                // This is iffy. Shouldn't index into a set like this.
                String dateString = schoolDates.get(i);
                LocalDate targetDate = LocalDate.parse(dateString);
                System.out.println("That date is a Day " + schoolDays.get(dateString));
                if (GAPlanner21_22.isThereClassToday(dateString, block, schoolDays)) {
                    Event event = setUpClassEvent(targetDate, block, className, upperclassmen, colorId);
                    allEvents.add(event);
                } else {
                    System.out.println("No " + block + " block class that day.");
                }
            }

            System.out.println("Generated " + allEvents.size() + " total events.");
            actuallyCreateEvents(service, allEvents, calendarId);
        }
    }
}
