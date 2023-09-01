package pl.agh.edu.management.employee.work_scheduler;

import java.util.*;

import pl.agh.edu.management.hotel.HotelHandler
import pl.agh.edu.model.Room;
import pl.agh.edu.model.employee.Employee;
import pl.agh.edu.model.employee.Profession;
import pl.agh.edu.time_command.TimeCommand;

public class CleaningScheduler extends WorkScheduler<Room> {
	public CleaningScheduler(HotelHandler hotelHandler) {
		super(hotelHandler, new PriorityQueue<>(roomComparator), Profession.CLEANER);
	}

	public void dailyAtCheckOutTimeUpdate() {
		int sizeBefore = entitiesToExecuteService.size();
		entitiesToExecuteService.addAll(hotelHandler.roomManager.getRooms().stream()
				.filter(room -> room.roomState.isOccupied())
				.toList());
		if (sizeBefore == 0 && !entitiesToExecuteService.isEmpty()) {
			workingEmployees.stream()
					.filter(cleaner -> !cleaner.isOccupied())
					.forEach(this::executeServiceIfPossible);
		}
	}

	public void dailyAtCheckInTimeUpdate() {
		entitiesToExecuteService.removeIf(room -> room.roomState.isOccupied());
	}

	@Override
	protected void executeService(Employee cleaner, Room room) {
		cleaner.setOccupied(true);

		timeCommandExecutor.addCommand(
				new TimeCommand(() -> {
					cleaner.setOccupied(false);
					room.roomState.setDirty(false);
					executeServiceIfPossible(cleaner);
				}, time.getTime().plusMinutes(cleaner.getServiceExecutionTime().toMinutes())));
	}

	private static final Comparator<Room> roomComparator = (o1, o2) -> Boolean.compare(o1.roomState.isOccupied(), o2.roomState.isOccupied());

}
