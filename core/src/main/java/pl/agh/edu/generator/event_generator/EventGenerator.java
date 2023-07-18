package pl.agh.edu.generator.event_generator;

import org.json.simple.parser.ParseException;
import pl.agh.edu.json.data.ClientNumberModificationCyclicTemporaryEventData;
import pl.agh.edu.json.data.ClientNumberModificationRandomTemporaryEventData;
import pl.agh.edu.json.data_loader.JSONEventDataLoader;
import pl.agh.edu.model.Time;
import pl.agh.edu.model.calendar.Calendar;
import pl.agh.edu.update.YearlyUpdatable;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Year;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Stream;

public class EventGenerator implements YearlyUpdatable {
    private static EventGenerator instance;
    private final EventLauncher eventLauncher;
    private static final List<ClientNumberModificationCyclicTemporaryEventData> clientNumberModificationCyclicTemporaryEventData = JSONEventDataLoader.clientNumberModificationCyclicTemporaryEventData;
    private static final List<ClientNumberModificationRandomTemporaryEventData> clientNumberModificationRandomTemporaryEventData = JSONEventDataLoader.clientNumberModificationRandomTemporaryEventData;
    private final Map<ClientNumberModificationRandomTemporaryEventData,Integer> lastYearDate;
    private final Time time;

    private final Random random = new Random();


    private EventGenerator(){
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

    public static EventGenerator getInstance(){
        if(instance == null) instance = new EventGenerator();
        return instance;
    }


    private void initializeClientNumberModificationCyclicTemporaryEvents(){
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



    public static void main(String[] args){
        EventGenerator eventGenerator = EventGenerator.getInstance();
    }

    @Override
    public void yearlyUpdate() {
        initializeClientNumberModificationCyclicTemporaryEvents();
    }
}
