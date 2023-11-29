package pl.agh.edu.engine.employee.scheduler;

import static pl.agh.edu.engine.employee.Profession.RECEPTIONIST;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.stream.Collectors;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import pl.agh.edu.data.loader.JSONGameDataLoader;
import pl.agh.edu.data.loader.JSONOpinionDataLoader;
import pl.agh.edu.engine.bank.BankAccountHandler;
import pl.agh.edu.engine.client.ClientGroup;
import pl.agh.edu.engine.client.report.collector.ClientGroupReportDataCollector;
import pl.agh.edu.engine.employee.Profession;
import pl.agh.edu.engine.employee.Shift;
import pl.agh.edu.engine.employee.hired.HiredEmployee;
import pl.agh.edu.engine.employee.hired.HiredEmployeeHandler;
import pl.agh.edu.engine.hotel.Hotel;
import pl.agh.edu.engine.opinion.OpinionBuilder;
import pl.agh.edu.engine.opinion.OpinionHandler;
import pl.agh.edu.engine.room.Room;
import pl.agh.edu.engine.room.RoomManager;
import pl.agh.edu.engine.time.Time;
import pl.agh.edu.engine.time.TimeCommandExecutor;
import pl.agh.edu.engine.time.command.TimeCommand;
import pl.agh.edu.serialization.KryoConfig;
import pl.agh.edu.utils.RandomUtils;

public class ReceptionScheduler extends WorkScheduler<ClientGroup> {
	private final OpinionHandler opinionHandler;
	private final ClientGroupReportDataCollector clientGroupReportDataCollector;
	private final RepairScheduler repairScheduler;
	private final CleaningScheduler cleaningScheduler;
	private final RoomManager roomManager;
	private final BankAccountHandler bankAccountHandler;
	private final Hotel hotel;

	public static void kryoRegister() {
		KryoConfig.kryo.register(ReceptionScheduler.class, new Serializer<ReceptionScheduler>() {
			@Override
			public void write(Kryo kryo, Output output, ReceptionScheduler object) {
				kryo.writeObject(output, object.time);
				kryo.writeObject(output, object.timeCommandExecutor);
				kryo.writeObject(output, object.employeeHandler);
				kryo.writeObject(output, object.opinionHandler);
				kryo.writeObject(output, object.clientGroupReportDataCollector);
				kryo.writeObject(output, object.repairScheduler);
				kryo.writeObject(output, object.cleaningScheduler);
				kryo.writeObject(output, object.roomManager);
				kryo.writeObject(output, object.bankAccountHandler);
				kryo.writeObject(output, object.hotel);
				kryo.writeObject(output, object.entitiesToExecuteService);
				kryo.writeObject(output, object.workingEmployees, KryoConfig.listSerializer(HiredEmployee.class));
				kryo.writeObject(output, object.currentShift);
			}

			@Override
			public ReceptionScheduler read(Kryo kryo, Input input, Class<? extends ReceptionScheduler> type) {
				return new ReceptionScheduler(
						kryo.readObject(input, Time.class),
						kryo.readObject(input, TimeCommandExecutor.class),
						kryo.readObject(input, HiredEmployeeHandler.class),
						kryo.readObject(input, OpinionHandler.class),
						kryo.readObject(input, ClientGroupReportDataCollector.class),
						kryo.readObject(input, RepairScheduler.class),
						kryo.readObject(input, CleaningScheduler.class),
						kryo.readObject(input, RoomManager.class),
						kryo.readObject(input, BankAccountHandler.class),
						kryo.readObject(input, Hotel.class),
						kryo.readObject(input, LinkedList.class),
						kryo.readObject(input, List.class, KryoConfig.listSerializer(HiredEmployee.class)),
						kryo.readObject(input, Shift.class));
			}
		});
	}

	public ReceptionScheduler(
			HiredEmployeeHandler employeeHandler,
			OpinionHandler opinionHandler,
			ClientGroupReportDataCollector clientGroupReportDataCollector,
			RepairScheduler repairScheduler,
			CleaningScheduler cleaningScheduler,
			RoomManager roomManager,
			BankAccountHandler bankAccountHandler,
			Hotel hotel) {
		super(employeeHandler, new LinkedList<>(), Profession.RECEPTIONIST);
		this.opinionHandler = opinionHandler;
		this.clientGroupReportDataCollector = clientGroupReportDataCollector;
		this.repairScheduler = repairScheduler;
		this.cleaningScheduler = cleaningScheduler;
		this.roomManager = roomManager;
		this.bankAccountHandler = bankAccountHandler;
		this.hotel = hotel;
	}

	private ReceptionScheduler(Time time,
			TimeCommandExecutor timeCommandExecutor,
			HiredEmployeeHandler employeeHandler,
			OpinionHandler opinionHandler,
			ClientGroupReportDataCollector clientGroupReportDataCollector,
			RepairScheduler repairScheduler,
			CleaningScheduler cleaningScheduler,
			RoomManager roomManager,
			BankAccountHandler bankAccountHandler,
			Hotel hotel,
			Queue<ClientGroup> entitiesToExecuteService,
			List<HiredEmployee> workingEmployees,
			Shift currentShift) {
		super(time, timeCommandExecutor, employeeHandler, entitiesToExecuteService, RECEPTIONIST, workingEmployees, currentShift);
		this.opinionHandler = opinionHandler;
		this.clientGroupReportDataCollector = clientGroupReportDataCollector;
		this.repairScheduler = repairScheduler;
		this.cleaningScheduler = cleaningScheduler;
		this.roomManager = roomManager;
		this.bankAccountHandler = bankAccountHandler;
		this.hotel = hotel;
	}

	@Override
	protected void executeService(HiredEmployee receptionist, ClientGroup clientGroup) {
		receptionist.setOccupied(true);

		OpinionBuilder.saveReceptionData(receptionist, clientGroup, time.getTime());

		timeCommandExecutor.addCommand(createTimeCommandForServingArrivedClients(receptionist, clientGroup));
	}

	private TimeCommand createTimeCommandForBreakingRoom(Room room, LocalDateTime checkOutTime) {
		return new TimeCommand(() -> {
			room.roomState.setFaulty(true);
			OpinionBuilder.saveRoomBreakingData(room);
			repairScheduler.addEntity(room);
		}, RandomUtils.randomDateTime(time.getTime(), checkOutTime));
	}

	private TimeCommand createTimeCommandForCheckingOutClients(Room room, LocalDateTime checkOutTime) {
		return new TimeCommand(() -> {
			opinionHandler.addOpinionWithProbability(room.getResidents(), JSONOpinionDataLoader.opinionProbabilityForClientWhoGotRoom);
			room.checkOut();
			cleaningScheduler.addEntity(room);
		}, checkOutTime);
	}

	private TimeCommand createTimeCommandForServingArrivedClients(HiredEmployee receptionist, ClientGroup clientGroup) {
		return new TimeCommand(
				() -> {
					Optional<Room> optionalRoom = roomManager.findRoomForClientGroup(clientGroup);
					if (optionalRoom.isPresent()) {
						clientGroupReportDataCollector.increaseClientGroupWithRoomCounter();
						Room room = optionalRoom.get();
						room.checkIn(clientGroup);
						BigDecimal roomPrice = roomManager.getRoomPriceList().getPrice(room);
						OpinionBuilder.saveRoomGettingData(clientGroup, room, roomPrice);
						bankAccountHandler.registerIncome(roomPrice.multiply(BigDecimal.valueOf(clientGroup.getNumberOfNights())));
						LocalDateTime checkOutTime = getCheckOutTime(clientGroup.getNumberOfNights(), hotel.getCheckOutTime());
						if (!room.roomState.isFaulty() && RandomUtils.randomBooleanWithProbability(JSONGameDataLoader.roomFaultProbability)) {
							timeCommandExecutor.addCommand(createTimeCommandForBreakingRoom(room, checkOutTime));
						}
						timeCommandExecutor.addCommand(createTimeCommandForCheckingOutClients(room, checkOutTime));
					} else {
						opinionHandler.addOpinionWithProbability(clientGroup, JSONOpinionDataLoader.opinionProbabilityForClientWhoDidNotGetRoom);
					}
					receptionist.setOccupied(false);
					executeServiceIfPossible(receptionist);
				}, time.getTime().plus(receptionist.getServiceExecutionTime()));
	}

	private LocalDateTime getCheckOutTime(int numberOfNight, LocalTime checkOutMaxTime) {
		return LocalDateTime.of(
				time.getTime().toLocalDate().plusDays(numberOfNight),
				RandomUtils.randomLocalTime(LocalTime.of(6, 0), checkOutMaxTime));
	}

	public boolean removeEntity(ClientGroup clientGroup) {
		return entitiesToExecuteService.remove(clientGroup);
	}

	public void perShiftUpdate() {
		currentShift = currentShift.next();
		workingEmployees = employeeHandler.getWorkingEmployeesByProfession(employeesProfession).stream()
				.filter(employee -> employee.getShift().equals(currentShift))
				.collect(Collectors.toList());
		workingEmployees.forEach(this::executeServiceIfPossible);

	}

}
