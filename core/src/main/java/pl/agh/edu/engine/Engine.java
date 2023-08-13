package pl.agh.edu.engine;

import pl.agh.edu.generator.client_generator.ClientGenerator;
import pl.agh.edu.generator.event_generator.EventGenerator;
import pl.agh.edu.management.employee.work_scheduler.ReceptionScheduler;
import pl.agh.edu.management.employee.work_scheduler.RepairScheduler;
import pl.agh.edu.model.Hotel;
import pl.agh.edu.model.advertisement.AdvertisementHandler;
import pl.agh.edu.management.employee.EmployeesToHireHandler;
import pl.agh.edu.management.employee.work_scheduler.CleaningScheduler;
import pl.agh.edu.model.time.Time;
import pl.agh.edu.time_command.RepeatingTimeCommand;
import pl.agh.edu.time_command.TimeCommand;
import pl.agh.edu.time_command.TimeCommandExecutor;

import java.time.LocalDateTime;
import java.time.LocalTime;

import static pl.agh.edu.time_command.RepeatingTimeCommand.Frequency.*;

public class Engine {
    private final Time time;
    private final ClientGenerator clientGenerator;
    private final Hotel hotel;
    private final TimeCommandExecutor timeCommandExecutor;
    private final AdvertisementHandler advertisementHandler;
    private final EventGenerator eventGenerator;
    private final CleaningScheduler cleaningScheduler;
    private final RepairScheduler repairScheduler;
    private final ReceptionScheduler receptionScheduler;
    private final EmployeesToHireHandler employeesToHireHandler;

    public Engine(){
        this.time = Time.getInstance();
        this.clientGenerator = ClientGenerator.getInstance();

        this.hotel = new Hotel(LocalTime.of(15,0),LocalTime.of(12,0));
        this.timeCommandExecutor = TimeCommandExecutor.getInstance();
        this.advertisementHandler = AdvertisementHandler.getInstance();
        this.eventGenerator = EventGenerator.getInstance();
        this.cleaningScheduler = new CleaningScheduler(hotel);
        this.repairScheduler = new RepairScheduler(hotel);
        this.receptionScheduler = new ReceptionScheduler(hotel,cleaningScheduler,repairScheduler);
        this.employeesToHireHandler = new EmployeesToHireHandler(hotel);

        //todo ustalic z kubą jak robimy zeby time.getTime było teraz o północy
        timeCommandExecutor.addCommand(time.getTime(),new RepeatingTimeCommand(EVERY_SHIFT, cleaningScheduler::perShiftUpdate));
        timeCommandExecutor.addCommand(time.getTime(),new RepeatingTimeCommand(EVERY_SHIFT, receptionScheduler::perShiftUpdate));
        timeCommandExecutor.addCommand(time.getTime(),new RepeatingTimeCommand(EVERY_SHIFT, repairScheduler::perShiftUpdate));

        timeCommandExecutor.addCommand(time.getTime(),new RepeatingTimeCommand(EVERY_DAY,advertisementHandler::dailyUpdate));
        timeCommandExecutor.addCommand(time.getTime(),new RepeatingTimeCommand(EVERY_DAY,employeesToHireHandler::dailyUpdate));
        timeCommandExecutor.addCommand(time.getTime(),new RepeatingTimeCommand(EVERY_DAY,this::dailyUpdate));
        timeCommandExecutor.addCommand(LocalDateTime.of(time.getTime().toLocalDate(),hotel.getCheckOutTime()),
                new RepeatingTimeCommand(EVERY_DAY,cleaningScheduler::dailyAtCheckOutTimeUpdate));
        timeCommandExecutor.addCommand(LocalDateTime.of(time.getTime().toLocalDate(),hotel.getCheckInTime()),
                new RepeatingTimeCommand(EVERY_DAY,cleaningScheduler::dailyAtCheckInTimeUpdate));

        timeCommandExecutor.addCommand(time.getTime(),new RepeatingTimeCommand(EVERY_MONTH,hotel::monthlyUpdate));

        timeCommandExecutor.addCommand(time.getTime(),new RepeatingTimeCommand(EVERY_YEAR,eventGenerator::yearlyUpdate));
    }

    private void generateClientArrivals(){
        clientGenerator.generateArrivalsForDay(hotel.getCheckInTime(),hotel.getCheckOutTime())
                .forEach(arrival -> timeCommandExecutor.addCommand(LocalDateTime.of(time.getTime().toLocalDate(),arrival.time()),
                        new TimeCommand(() -> receptionScheduler.addEntity(arrival.clientGroup()))
                        ));
    }

    public void dailyUpdate(){
        generateClientArrivals();
    }



    public static void main(String[] args){
        Engine engine = new Engine();
    }



}
