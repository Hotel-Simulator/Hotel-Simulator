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
	private boolean isOccupied;
	private boolean isDirty;
	private boolean isFaulty;
	private boolean maintenance;
	private boolean upgrading;

	public Room(RoomRank rank, int capacity) {
		this.rank = rank;
		this.capacity = capacity;
		isOccupied = false;
		isDirty = false;
		isFaulty = false;
		maintenance = false;
		upgrading = false;
	}

	public boolean isOccupied() {
		return isOccupied;
	}

	public void setOccupied(boolean occupied) {
		isOccupied = occupied;
	}

	public boolean isDirty() {
		return isDirty;
	}

	public void setDirty(boolean dirty) {
		isDirty = dirty;
	}

	public boolean isFaulty() {
		return isFaulty;
	}

	public void setFaulty(boolean faulty) {
		isFaulty = faulty;
	}

	public boolean isMaintenance() {
		return maintenance;
	}

	public void setMaintenance(boolean maintenance) {
		this.maintenance = maintenance;
	}

	public boolean isUpgrading() {
		return upgrading;
	}

	public void setUpgrading(boolean upgrading) {
		this.upgrading = upgrading;
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

	public boolean upgradeRank() {
		switch (rank) {
		case ONE -> this.rank = RoomRank.TWO;
		case TWO -> this.rank = RoomRank.THREE;
		case THREE -> this.rank = RoomRank.FOUR;
		case FOUR -> this.rank = RoomRank.FIVE;
		case FIVE -> {
			return false;
		}
		}
		return true;
	}

	public boolean clean() {
		if (isDirty) {
			isDirty = false;
			return true;
		}
		return false;
	}

	public boolean fix() {
		if (isFaulty) {
			isFaulty = false;
			return true;
		}
		return false;
	}

	public boolean upgradeRankMany(int num) {
		if (rank.ordinal() + 1 + num > 5) {
			return false;
		}
		for (int i = 0; i < num; i++) {
			this.upgradeRank();
		}
		upgrading = true;
		return true;
	}

	public BigDecimal getStandard() {
		BigDecimal added = rentPrice.add(marketPrice).multiply(BigDecimal.valueOf(3));
		BigDecimal multiplied = rentPrice.multiply(BigDecimal.valueOf(4));
		return added.divide(multiplied, BigDecimal.ROUND_DOWN).min(BigDecimal.valueOf(1));
	}

	public void checkIn(ClientGroup residents) {
		this.residents = residents;
		isOccupied = true;
	}

	public void checkOut() {
		this.residents = null;
		isDirty = true;
	}

	public ClientGroup getResidents() {
		return this.residents;
	}
}
