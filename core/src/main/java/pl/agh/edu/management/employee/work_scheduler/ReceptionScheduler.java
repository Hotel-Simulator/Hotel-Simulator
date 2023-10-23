package pl.agh.edu.management.employee.work_scheduler;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

import pl.agh.edu.json.data_loader.JSONGameDataLoader;
import pl.agh.edu.json.data_loader.JSONOpinionDataLoader;
import pl.agh.edu.management.client.report.collector.ClientGroupReportDataCollector;
import pl.agh.edu.management.hotel.HotelHandler;
import pl.agh.edu.management.opinion.OpinionHandler;
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
		clientGroup.opinion.employeesSatisfaction.addSatisfaction(receptionist.getSatisfaction());
		timeCommandExecutor.addCommand(
				createTimeCommandForServingArrivedClients(receptionist, clientGroup));
	}

	private TimeCommand createTimeCommandForBreakingRoom(Room room, LocalDateTime checkOutTime) {
		return new TimeCommand(() -> {
			room.roomState.setFaulty(true);
			room.getResidents().opinion.roomBreaking.roomBroke();
			hotelHandler.repairScheduler.addEntity(room);
		}, RandomUtils.randomDateTime(time.getTime(), checkOutTime));
	}

	private TimeCommand createTimeCommandForCheckingOutClients(Room room, LocalDateTime checkOutTime) {
		return new TimeCommand(() -> {
			OpinionHandler.addOpinionWithProbability(room.getResidents().opinion, JSONOpinionDataLoader.opinionProbabilityForClientWhoGotRoom);
			room.checkOut();
			hotelHandler.cleaningScheduler.addEntity(room);
		}, checkOutTime);
	}

	private TimeCommand createTimeCommandForServingArrivedClients(Employee receptionist, ClientGroup clientGroup) {
		return new TimeCommand(
				() -> {
					clientGroup.opinion.queueWaiting.setEndDate(time.getTime());
					Optional<Room> optionalRoom = hotelHandler.roomManager.findRoomForClientGroup(clientGroup);
					if (optionalRoom.isPresent()) {
						clientGroup.opinion.setClientGroupGotRoom();
						ClientGroupReportDataCollector.increaseClientGroupWithRoomCounter();
						Room room = optionalRoom.get();
						room.checkIn(clientGroup);
						if (!room.roomState.isDirty()) {
							clientGroup.opinion.roomCleaning.setRoomCleaned();
						}
						BigDecimal roomPrice = hotelHandler.roomManager.getRoomPriceList().getPrice(room);
						clientGroup.opinion.roomPrice.setPrices(roomPrice);
						hotelHandler.bankAccountHandler.registerIncome(roomPrice.multiply(BigDecimal.valueOf(clientGroup.getNumberOfNights())));
						LocalDateTime checkOutTime = getCheckOutTime(clientGroup.getNumberOfNights(), hotelHandler.hotel.getCheckOutTime());
						if (RandomUtils.randomBooleanWithProbability(JSONGameDataLoader.roomFaultProbability)) {
							timeCommandExecutor.addCommand(createTimeCommandForBreakingRoom(room, checkOutTime));
						}
						timeCommandExecutor.addCommand(createTimeCommandForCheckingOutClients(room, checkOutTime));
					} else {
						OpinionHandler.addOpinionWithProbability(clientGroup.opinion, JSONOpinionDataLoader.opinionProbabilityForClientWhoDidNotGetRoom);
					}
					receptionist.setOccupied(false);
					executeServiceIfPossible(receptionist);
				}, time.getTime().plus(receptionist.getServiceExecutionTime()));
	}

	private LocalDateTime getCheckOutTime(int numberOfNight, LocalTime checkOutMaxTime) {

		return LocalDateTime.of(time.getTime().toLocalDate().plusDays(numberOfNight), RandomUtils.randomLocalTime(LocalTime.of(6, 0), checkOutMaxTime));
	}

	public void removeEntity(ClientGroup clientGroup) {
		entitiesToExecuteService.remove(clientGroup);
	}

}
