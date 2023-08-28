package pl.agh.edu.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

import pl.agh.edu.enums.RoomRank;
import pl.agh.edu.enums.RoomState;
import pl.agh.edu.model.client.ClientGroup;

public class Room {
	private RoomRank rank;
	private RoomState state;
	private final int capacity;
	private BigDecimal marketPrice;
	private BigDecimal maintenancePrice;
	private ClientGroup residents;

	public Room(RoomRank rank, int capacity) {
		this.rank = rank;
		this.state = RoomState.EMPTY;
		this.capacity = capacity;
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

	public RoomState getState() {
		return state;
	}

	public void setState(RoomState state) {
		this.state = state;
	}

	public int getCapacity() {
		return capacity;
	}

	public void upgradeRank(RoomRank desiredRank) {
		if (desiredRank.ordinal() <= rank.ordinal()) {
			throw new IllegalArgumentException("Desired roomRank must be greater than current.");
		}
		this.rank = desiredRank;
	}

	public boolean clean() {
		if (state == RoomState.DIRTY) {
			state = RoomState.EMPTY;
			return true;
		}
		return false;
	}

	public boolean fix() {
		if (state == RoomState.FAULT) {
			state = RoomState.EMPTY;
			return true;
		}
		return false;
	}

	public BigDecimal getStandard() {
		BigDecimal added = RoomPriceList.getPrice(this).add(marketPrice).multiply(BigDecimal.valueOf(3));
		BigDecimal multiplied = RoomPriceList.getPrice(this).multiply(BigDecimal.valueOf(4));
		return added.divide(multiplied, RoundingMode.DOWN).min(BigDecimal.valueOf(1));
	}

	public void checkIn(ClientGroup residents) {
		this.residents = residents;
		this.state = RoomState.OCCUPIED;
	}

	public void checkOut() {
		this.residents = null;
		this.state = RoomState.DIRTY;
	}

	public ClientGroup getResidents() {
		return this.residents;
	}

	public boolean canBeUpgraded() {
		return rank.ordinal() < RoomRank.FIVE.ordinal(); // todo !isOccupied() && !isBroken() && !isBeingUpgraded
	}
}
