package pl.agh.edu.time_command;

import pl.agh.edu.model.Room;
import pl.agh.edu.management.employee.CleaningScheduler;

public class ClientLeavingTimeCommand implements TimeCommand {

    private final Room room;
    private final CleaningScheduler cleaningScheduler;

    public ClientLeavingTimeCommand(Room room,CleaningScheduler cleaningScheduler) {
        this.room = room;
        this.cleaningScheduler = cleaningScheduler;
    }

    @Override
    public void execute() {
        room.checkOut();
        cleaningScheduler.newDirtyRoom(room);
    }
}
