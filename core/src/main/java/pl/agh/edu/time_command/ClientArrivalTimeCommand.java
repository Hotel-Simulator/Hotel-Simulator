package pl.agh.edu.time_command;

import org.json.simple.parser.ParseException;
import pl.agh.edu.model.ClientGroup;
import pl.agh.edu.model.Hotel;
import pl.agh.edu.model.Room;
import pl.agh.edu.model.employee.cleaner.CleaningScheduler;

import java.io.IOException;

public class ClientArrivalTimeCommand implements TimeCommand {
    private final ClientGroup clientGroup;

    private final Hotel hotel;
    private final TimeCommandExecutor timeCommandExecutor;
    private final CleaningScheduler cleaningScheduler;

    public ClientArrivalTimeCommand(Hotel hotel, ClientGroup clientGroup, CleaningScheduler cleaningScheduler) throws IOException, ParseException {
        this.timeCommandExecutor = TimeCommandExecutor.getInstance();
        this.clientGroup = clientGroup;
        this.hotel = hotel;
        this.cleaningScheduler = cleaningScheduler;
    }
    @Override
    public void execute() {
        Room room = hotel.findRoomForClientGroup(clientGroup);
        if(room != null){
            room.checkIn(clientGroup);
            timeCommandExecutor.addCommand(clientGroup.getCheckOutTime(),new ClientLeavingTimeCommand(room,cleaningScheduler));
        }
    }
}
