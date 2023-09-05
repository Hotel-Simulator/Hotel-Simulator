package pl.agh.edu.model;

import java.math.BigDecimal;

import pl.agh.edu.enums.RoomRank;
import pl.agh.edu.enums.RoomSize;
import pl.agh.edu.model.client.ClientGroup;

public class Room {
	private RoomRank rank;
	public final RoomSize size;
	private BigDecimal marketPrice;
	private BigDecimal maintenancePrice;
	private ClientGroup residents;
	public RoomState roomState = new RoomState();

	public Room(RoomRank rank, RoomSize size) {
		this.size = size;
		this.rank = rank;
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

	public void checkIn(ClientGroup clientGroup) {
		this.residents = clientGroup;
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
