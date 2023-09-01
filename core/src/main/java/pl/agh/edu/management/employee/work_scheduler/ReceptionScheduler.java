package pl.agh.edu.management.employee.work_scheduler;

import java.util.*;

import pl.agh.edu.json.data_loader.JSONGameDataLoader;
import pl.agh.edu.model.Hotel;
import pl.agh.edu.model.Room;
import pl.agh.edu.model.client.ClientGroup;
import pl.agh.edu.model.employee.Employee;
import pl.agh.edu.model.employee.Profession;
import pl.agh.edu.time_command.TimeCommand;
import pl.agh.edu.utils.RandomUtils;

public class ReceptionScheduler extends WorkScheduler<ClientGroup> {

	private final CleaningScheduler cleaningScheduler;
	private final RepairScheduler repairScheduler;

	public ReceptionScheduler(Hotel hotel, CleaningScheduler cleaningScheduler, RepairScheduler repairScheduler) {
		super(hotel, new LinkedList<>(), Profession.RECEPTIONIST);
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
		return new TimeCommand(() -> {
			room.roomState.setFaulty(true);
			repairScheduler.addEntity(room);
		}, RandomUtils.randomDateTime(time.getTime(), clientGroup.getCheckOutTime()));
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
					Optional<Room> optionalRoom = hotel.findRoomForClientGroup(clientGroup);
					if (optionalRoom.isPresent()) {
						Room room = optionalRoom.get();
						room.checkIn(clientGroup);
						if (RandomUtils.randomBooleanWithProbability(JSONGameDataLoader.roomFaultProbability)) {
							timeCommandExecutor.addCommand(breakRoomTimeCommand(room, clientGroup));
						}
						timeCommandExecutor.addCommand(checkOutTimeCommand(room, clientGroup));
					}
					receptionist.setOccupied(false);
				}, time.getTime().plus(receptionist.getServiceExecutionTime()));
	}

	public void removeEntity(ClientGroup clientGroup) {
		entitiesToExecuteService.remove(clientGroup);
	}

}
