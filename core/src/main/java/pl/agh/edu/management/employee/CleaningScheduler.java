package pl.agh.edu.management.employee;

import java.util.*;

import pl.agh.edu.enums.RoomState;
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
				.filter(room -> room.getState() == RoomState.OCCUPIED)
				.toList());
		if (sizeBefore == 0 && !roomsToExecuteService.isEmpty()) {
			workingEmployees.stream()
					.filter(cleaner -> !cleaner.isOccupied())
					.forEach(this::executeServiceIfPossible);
		}
	}

	public void dailyAtCheckInTimeUpdate() {
		roomsToExecuteService.removeIf(room -> room.getState() == RoomState.OCCUPIED);
	}

	@Override
	protected void executeService(Employee cleaner, Room room) {
		cleaner.setOccupied(true);
		if (room.getState() == RoomState.DIRTY)
			room.setState(RoomState.MAINTENANCE);
		else if (room.getState() == RoomState.OCCUPIED)
			room.setState(RoomState.OCCUPIED_MAINTENANCE);
		timeCommandExecutor.addCommand(time.getTime().plusMinutes(cleaner.getServiceExecutionTime().toMinutes()),
				new TimeCommand(() -> {
					cleaner.setOccupied(false);
					if (room.getState() == RoomState.MAINTENANCE)
						room.setState(RoomState.EMPTY);
					else if (room.getState() == RoomState.OCCUPIED_MAINTENANCE)
						room.setState(RoomState.OCCUPIED);
					executeServiceIfPossible(cleaner);
				}));
	}

	private static final Comparator<Room> roomComparator = (o1, o2) -> {
		if (o1.getState() == o2.getState())
			return 0;
		if (o1.getState() == RoomState.OCCUPIED)
			return 1;
		return -1;
	};

}
