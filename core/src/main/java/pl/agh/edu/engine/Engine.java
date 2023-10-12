package pl.agh.edu.engine;

import java.time.LocalDateTime;

import pl.agh.edu.enums.Frequency;
import pl.agh.edu.generator.client_generator.Arrival;
import pl.agh.edu.json.data_loader.JSONBankDataLoader;
import pl.agh.edu.management.client.ClientGroupGenerationHandler;
import pl.agh.edu.management.event.EventHandler;
import pl.agh.edu.management.hotel.HotelHandler;
import pl.agh.edu.model.Hotel;
import pl.agh.edu.model.time.Time;
import pl.agh.edu.time_command.RepeatingTimeCommand;
import pl.agh.edu.time_command.TimeCommand;
import pl.agh.edu.time_command.TimeCommandExecutor;

public class Engine {
	private final Time time = Time.getInstance();
	private final ClientGroupGenerationHandler clientGroupGenerationHandler = new ClientGroupGenerationHandler();
	private final Hotel hotel = new Hotel();
	private final TimeCommandExecutor timeCommandExecutor = TimeCommandExecutor.getInstance();
	private final EventHandler eventHandler = new EventHandler();
	private final HotelHandler hotelHandler = new HotelHandler();

	public Engine() {

		LocalDateTime currentTime = time.getTime();

		initializeEveryShiftUpdates(currentTime);

		initializeEveryDayUpdates(currentTime);

		initializeEveryMonthUpdates(currentTime);

		initializeEveryYearUpdates(currentTime);
	}

	private void initializeEveryShiftUpdates(LocalDateTime currentTime) {
		timeCommandExecutor.addCommand(new RepeatingTimeCommand(Frequency.EVERY_SHIFT, hotelHandler.cleaningScheduler::perShiftUpdate, currentTime));
		timeCommandExecutor.addCommand(new RepeatingTimeCommand(Frequency.EVERY_SHIFT, hotelHandler.receptionScheduler::perShiftUpdate, currentTime));
		timeCommandExecutor.addCommand(new RepeatingTimeCommand(Frequency.EVERY_SHIFT, hotelHandler.repairScheduler::perShiftUpdate, currentTime));
	}

	private void initializeEveryDayUpdates(LocalDateTime currentTime) {
		timeCommandExecutor.addCommand(new RepeatingTimeCommand(Frequency.EVERY_DAY, hotelHandler.possibleEmployeeHandler::dailyUpdate, currentTime));
		timeCommandExecutor.addCommand(new RepeatingTimeCommand(Frequency.EVERY_DAY, this::dailyUpdate, currentTime));
		timeCommandExecutor.addCommand(new RepeatingTimeCommand(Frequency.EVERY_DAY, hotelHandler.cleaningScheduler::dailyAtCheckOutTimeUpdate, LocalDateTime.of(currentTime
				.toLocalDate(),
				hotel.getCheckOutTime())));
		timeCommandExecutor.addCommand(new RepeatingTimeCommand(Frequency.EVERY_DAY, hotelHandler.cleaningScheduler::dailyAtCheckInTimeUpdate, LocalDateTime.of(currentTime
				.toLocalDate(),
				hotel.getCheckOutTime())));
	}

	private void initializeEveryMonthUpdates(LocalDateTime currentTime) {
		timeCommandExecutor.addCommand(new RepeatingTimeCommand(Frequency.EVERY_MONTH, hotelHandler.employeeSalaryHandler::monthlyUpdate, currentTime));
		timeCommandExecutor.addCommand(new RepeatingTimeCommand(Frequency.EVERY_MONTH, hotelHandler.bankAccount::monthlyUpdate, currentTime.plusDays(
				JSONBankDataLoader.chargeAccountFeeDayOfMonth - 1)));
	}

	private void initializeEveryYearUpdates(LocalDateTime currentTime) {
		timeCommandExecutor.addCommand(new RepeatingTimeCommand(Frequency.EVERY_YEAR, eventHandler::yearlyUpdate, currentTime));
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
									arrival.time().plus(arrival.clientGroup().getMaxWaitingTime()))));
		}, LocalDateTime.of(time.getTime().toLocalDate(), arrival.time()));
	}

	public void dailyUpdate() {
		generateClientArrivals();
	}

	public static void main(String[] args) {
		Engine engine = new Engine();
	}

}
