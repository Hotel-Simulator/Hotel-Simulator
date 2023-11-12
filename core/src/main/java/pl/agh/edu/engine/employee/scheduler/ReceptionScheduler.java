package pl.agh.edu.engine.employee.scheduler;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.LinkedList;
import java.util.Optional;

import pl.agh.edu.data.loader.JSONGameDataLoader;
import pl.agh.edu.data.loader.JSONOpinionDataLoader;
import pl.agh.edu.engine.client.ClientGroup;
import pl.agh.edu.engine.client.report.collector.ClientGroupReportDataCollector;
import pl.agh.edu.engine.employee.Employee;
import pl.agh.edu.engine.employee.Profession;
import pl.agh.edu.engine.hotel.HotelHandler;
import pl.agh.edu.engine.opinion.OpinionBuilder;
import pl.agh.edu.engine.opinion.OpinionHandler;
import pl.agh.edu.engine.room.Room;
import pl.agh.edu.engine.time.command.SerializableRunnable;
import pl.agh.edu.engine.time.command.TimeCommand;
import pl.agh.edu.utils.RandomUtils;

public class ReceptionScheduler extends WorkScheduler<ClientGroup> {

	public ReceptionScheduler(HotelHandler hotelHandler) {
		super(hotelHandler, new LinkedList<>(), Profession.RECEPTIONIST);
	}

	@Override
	protected void executeService(Employee receptionist, ClientGroup clientGroup) {
		receptionist.setOccupied(true);

		OpinionBuilder.saveReceptionData(receptionist, clientGroup);

		timeCommandExecutor.addCommand(createTimeCommandForServingArrivedClients(receptionist, clientGroup));
	}

	private TimeCommand createTimeCommandForBreakingRoom(Room room, LocalDateTime checkOutTime) {
		return new TimeCommand((SerializableRunnable) () -> {
			room.roomState.setFaulty(true);
			OpinionBuilder.saveRoomBreakingData(room);
			hotelHandler.repairScheduler.addEntity(room);
		}, RandomUtils.randomDateTime(time.getTime(), checkOutTime));
	}

	private TimeCommand createTimeCommandForCheckingOutClients(Room room, LocalDateTime checkOutTime) {
		return new TimeCommand((SerializableRunnable) () -> {
			OpinionHandler.addOpinionWithProbability(room.getResidents(), JSONOpinionDataLoader.opinionProbabilityForClientWhoGotRoom);
			room.checkOut();
			hotelHandler.cleaningScheduler.addEntity(room);
		}, checkOutTime);
	}

	private TimeCommand createTimeCommandForServingArrivedClients(Employee receptionist, ClientGroup clientGroup) {
		return new TimeCommand(
				(SerializableRunnable) () -> {
					Optional<Room> optionalRoom = hotelHandler.roomManager.findRoomForClientGroup(clientGroup);
					if (optionalRoom.isPresent()) {
						ClientGroupReportDataCollector.increaseClientGroupWithRoomCounter();
						Room room = optionalRoom.get();
						room.checkIn(clientGroup);
						BigDecimal roomPrice = hotelHandler.roomManager.getRoomPriceList().getPrice(room);
						OpinionBuilder.saveRoomGettingData(clientGroup, room, roomPrice);
						hotelHandler.bankAccountHandler.registerIncome(roomPrice.multiply(BigDecimal.valueOf(clientGroup.getNumberOfNights())));
						LocalDateTime checkOutTime = getCheckOutTime(clientGroup.getNumberOfNights(), hotelHandler.hotel.getCheckOutTime());
						if (!room.roomState.isFaulty() && RandomUtils.randomBooleanWithProbability(JSONGameDataLoader.roomFaultProbability)) {
							timeCommandExecutor.addCommand(createTimeCommandForBreakingRoom(room, checkOutTime));
						}
						timeCommandExecutor.addCommand(createTimeCommandForCheckingOutClients(room, checkOutTime));
					} else {
						OpinionHandler.addOpinionWithProbability(clientGroup, JSONOpinionDataLoader.opinionProbabilityForClientWhoDidNotGetRoom);
					}
					receptionist.setOccupied(false);
					executeServiceIfPossible(receptionist);
				}, time.getTime().plus(receptionist.getServiceExecutionTime()));
	}

	private LocalDateTime getCheckOutTime(int numberOfNight, LocalTime checkOutMaxTime) {
		return LocalDateTime.of(
				time.getTime().toLocalDate().plusDays(numberOfNight),
				RandomUtils.randomLocalTime(LocalTime.of(6, 0), checkOutMaxTime));
	}

	public boolean removeEntity(ClientGroup clientGroup) {
		return entitiesToExecuteService.remove(clientGroup);
	}

}
