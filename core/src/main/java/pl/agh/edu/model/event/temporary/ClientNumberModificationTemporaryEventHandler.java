package pl.agh.edu.model.event.temporary;

import org.json.simple.parser.ParseException;
import pl.agh.edu.enums.HotelVisitPurpose;
import pl.agh.edu.generator.event_generator.EventGenerator;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.PriorityQueue;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ClientNumberModificationTemporaryEventHandler {
    private static ClientNumberModificationTemporaryEventHandler instance;
    private final PriorityQueue<ClientNumberModificationTemporaryEvent> currentClientNumberModificationTemporaryEvents;
    private final PriorityQueue<ClientNumberModificationTemporaryEvent> upcomingClientNumberModificationTemporaryEvents;

    private ClientNumberModificationTemporaryEventHandler(){
        this.currentClientNumberModificationTemporaryEvents = new PriorityQueue<>(Comparator.comparing(TemporaryEvent::getEndDate));
        this.upcomingClientNumberModificationTemporaryEvents = new PriorityQueue<>(Comparator.comparing(TemporaryEvent::getStartDate));
    }

    public static ClientNumberModificationTemporaryEventHandler getInstance(){
        if(instance == null) instance = new ClientNumberModificationTemporaryEventHandler();
        return instance;
    }

    // TODO: 02.06.2023  usunąć currentDate i korzystać z Time
    public void update(LocalDate currentDate){
        ClientNumberModificationTemporaryEvent event = currentClientNumberModificationTemporaryEvents.peek();
        while(event != null && event.getEndDate().isEqual(currentDate)){
            currentClientNumberModificationTemporaryEvents.poll();
            event = currentClientNumberModificationTemporaryEvents.peek();
        }
        event = upcomingClientNumberModificationTemporaryEvents.peek();
        while(event != null && event.getStartDate().isEqual(currentDate)){
            currentClientNumberModificationTemporaryEvents.add(upcomingClientNumberModificationTemporaryEvents.poll());
            event = upcomingClientNumberModificationTemporaryEvents.peek();

        }
    }
    public boolean add(ClientNumberModificationTemporaryEvent event){
        return upcomingClientNumberModificationTemporaryEvents.add(event);
    }

    public EnumMap<HotelVisitPurpose,Double> getClientNumberModifier(){
        return currentClientNumberModificationTemporaryEvents.stream()
                .map(ClientNumberModificationTemporaryEvent::getModifier)
                .reduce(
                        Stream.of(HotelVisitPurpose.values()).collect(Collectors.toMap(
                                e -> e,
                                e -> 0.,
                                (a,b) -> b,
                                () -> new EnumMap<>(HotelVisitPurpose.class)
                        )),
                        (resultMap, enumMap) -> {
                            for (HotelVisitPurpose key : enumMap.keySet()) {
                                Double value = enumMap.get(key);
                                resultMap.merge(key, value, Double::sum);
                            }
                            return resultMap;});
    }

    public static void main(String[] args){
        var m1 = new EnumMap<HotelVisitPurpose,Double>(HotelVisitPurpose.class);
        m1.put(HotelVisitPurpose.VACATION,0.1);
        m1.put(HotelVisitPurpose.BUSINESS_TRIP,0.1);
        m1.put(HotelVisitPurpose.REHABILITATION,0.1);
        var e1 = new ClientNumberModificationTemporaryEvent(LocalDate.now().plusDays(1),LocalDate.now().plusDays(3),m1);
        var e2 = new ClientNumberModificationTemporaryEvent(LocalDate.now().plusDays(2),LocalDate.now().plusDays(4),m1);
        var e3 = new ClientNumberModificationTemporaryEvent(LocalDate.now().plusDays(3),LocalDate.now().plusDays(5),m1);

        var temporaryEventHandler = getInstance();
        temporaryEventHandler.add(e2);
        temporaryEventHandler.add(e1);
        temporaryEventHandler.add(e3);
        System.out.println("Day 0");
        System.out.println("modifier: " + temporaryEventHandler.getClientNumberModifier());
        System.out.println(temporaryEventHandler.upcomingClientNumberModificationTemporaryEvents.peek());
        System.out.println(temporaryEventHandler.currentClientNumberModificationTemporaryEvents.peek());
        System.out.println("Day 1");
        temporaryEventHandler.update(LocalDate.now().plusDays(1));
        System.out.println("modifier: " + temporaryEventHandler.getClientNumberModifier());
        System.out.println(temporaryEventHandler.upcomingClientNumberModificationTemporaryEvents.peek());
        System.out.println(temporaryEventHandler.currentClientNumberModificationTemporaryEvents.peek());
        System.out.println("Day 2");
        temporaryEventHandler.update(LocalDate.now().plusDays(2));
        System.out.println("modifier: " + temporaryEventHandler.getClientNumberModifier());
        System.out.println(temporaryEventHandler.upcomingClientNumberModificationTemporaryEvents.peek());
        System.out.println(temporaryEventHandler.currentClientNumberModificationTemporaryEvents.peek());
        System.out.println("Day 3");
        temporaryEventHandler.update(LocalDate.now().plusDays(3));
        System.out.println("modifier: " + temporaryEventHandler.getClientNumberModifier());
        System.out.println(temporaryEventHandler.upcomingClientNumberModificationTemporaryEvents.peek());
        System.out.println(temporaryEventHandler.currentClientNumberModificationTemporaryEvents.peek());


        EventGenerator eventGenerator = EventGenerator.getInstance();
        System.out.println(temporaryEventHandler.upcomingClientNumberModificationTemporaryEvents);
    }

}
