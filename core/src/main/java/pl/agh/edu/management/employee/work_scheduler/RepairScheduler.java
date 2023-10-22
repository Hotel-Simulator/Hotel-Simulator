package pl.agh.edu.management.employee.work_scheduler;

import java.util.LinkedList;

import pl.agh.edu.management.hotel.HotelHandler;
import pl.agh.edu.model.Room;
import pl.agh.edu.model.employee.Employee;
import pl.agh.edu.model.employee.Profession;
import pl.agh.edu.time_command.TimeCommand;

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
