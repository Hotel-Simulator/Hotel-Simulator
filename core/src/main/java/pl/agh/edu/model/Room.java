package pl.agh.edu.model;

import java.math.BigDecimal;

import pl.agh.edu.enums.RoomRank;
import pl.agh.edu.model.client.ClientGroup;

public class Room {
	public final int capacity;
	private RoomRank rank;
	private BigDecimal rentPrice;
	private ClientGroup residents;
	public RoomState roomState = new RoomState();

	public Room(RoomRank rank, int capacity) {
		this.capacity = capacity;
		this.rank = rank;
	}

	public RoomRank getRank() {
		return rank;
	}

	public BigDecimal getRentPrice() {
		return rentPrice;
	}

	public void setRank(RoomRank rank) {
		this.rank = rank;
	}

	public void setRentPrice(BigDecimal rentPrice) {
		this.rentPrice = rentPrice;
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
