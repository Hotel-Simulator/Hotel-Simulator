package pl.agh.edu.engine;

import static pl.agh.edu.engine.time.Frequency.EVERY_DAY;
import static pl.agh.edu.engine.time.Frequency.EVERY_MONTH;
import static pl.agh.edu.engine.time.Frequency.EVERY_SHIFT;
import static pl.agh.edu.engine.time.Frequency.EVERY_YEAR;

import java.time.LocalDateTime;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import pl.agh.edu.data.loader.JSONBankDataLoader;
import pl.agh.edu.data.loader.JSONHotelDataLoader;
import pl.agh.edu.data.type.BankData;
import pl.agh.edu.engine.advertisement.AdvertisementHandler;
import pl.agh.edu.engine.attraction.AttractionHandler;
import pl.agh.edu.engine.bank.BankAccount;
import pl.agh.edu.engine.bank.BankAccountHandler;
import pl.agh.edu.engine.building_cost.BuildingCostMultiplierHandler;
import pl.agh.edu.engine.building_cost.BuildingCostSupplier;
import pl.agh.edu.engine.client.ClientGroupArrivalGenerationHandler;
import pl.agh.edu.engine.client.ClientGroupGenerationHandler;
import pl.agh.edu.engine.client.report.collector.ClientGroupReportDataCollector;
import pl.agh.edu.engine.employee.EmployeeSalaryHandler;
import pl.agh.edu.engine.employee.hired.HiredEmployeeHandler;
import pl.agh.edu.engine.employee.possible.PossibleEmployeeHandler;
import pl.agh.edu.engine.employee.scheduler.CleaningScheduler;
import pl.agh.edu.engine.employee.scheduler.ReceptionScheduler;
import pl.agh.edu.engine.employee.scheduler.RepairScheduler;
import pl.agh.edu.engine.event.EventHandler;
import pl.agh.edu.engine.event.temporary.ClientNumberModificationEventHandler;
import pl.agh.edu.engine.hotel.Hotel;
import pl.agh.edu.engine.hotel.HotelType;
import pl.agh.edu.engine.hotel.dificulty.DifficultyLevel;
import pl.agh.edu.engine.hotel.dificulty.GameDifficultyManager;
import pl.agh.edu.engine.hotel.scenario.HotelScenariosManager;
import pl.agh.edu.engine.opinion.OpinionHandler;
import pl.agh.edu.engine.room.RoomManager;
import pl.agh.edu.engine.time.Time;
import pl.agh.edu.engine.time.TimeCommandExecutor;
import pl.agh.edu.engine.time.command.RepeatingTimeCommand;
import pl.agh.edu.serialization.KryoConfig;

public class Engine {
	public final Time time;
	private final TimeCommandExecutor timeCommandExecutor;
	public final OpinionHandler opinionHandler;
	private final GameDifficultyManager gameDifficultyManager;
	public final Hotel hotel;
	public final ClientGroupReportDataCollector clientGroupReportDataCollector;
	public final HiredEmployeeHandler hiredEmployeeHandler;
	public final PossibleEmployeeHandler possibleEmployeeHandler;
	public final BankAccountHandler bankAccountHandler;
	private final BuildingCostSupplier buildingCostSupplier;
	public final ClientNumberModificationEventHandler clientNumberModificationEventHandler;
	private final HotelScenariosManager hotelScenariosManager;
	public final AdvertisementHandler advertisementHandler;
	public final EventHandler eventHandler;
	public final EmployeeSalaryHandler employeeSalaryHandler;
	public final RoomManager roomManager;
	public final AttractionHandler attractionHandler;
	public final ClientGroupGenerationHandler clientGroupGenerationHandler;
	public final CleaningScheduler cleaningScheduler;
	public final RepairScheduler repairScheduler;
	public final ReceptionScheduler receptionScheduler;
	public final ClientGroupArrivalGenerationHandler clientGroupArrivalGenerationHandler;

	public static void kryoRegister() {
		KryoConfig.kryo.register(Engine.class, new Serializer<Engine>() {
			@Override
			public void write(Kryo kryo, Output output, Engine object) {
				kryo.writeObject(output, object.time);
				kryo.writeObject(output, object.timeCommandExecutor);
				kryo.writeObject(output, object.opinionHandler);
				kryo.writeObject(output, object.gameDifficultyManager);
				kryo.writeObject(output, object.hotel);
				kryo.writeObject(output, object.clientGroupReportDataCollector);
				kryo.writeObject(output, object.hiredEmployeeHandler);
				kryo.writeObject(output, object.possibleEmployeeHandler);
				kryo.writeObject(output, object.bankAccountHandler);
				kryo.writeObject(output, object.buildingCostSupplier);
				kryo.writeObject(output, object.clientNumberModificationEventHandler);
				kryo.writeObject(output, object.hotelScenariosManager);
				kryo.writeObject(output, object.advertisementHandler);
				kryo.writeObject(output, object.eventHandler);
				kryo.writeObject(output, object.employeeSalaryHandler);
				kryo.writeObject(output, object.roomManager);
				kryo.writeObject(output, object.attractionHandler);
				kryo.writeObject(output, object.clientGroupGenerationHandler);
				kryo.writeObject(output, object.cleaningScheduler);
				kryo.writeObject(output, object.repairScheduler);
				kryo.writeObject(output, object.receptionScheduler);
				kryo.writeObject(output, object.clientGroupArrivalGenerationHandler);

			}

			@Override
			public Engine read(Kryo kryo, Input input, Class<? extends Engine> type) {
				return new Engine(
						kryo.readObject(input, Time.class),
						kryo.readObject(input, TimeCommandExecutor.class),
						kryo.readObject(input, OpinionHandler.class),
						kryo.readObject(input, GameDifficultyManager.class),
						kryo.readObject(input, Hotel.class),
						kryo.readObject(input, ClientGroupReportDataCollector.class),
						kryo.readObject(input, HiredEmployeeHandler.class),
						kryo.readObject(input, PossibleEmployeeHandler.class),
						kryo.readObject(input, BankAccountHandler.class),
						kryo.readObject(input, BuildingCostSupplier.class),
						kryo.readObject(input, ClientNumberModificationEventHandler.class),
						kryo.readObject(input, HotelScenariosManager.class),
						kryo.readObject(input, AdvertisementHandler.class),
						kryo.readObject(input, EventHandler.class),
						kryo.readObject(input, EmployeeSalaryHandler.class),
						kryo.readObject(input, RoomManager.class),
						kryo.readObject(input, AttractionHandler.class),
						kryo.readObject(input, ClientGroupGenerationHandler.class),
						kryo.readObject(input, CleaningScheduler.class),
						kryo.readObject(input, RepairScheduler.class),
						kryo.readObject(input, ReceptionScheduler.class),
						kryo.readObject(input, ClientGroupArrivalGenerationHandler.class));

			}
		});
	}

	public Engine(String hotelName, HotelType type, DifficultyLevel difficultyLevel) {
		this.time = Time.getInstance();
		this.timeCommandExecutor = TimeCommandExecutor.getInstance();
		this.opinionHandler = new OpinionHandler();
		this.gameDifficultyManager = new GameDifficultyManager(difficultyLevel);
		this.hotel = new Hotel(hotelName);
		this.clientGroupReportDataCollector = new ClientGroupReportDataCollector();
		this.hiredEmployeeHandler = new HiredEmployeeHandler();
		this.possibleEmployeeHandler = new PossibleEmployeeHandler(hiredEmployeeHandler);
		this.bankAccountHandler = new BankAccountHandler(
				new BankAccount(
						gameDifficultyManager.getInitialBalance(),
						new BankData(JSONBankDataLoader.scenarios.get(0).id(),
								JSONBankDataLoader.scenarios.get(0).name(),
								JSONBankDataLoader.scenarios.get(0).accountDetails())));
		BuildingCostMultiplierHandler buildingCostMultiplierHandler = new BuildingCostMultiplierHandler();
		this.buildingCostSupplier = new BuildingCostSupplier(buildingCostMultiplierHandler);
		this.clientNumberModificationEventHandler = new ClientNumberModificationEventHandler();
		this.hotelScenariosManager = new HotelScenariosManager(type);
		this.advertisementHandler = new AdvertisementHandler(bankAccountHandler);
		this.eventHandler = new EventHandler(buildingCostMultiplierHandler, hotelScenariosManager, clientNumberModificationEventHandler);
		this.employeeSalaryHandler = new EmployeeSalaryHandler(hiredEmployeeHandler, bankAccountHandler);
		this.roomManager = new RoomManager(JSONHotelDataLoader.initialRooms, bankAccountHandler, buildingCostSupplier);
		this.attractionHandler = new AttractionHandler(bankAccountHandler, roomManager, buildingCostSupplier);
		this.clientGroupGenerationHandler = new ClientGroupGenerationHandler(
				opinionHandler,
				clientNumberModificationEventHandler,
				advertisementHandler,
				attractionHandler,
				hotelScenariosManager,
				clientGroupReportDataCollector,
				gameDifficultyManager);
		this.cleaningScheduler = new CleaningScheduler(hiredEmployeeHandler, roomManager);
		this.repairScheduler = new RepairScheduler(hiredEmployeeHandler);
		this.receptionScheduler = new ReceptionScheduler(
				hiredEmployeeHandler,
				opinionHandler,
				clientGroupReportDataCollector,
				repairScheduler,
				cleaningScheduler,
				roomManager,
				bankAccountHandler,
				hotel);
		this.clientGroupArrivalGenerationHandler = new ClientGroupArrivalGenerationHandler(
				opinionHandler,
				hotel,
				clientGroupGenerationHandler,
				receptionScheduler);

		LocalDateTime currentTime = time.startingTime;

		initializeEveryShiftUpdates(currentTime);

		initializeEveryDayUpdates(currentTime);

		initializeEveryMonthUpdates(currentTime);

		initializeEveryYearUpdates(currentTime);
	}

	private Engine(Time time,
			TimeCommandExecutor timeCommandExecutor,
			OpinionHandler opinionHandler,
			GameDifficultyManager gameDifficultyManager,
			Hotel hotel,
			ClientGroupReportDataCollector clientGroupReportDataCollector,
			HiredEmployeeHandler hiredEmployeeHandler,
			PossibleEmployeeHandler possibleEmployeeHandler,
			BankAccountHandler bankAccountHandler,
			BuildingCostSupplier buildingCostSupplier,
			ClientNumberModificationEventHandler clientNumberModificationEventHandler,
			HotelScenariosManager hotelScenariosManager,
			AdvertisementHandler advertisementHandler,
			EventHandler eventHandler,
			EmployeeSalaryHandler employeeSalaryHandler,
			RoomManager roomManager,
			AttractionHandler attractionHandler,
			ClientGroupGenerationHandler clientGroupGenerationHandler,
			CleaningScheduler cleaningScheduler,
			RepairScheduler repairScheduler,
			ReceptionScheduler receptionScheduler,
			ClientGroupArrivalGenerationHandler clientGroupArrivalGenerationHandler) {
		this.time = time;
		this.timeCommandExecutor = timeCommandExecutor;
		this.opinionHandler = opinionHandler;
		this.gameDifficultyManager = gameDifficultyManager;
		this.hotel = hotel;
		this.clientGroupReportDataCollector = clientGroupReportDataCollector;
		this.hiredEmployeeHandler = hiredEmployeeHandler;
		this.possibleEmployeeHandler = possibleEmployeeHandler;
		this.bankAccountHandler = bankAccountHandler;
		this.buildingCostSupplier = buildingCostSupplier;
		this.clientNumberModificationEventHandler = clientNumberModificationEventHandler;
		this.hotelScenariosManager = hotelScenariosManager;
		this.advertisementHandler = advertisementHandler;
		this.eventHandler = eventHandler;
		this.employeeSalaryHandler = employeeSalaryHandler;
		this.roomManager = roomManager;
		this.attractionHandler = attractionHandler;
		this.clientGroupGenerationHandler = clientGroupGenerationHandler;
		this.cleaningScheduler = cleaningScheduler;
		this.repairScheduler = repairScheduler;
		this.receptionScheduler = receptionScheduler;
		this.clientGroupArrivalGenerationHandler = clientGroupArrivalGenerationHandler;
	}

	private void initializeEveryShiftUpdates(LocalDateTime currentTime) {
		timeCommandExecutor.addCommand(new RepeatingTimeCommand(EVERY_SHIFT, cleaningScheduler::perShiftUpdate, currentTime));
		timeCommandExecutor.addCommand(new RepeatingTimeCommand(EVERY_SHIFT, receptionScheduler::perShiftUpdate, currentTime));
		timeCommandExecutor.addCommand(new RepeatingTimeCommand(EVERY_SHIFT, repairScheduler::perShiftUpdate, currentTime));
	}

	private void initializeEveryDayUpdates(LocalDateTime currentTime) {
		timeCommandExecutor.addCommand(new RepeatingTimeCommand(EVERY_DAY, attractionHandler::dailyUpdate, currentTime));
		timeCommandExecutor.addCommand(new RepeatingTimeCommand(EVERY_DAY, opinionHandler::dailyUpdate, currentTime));
		timeCommandExecutor.addCommand(new RepeatingTimeCommand(EVERY_DAY, possibleEmployeeHandler::dailyUpdate, currentTime));
		timeCommandExecutor.addCommand(new RepeatingTimeCommand(EVERY_DAY, clientGroupArrivalGenerationHandler::dailyUpdate, currentTime));
		timeCommandExecutor.addCommand(new RepeatingTimeCommand(EVERY_DAY, cleaningScheduler::dailyAtCheckOutTimeUpdate,
				LocalDateTime.of(currentTime.toLocalDate(), hotel.getCheckOutTime())));
		timeCommandExecutor.addCommand(new RepeatingTimeCommand(EVERY_DAY, cleaningScheduler::dailyAtCheckInTimeUpdate,
				LocalDateTime.of(currentTime.toLocalDate(), hotel.getCheckOutTime())));
	}

	private void initializeEveryMonthUpdates(LocalDateTime currentTime) {
		timeCommandExecutor.addCommand(new RepeatingTimeCommand(EVERY_MONTH, employeeSalaryHandler::monthlyUpdate, currentTime));
		timeCommandExecutor.addCommand(new RepeatingTimeCommand(EVERY_MONTH, bankAccountHandler.account::monthlyUpdate, currentTime.plusDays(
				JSONBankDataLoader.chargeAccountFeeDayOfMonth - 1)));
	}

	private void initializeEveryYearUpdates(LocalDateTime currentTime) {
		timeCommandExecutor.addCommand(new RepeatingTimeCommand(EVERY_YEAR, eventHandler::yearlyUpdate, currentTime));
	}
}
