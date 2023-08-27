package pl.agh.edu.management.employee.work_scheduler;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.*;

import pl.agh.edu.enums.RoomState;
import pl.agh.edu.json.data_loader.JSONGameDataLoader;
import pl.agh.edu.management.hotel.HotelHandler;
import pl.agh.edu.model.Room;
import pl.agh.edu.model.client.ClientGroup;
import pl.agh.edu.model.employee.Employee;
import pl.agh.edu.model.employee.Profession;
import pl.agh.edu.time_command.TimeCommand;
import pl.agh.edu.utils.RandomUtils;

public class ReceptionScheduler extends WorkScheduler<ClientGroup> {


	public ReceptionScheduler(HotelHandler hotelHandler) {
		super(hotelHandler, new LinkedList<>(), Profession.RECEPTIONIST);
		this.random = new Random();
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
			hotelHandler.repairScheduler.addEntity(room);
		}, RandomUtils.randomDateTime(time.getTime(), clientGroup.getCheckOutTime()));
	}

	private TimeCommand checkOutTimeCommand(Room room, ClientGroup clientGroup) {
		return new TimeCommand(() -> {
			room.checkOut();
			hotelHandler.cleaningScheduler.addEntity(room);
		}, clientGroup.getCheckOutTime());
	}

	private TimeCommand serveCheckingInClientsTimeCommand(Employee receptionist, ClientGroup clientGroup) {
		return new TimeCommand(
				() -> {
					Room room = hotelHandler.roomHandler.findRoomForClientGroup(clientGroup);
					if (room != null) {
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
