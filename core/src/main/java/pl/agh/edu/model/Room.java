package pl.agh.edu.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

import pl.agh.edu.enums.RoomRank;
import pl.agh.edu.model.client.ClientGroup;

public class Room {
	public final int capacity;
	private RoomRank rank;
	private BigDecimal marketPrice;
	private BigDecimal rentPrice;
	private BigDecimal maintenancePrice;
	private ClientGroup residents;
	private final RoomState roomState;

	public Room(RoomRank rank, int capacity, BigDecimal marketPrice, BigDecimal rentPrice) {
		this.capacity = capacity;
		this.rank = rank;
		this.marketPrice = marketPrice;
		this.rentPrice = rentPrice;
		this.roomState = new RoomState();
	}

	public RoomState getRoomStates() {
		return roomState;
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
		roomState.setBeingUpgraded(true);
	}

	public BigDecimal getStandard() {
		BigDecimal added = rentPrice.add(marketPrice).multiply(BigDecimal.valueOf(3));
		BigDecimal multiplied = rentPrice.multiply(BigDecimal.valueOf(4));
		return added.divide(multiplied, RoundingMode.DOWN).min(BigDecimal.valueOf(1));
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
