package pl.agh.edu.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoomStates {
    private boolean isOccupied;
    private boolean isDirty;
    private boolean isFaulty;
    private boolean isBeingUpgraded;

    public RoomStates(){
        isOccupied = false;
        isDirty = false;
        isFaulty = false;
        isBeingUpgraded = false;
    }

    public RoomStates(boolean isOccupied, boolean isDirty, boolean isFaulty, boolean isBeingUpgraded) {
        this.isOccupied = isOccupied;
        this.isDirty = isDirty;
        this.isFaulty = isFaulty;
        this.isBeingUpgraded = isBeingUpgraded;
    }
}
