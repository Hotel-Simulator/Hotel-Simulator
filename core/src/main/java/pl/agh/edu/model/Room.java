package pl.agh.edu.model;

import java.math.BigDecimal;

import pl.agh.edu.enums.RoomRank;
import pl.agh.edu.model.client.ClientGroup;

public class Room {
	public final int capacity;
	private RoomRank rank;
	private BigDecimal marketPrice;
	private BigDecimal rentPrice;
	private BigDecimal maintenancePrice;
	private ClientGroup residents;
	private RoomStates roomStates;

	public Room(RoomRank rank, int capacity) {
		this.rank = rank;
		this.capacity = capacity;
		roomStates = new RoomStates();
	}

	public RoomStates getRoomStates() {
		return roomStates;
	}

	public BigDecimal getRentPrice() {
		return rentPrice;
	}

	public void setRentPrice(BigDecimal rentPrice) {
		this.rentPrice = rentPrice;
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

	public void upgradeRank() {
		switch (rank) {
		case ONE -> this.rank = RoomRank.TWO;
		case TWO -> this.rank = RoomRank.THREE;
		case THREE -> this.rank = RoomRank.FOUR;
		case FOUR -> this.rank = RoomRank.FIVE;
		}
	}

	public void upgradeRankMany(int num) {
		if (rank.ordinal() + num > RoomRank.FIVE.ordinal()) {
			return;
		}
		for (int i = 0; i < num; i++) {
			this.upgradeRank();
		}
		roomStates.setBeingUpgraded(true);
	}

	public BigDecimal getStandard() {
		BigDecimal added = rentPrice.add(marketPrice).multiply(BigDecimal.valueOf(3));
		BigDecimal multiplied = rentPrice.multiply(BigDecimal.valueOf(4));
		return added.divide(multiplied, BigDecimal.ROUND_DOWN).min(BigDecimal.valueOf(1));
	}

	public void checkIn(ClientGroup residents) {
		this.residents = residents;
		roomStates.setOccupied(true);
	}

	public void checkOut() {
		this.residents = null;
		roomStates.setOccupied(false);
		roomStates.setDirty(true);
	}

	public ClientGroup getResidents() {
		return this.residents;
	}
}
