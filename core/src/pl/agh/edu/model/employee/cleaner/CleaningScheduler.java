package pl.agh.edu.model.employee.cleaner;

import pl.agh.edu.enums.RoomState;
import pl.agh.edu.model.Hotel;
import pl.agh.edu.model.Room;
import pl.agh.edu.model.time.Time;
import pl.agh.edu.model.employee.Shift;
import pl.agh.edu.time_command.EndRoomCleaningTimeCommand;
import pl.agh.edu.time_command.TimeCommandExecutor;

import java.util.LinkedList;
import java.util.List;

public class CleaningScheduler {
    private List<Cleaner> cleaners;
    private final Hotel hotel;
    private final LinkedList<Room> dirtyRooms;
    private final TimeCommandExecutor timeCommandExecutor;
    private Shift currentShift;
    private final Time time;
    public CleaningScheduler(Hotel hotel){
        this.hotel = hotel;
        this.dirtyRooms = new LinkedList<>();
        currentShift = Shift.EVENING;
        this.timeCommandExecutor = TimeCommandExecutor.getInstance();
        this.time = Time.getInstance();
    }

    public void addRoomToClean(Room room){
        dirtyRooms.add(room);
        if(dirtyRooms.size() == 1){
            cleaners.stream()
                    .filter(cleaner -> !cleaner.isOccupied() && cleaner.isAtWork(time.getTime().plusMinutes(cleaner.getCleaningTime().toMinutes()).toLocalTime()))
                    .findFirst().ifPresent(cleaner -> cleanRoom(cleaner,dirtyRooms.removeFirst()));
        }
    }


    public void update(){
        cleaners = hotel.getEmployeesByPosition(Cleaner.class);
    }
    public void shiftChange(){
        currentShift = currentShift.next();
        cleaners.stream()
                .filter(cleaner -> cleaner.getShift().equals(currentShift))
                .forEach(this::cleanRoomIfPossible);
    }

    public void cleanRoomIfPossible(Cleaner cleaner){
        if(cleaner.isAtWork(time.getTime().plusMinutes(cleaner.getCleaningTime().toMinutes()).toLocalTime()) && !dirtyRooms.isEmpty()){
            cleanRoom(cleaner,dirtyRooms.removeFirst());
        }
    }
    private void cleanRoom(Cleaner cleaner, Room room){
        cleaner.setOccupied(true);
        room.setState(RoomState.MAINTENANCE);
        timeCommandExecutor.addCommand(time.getTime().plusMinutes(cleaner.getCleaningTime().toMinutes()),new EndRoomCleaningTimeCommand(room,cleaner,this));
    }

}
