package pl.agh.edu.engine;

import java.time.LocalDateTime;

import pl.agh.edu.enums.Frequency;
import pl.agh.edu.generator.client_generator.Arrival;
import pl.agh.edu.generator.client_generator.ClientGenerator;
import pl.agh.edu.generator.event_generator.EventGenerator;
import pl.agh.edu.management.hotel.HotelHandler;
import pl.agh.edu.model.Hotel;
import pl.agh.edu.model.advertisement.AdvertisementHandler;
import pl.agh.edu.model.time.Time;
import pl.agh.edu.time_command.RepeatingTimeCommand;
import pl.agh.edu.time_command.TimeCommand;
import pl.agh.edu.time_command.TimeCommandExecutor;

public class Engine {
	private final Time time;
	private final ClientGenerator clientGenerator;
	private final Hotel hotel;
	private final TimeCommandExecutor timeCommandExecutor;
	private final AdvertisementHandler advertisementHandler;
	private final EventGenerator eventGenerator;
	private final HotelHandler hotelHandler;

	public Engine() {
		this.time = Time.getInstance();
		this.clientGenerator = ClientGenerator.getInstance();
		this.hotelHandler = new HotelHandler();

		this.hotel = new Hotel();
		this.timeCommandExecutor = TimeCommandExecutor.getInstance();
		this.advertisementHandler = AdvertisementHandler.getInstance();
		this.eventGenerator = EventGenerator.getInstance();

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
	}

	private void initializeEveryYearUpdates(LocalDateTime currentTime) {
		timeCommandExecutor.addCommand(new RepeatingTimeCommand(Frequency.EVERY_YEAR, eventGenerator::yearlyUpdate, currentTime));
	}

	private void generateClientArrivals() {
		clientGenerator.generateArrivalsForDay(hotel.getCheckInTime(), hotel.getCheckOutTime())
				.forEach(arrival -> timeCommandExecutor.addCommand(
						clientArrivalTimeCommand(arrival)));
	}

	private TimeCommand clientArrivalTimeCommand(Arrival arrival) {
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
