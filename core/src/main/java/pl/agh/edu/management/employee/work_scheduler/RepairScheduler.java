package pl.agh.edu.management.employee.work_scheduler;

import java.util.LinkedList;

import pl.agh.edu.enums.RoomState;
import pl.agh.edu.model.Hotel;
import pl.agh.edu.model.Room;
import pl.agh.edu.model.employee.Employee;
import pl.agh.edu.model.employee.Profession;
import pl.agh.edu.time_command.TimeCommand;

public class RepairScheduler extends WorkScheduler<Room> {
	public RepairScheduler(Hotel hotel) {
		super(hotel, new LinkedList<>(), Profession.CLEANER);
	}

	@Override
	protected void executeService(Employee technician, Room room) {
		technician.setOccupied(true);
		room.setState(RoomState.MAINTENANCE);
		timeCommandExecutor.addCommand(
				new TimeCommand(() -> {
					technician.setOccupied(false);
					room.setState(RoomState.EMPTY);
					executeServiceIfPossible(technician);
				}, time.getTime().plus(technician.getServiceExecutionTime())));
	}

}