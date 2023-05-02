package pl.agh.edu.model;

import pl.agh.edu.enums.RoomRank;
import pl.agh.edu.enums.RoomState;

public class Room {
    private RoomRank rank;
    private RoomState state;
    private final int capacity;

    public Room(RoomRank rank, int capacity) {
        this.rank = rank;
        this.state = RoomState.CLEAN;
        this.capacity = capacity;
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
}
