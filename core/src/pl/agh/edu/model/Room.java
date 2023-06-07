package pl.agh.edu.model;

import pl.agh.edu.enums.RoomRank;
import pl.agh.edu.enums.RoomState;

import java.math.BigDecimal;
import java.sql.Date;

//TODO room standard = min(3* (moja_cena + cena_runkowa)/ 4* moja_cena, 1) decimale builder
// dopracuj troszkę nizej wartośc wzoru - rename standard for competitiveness

public class Room {
    private RoomRank rank;
    private RoomState state;
    private final int capacity;
    private BigDecimal marketPrice;
    private BigDecimal rentPrice;
    private BigDecimal maintenancePrice;

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

    public boolean clean(){
        if (state == RoomState.DIRTY){
            setState(RoomState.MAINTENANCE);
            return true;
        }
        return false;
    }

    public boolean fix(){
        if (state == RoomState.FAULT){
            setState(RoomState.MAINTENANCE);
            return true;
        }
        return false;
    }

    public boolean upgradeRankMany(int num){
        if(rank.ordinal()+ 1 + num > 5){
            return false;
        }
        for(int i = 0; i< num; i++){
            this.upgradeRank();
        }

        setState(RoomState.UPGRADING);
        return true;
    }

    public BigDecimal getStandard(){
        BigDecimal added = rentPrice.add(marketPrice).multiply(BigDecimal.valueOf(3));
        BigDecimal multiplied = rentPrice.multiply(BigDecimal.valueOf(4));
        return  added.divide(multiplied, BigDecimal.ROUND_DOWN).min(BigDecimal.valueOf(1));
    }
}
