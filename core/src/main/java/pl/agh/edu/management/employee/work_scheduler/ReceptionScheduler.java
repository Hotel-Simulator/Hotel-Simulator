package pl.agh.edu.management.employee.work_scheduler;

import pl.agh.edu.enums.RoomState;
import pl.agh.edu.json.data_loader.JSONGameDataLoader;
import pl.agh.edu.model.client.ClientGroup;
import pl.agh.edu.model.Hotel;
import pl.agh.edu.model.Room;
import pl.agh.edu.model.client.ClientGroupState;
import pl.agh.edu.model.employee.Employee;
import pl.agh.edu.model.employee.Profession;
import pl.agh.edu.time_command.TimeCommand;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class ReceptionScheduler extends WorkScheduler<ClientGroup> {

    private final Random random;
    private final CleaningScheduler cleaningScheduler;
    private final RepairScheduler repairScheduler;

    public ReceptionScheduler(Hotel hotel, CleaningScheduler cleaningScheduler, RepairScheduler repairScheduler) {
        super(hotel, new LinkedList<>(), Profession.RECEPTIONIST);
        this.random = new Random();
        this.cleaningScheduler = cleaningScheduler;
        this.repairScheduler = repairScheduler;
    }

    @Override
    protected void executeService(Employee receptionist, ClientGroup clientGroup) {
        receptionist.setOccupied(true);
        switch(clientGroup.getState()){
            case CHECKING_IN -> timeCommandExecutor.addCommand(
                    time.getTime().plus(receptionist.getServiceExecutionTime()),
                    serveCheckingInClientsTimeCommand(receptionist, clientGroup));
            case CHECKING_OUT -> timeCommandExecutor.addCommand(
                    time.getTime().plus(receptionist.getServiceExecutionTime()), //todo jaki tu dac czas i skad go wziac
                    serveCheckingOutClientsTimeCommand(receptionist ,clientGroup.getRoom()));
            case CHECKED_IN -> throw new IllegalStateException();
        }

    }



    private TimeCommand breakRoomTimeCommand(Room room){
        return new TimeCommand( () -> {
            room.setState(RoomState.FAULT); // todo zmienic na isFault()
            repairScheduler.addEntity(room);
        });
    }

    private TimeCommand leaveRoomTimeCommand(ClientGroup clientGroup){
        return new TimeCommand( () -> {
            clientGroup.setState(ClientGroupState.CHECKING_OUT);
            this.addEntity(clientGroup);
        });
    }

    private TimeCommand serveCheckingInClientsTimeCommand(Employee receptionist, ClientGroup clientGroup){
        return new TimeCommand(
                () -> {
                    Room room = hotel.findRoomForClientGroup(clientGroup);
                    if(room != null){
                        room.checkIn(clientGroup);
                        clientGroup.setState(ClientGroupState.CHECKED_IN);
                        if(random.nextDouble() < JSONGameDataLoader.roomFaultProbability){
                            long minutes = Duration.between(time.getTime(), clientGroup.getCheckOutTime()).toMinutes();
                            timeCommandExecutor.addCommand(
                                    time.generateRandomTime(minutes, ChronoUnit.MINUTES),
                                    breakRoomTimeCommand(room)
                            );
                        }
                        timeCommandExecutor.addCommand(
                                clientGroup.getCheckOutTime(),
                                leaveRoomTimeCommand(clientGroup)
                        );
                    }
                    receptionist.setOccupied(true);
                }
        );
    }

    private TimeCommand serveCheckingOutClientsTimeCommand(Employee receptionist, Room room){
        return new TimeCommand( () -> {
            room.checkOut();
            cleaningScheduler.addEntity(room);
            receptionist.setOccupied(false);
        });
    }

}
