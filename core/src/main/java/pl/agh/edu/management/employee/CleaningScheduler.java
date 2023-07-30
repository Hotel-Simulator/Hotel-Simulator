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
import pl.agh.edu.update.DailyAtCheckInTimeUpdatable;
import pl.agh.edu.update.DailyAtCheckOutTimeUpdatable;
import pl.agh.edu.update.PerShiftUpdatable;

import java.util.*;
import java.util.stream.Collectors;

public class CleaningScheduler implements PerShiftUpdatable, DailyAtCheckInTimeUpdatable, DailyAtCheckOutTimeUpdatable {
    private List<Employee> cleaners;
    private final Hotel hotel;
    private final PriorityQueue<Room> dirtyRooms;
    private final TimeCommandExecutor timeCommandExecutor;
    private Shift currentShift;
    private final Time time;
    public CleaningScheduler(Hotel hotel){
        this.hotel = hotel;
        this.dirtyRooms = new PriorityQueue<>(roomComparator);
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
                    .ifPresent(cleaner -> cleanRoom(cleaner,dirtyRooms.remove()));
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
    @Override
    public void dailyAtCheckOutTimeUpdate(){
        int sizeBefore = dirtyRooms.size();
        dirtyRooms.addAll(hotel.getRooms().stream()
                .filter(room -> room.getState() == RoomState.OCCUPIED)
                .toList());
        if(sizeBefore == 0 && !dirtyRooms.isEmpty()){
            cleaners.stream()
                    .filter(cleaner -> !cleaner.isOccupied())
                    .forEach(this::cleanRoomIfPossible);
        }
    }
    @Override
    public void dailyAtCheckInTimeUpdate(){
        dirtyRooms.removeIf(room -> room.getState() == RoomState.OCCUPIED);
    }


    public void cleanRoomIfPossible(Employee cleaner){

        if(!dirtyRooms.isEmpty() && willEmployeeExecuteServiceBeforeShiftEnds(cleaner)){
            cleanRoom(cleaner,dirtyRooms.remove());
        }
    }
    private void cleanRoom(Employee cleaner, Room room){
        cleaner.setOccupied(true);
        if(room.getState() == RoomState.DIRTY) room.setState(RoomState.MAINTENANCE);
        else if(room.getState() == RoomState.OCCUPIED) room.setState(RoomState.OCCUPIED_MAINTENANCE);
        timeCommandExecutor.addCommand(time.getTime().plusMinutes(cleaner.getServiceExecutionTime().toMinutes()),
                new EndRoomCleaningTimeCommand(room,cleaner,this));
    }

    private boolean willEmployeeExecuteServiceBeforeShiftEnds(Employee cleaner){
        return cleaner.isAtWork(time.getTime().toLocalTime().plusMinutes(cleaner.getServiceExecutionTime().toMinutes()));
    }

    private static final Comparator<Room> roomComparator = (o1, o2) -> {
        if(o1.getState() == o2.getState()) return 0;
        if(o1.getState() == RoomState.OCCUPIED) return 1;
        return -1;
    };


}
