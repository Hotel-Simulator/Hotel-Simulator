package pl.agh.edu.engine;

import jdk.jfr.Event;
import org.json.simple.parser.ParseException;
import pl.agh.edu.generator.client_generator.ClientGenerator;
import pl.agh.edu.generator.event_generator.EventGenerator;
import pl.agh.edu.model.Hotel;
import pl.agh.edu.model.Time;
import pl.agh.edu.model.advertisement.AdvertisementHandler;
import pl.agh.edu.model.employee.cleaner.CleaningScheduler;
import pl.agh.edu.time_command.ClientArrivalTimeCommand;
import pl.agh.edu.time_command.TimeCommandExecutor;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
public class Engine {
    private final Time time;
    private final ClientGenerator clientGenerator;
    private final Hotel hotel;
    private final TimeCommandExecutor timeCommandExecutor;
    private final AdvertisementHandler advertisementHandler;
    private final EventGenerator eventGenerator;
    private final CleaningScheduler cleaningScheduler;

    public Engine() throws IOException, ParseException {
        this.time = Time.getInstance();
        this.clientGenerator = ClientGenerator.getInstance();

        this.hotel = new Hotel(LocalTime.of(15,0),LocalTime.of(12,0));
        this.timeCommandExecutor = TimeCommandExecutor.getInstance();
        this.advertisementHandler = AdvertisementHandler.getInstance();
        this.eventGenerator = EventGenerator.getInstance();
        this.cleaningScheduler = new CleaningScheduler(hotel);

    }

    private void generateClientArrivals(){
        clientGenerator.generateArrivalsForDay(hotel.getCheckInTime(),hotel.getCheckOutTime())
                .forEach(arrival -> {
                    try {
                        timeCommandExecutor.addCommand(LocalDateTime.of(time.getTime().toLocalDate(),arrival.time()),new ClientArrivalTimeCommand(this.hotel ,arrival.clientGroup(),cleaningScheduler));
                    } catch (IOException | ParseException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    private void updateAdvertisements(){

    }

    public void updateEveryShift(){
        cleaningScheduler.shiftChange();
    }

    public void everyDayUpdate(){
        advertisementHandler.update();
        generateClientArrivals();
        cleaningScheduler.update();
    }

    public void everyYearUpdate() throws IOException, ParseException {
        eventGenerator.initializeClientNumberModificationCyclicTemporaryEvents();
    }


    public static void main(String[] args) throws IOException, ParseException {
        Engine engine = new Engine();
    }



}
