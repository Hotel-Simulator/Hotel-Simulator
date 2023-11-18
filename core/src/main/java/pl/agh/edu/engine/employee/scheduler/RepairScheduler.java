package pl.agh.edu.engine.employee.scheduler;

import static pl.agh.edu.engine.employee.Profession.CLEANER;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import pl.agh.edu.engine.employee.Employee;
import pl.agh.edu.engine.employee.EmployeeHandler;
import pl.agh.edu.engine.employee.Profession;
import pl.agh.edu.engine.employee.Shift;
import pl.agh.edu.engine.opinion.OpinionBuilder;
import pl.agh.edu.engine.room.Room;
import pl.agh.edu.engine.time.Time;
import pl.agh.edu.engine.time.TimeCommandExecutor;
import pl.agh.edu.engine.time.command.TimeCommand;
import pl.agh.edu.serialization.KryoConfig;

public class RepairScheduler extends WorkScheduler<Room> {

	static {
		KryoConfig.kryo.register(RepairScheduler.class, new Serializer<RepairScheduler>() {
			@Override
			public void write(Kryo kryo, Output output, RepairScheduler object) {
				kryo.writeObject(output, object.time);
				kryo.writeObject(output, object.timeCommandExecutor);
				kryo.writeObject(output, object.employeeHandler);
				kryo.writeObject(output, object.entitiesToExecuteService);
				kryo.writeObject(output, object.workingEmployees);
				kryo.writeObject(output, object.currentShift);

			}

			@Override
			public RepairScheduler read(Kryo kryo, Input input, Class<? extends RepairScheduler> type) {
				return new RepairScheduler(
						kryo.readObject(input, Time.class),
						kryo.readObject(input, TimeCommandExecutor.class),
						kryo.readObject(input, EmployeeHandler.class),
						kryo.readObject(input, LinkedList.class),
						kryo.readObject(input, List.class, KryoConfig.listSerializer(Employee.class)),
						kryo.readObject(input, Shift.class));
			}
		});
	}

	public RepairScheduler(EmployeeHandler employeeHandler) {
		super(employeeHandler, new LinkedList<>(), Profession.TECHNICIAN);
	}

	private RepairScheduler(Time time,
			TimeCommandExecutor timeCommandExecutor,
			EmployeeHandler employeeHandler,
			Queue<Room> entitiesToExecuteService,
			List<Employee> workingEmployees,
			Shift currentShift) {
		super(time, timeCommandExecutor, employeeHandler, entitiesToExecuteService, CLEANER, workingEmployees, currentShift);
	}

	@Override
	protected void executeService(Employee technician, Room room) {
		technician.setOccupied(true);

		timeCommandExecutor.addCommand(
				new TimeCommand(() -> {
					technician.setOccupied(false);
					room.roomState.setFaulty(false);
					OpinionBuilder.saveRoomRepairingData(technician, room);
					executeServiceIfPossible(technician);
				}, time.getTime().plus(technician.getServiceExecutionTime())));
	}

	public void perShiftUpdate() {
		currentShift = currentShift.next();
		workingEmployees = employeeHandler.getWorkingEmployeesByProfession(employeesProfession).stream()
				.filter(employee -> employee.shift.equals(currentShift))
				.collect(Collectors.toList());
		workingEmployees.forEach(this::executeServiceIfPossible);

	}

}
