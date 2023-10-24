package pl.agh.edu.engine.employee.scheduler;

import java.util.LinkedList;
import java.util.Optional;

import pl.agh.edu.data.loader.JSONGameDataLoader;
import pl.agh.edu.engine.client.ClientGroup;
import pl.agh.edu.engine.client.report.collector.ClientGroupReportDataCollector;
import pl.agh.edu.engine.employee.Employee;
import pl.agh.edu.engine.employee.Profession;
import pl.agh.edu.engine.hotel.HotelHandler;
import pl.agh.edu.engine.room.Room;
import pl.agh.edu.engine.time.command.TimeCommand;
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
						ClientGroupReportDataCollector.increaseClientGroupWithRoomCounter();
						Room room = optionalRoom.get();
						room.checkIn(clientGroup);
						hotelHandler.bankAccountHandler.registerIncome(hotelHandler.roomManager.getRoomPriceList().getPrice(room));
						if (RandomUtils.randomBooleanWithProbability(JSONGameDataLoader.roomFaultProbability)) {
							timeCommandExecutor.addCommand(createTimeCommandForBreakingRoom(room, clientGroup));
						}
						timeCommandExecutor.addCommand(createTimeCommandForCheckingOutClients(room, clientGroup));
					}
					receptionist.setOccupied(false);
					executeServiceIfPossible(receptionist);
				}, time.getTime().plus(receptionist.getServiceExecutionTime()));
	}

	public void removeEntity(ClientGroup clientGroup) {
		entitiesToExecuteService.remove(clientGroup);
	}

}
