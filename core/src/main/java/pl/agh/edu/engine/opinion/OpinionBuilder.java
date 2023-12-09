package pl.agh.edu.engine.opinion;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import pl.agh.edu.engine.client.ClientGroup;
import pl.agh.edu.engine.employee.hired.HiredEmployee;
import pl.agh.edu.engine.room.Room;

public class OpinionBuilder {
	private OpinionBuilder() {}

	public static void saveStartWaitingAtQueueData(ClientGroup clientGroup, LocalDateTime time) {
		clientGroup.opinion.queueWaiting.setStartDate(time);
	}

	public static void saveRoomRepairingData(HiredEmployee technician, Room room) {
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

	public static void saveRoomDailyCleaningData(HiredEmployee cleaner, Room room) {
		if (room.roomState.isOccupied()) {
			room.getResidents().opinion.roomCleaning.setRoomCleaned();
			room.getResidents().opinion.employeesSatisfaction.addSatisfaction(cleaner.getSatisfaction());
		}
	}

	public static void saveReceptionData(HiredEmployee receptionist, ClientGroup clientGroup, LocalDateTime time) {
		clientGroup.opinion.employeesSatisfaction.addSatisfaction(receptionist.getSatisfaction());
		clientGroup.opinion.queueWaiting.setEndDate(time);
	}

	public static void saveRoomGettingData(ClientGroup clientGroup, Room room, BigDecimal roomPrice) {
		clientGroup.opinion.setClientGroupGotRoom();
		clientGroup.opinion.roomPrice.setPrices(roomPrice);
		clientGroup.opinion.roomCleaning.setGotCleanRoom(!room.roomState.isDirty());
		clientGroup.opinion.roomBreaking.setGotBrokenRoom(room.roomState.isFaulty());
	}

	public static void saveSteppingOutOfQueueData(ClientGroup clientGroup) {
		clientGroup.opinion.setClientSteppedOutOfQueue();
	}
}
