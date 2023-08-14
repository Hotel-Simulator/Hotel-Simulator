package pl.agh.edu.engine;

import static pl.agh.edu.time_command.RepeatingTimeCommand.Frequency.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Random;

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
	private final CleaningScheduler cleaningScheduler;
	private final RepairScheduler repairScheduler;
	private final Random random;

	public Engine() {
		this.time = Time.getInstance();
		this.clientGenerator = ClientGenerator.getInstance();

		this.hotel = new Hotel(LocalTime.of(15, 0), LocalTime.of(12, 0));
		this.timeCommandExecutor = TimeCommandExecutor.getInstance();
		AdvertisementHandler advertisementHandler = AdvertisementHandler.getInstance();
		EventGenerator eventGenerator = EventGenerator.getInstance();
		this.cleaningScheduler = new CleaningScheduler(hotel);
		this.repairScheduler = new RepairScheduler(hotel);
		EmployeesToHireHandler employeesToHireHandler = new EmployeesToHireHandler(hotel);
		this.random = new Random();

		timeCommandExecutor.addCommand(time.getTime(), new RepeatingTimeCommand(EVERY_SHIFT, cleaningScheduler::perShiftUpdate));
		timeCommandExecutor.addCommand(time.getTime(), new RepeatingTimeCommand(EVERY_SHIFT, repairScheduler::perShiftUpdate));

		timeCommandExecutor.addCommand(time.getTime(), new RepeatingTimeCommand(EVERY_DAY, advertisementHandler::dailyUpdate));
		timeCommandExecutor.addCommand(time.getTime(), new RepeatingTimeCommand(EVERY_DAY, employeesToHireHandler::dailyUpdate));
		timeCommandExecutor.addCommand(time.getTime(), new RepeatingTimeCommand(EVERY_DAY, this::dailyUpdate));
		timeCommandExecutor.addCommand(LocalDateTime.of(time.getTime().toLocalDate(), hotel.getCheckOutTime()),
				new RepeatingTimeCommand(EVERY_DAY, cleaningScheduler::dailyAtCheckOutTimeUpdate));
		timeCommandExecutor.addCommand(LocalDateTime.of(time.getTime().toLocalDate(), hotel.getCheckInTime()),
				new RepeatingTimeCommand(EVERY_DAY, cleaningScheduler::dailyAtCheckInTimeUpdate));

		timeCommandExecutor.addCommand(time.getTime(), new RepeatingTimeCommand(EVERY_MONTH, hotel::monthlyUpdate));

		timeCommandExecutor.addCommand(time.getTime(), new RepeatingTimeCommand(EVERY_YEAR, eventGenerator::yearlyUpdate));
	}

	private void generateClientArrivals() {
		clientGenerator.generateArrivalsForDay(hotel.getCheckInTime(), hotel.getCheckOutTime())
				.forEach(arrival -> timeCommandExecutor.addCommand(LocalDateTime.of(time.getTime().toLocalDate(), arrival.time()),
						new TimeCommand(() -> {
							Room room = hotel.findRoomForClientGroup(arrival.clientGroup());
							if (room != null) {
								room.checkIn(arrival.clientGroup());
								if (random.nextDouble() < JSONGameDataLoader.roomFaultProbability) {
									long minutes = Duration.between(time.getTime(), arrival.clientGroup().getCheckOutTime()).toMinutes();
									timeCommandExecutor.addCommand(time.generateRandomTime(minutes, ChronoUnit.MINUTES),
											new TimeCommand(() -> {
												room.setState(RoomState.FAULT);
												repairScheduler.addRoom(room);
											})

						);
								}
								timeCommandExecutor.addCommand(arrival.clientGroup().getCheckOutTime(),
										new TimeCommand(() -> {
											room.checkOut();
											cleaningScheduler.addRoom(room);
										}));
							}
						})));
	}

	public void dailyUpdate() {
		generateClientArrivals();
	}

}
