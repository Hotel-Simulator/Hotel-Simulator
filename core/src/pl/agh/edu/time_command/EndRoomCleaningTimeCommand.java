package pl.agh.edu.time_command;

import pl.agh.edu.enums.RoomState;
import pl.agh.edu.model.Room;
import pl.agh.edu.model.employee.cleaner.Cleaner;
import pl.agh.edu.model.employee.cleaner.CleaningScheduler;

public class EndRoomCleaningTimeCommand implements TimeCommand{
    private final Room cleanedRoom;
    private final Cleaner cleaner;

    private final CleaningScheduler cleaningScheduler;

    public EndRoomCleaningTimeCommand(Room cleanedRoom, Cleaner cleaner,CleaningScheduler cleaningScheduler) {
        this.cleanedRoom = cleanedRoom;
        this.cleaner = cleaner;
        this.cleaningScheduler = cleaningScheduler;
    }

    @Override
    public void execute() {
        cleaner.setOccupied(false);
        cleanedRoom.setState(RoomState.EMPTY);
        cleaningScheduler.cleanRoomIfPossible(cleaner);
    }
}
