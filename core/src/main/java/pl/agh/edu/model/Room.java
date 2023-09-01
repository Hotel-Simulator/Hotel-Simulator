package pl.agh.edu.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

import pl.agh.edu.enums.RoomCapacity;
import pl.agh.edu.enums.RoomRank;
import pl.agh.edu.model.client.ClientGroup;

public class Room {
	private RoomRank rank;
	public final RoomCapacity capacity;
	private BigDecimal marketPrice;
	private BigDecimal maintenancePrice;
	private ClientGroup residents;
	public RoomState roomState = new RoomState();

	public Room(RoomRank rank, RoomCapacity capacity) {
		this.capacity = capacity;
		this.rank = rank;
	}
	}

	public BigDecimal getMaintenancePrice() {
		return maintenancePrice;
	}

	public void setMaintenancePrice(BigDecimal maintenancePrice) {
		this.maintenancePrice = maintenancePrice;
	}

	public RoomRank getRank() {
		return rank;
	}

	public void setRank(RoomRank rank) {
		this.rank = rank;
	}

	public void upgradeRank(RoomRank desiredRank) {
		if (desiredRank.ordinal() <= rank.ordinal()) {
			throw new IllegalArgumentException("Desired roomRank must be greater than current.");
		}
		this.rank = desiredRank;
	}

	public void checkIn(ClientGroup residents) {
		this.residents = residents;
		roomState.setOccupied(true);
	}

	public void checkOut() {
		this.residents = null;
		roomState.setOccupied(false);
		roomState.setDirty(true);
	}

	public ClientGroup getResidents() {
		return this.residents;
	}
}
