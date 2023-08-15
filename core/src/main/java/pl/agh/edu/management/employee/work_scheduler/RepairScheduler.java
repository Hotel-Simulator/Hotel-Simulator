package pl.agh.edu.management.employee.work_scheduler;

import pl.agh.edu.enums.RoomState;
import pl.agh.edu.model.Hotel;
import pl.agh.edu.model.Room;
import pl.agh.edu.model.employee.Employee;
import pl.agh.edu.model.employee.Profession;
import pl.agh.edu.time_command.TimeCommand;
import java.util.LinkedList;


public class RepairScheduler extends WorkScheduler<Room> {
    public RepairScheduler(Hotel hotel){
        super(hotel, new LinkedList<>(),Profession.CLEANER);
    }
    @Override
    protected void executeService(Employee technician, Room room){
        technician.setOccupied(true);
        room.setState(RoomState.MAINTENANCE);
        timeCommandExecutor.addCommand(time.getTime().plus(technician.getServiceExecutionTime()),
                new TimeCommand(() ->{
                    technician.setOccupied(false);
                    room.setState(RoomState.EMPTY);
                    executeServiceIfPossible(technician);
                }));
    }

}
