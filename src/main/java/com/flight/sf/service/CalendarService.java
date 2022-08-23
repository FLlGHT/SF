package com.flight.sf.service;

import com.flight.sf.common.CategoryColor;
import com.flight.sf.common.StatsDTO;
import com.flight.sf.common.TaskDTO;
import com.flight.sf.utilities.DateUtils;
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
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class CalendarService {

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


    public void getProductivityByWeek(Model model) throws IOException {
        List<Event> events = getMonthEvents();
        Map<String, TaskDTO> tasks = new HashMap<>();
        StatsDTO stats = new StatsDTO();

        long totalTime = ChronoUnit.MILLIS.between(LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0), LocalDateTime.now());
        long totalProductiveTime = 0;
        int weeksNumber = 0;

        for (Event event : events) {
            TaskDTO task = tasks.computeIfAbsent(event.getSummary().toLowerCase(), param -> new TaskDTO());
            long eventDuration = eventDuration(event);
            int weekNumber = DateUtils.weekNumber(event.getEnd().getDateTime().getValue());
            weeksNumber = Math.max(weekNumber, weeksNumber);

            Map<Integer, Long> taskTimeByWeek = task.getMillisByWeek();
            taskTimeByWeek.put(weekNumber, taskTimeByWeek.get(weekNumber) + eventDuration);

            Map<Integer, Long> totalTimeByWeek = stats.getProductiveTimeByWeek();
            totalTimeByWeek.put(weekNumber, totalTimeByWeek.get(weekNumber) + eventDuration);
            totalProductiveTime += eventDuration;

            task.setName(event.getSummary());
            task.addMillis(eventDuration);
            task.setCategory(CategoryColor.getCategoryNameById(event.getColorId()));
        }

        stats.setTotalProductiveTime(DateUtils.millisToDate(totalProductiveTime));
        stats.setTotalTime(DateUtils.millisToDate(totalTime));
        stats.setTotalPercentage(String.format("%,.2f", totalProductiveTime / (totalTime * 0.66) * 100));
        stats.setWeeksNumber(weeksNumber);

        addAttributes(model, new ArrayList<>(tasks.values()), stats);
    }

    private void addAttributes(Model model, List<TaskDTO> tasks, StatsDTO stats) {
        model.addAttribute("tasks", tasks);
        model.addAttribute("stats", stats);
    }

    private List<Event> getMonthEvents() throws IOException {
        Calendar service = getService();
        DateTime monthBegin = new DateTime(
                Date.from(LocalDate.now().withDayOfMonth(1).atStartOfDay(ZoneId.systemDefault()).toInstant()));

        return service.events().list("primary")
                .setTimeMin(monthBegin)
                .setOrderBy("startTime")
                .setSingleEvents(true)
                .execute()
                .getItems();
    }

    private long eventDuration(Event event) {
        return event.getEnd().getDateTime().getValue() - event.getStart().getDateTime().getValue();
    }

    public StatsDTO getTotalStats(List<TaskDTO> tasks) {
        long productiveTime = tasks.stream().map(TaskDTO::getMillis).reduce(0L, Long::sum);
        long totalTime = ChronoUnit.MILLIS.between(LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0), LocalDateTime.now());
        double percentage = productiveTime / (totalTime * 0.66);
        int weeksNumber = tasks.stream().map(task -> task.getMillisByWeek().keySet().stream().max(Comparator.naturalOrder()).orElse(4))
                               .max(Comparator.naturalOrder()).orElse(4);

        return new StatsDTO(productiveTime, totalTime, percentage, weeksNumber);
    }
}