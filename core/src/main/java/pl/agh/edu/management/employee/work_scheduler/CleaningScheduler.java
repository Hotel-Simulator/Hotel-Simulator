package pl.agh.edu.management.employee.work_scheduler;

import java.util.*;

import pl.agh.edu.model.Hotel;
import pl.agh.edu.model.Room;
import pl.agh.edu.model.employee.Employee;
import pl.agh.edu.model.employee.Profession;
import pl.agh.edu.time_command.TimeCommand;

public class CleaningScheduler extends WorkScheduler<Room> {
	public CleaningScheduler(Hotel hotel) {
		super(hotel, new PriorityQueue<>(roomComparator), Profession.CLEANER);
	}

	public void dailyAtCheckOutTimeUpdate() {
		int sizeBefore = entitiesToExecuteService.size();
		entitiesToExecuteService.addAll(hotel.getRooms().stream()
				.filter(room -> room.getRoomStates().isOccupied())
				.toList());
		if (sizeBefore == 0 && !entitiesToExecuteService.isEmpty()) {
			workingEmployees.stream()
					.filter(cleaner -> !cleaner.isOccupied())
					.forEach(this::executeServiceIfPossible);
		}
	}

	public void dailyAtCheckInTimeUpdate() {
		entitiesToExecuteService.removeIf(room -> room.getRoomStates().isOccupied());
	}

	@Override
	protected void executeService(Employee cleaner, Room room) {
		cleaner.setOccupied(true);

		timeCommandExecutor.addCommand(
				new TimeCommand(() -> {
					cleaner.setOccupied(false);
					room.getRoomStates().setDirty(false);
					executeServiceIfPossible(cleaner);
				}, time.getTime().plusMinutes(cleaner.getServiceExecutionTime().toMinutes())));
	}

	private static final Comparator<Room> roomComparator = (o1, o2) -> Boolean.compare(o1.getRoomStates().isOccupied(), o2.getRoomStates().isOccupied());

}
