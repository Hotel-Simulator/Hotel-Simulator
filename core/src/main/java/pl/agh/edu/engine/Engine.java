package pl.agh.edu.engine;

import static pl.agh.edu.engine.time.Frequency.EVERY_DAY;
import static pl.agh.edu.engine.time.Frequency.EVERY_MONTH;
import static pl.agh.edu.engine.time.Frequency.EVERY_SHIFT;
import static pl.agh.edu.engine.time.Frequency.EVERY_YEAR;

import java.time.LocalDateTime;

import pl.agh.edu.data.loader.JSONBankDataLoader;
import pl.agh.edu.data.loader.JSONOpinionDataLoader;
import pl.agh.edu.engine.client.Arrival;
import pl.agh.edu.engine.client.ClientGroupGenerationHandler;
import pl.agh.edu.engine.event.EventHandler;
import pl.agh.edu.engine.hotel.HotelHandler;
import pl.agh.edu.engine.hotel.HotelType;
import pl.agh.edu.engine.hotel.scenario.HotelScenariosManager;
import pl.agh.edu.engine.opinion.OpinionBuilder;
import pl.agh.edu.engine.opinion.OpinionHandler;
import pl.agh.edu.engine.time.Time;
import pl.agh.edu.engine.time.TimeCommandExecutor;
import pl.agh.edu.engine.time.command.RepeatingTimeCommand;
import pl.agh.edu.engine.time.command.SerializableRunnable;
import pl.agh.edu.engine.time.command.TimeCommand;

public class Engine {
	public final Time time = Time.getInstance();
	private final TimeCommandExecutor timeCommandExecutor = TimeCommandExecutor.getInstance();
	private final HotelScenariosManager hotelScenariosManager = new HotelScenariosManager(HotelType.HOTEL);
	public final EventHandler eventHandler = new EventHandler(hotelScenariosManager);
	private final HotelHandler hotelHandler = new HotelHandler();
	private final ClientGroupGenerationHandler clientGroupGenerationHandler = new ClientGroupGenerationHandler(
			hotelScenariosManager,
			hotelHandler.bankAccountHandler,
			hotelHandler.attractionHandler);

	public Engine() {

		LocalDateTime currentTime = time.getTime();

		initializeEveryShiftUpdates(currentTime);

		initializeEveryDayUpdates(currentTime);

		initializeEveryMonthUpdates(currentTime);

		initializeEveryYearUpdates(currentTime);
	}

	private void initializeEveryShiftUpdates(LocalDateTime currentTime) {
		timeCommandExecutor.addCommand(new RepeatingTimeCommand(EVERY_SHIFT, (SerializableRunnable) hotelHandler.cleaningScheduler::perShiftUpdate, currentTime));
		timeCommandExecutor.addCommand(new RepeatingTimeCommand(EVERY_SHIFT, (SerializableRunnable) hotelHandler.receptionScheduler::perShiftUpdate, currentTime));
		timeCommandExecutor.addCommand(new RepeatingTimeCommand(EVERY_SHIFT, (SerializableRunnable) hotelHandler.repairScheduler::perShiftUpdate, currentTime));
	}

	private void initializeEveryDayUpdates(LocalDateTime currentTime) {
		timeCommandExecutor.addCommand(new RepeatingTimeCommand(EVERY_DAY, (SerializableRunnable)hotelHandler.attractionHandler::dailyUpdate, currentTime));
		timeCommandExecutor.addCommand(new RepeatingTimeCommand(EVERY_DAY, (SerializableRunnable)OpinionHandler::dailyUpdate, currentTime));
		timeCommandExecutor.addCommand(new RepeatingTimeCommand(EVERY_DAY, (SerializableRunnable)hotelHandler.possibleEmployeeHandler::dailyUpdate, currentTime));
		timeCommandExecutor.addCommand(new RepeatingTimeCommand(EVERY_DAY, (SerializableRunnable)this::dailyUpdate, currentTime));
		timeCommandExecutor.addCommand(new RepeatingTimeCommand(EVERY_DAY, (SerializableRunnable)hotelHandler.cleaningScheduler::dailyAtCheckOutTimeUpdate,
				LocalDateTime.of(currentTime.toLocalDate(), hotelHandler.hotel.getCheckOutTime())));
		timeCommandExecutor.addCommand(new RepeatingTimeCommand(EVERY_DAY, (SerializableRunnable)hotelHandler.cleaningScheduler::dailyAtCheckInTimeUpdate,
				LocalDateTime.of(currentTime.toLocalDate(), hotelHandler.hotel.getCheckOutTime())));
	}

	private void initializeEveryMonthUpdates(LocalDateTime currentTime) {
		timeCommandExecutor.addCommand(new RepeatingTimeCommand(EVERY_MONTH, (SerializableRunnable)hotelHandler.employeeSalaryHandler::monthlyUpdate, currentTime));
		timeCommandExecutor.addCommand(new RepeatingTimeCommand(EVERY_MONTH, (SerializableRunnable)hotelHandler.bankAccount::monthlyUpdate, currentTime.plusDays(
				JSONBankDataLoader.chargeAccountFeeDayOfMonth - 1)));
	}

	private void initializeEveryYearUpdates(LocalDateTime currentTime) {
		timeCommandExecutor.addCommand(new RepeatingTimeCommand(EVERY_YEAR, (SerializableRunnable)eventHandler::yearlyUpdate, currentTime));
	}

	private void generateClientArrivals() {
		clientGroupGenerationHandler.getArrivalsForDay(hotelHandler.hotel.getCheckInTime())
				.forEach(arrival -> timeCommandExecutor.addCommand(
						createTimeCommandForClientArrival(arrival)));
	}

	private TimeCommand createTimeCommandForClientArrival(Arrival arrival) {
		return new TimeCommand((SerializableRunnable)() -> {
			OpinionBuilder.saveStartWaitingAtQueueData(arrival.clientGroup());
			hotelHandler.receptionScheduler.addEntity(arrival.clientGroup());
			timeCommandExecutor.addCommand(
					new TimeCommand((SerializableRunnable)() -> {
						if (hotelHandler.receptionScheduler.removeEntity(arrival.clientGroup())) {
							OpinionBuilder.saveSteppingOutOfQueueData(arrival.clientGroup());
							OpinionHandler.addOpinionWithProbability(arrival.clientGroup(), JSONOpinionDataLoader.opinionProbabilityForClientWhoSteppedOutOfQueue);
						}
					}, LocalDateTime.of(time.getTime().toLocalDate(), arrival.time()).plus(arrival.clientGroup().getMaxWaitingTime())));
		}, LocalDateTime.of(time.getTime().toLocalDate(), arrival.time()));
	}

	public void dailyUpdate() {
		generateClientArrivals();
	}

}
