package pl.agh.edu.engine.employee.scheduler;

import java.util.LinkedList;

import pl.agh.edu.engine.employee.Employee;
import pl.agh.edu.engine.employee.Profession;
import pl.agh.edu.engine.hotel.HotelHandler;
import pl.agh.edu.engine.room.Room;
import pl.agh.edu.engine.time.command.TimeCommand;

public class RepairScheduler extends WorkScheduler<Room> {
	public RepairScheduler(HotelHandler hotelHandler) {
		super(hotelHandler, new LinkedList<>(), Profession.TECHNICIAN);
	}

	@Override
	protected void executeService(Employee technician, Room room) {
		technician.setOccupied(true);

		timeCommandExecutor.addCommand(
				new TimeCommand(() -> {
					technician.setOccupied(false);
					room.roomState.setFaulty(false);
					executeServiceIfPossible(technician);
				}, time.getTime().plus(technician.getServiceExecutionTime())));
	}

}
