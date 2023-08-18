package pl.agh.edu.model;

import java.math.BigDecimal;

import pl.agh.edu.enums.RoomRank;

public class Room {
	private RoomRank rank;
	private final int capacity;
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

	public RoomStates getRoomStates(){
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

	public int getCapacity() {
		return capacity;
	}

	public void upgradeRank() {
		switch (rank) {
		case ONE -> this.rank = RoomRank.TWO;
		case TWO -> this.rank = RoomRank.THREE;
		case THREE -> this.rank = RoomRank.FOUR;
		case FOUR -> this.rank = RoomRank.FIVE;
		case FIVE -> {
		}
		}
	}

	public void upgradeRankMany(int num) {
		if (rank.ordinal() + 1 + num > 5) {
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
		roomStates.setBeingUpgraded(false);
		roomStates.setDirty(true);
	}

	public ClientGroup getResidents() {
		return this.residents;
	}
}
