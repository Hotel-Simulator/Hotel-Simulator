package pl.agh.edu.management.employee;

import java.util.*;

import pl.agh.edu.model.Hotel;
import pl.agh.edu.model.Room;
import pl.agh.edu.model.employee.Employee;
import pl.agh.edu.model.employee.Profession;
import pl.agh.edu.time_command.TimeCommand;

public class CleaningScheduler extends WorkScheduler {
	public CleaningScheduler(Hotel hotel) {
		super(hotel, new PriorityQueue<>(roomComparator), Profession.CLEANER);
	}

	public void dailyAtCheckOutTimeUpdate() {
		int sizeBefore = roomsToExecuteService.size();
		roomsToExecuteService.addAll(hotel.getRooms().stream()
				.filter(Room::isOccupied)
				.toList());
		if (sizeBefore == 0 && !roomsToExecuteService.isEmpty()) {
			workingEmployees.stream()
					.filter(cleaner -> !cleaner.isOccupied())
					.forEach(this::executeServiceIfPossible);
		}
	}

	public void dailyAtCheckInTimeUpdate() {
		roomsToExecuteService.removeIf(Room::isOccupied);
	}

	@Override
	protected void executeService(Employee cleaner, Room room) {
		cleaner.setOccupied(true);
		if (room.isDirty() || room.isOccupied())
			room.setMaintenance(true);

		timeCommandExecutor.addCommand(time.getTime().plusMinutes(cleaner.getServiceExecutionTime().toMinutes()),
				new TimeCommand(() -> {
					cleaner.setOccupied(false);
					if (room.isMaintenance())
						room.setMaintenance(false);
					executeServiceIfPossible(cleaner);
				}));
	}

	private static final Comparator<Room> roomComparator = (o1, o2) -> {
		// if (o1.getState() == o2.getState()) wcześniej było tak ale nie do końca wiem na co to zmienić
		if (o1.isOccupied() == o2.isOccupied())
			return 0;
		if (o1.isOccupied())
			return 1;
		return -1;
	};

}
