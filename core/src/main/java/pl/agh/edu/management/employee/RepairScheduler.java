package pl.agh.edu.management.employee;

import java.util.LinkedList;

import pl.agh.edu.model.Hotel;
import pl.agh.edu.model.Room;
import pl.agh.edu.model.employee.Employee;
import pl.agh.edu.model.employee.Profession;
import pl.agh.edu.time_command.TimeCommand;

public class RepairScheduler extends WorkScheduler {
	public RepairScheduler(Hotel hotel) {
		super(hotel, new LinkedList<>(), Profession.CLEANER);
	}

	@Override
	protected void executeService(Employee technician, Room room) {
		technician.setOccupied(true);
		room.setMaintenance(true);
		timeCommandExecutor.addCommand(time.getTime().plusMinutes(technician.getServiceExecutionTime().toMinutes()),
				new TimeCommand(() -> {
					technician.setOccupied(false);
					room.setMaintenance(false);
					executeServiceIfPossible(technician);
				}));
	}

}
