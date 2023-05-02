package pl.agh.edu.model;

import com.badlogic.gdx.utils.Array;
import pl.agh.edu.enums.HotelVisitPurpose;
import pl.agh.edu.enums.RoomRank;

import java.time.LocalDateTime;

public class ClientGroup {
    private final Array<Client> members;
    private final HotelVisitPurpose hotelVisitPurpose;
    private LocalDateTime checkInTime;
    private final LocalDateTime checkOutTime;

    private final short budgetPerNight;
    private final RoomRank desiredRoomRank;
    private Room room;

    public ClientGroup(HotelVisitPurpose hotelVisitPurpose, Array<Client> members, LocalDateTime checkOutTime, short budgetPerNight, RoomRank desiredRoomRank) {
        this.hotelVisitPurpose = hotelVisitPurpose;
        this.members = members;
        this.checkOutTime = checkOutTime;
        this.budgetPerNight = budgetPerNight;
        this.desiredRoomRank = desiredRoomRank;
    }

    public Array<Client> getMembers() {
        return members;
    }

    public HotelVisitPurpose getHotelVisitPurpose() {
        return hotelVisitPurpose;
    }

    public int getNumberOfClients(){
        return members.size;
    }

    public LocalDateTime getCheckInTime() {
        return checkInTime;
    }

    public LocalDateTime getCheckOutTime() {
        return checkOutTime;
    }

    public Room getRoom() {
        return room;
    }

    public void checkIn(Room room, LocalDateTime checkInTime){
        this.room = room;
        // TODO: 01.05.2023 dodać room.setState(RoomState.Occupied)
        this.checkInTime = checkInTime;
    }

    public RoomRank getDesiredRoomRank() {
        return desiredRoomRank;
    }

    public short getBudgetPerNight() {
        return budgetPerNight;
    }
}
