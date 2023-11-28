package pl.agh.edu.engine.employee.scheduler;

import static pl.agh.edu.engine.employee.Profession.CLEANER;

import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.stream.Collectors;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import pl.agh.edu.engine.employee.Employee;
import pl.agh.edu.engine.employee.EmployeeHandler;
import pl.agh.edu.engine.employee.Shift;
import pl.agh.edu.engine.opinion.OpinionBuilder;
import pl.agh.edu.engine.room.Room;
import pl.agh.edu.engine.room.RoomManager;
import pl.agh.edu.engine.time.Time;
import pl.agh.edu.engine.time.TimeCommandExecutor;
import pl.agh.edu.engine.time.command.TimeCommand;
import pl.agh.edu.serialization.KryoConfig;

public class CleaningScheduler extends WorkScheduler<Room> {
	private final RoomManager roomManager;
	private static final Comparator<Room> roomComparator = new RoomComparator();

	public static void kryoRegister() {
		KryoConfig.kryo.register(CleaningScheduler.class, new Serializer<CleaningScheduler>() {
			@Override
			public void write(Kryo kryo, Output output, CleaningScheduler object) {
				kryo.writeObject(output, object.time);
				kryo.writeObject(output, object.timeCommandExecutor);
				kryo.writeObject(output, object.employeeHandler);
				kryo.writeObject(output, object.roomManager);
				kryo.writeObject(output, object.entitiesToExecuteService);
				kryo.writeObject(output, object.workingEmployees, KryoConfig.listSerializer(Employee.class));
				kryo.writeObject(output, object.currentShift);

			}

			@Override
			public CleaningScheduler read(Kryo kryo, Input input, Class<? extends CleaningScheduler> type) {
				return new CleaningScheduler(
						kryo.readObject(input, Time.class),
						kryo.readObject(input, TimeCommandExecutor.class),
						kryo.readObject(input, EmployeeHandler.class),
						kryo.readObject(input, RoomManager.class),
						kryo.readObject(input, PriorityQueue.class),
						kryo.readObject(input, List.class, KryoConfig.listSerializer(Employee.class)),
						kryo.readObject(input, Shift.class));
			}
		});
	}

	public CleaningScheduler(EmployeeHandler employeeHandler, RoomManager roomManager) {
		super(employeeHandler, new PriorityQueue<>(roomComparator), CLEANER);
		this.roomManager = roomManager;
	}

	private CleaningScheduler(Time time,
			TimeCommandExecutor timeCommandExecutor,
			EmployeeHandler employeeHandler,
			RoomManager roomManager,
			Queue<Room> entitiesToExecuteService,
			List<Employee> workingEmployees,
			Shift currentShift) {
		super(time, timeCommandExecutor, employeeHandler, entitiesToExecuteService, CLEANER, workingEmployees, currentShift);
		this.roomManager = roomManager;
	}

	public void dailyAtCheckOutTimeUpdate() {
		int sizeBefore = entitiesToExecuteService.size();
		entitiesToExecuteService.addAll(roomManager.getRooms().stream()
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
					OpinionBuilder.saveRoomDailyCleaningData(cleaner, room);
					executeServiceIfPossible(cleaner);
				}, time.getTime().plusMinutes(cleaner.getServiceExecutionTime().toMinutes())));
	}

	public void perShiftUpdate() {
		currentShift = currentShift.next();
		workingEmployees = employeeHandler.getWorkingEmployeesByProfession(employeesProfession).stream()
				.filter(employee -> employee.shift.equals(currentShift))
				.collect(Collectors.toList());
		workingEmployees.forEach(this::executeServiceIfPossible);

	}

}
