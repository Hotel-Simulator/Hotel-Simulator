package pl.agh.edu.model;

import pl.agh.edu.enums.RoomRank;
import pl.agh.edu.enums.RoomState;

import java.math.BigDecimal;
import java.sql.Date;

//TODO room standard = min(moja_cena + cena_runkowa/ 2* moja_cena, 1) decimale builder

public class Room {
    private RoomRank rank;
    private RoomState state;
    private final int capacity;
    private BigDecimal rentPrice;
    private BigDecimal maintenancePrice;
    private long changeStart;  // whole date or just hour?
    private Double changeLength;   // length of change in hours / days

    public Room(RoomRank rank, int capacity) {
        this.rank = rank;
        this.state = RoomState.CLEAN;
        this.capacity = capacity;
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

    private boolean clean(){
        if (state == RoomState.DIRTY){
            setState(RoomState.CLEANING);
            Date time = (Date) new java.util.Date();
            changeStart = time.getTime();
            changeLength = 5.;
            return true;
        }
        return false;
    }

    private boolean fix(){
        if (state == RoomState.FAULT){
            setState(RoomState.FIXING);
            Date time = (Date) new java.util.Date();
            changeStart = time.getTime();
            changeLength = 10.;
            return true;
        }
        return false;
    }

    private boolean upgradeRankMany(int num){
        if(rank.ordinal()+ 1 + num > 5){
            return false;
        }
        for(int i = 0; i< num; i++){
            this.upgradeRank();
        }

        setState(RoomState.UPGRADING);
        Date time = (Date) new java.util.Date();
        changeStart = time.getTime();
        changeLength = 30 * Math.pow(0.9, num-1);

        return true;
    }
}
