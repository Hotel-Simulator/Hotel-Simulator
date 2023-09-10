package pl.agh.edu.engine;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import pl.agh.edu.enums.Frequency;
import pl.agh.edu.generator.client_generator.Arrival;
import pl.agh.edu.generator.client_generator.ClientGenerator;
import pl.agh.edu.generator.event_generator.EventGenerator;
import pl.agh.edu.json.data_loader.JSONBankDataLoader;
import pl.agh.edu.management.hotel.HotelHandler;
import pl.agh.edu.model.Hotel;
import pl.agh.edu.model.advertisement.AdvertisementHandler;
import pl.agh.edu.model.bank.BankAccount;
import pl.agh.edu.model.time.Time;
import pl.agh.edu.time_command.RepeatingTimeCommand;
import pl.agh.edu.time_command.TimeCommand;
import pl.agh.edu.time_command.TimeCommandExecutor;

public class Engine {
	private final Time time = Time.getInstance();
	private final ClientGenerator clientGenerator = ClientGenerator.getInstance();
	private final Hotel hotel = new Hotel();
	private final TimeCommandExecutor timeCommandExecutor = TimeCommandExecutor.getInstance();
	private final AdvertisementHandler advertisementHandler = AdvertisementHandler.getInstance();
	private final EventGenerator eventGenerator = EventGenerator.getInstance();
	private final HotelHandler hotelHandler = new HotelHandler();

	private final BankAccount bankAccount = new BankAccount(1, BigDecimal.valueOf(2));

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
		timeCommandExecutor.addCommand(new RepeatingTimeCommand(Frequency.EVERY_DAY, advertisementHandler::dailyUpdate, currentTime));
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
		timeCommandExecutor.addCommand(new RepeatingTimeCommand(Frequency.EVERY_MONTH, hotelHandler.employeeHandler::monthlyUpdate, currentTime));
		timeCommandExecutor.addCommand(new RepeatingTimeCommand(Frequency.EVERY_MONTH, bankAccount::monthlyUpdate, currentTime.plusDays(
				JSONBankDataLoader.chargeAccountFeeDayOfMonth - 1)));
	}

	private void initializeEveryYearUpdates(LocalDateTime currentTime) {
		timeCommandExecutor.addCommand(new RepeatingTimeCommand(Frequency.EVERY_YEAR, eventGenerator::yearlyUpdate, currentTime));
	}

	private void generateClientArrivals() {
		clientGenerator.generateArrivalsForDay(hotel.getCheckInTime(), hotel.getCheckOutTime())
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
