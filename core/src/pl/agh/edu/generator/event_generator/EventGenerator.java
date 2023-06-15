package pl.agh.edu.generator.event_generator;

import org.json.simple.parser.ParseException;
import pl.agh.edu.generator.client_generator.JSONExtractor;
import pl.agh.edu.generator.event_generator.json_data.ClientNumberModificationCyclicTemporaryEventData;
import pl.agh.edu.generator.event_generator.json_data.ClientNumberModificationRandomTemporaryEventData;
import pl.agh.edu.model.Time;
import pl.agh.edu.model.calendar.Calendar;
import pl.agh.edu.model.calendar.CalendarEvent;
import pl.agh.edu.model.event.temporary.ClientNumberModificationTemporaryEvent;
import pl.agh.edu.model.event.temporary.ClientNumberModificationTemporaryEventHandler;


import java.io.IOException;
import java.time.LocalDate;
import java.time.Year;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EventGenerator {
    private static EventGenerator instance;
    private final EventLauncher eventLauncher;
    private final List<ClientNumberModificationCyclicTemporaryEventData> clientNumberModificationCyclicTemporaryEventData;
    private final List<ClientNumberModificationRandomTemporaryEventData> clientNumberModificationRandomTemporaryEventData;
    private final Map<ClientNumberModificationRandomTemporaryEventData,Integer> lastYearDate;
    private final Time time;

    private final Random random = new Random();


    private EventGenerator() throws IOException, ParseException {
        this.clientNumberModificationCyclicTemporaryEventData = JSONExtractor.getClientModificationCyclicTemporaryEventData();
        this.clientNumberModificationRandomTemporaryEventData = JSONExtractor.getClientNumberModificationRandomTemporaryEventData();
        this.lastYearDate = new HashMap<>();
        time = Time.getInstance();
        this.eventLauncher = EventLauncher.getInstance();
        initializeClientNumberModificationCyclicTemporaryEvents();
        initializeClientNumberModificationRandomTemporaryEventsForThisYear();
    }

    private void initializeClientNumberModificationRandomTemporaryEventsForThisYear() {
        int daysInYear = Year.isLeap(time.getTime().getYear()) ? 366 : 365;
        clientNumberModificationRandomTemporaryEventData.stream().filter(
                eventData ->{
                    boolean willOccur = eventData.occurrenceProbability() > random.nextDouble();
                    if(!willOccur) lastYearDate.remove(eventData);
                    return willOccur;
                }
        ).map(eventData ->{
            LocalDate launchDate = LocalDate.ofYearDay(time.getTime().getYear(), random.nextInt((lastYearDate.get(eventData) == null || lastYearDate.get(eventData) < daysInYear/2) ? 1 : daysInYear/2, (daysInYear) + 1));
            lastYearDate.put(eventData,launchDate.getDayOfYear());
            return new ClientNumberModificationRandomTemporaryEvent(
                    eventData.name(),
                    eventData.calendarDescription(),
                    eventData.popupDescription(),
                    random.nextInt(eventData.minDurationDays(), eventData.maxDurationDays()+1),
                    launchDate,
                    eventData.modifiers(),
                    eventData.imagePath());

        }
        ).forEach(eventLauncher::add);
    }

    public static EventGenerator getInstance() throws IOException, ParseException {
        if(instance == null) instance = new EventGenerator();
        return instance;
    }

    private void initializeClientNumberModificationCyclicTemporaryEvents() throws IOException, ParseException {
        Calendar calendar = Calendar.getInstance();
        Stream.iterate(calendar.getGameStartDate().getYear(), year -> year <=calendar.getGameEndDate().getYear(), year -> year+1)
                .forEach(year ->{
                    clientNumberModificationCyclicTemporaryEventData.forEach(
                          event ->{
                              eventLauncher.launchCyclicEventsForYear(year, event);
                          }
                    );
                });
    }



    public static void main(String[] args) throws IOException, ParseException {
        EventGenerator eventGenerator = EventGenerator.getInstance();
    }

}
