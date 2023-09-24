package pl.agh.edu.management.employee.work_scheduler;

import java.util.*;

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
	}

	@Override
	protected void executeService(Employee receptionist, ClientGroup clientGroup) {
		receptionist.setOccupied(true);
		timeCommandExecutor.addCommand(
				createTimeCommandForServingArrivedClients(receptionist, clientGroup));
	}

	private TimeCommand createTimeCommandForBreakingRoom(Room room, ClientGroup clientGroup) {
		return new TimeCommand(() -> {
			room.roomState.setFaulty(true);
			hotelHandler.repairScheduler.addEntity(room);
		}, RandomUtils.randomDateTime(time.getTime(), clientGroup.getCheckOutTime()));
	}

	private TimeCommand createTimeCommandForCheckingOutClients(Room room, ClientGroup clientGroup) {
		return new TimeCommand(() -> {
			room.checkOut();
			hotelHandler.cleaningScheduler.addEntity(room);
		}, clientGroup.getCheckOutTime());
	}

	private TimeCommand createTimeCommandForServingArrivedClients(Employee receptionist, ClientGroup clientGroup) {
		return new TimeCommand(
				() -> {
					Optional<Room> optionalRoom = hotelHandler.roomManager.findRoomForClientGroup(clientGroup);
					if (optionalRoom.isPresent()) {
						Room room = optionalRoom.get();
						room.checkIn(clientGroup);
						hotelHandler.bankAccountHandler.registerIncome(hotelHandler.roomManager.getRoomPriceList().getPrice(room));
						if (RandomUtils.randomBooleanWithProbability(JSONGameDataLoader.roomFaultProbability)) {
							timeCommandExecutor.addCommand(createTimeCommandForBreakingRoom(room, clientGroup));
						}
						timeCommandExecutor.addCommand(createTimeCommandForCheckingOutClients(room, clientGroup));
					}
					receptionist.setOccupied(false);
				}, time.getTime().plus(receptionist.getServiceExecutionTime()));
	}

	public void removeEntity(ClientGroup clientGroup) {
		entitiesToExecuteService.remove(clientGroup);
	}

}
