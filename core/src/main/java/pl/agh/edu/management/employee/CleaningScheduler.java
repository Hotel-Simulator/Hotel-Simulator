package pl.agh.edu.management.employee;

import pl.agh.edu.enums.RoomState;
import pl.agh.edu.model.Hotel;
import pl.agh.edu.model.Room;
import pl.agh.edu.model.Time;
import pl.agh.edu.model.employee.Employee;
import pl.agh.edu.model.employee.Profession;
import pl.agh.edu.model.employee.Shift;
import pl.agh.edu.time_command.EndRoomCleaningTimeCommand;
import pl.agh.edu.time_command.TimeCommandExecutor;
import pl.agh.edu.update.PerShiftUpdatable;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class CleaningScheduler implements PerShiftUpdatable {
    private List<Employee> cleaners;
    private final Hotel hotel;
    private final LinkedList<Room> dirtyRooms;
    private final TimeCommandExecutor timeCommandExecutor;
    private Shift currentShift;
    private final Time time;
    public CleaningScheduler(Hotel hotel){
        this.hotel = hotel;
        this.dirtyRooms = new LinkedList<>();
        currentShift = Shift.NIGHT;
        this.timeCommandExecutor = TimeCommandExecutor.getInstance();
        this.time = Time.getInstance();
        this.cleaners = new ArrayList<>();
    }

    public void newDirtyRoom(Room room){
        dirtyRooms.add(room);
        if(dirtyRooms.size() == 1){
            cleaners.stream()
                    .filter(cleaner -> !cleaner.isOccupied() && willEmployeeExecuteServiceBeforeShiftEnds(cleaner))
                    .findFirst()
                    .ifPresent(cleaner -> cleanRoom(cleaner,room));
        }
    }
    @Override
    public void perShiftUpdate(){
        currentShift = currentShift.next();
        cleaners = hotel.getEmployeesByProfession(Profession.CLEANER).stream()
                .filter(cleaner -> cleaner.getShift().equals(currentShift))
                .collect(Collectors.toList());
        cleaners.forEach(this::cleanRoomIfPossible);

    }

    public void cleanRoomIfPossible(Employee cleaner){

        if(!dirtyRooms.isEmpty() && willEmployeeExecuteServiceBeforeShiftEnds(cleaner)){
            cleanRoom(cleaner,dirtyRooms.removeFirst());
        }
    }
    private void cleanRoom(Employee cleaner, Room room){
        cleaner.setOccupied(true);
        room.setState(RoomState.MAINTENANCE);
        timeCommandExecutor.addCommand(time.getTime().plusMinutes(cleaner.getServiceExecutionTime().toMinutes()),
                new EndRoomCleaningTimeCommand(room,cleaner,this));
    }

    private boolean willEmployeeExecuteServiceBeforeShiftEnds(Employee cleaner){
        return cleaner.isAtWork(time.getTime().toLocalTime().plusMinutes(cleaner.getServiceExecutionTime().toMinutes()));
    }


}
