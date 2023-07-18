package pl.agh.edu.generator.event_generator;

import org.json.simple.parser.ParseException;
import pl.agh.edu.json.data.ClientNumberModificationCyclicTemporaryEventData;
import pl.agh.edu.model.Time;
import pl.agh.edu.model.calendar.Calendar;
import pl.agh.edu.model.calendar.CalendarEvent;
import pl.agh.edu.model.event.temporary.ClientNumberModificationTemporaryEvent;
import pl.agh.edu.model.event.temporary.ClientNumberModificationTemporaryEventHandler;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Random;

public class EventLauncher {
    private static EventLauncher instance;
    private final Calendar calendar;
    private final ClientNumberModificationTemporaryEventHandler  clientNumberModificationTemporaryEventHandler;
    private final PriorityQueue<ClientNumberModificationRandomTemporaryEvent> eventsToLaunch;
    private final Time time;
    private final Random random = new Random();

    private EventLauncher() throws IOException, ParseException {
        calendar = Calendar.getInstance();
        clientNumberModificationTemporaryEventHandler = ClientNumberModificationTemporaryEventHandler.getInstance();
        this.eventsToLaunch = new PriorityQueue<>(Comparator.comparing(ClientNumberModificationRandomTemporaryEvent::launchDate));
        this.time = Time.getInstance();
    }

    public static EventLauncher getInstance() throws IOException, ParseException {
        if(instance == null) instance = new EventLauncher();
        return instance;
    }

    public boolean add(ClientNumberModificationRandomTemporaryEvent event){
        return eventsToLaunch.add(event);
    }

    public void launchEventsForToday(){
        ClientNumberModificationRandomTemporaryEvent event = eventsToLaunch.peek();
        while(event != null && event.launchDate().equals(time.getTime().toLocalDate())){
            launch(Objects.requireNonNull(eventsToLaunch.poll()));
            event = eventsToLaunch.peek();
        }
    }
    public void launchCyclicEventsForYear(Integer year, ClientNumberModificationCyclicTemporaryEventData event) {
        calendar.addEvent(
                new CalendarEvent(
                        LocalDate.of(year, event.startDateWithoutYear().getMonth(), event.startDateWithoutYear().getDayOfMonth()),
                        event.name(),
                        event.calendarDescription()
                ));
        clientNumberModificationTemporaryEventHandler.add(
                new ClientNumberModificationTemporaryEvent(
                        LocalDate.of(year, event.startDateWithoutYear().getMonth(), event.startDateWithoutYear().getDayOfMonth()),
                        LocalDate.of(year, event.endDateWithoutYear().getMonth(), event.endDateWithoutYear().getDayOfMonth()),
                        event.modifiers()

                ));
    }

    private void launch(ClientNumberModificationRandomTemporaryEvent event){
        LocalDate startDate = event.launchDate().plusDays(random.nextInt(80,100));
        String calendarDescription = event.calendarDescription().replaceFirst("#",startDate.toString()).replaceFirst("#",String.valueOf(event.durationDays()));
        String popupDescription = event.popupDescription().replaceFirst("#",String.valueOf(event.durationDays()));
        calendar.addEvent(new CalendarEvent(startDate,event.name(),calendarDescription));
        clientNumberModificationTemporaryEventHandler.add(new ClientNumberModificationTemporaryEvent(startDate,startDate.plusDays(event.durationDays()),event.modifiers()));
        new PopUpEvent(event.name(),popupDescription,event.imagePath());
    }

    public static void main(String[] args) throws IOException, ParseException {
        EventGenerator eventGenerator = EventGenerator.getInstance();
        EventLauncher eventLauncher = EventLauncher.getInstance();
        System.out.println(eventLauncher.eventsToLaunch);
    }

}
