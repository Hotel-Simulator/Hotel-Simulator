package pl.agh.edu.engine;

import org.json.simple.parser.ParseException;
import pl.agh.edu.generator.client_generator.ClientGenerator;
import pl.agh.edu.generator.event_generator.EventGenerator;
import pl.agh.edu.model.Hotel;
import pl.agh.edu.model.time.Time;
import pl.agh.edu.model.advertisement.AdvertisementHandler;
import pl.agh.edu.model.employee.EmployeesToHireHandler;
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
    private final EmployeesToHireHandler employeesToHireHandler;

    public Engine(){
        this.time = Time.getInstance();
        this.clientGenerator = ClientGenerator.getInstance();

        this.hotel = new Hotel(LocalTime.of(15,0),LocalTime.of(12,0));
        this.timeCommandExecutor = TimeCommandExecutor.getInstance();
        this.advertisementHandler = AdvertisementHandler.getInstance();
        this.eventGenerator = EventGenerator.getInstance();
        this.cleaningScheduler = new CleaningScheduler(hotel);
        this.employeesToHireHandler = new EmployeesToHireHandler(hotel);

    }

    private void generateClientArrivals(){
        clientGenerator.generateArrivalsForDay(hotel.getCheckInTime(),hotel.getCheckOutTime())
                .forEach(arrival -> {
                    timeCommandExecutor.addCommand(LocalDateTime.of(time.getTime().toLocalDate(),arrival.time()),new ClientArrivalTimeCommand(this.hotel ,arrival.clientGroup(),cleaningScheduler));
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
        employeesToHireHandler.update();
    }

    public void everyMonthUpdate(){
        hotel.update();
    }

    public void everyYearUpdate(){
        eventGenerator.initializeClientNumberModificationCyclicTemporaryEvents();
    }


    public static void main(String[] args){
        Engine engine = new Engine();
    }



}
