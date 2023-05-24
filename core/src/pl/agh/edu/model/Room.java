package pl.agh.edu.model;

import pl.agh.edu.enums.RoomRank;
import pl.agh.edu.enums.RoomState;

public class Room {
    private RoomRank rank;
    private RoomState state;
    private final int capacity;
    private Integer rentPrice;
    private Integer maintenancePrice;

    public Room(RoomRank rank, int capacity) {
        this.rank = rank;
        this.state = RoomState.CLEAN;
        this.capacity = capacity;
    }

    public Integer getRentPrice() {
        return rentPrice;
    }

    public void setRentPrice(Integer rentPrice) {
        this.rentPrice = rentPrice;
    }

    public Integer getMaintenancePrice() {
        return maintenancePrice;
    }

    public void setMaintenancePrice(Integer maintenancePrice) {
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

    public boolean upgradeRank(){
        switch (rank){
            case ONE -> rank = RoomRank.TWO;
            case TWO -> rank = RoomRank.THREE;
            case THREE -> rank = RoomRank.FOUR;
            case FOUR -> rank = RoomRank.FIVE;
            case FIVE -> {
                return false;
            }
        }
        return true;
    }
}
