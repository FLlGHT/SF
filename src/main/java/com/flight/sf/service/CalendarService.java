package com.flight.sf.service;

import com.flight.sf.common.CategoryDTO;
import com.flight.sf.common.EventDTO;
import com.flight.sf.common.Mapper;
import com.flight.sf.common.TaskDTO;
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
import com.google.api.services.calendar.model.Events;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class CalendarService {

    @Autowired
    private Mapper mapper;

    private static final String APPLICATION_NAME = "Google Calendar API Java Quickstart";

    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

    private static final String TOKENS_DIRECTORY_PATH = "tokens";

    /**
     * Global instance of the scopes required by this quickstart.
     * If modifying these scopes, delete your previously saved tokens/ folder.
     */
    private static final List<String> SCOPES =
            Collections.singletonList(CalendarScopes.CALENDAR_READONLY);
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";


    private Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT)
            throws IOException {
        // Load client secrets.
        InputStream in = CalendarService.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets =
                GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        //returns an authorized Credential object.
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    public Calendar getService() {
        try {
            NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();

            return new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                    .setApplicationName(APPLICATION_NAME)
                    .build();
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public List<EventDTO> getNextEvents(int count) throws IOException {
        Calendar service = getService();
        // List the next 10 events from the primary calendar.
        DateTime now = new DateTime(System.currentTimeMillis());
        Events events = service.events().list("primary")
                .setMaxResults(count)
                .setTimeMin(now)
                .setOrderBy("startTime")
                .setSingleEvents(true)
                .execute();
        return mapper.toEventsDTO(events.getItems());
    }

    public List<EventDTO> getLastMonthEvents() throws IOException {
        Calendar service = getService();
        DateTime monthBegin = new DateTime(Date.from(LocalDate.now().withDayOfMonth(1).atStartOfDay(ZoneId.systemDefault()).toInstant()));
        Events events = service.events().list("primary")
                .setTimeMin(monthBegin)
                .setOrderBy("startTime")
                .setSingleEvents(true)
                .execute();
        return mapper.toEventsDTO(events.getItems());
    }

    public List<CategoryDTO> getLastMonthProductivity() throws IOException, ParseException {
        Calendar service = getService();
        DateTime monthBegin = new DateTime(Date.from(LocalDate.now().withDayOfMonth(1).atStartOfDay(ZoneId.systemDefault()).toInstant()));

        List<Event> events = service.events().list("primary")
                .setTimeMin(monthBegin)
                .setOrderBy("startTime")
                .setSingleEvents(true)
                .execute()
                .getItems();

        Map<String, CategoryDTO> categories = new HashMap<>();
        for (Event event : events) {
            String colorId = event.getColorId() == null ? "default" : event.getColorId();
            categories.computeIfAbsent(colorId, param -> new CategoryDTO()).setName(colorId);
            CategoryDTO category = categories.get(colorId);

            Map<String, TaskDTO> tasks = category.getTasks();
            tasks.computeIfAbsent(event.getSummary(), param -> new TaskDTO()).setName(event.getSummary());

            TaskDTO task = tasks.get(event.getSummary());
            task.setMillis(task.getMillis() + (event.getEnd().getDateTime().getValue() - event.getStart().getDateTime().getValue()));
        }

        Collection<CategoryDTO> collection = categories.values();
        return new ArrayList<>(collection);
    }
}