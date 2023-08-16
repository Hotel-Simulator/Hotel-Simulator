package pl.agh.edu.management.employee.work_scheduler;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.*;

import pl.agh.edu.enums.RoomState;
import pl.agh.edu.json.data_loader.JSONGameDataLoader;
import pl.agh.edu.model.Hotel;
import pl.agh.edu.model.Room;
import pl.agh.edu.model.client.ClientGroup;
import pl.agh.edu.model.employee.Employee;
import pl.agh.edu.model.employee.Profession;
import pl.agh.edu.time_command.TimeCommand;

public class ReceptionScheduler extends WorkScheduler<ClientGroup> {

	private final Random random;
	private final CleaningScheduler cleaningScheduler;
	private final RepairScheduler repairScheduler;

	public ReceptionScheduler(Hotel hotel, CleaningScheduler cleaningScheduler, RepairScheduler repairScheduler) {
		super(hotel, new LinkedList<>(), Profession.RECEPTIONIST);
		this.random = new Random();
		this.cleaningScheduler = cleaningScheduler;
		this.repairScheduler = repairScheduler;
	}

	@Override
	protected void executeService(Employee receptionist, ClientGroup clientGroup) {
		receptionist.setOccupied(true);
		timeCommandExecutor.addCommand(
				serveCheckingInClientsTimeCommand(receptionist, clientGroup));
	}

	private TimeCommand breakRoomTimeCommand(Room room, ClientGroup clientGroup) {
		long minutes = Duration.between(time.getTime(), clientGroup.getCheckOutTime()).toMinutes();
		return new TimeCommand(() -> {
			room.setState(RoomState.FAULT);
			repairScheduler.addEntity(room);
		}, time.generateRandomTime(minutes, ChronoUnit.MINUTES));
	}

	private TimeCommand checkOutTimeCommand(Room room, ClientGroup clientGroup) {
		return new TimeCommand(() -> {
			room.checkOut();
			cleaningScheduler.addEntity(room);
		}, clientGroup.getCheckOutTime());
	}

	private TimeCommand serveCheckingInClientsTimeCommand(Employee receptionist, ClientGroup clientGroup) {
		return new TimeCommand(
				() -> {
					Room room = hotel.findRoomForClientGroup(clientGroup);
					if (room != null) {
						room.checkIn(clientGroup);
						if (random.nextDouble() < JSONGameDataLoader.roomFaultProbability) {
							timeCommandExecutor.addCommand(
									breakRoomTimeCommand(room, clientGroup));
						}
						timeCommandExecutor.addCommand(

								checkOutTimeCommand(room, clientGroup));
					}
					receptionist.setOccupied(false);
				}, time.getTime().plus(receptionist.getServiceExecutionTime()));
	}

	public void removeEntity(ClientGroup clientGroup) {
		entitiesToExecuteService.remove(clientGroup);
	}

}
