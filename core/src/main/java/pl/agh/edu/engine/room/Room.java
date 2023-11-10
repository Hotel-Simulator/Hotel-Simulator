package pl.agh.edu.engine.room;

import pl.agh.edu.engine.client.ClientGroup;

public class Room {
	private RoomRank rank;
	public final RoomSize size;
	public final RoomState roomState = new RoomState();
	private ClientGroup residents;

	public Room(RoomRank rank, RoomSize size) {
		this.size = size;
		this.rank = rank;
	}

	public RoomRank getRank() {
		return rank;
	}

	public void setRank(RoomRank rank) {
		this.rank = rank;
	}

	public void changeRank(RoomRank desiredRank) {
		if (desiredRank == rank) {
			throw new IllegalArgumentException("Desired roomRank must be different from the current rank.");
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
