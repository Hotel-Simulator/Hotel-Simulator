package pl.agh.edu.engine;

import java.time.LocalDateTime;
import java.time.LocalTime;

import pl.agh.edu.enums.Frequency;
import pl.agh.edu.generator.client_generator.Arrival;
import pl.agh.edu.generator.client_generator.ClientGenerator;
import pl.agh.edu.generator.event_generator.EventGenerator;
import pl.agh.edu.management.employee.EmployeesToHireHandler;
import pl.agh.edu.management.employee.work_scheduler.CleaningScheduler;
import pl.agh.edu.management.employee.work_scheduler.ReceptionScheduler;
import pl.agh.edu.management.employee.work_scheduler.RepairScheduler;
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
	private final CleaningScheduler cleaningScheduler;
	private final RepairScheduler repairScheduler;
	private final ReceptionScheduler receptionScheduler;
	private final EmployeesToHireHandler employeesToHireHandler;

	public Engine() {
		this.time = Time.getInstance();
		this.clientGenerator = ClientGenerator.getInstance();

		this.hotel = new Hotel(LocalTime.of(15, 0), LocalTime.of(12, 0));
		this.timeCommandExecutor = TimeCommandExecutor.getInstance();
		this.advertisementHandler = AdvertisementHandler.getInstance();
		this.eventGenerator = EventGenerator.getInstance();
		this.cleaningScheduler = new CleaningScheduler(hotel);
		this.repairScheduler = new RepairScheduler(hotel);
		this.receptionScheduler = new ReceptionScheduler(hotel, cleaningScheduler, repairScheduler);
		this.employeesToHireHandler = new EmployeesToHireHandler(hotel);

		initializeEveryShiftUpdates();

		initializeEveryDayUpdates();

		initializeEveryMonthUpdates();

		initializeEveryYearUpdates();
	}

	private void initializeEveryYearUpdates() {
		timeCommandExecutor.addCommand(new RepeatingTimeCommand(Frequency.EVERY_YEAR, eventGenerator::yearlyUpdate, time.getTime()));
	}

	private void initializeEveryMonthUpdates() {
		timeCommandExecutor.addCommand(new RepeatingTimeCommand(Frequency.EVERY_MONTH, hotel::monthlyUpdate, time.getTime()));
	}

	private void initializeEveryDayUpdates() {
		timeCommandExecutor.addCommand(new RepeatingTimeCommand(Frequency.EVERY_DAY, advertisementHandler::dailyUpdate, time.getTime()));
		timeCommandExecutor.addCommand(new RepeatingTimeCommand(Frequency.EVERY_DAY, employeesToHireHandler::dailyUpdate, time.getTime()));
		timeCommandExecutor.addCommand(new RepeatingTimeCommand(Frequency.EVERY_DAY, this::dailyUpdate, time.getTime()));
		timeCommandExecutor.addCommand(new RepeatingTimeCommand(Frequency.EVERY_DAY, cleaningScheduler::dailyAtCheckOutTimeUpdate, LocalDateTime.of(time.getTime().toLocalDate(),
				hotel.getCheckOutTime())));
		timeCommandExecutor.addCommand(new RepeatingTimeCommand(Frequency.EVERY_DAY, cleaningScheduler::dailyAtCheckInTimeUpdate, LocalDateTime.of(time.getTime().toLocalDate(),
				hotel.getCheckOutTime())));
	}

	private void initializeEveryShiftUpdates() {
		timeCommandExecutor.addCommand(new RepeatingTimeCommand(Frequency.EVERY_SHIFT, cleaningScheduler::perShiftUpdate, time.getTime()));
		timeCommandExecutor.addCommand(new RepeatingTimeCommand(Frequency.EVERY_SHIFT, receptionScheduler::perShiftUpdate, time.getTime()));
		timeCommandExecutor.addCommand(new RepeatingTimeCommand(Frequency.EVERY_SHIFT, repairScheduler::perShiftUpdate, time.getTime()));
	}

	private void generateClientArrivals() {
		clientGenerator.generateArrivalsForDay(hotel.getCheckInTime(), hotel.getCheckOutTime())
				.forEach(arrival -> timeCommandExecutor.addCommand(
						clientArrivalTimeCommand(arrival)));
	}

	private TimeCommand clientArrivalTimeCommand(Arrival arrival) {
		return new TimeCommand(() -> {
			receptionScheduler.addEntity(arrival.clientGroup());
			timeCommandExecutor.addCommand(
					new TimeCommand(() -> receptionScheduler.removeEntity(arrival.clientGroup()),
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
