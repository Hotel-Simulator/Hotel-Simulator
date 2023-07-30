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
        cleanedRoom.setState(RoomState.EMPTY);
        cleaningScheduler.cleanRoomIfPossible(cleaner);
    }
}
