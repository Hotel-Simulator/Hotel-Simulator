package pl.agh.edu.engine;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Random;

import pl.agh.edu.enums.Frequency;
import pl.agh.edu.enums.RoomState;
import pl.agh.edu.generator.client_generator.ClientGenerator;
import pl.agh.edu.generator.event_generator.EventGenerator;
import pl.agh.edu.json.data_loader.JSONGameDataLoader;
import pl.agh.edu.management.employee.CleaningScheduler;
import pl.agh.edu.management.employee.EmployeesToHireHandler;
import pl.agh.edu.management.employee.RepairScheduler;
import pl.agh.edu.model.Hotel;
import pl.agh.edu.model.Room;
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
	private final CleaningScheduler cleaningScheduler;
	private final RepairScheduler repairScheduler;
	private final EmployeesToHireHandler employeesToHireHandler;
	private final Random random;

	public Engine() {
		this.time = Time.getInstance();
		this.clientGenerator = ClientGenerator.getInstance();

		this.hotel = new Hotel(LocalTime.of(15, 0), LocalTime.of(12, 0));
		this.timeCommandExecutor = TimeCommandExecutor.getInstance();
		this.advertisementHandler = AdvertisementHandler.getInstance();
		this.eventGenerator = EventGenerator.getInstance();
		this.cleaningScheduler = new CleaningScheduler(hotel);
		this.repairScheduler = new RepairScheduler(hotel);
		this.employeesToHireHandler = new EmployeesToHireHandler(hotel);
		this.random = new Random();

		timeCommandExecutor.addCommand(new RepeatingTimeCommand(Frequency.EVERY_SHIFT, cleaningScheduler::perShiftUpdate, time.getTime()));
		timeCommandExecutor.addCommand(new RepeatingTimeCommand(Frequency.EVERY_SHIFT, repairScheduler::perShiftUpdate, time.getTime()));

		timeCommandExecutor.addCommand(new RepeatingTimeCommand(Frequency.EVERY_DAY, advertisementHandler::dailyUpdate, time.getTime()));
		timeCommandExecutor.addCommand(new RepeatingTimeCommand(Frequency.EVERY_DAY, employeesToHireHandler::dailyUpdate, time.getTime()));
		timeCommandExecutor.addCommand(new RepeatingTimeCommand(Frequency.EVERY_DAY, this::dailyUpdate, time.getTime()));
		timeCommandExecutor.addCommand(
				new RepeatingTimeCommand(Frequency.EVERY_DAY, cleaningScheduler::dailyAtCheckOutTimeUpdate, LocalDateTime.of(time.getTime().toLocalDate(), hotel
						.getCheckOutTime())));
		timeCommandExecutor.addCommand(
				new RepeatingTimeCommand(Frequency.EVERY_DAY, cleaningScheduler::dailyAtCheckInTimeUpdate, LocalDateTime.of(time.getTime().toLocalDate(), hotel.getCheckInTime())));

		timeCommandExecutor.addCommand(new RepeatingTimeCommand(Frequency.EVERY_MONTH, hotel::monthlyUpdate, time.getTime()));

		timeCommandExecutor.addCommand(new RepeatingTimeCommand(Frequency.EVERY_YEAR, eventGenerator::yearlyUpdate, time.getTime()));
	}

	private void generateClientArrivals() {
		clientGenerator.generateArrivalsForDay(hotel.getCheckInTime(), hotel.getCheckOutTime())
				.forEach(arrival -> timeCommandExecutor.addCommand(
						new TimeCommand(() -> {
							Room room = hotel.findRoomForClientGroup(arrival.clientGroup());
							if (room != null) {
								room.checkIn(arrival.clientGroup());
								if (random.nextDouble() < JSONGameDataLoader.roomFaultProbability) {
									long minutes = Duration.between(time.getTime(), arrival.clientGroup().getCheckOutTime()).toMinutes();
									timeCommandExecutor.addCommand(
											new TimeCommand(() -> {
												room.setState(RoomState.FAULT);
												repairScheduler.addRoom(room);
											}, time.generateRandomTime(minutes, ChronoUnit.MINUTES))

						);
								}
								timeCommandExecutor.addCommand(
										new TimeCommand(() -> {
											room.checkOut();
											cleaningScheduler.addRoom(room);
										}, arrival.clientGroup().getCheckOutTime()));
							}
						}, LocalDateTime.of(time.getTime().toLocalDate(), arrival.time()))));
	}

	public void dailyUpdate() {
		generateClientArrivals();
	}

	public static void main(String[] args) {
		Engine engine = new Engine();
	}

}
