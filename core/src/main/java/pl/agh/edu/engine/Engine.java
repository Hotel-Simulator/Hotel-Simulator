package pl.agh.edu.engine;

import static pl.agh.edu.engine.time.Frequency.EVERY_DAY;
import static pl.agh.edu.engine.time.Frequency.EVERY_MONTH;
import static pl.agh.edu.engine.time.Frequency.EVERY_SHIFT;
import static pl.agh.edu.engine.time.Frequency.EVERY_YEAR;

import java.time.LocalDateTime;

import pl.agh.edu.data.loader.JSONBankDataLoader;
import pl.agh.edu.engine.client.Arrival;
import pl.agh.edu.engine.client.ClientGroupGenerationHandler;
import pl.agh.edu.engine.event.EventHandler;
import pl.agh.edu.engine.hotel.Hotel;
import pl.agh.edu.engine.hotel.HotelHandler;
import pl.agh.edu.engine.hotel.HotelType;
import pl.agh.edu.engine.hotel.scenario.HotelScenariosManager;
import pl.agh.edu.engine.time.Time;
import pl.agh.edu.engine.time.TimeCommandExecutor;
import pl.agh.edu.engine.time.command.RepeatingTimeCommand;
import pl.agh.edu.engine.time.command.TimeCommand;

public class Engine {
	public final Time time = Time.getInstance();
	private final Hotel hotel = new Hotel();
	private final TimeCommandExecutor timeCommandExecutor = TimeCommandExecutor.getInstance();
	private final HotelScenariosManager hotelScenariosManager = new HotelScenariosManager(HotelType.HOTEL);
	public final EventHandler eventHandler = new EventHandler(hotelScenariosManager);
	private final HotelHandler hotelHandler = new HotelHandler();
	private final ClientGroupGenerationHandler clientGroupGenerationHandler = new ClientGroupGenerationHandler(hotelScenariosManager, hotelHandler.bankAccountHandler);

	public Engine() {

		LocalDateTime currentTime = time.getTime();

		initializeEveryShiftUpdates(currentTime);

		initializeEveryDayUpdates(currentTime);

		initializeEveryMonthUpdates(currentTime);

		initializeEveryYearUpdates(currentTime);
	}

	private void initializeEveryShiftUpdates(LocalDateTime currentTime) {
		timeCommandExecutor.addCommand(new RepeatingTimeCommand(EVERY_SHIFT, hotelHandler.cleaningScheduler::perShiftUpdate, currentTime));
		timeCommandExecutor.addCommand(new RepeatingTimeCommand(EVERY_SHIFT, hotelHandler.receptionScheduler::perShiftUpdate, currentTime));
		timeCommandExecutor.addCommand(new RepeatingTimeCommand(EVERY_SHIFT, hotelHandler.repairScheduler::perShiftUpdate, currentTime));
	}

	private void initializeEveryDayUpdates(LocalDateTime currentTime) {
		timeCommandExecutor.addCommand(new RepeatingTimeCommand(EVERY_DAY, hotelHandler.possibleEmployeeHandler::dailyUpdate, currentTime));
		timeCommandExecutor.addCommand(new RepeatingTimeCommand(EVERY_DAY, this::dailyUpdate, currentTime));
		timeCommandExecutor.addCommand(new RepeatingTimeCommand(EVERY_DAY, hotelHandler.cleaningScheduler::dailyAtCheckOutTimeUpdate, LocalDateTime.of(currentTime
				.toLocalDate(),
				hotel.getCheckOutTime())));
		timeCommandExecutor.addCommand(new RepeatingTimeCommand(EVERY_DAY, hotelHandler.cleaningScheduler::dailyAtCheckInTimeUpdate, LocalDateTime.of(currentTime
				.toLocalDate(),
				hotel.getCheckOutTime())));
	}

	private void initializeEveryMonthUpdates(LocalDateTime currentTime) {
		timeCommandExecutor.addCommand(new RepeatingTimeCommand(EVERY_MONTH, hotelHandler.employeeSalaryHandler::monthlyUpdate, currentTime));
		timeCommandExecutor.addCommand(new RepeatingTimeCommand(EVERY_MONTH, hotelHandler.bankAccount::monthlyUpdate, currentTime.plusDays(
				JSONBankDataLoader.chargeAccountFeeDayOfMonth - 1)));
	}

	private void initializeEveryYearUpdates(LocalDateTime currentTime) {
		timeCommandExecutor.addCommand(new RepeatingTimeCommand(EVERY_YEAR, eventHandler::yearlyUpdate, currentTime));
	}

	private void generateClientArrivals() {
		clientGroupGenerationHandler.getArrivalsForDay(hotel.getCheckInTime(), hotel.getCheckOutTime())
				.forEach(arrival -> timeCommandExecutor.addCommand(
						createTimeCommandForClientArrival(arrival)));
	}

	private TimeCommand createTimeCommandForClientArrival(Arrival arrival) {
		return new TimeCommand(() -> {
			hotelHandler.receptionScheduler.addEntity(arrival.clientGroup());
			timeCommandExecutor.addCommand(
					new TimeCommand(() -> hotelHandler.receptionScheduler.removeEntity(arrival.clientGroup()),
							LocalDateTime.of(
									time.getTime().toLocalDate(),
									arrival.time()).plus(arrival.clientGroup().getMaxWaitingTime())));
		}, LocalDateTime.of(time.getTime().toLocalDate(), arrival.time()));
	}

	public void dailyUpdate() {
		generateClientArrivals();
	}

}
