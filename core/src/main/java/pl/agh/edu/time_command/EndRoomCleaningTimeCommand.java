package pl.agh.edu.time_command;

import pl.agh.edu.enums.RoomState;
import pl.agh.edu.model.Room;
import pl.agh.edu.model.employee.Employee;
import pl.agh.edu.management.employee.CleaningScheduler;

public record EndRoomCleaningTimeCommand(Room cleanedRoom,
                                         Employee cleaner,
                                         CleaningScheduler cleaningScheduler) implements TimeCommand {

    @Override
    public void execute() {
        cleaner.setOccupied(false);
        if(cleanedRoom().getState() == RoomState.MAINTENANCE) cleanedRoom.setState(RoomState.EMPTY);
        else if(cleanedRoom.getState() == RoomState.OCCUPIED_MAINTENANCE) cleanedRoom.setState(RoomState.OCCUPIED);
        cleaningScheduler.cleanRoomIfPossible(cleaner);
    }
}
