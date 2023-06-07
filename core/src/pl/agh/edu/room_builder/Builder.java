package pl.agh.edu.room_builder;

import org.json.simple.parser.ParseException;
import pl.agh.edu.enums.RoomState;
import pl.agh.edu.generator.client_generator.JSONExtractor;
import pl.agh.edu.model.Room;

import java.io.IOException;
import java.util.HashMap;

public class Builder {

    private boolean isOccupied = false;
    private HashMap<Integer, Integer> buildTimes = new HashMap<>();

    public Builder() throws IOException, ParseException {

        HashMap<String, Long>  upgradeTimes = JSONExtractor.getUpgradeTimesFromJSON();

        for(int i=1; i < JSONExtractor.getMaxRoomSize()-1; i++){
            assert false;
            buildTimes.put(i, Math.toIntExact(upgradeTimes.get(String.valueOf(i))));
        }
    }

    public boolean isOccupied() {
        return isOccupied;
    }

    public void setOccupied(boolean occupied) {
        isOccupied = occupied;
    }

    public void upgradeRoom(Room room, int numUpgrades) throws InterruptedException {

        if(room.getRank().ordinal() + 1 + numUpgrades > 5){
            return ;
        }

        isOccupied = true;

        room.setState(RoomState.UPGRADING);

        Thread.sleep(1000L * buildTimes.get(numUpgrades));

        room.upgradeRankMany(numUpgrades);

        isOccupied = false;

        Thread.currentThread().stop();

    }
}
