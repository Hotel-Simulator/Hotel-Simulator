package pl.agh.edu.engine;

import pl.agh.edu.generator.client_generator.ClientGenerator;
import pl.agh.edu.generator.event_generator.EventGenerator;
import pl.agh.edu.model.Hotel;
import pl.agh.edu.model.Time;
import pl.agh.edu.model.advertisement.AdvertisementHandler;
import pl.agh.edu.management.employee.EmployeesToHireHandler;
import pl.agh.edu.management.employee.CleaningScheduler;
import pl.agh.edu.time_command.ClientArrivalTimeCommand;
import pl.agh.edu.time_command.TimeCommandExecutor;
import pl.agh.edu.update.DailyUpdatable;
import pl.agh.edu.update.Updater;

import java.time.LocalDateTime;
import java.time.LocalTime;
public class Engine implements DailyUpdatable {
    private final Time time;
    private final ClientGenerator clientGenerator;
    private final Hotel hotel;
    private final TimeCommandExecutor timeCommandExecutor;
    private final AdvertisementHandler advertisementHandler;
    private final EventGenerator eventGenerator;
    private final CleaningScheduler cleaningScheduler;
    private final EmployeesToHireHandler employeesToHireHandler;
    private final Updater updater;


    public Engine(){
        this.time = Time.getInstance();
        this.clientGenerator = ClientGenerator.getInstance();

        this.hotel = new Hotel(LocalTime.of(15,0),LocalTime.of(12,0));
        this.timeCommandExecutor = TimeCommandExecutor.getInstance();
        this.advertisementHandler = AdvertisementHandler.getInstance();
        this.eventGenerator = EventGenerator.getInstance();
        this.cleaningScheduler = new CleaningScheduler(hotel);
        this.employeesToHireHandler = new EmployeesToHireHandler(hotel);
        this.updater = Updater.getInstance();


        updater.registerPerShiftUpdatable(cleaningScheduler);

        updater.registerDailyUpdatable(advertisementHandler);
        updater.registerDailyUpdatable(employeesToHireHandler);
        updater.registerDailyUpdatable(this);

        updater.registerDailyAtCheckOutTimeUpdatable(cleaningScheduler);

        updater.registerDailyAtCheckInTimeUpdatable(cleaningScheduler);

        updater.registerMonthlyUpdatable(hotel);

        updater.registerYearlyUpdatable(eventGenerator);

    }

    private void generateClientArrivals(){
        clientGenerator.generateArrivalsForDay(hotel.getCheckInTime(),hotel.getCheckOutTime())
                .forEach(arrival -> {
                    timeCommandExecutor.addCommand(LocalDateTime.of(time.getTime().toLocalDate(),arrival.time()),new ClientArrivalTimeCommand(this.hotel ,arrival.clientGroup(),cleaningScheduler));
                });
    }
    @Override
    public void dailyUpdate(){
        generateClientArrivals();
    }



    public static void main(String[] args){
        Engine engine = new Engine();
    }



}
