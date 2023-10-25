package pl.agh.edu.engine.opinion;

import java.math.BigDecimal;

import pl.agh.edu.engine.client.ClientGroup;
import pl.agh.edu.engine.employee.Employee;
import pl.agh.edu.engine.room.Room;
import pl.agh.edu.engine.time.Time;

public class OpinionBuilder {
	private static final Time time = Time.getInstance();

	private OpinionBuilder() {}

	public static void saveStartWaitingAtQueueData(ClientGroup clientGroup) {
		clientGroup.opinion.queueWaiting.setStartDate(time.getTime());
	}

	public static void saveRoomRepairingData(Employee technician, Room room) {
		if (room.roomState.isOccupied()) {
			room.getResidents().opinion.roomBreaking.roomRepaired();
			room.getResidents().opinion.employeesSatisfaction.addSatisfaction(technician.getSatisfaction());
		}
	}

	public static void saveRoomBreakingData(Room room) {
		if (room.roomState.isOccupied()) {
			room.getResidents().opinion.roomBreaking.roomBroke();
		}
	}

	public static void saveRoomDailyCleaningData(Employee cleaner, Room room) {
		if (room.roomState.isOccupied()) {
			room.getResidents().opinion.roomCleaning.setRoomCleaned();
			room.getResidents().opinion.employeesSatisfaction.addSatisfaction(cleaner.getSatisfaction());
		}
	}

	public static void saveReceptionData(Employee receptionist, ClientGroup clientGroup) {
		clientGroup.opinion.employeesSatisfaction.addSatisfaction(receptionist.getSatisfaction());
		clientGroup.opinion.queueWaiting.setEndDate(time.getTime());
	}

	public static void saveRoomGettingData(ClientGroup clientGroup, Room room, BigDecimal roomPrice) {
		clientGroup.opinion.setClientGroupGotRoom();
		clientGroup.opinion.roomPrice.setPrices(roomPrice);
		if (!room.roomState.isDirty()) {
			clientGroup.opinion.roomCleaning.setRoomCleaned();
		}
	}
}
