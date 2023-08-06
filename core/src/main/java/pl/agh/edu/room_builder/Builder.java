package pl.agh.edu.room_builder;

import org.json.simple.parser.ParseException;
import pl.agh.edu.enums.RoomState;
import pl.agh.edu.json.data_loader.JSONRoomDataLoader;
import pl.agh.edu.model.Room;
import pl.agh.edu.model.time.Time;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class Builder {

    private boolean isOccupied = false;
    private Map<Integer, Integer> buildTimes = new HashMap<>();
    private Room upgradingRoom;
    private int upgradesNum;
    private LocalDateTime plannedEndTime;

    public Builder(){

        Map<String, Long> upgradeTimes = JSONRoomDataLoader.upgradeTimes;

        for(int i=1; i < JSONRoomDataLoader.maxSize-1; i++){
            buildTimes.put(i, Math.toIntExact(upgradeTimes.get(String.valueOf(i))));
        }
    }

    public boolean isOccupied() {
        return isOccupied;
    }

    public void setOccupied(boolean occupied) {
        isOccupied = occupied;
    }

    public void upgradeRoom(Room room, int numUpgrades){

        if(room.getRank().ordinal() + 1 + numUpgrades > 5){
            return ;
        }

        isOccupied = true;

        room.setState(RoomState.UPGRADING);

        this.upgradesNum = numUpgrades;

        Map<String, Long> times = JSONRoomDataLoader.upgradeTimes;

        this.plannedEndTime = Time.getInstance().getTime().plusDays(times.get(String.valueOf(numUpgrades)));

    }

    public boolean finishUpgrade(){

        if(plannedEndTime.isAfter(Time.getInstance().getTime())){
            this.upgradingRoom.upgradeRankMany(upgradesNum);
            this.upgradingRoom.setState(RoomState.EMPTY);

            isOccupied = false;
            return true;
        }
        return false;
    }
}
