package pl.agh.edu.engine.employee.scheduler;

import static pl.agh.edu.engine.employee.Profession.CLEANER;

import java.util.Comparator;
import java.util.PriorityQueue;

import pl.agh.edu.engine.employee.Employee;
import pl.agh.edu.engine.hotel.HotelHandler;
import pl.agh.edu.engine.opinion.OpinionBuilder;
import pl.agh.edu.engine.room.Room;
import pl.agh.edu.engine.time.command.SerializableRunnable;
import pl.agh.edu.engine.time.command.TimeCommand;

public class CleaningScheduler extends WorkScheduler<Room> {
	private static final Comparator<Room> roomComparator = (o1, o2) -> Boolean.compare(o1.roomState.isOccupied(), o2.roomState.isOccupied());

	public CleaningScheduler(HotelHandler hotelHandler) {
		super(hotelHandler, new PriorityQueue<>(roomComparator), CLEANER);
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
				new TimeCommand((SerializableRunnable)() -> {
					cleaner.setOccupied(false);
					room.roomState.setDirty(false);
					OpinionBuilder.saveRoomDailyCleaningData(cleaner, room);
					executeServiceIfPossible(cleaner);
				}, time.getTime().plusMinutes(cleaner.getServiceExecutionTime().toMinutes())));
	}

}
