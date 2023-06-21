package pl.agh.edu.model;

import com.badlogic.gdx.utils.Array;
import pl.agh.edu.enums.HotelVisitPurpose;
import pl.agh.edu.enums.RoomRank;
import pl.agh.edu.enums.RoomState;

import java.time.LocalDateTime;
import java.util.List;

public class ClientGroup {
    private final List<Client> members;
    private final HotelVisitPurpose hotelVisitPurpose;
    private LocalDateTime checkInTime;
    private final LocalDateTime checkOutTime;
    private Opinion opinion;

    //need BIgDecimal
    private final int budgetPerNight;
    private final RoomRank desiredRoomRank;
    private Room room;

    public ClientGroup(HotelVisitPurpose hotelVisitPurpose, List<Client> members, LocalDateTime checkOutTime, int budgetPerNight, RoomRank desiredRoomRank) {
        this.hotelVisitPurpose = hotelVisitPurpose;
        this.members = members;
        this.checkOutTime = checkOutTime;
        this.budgetPerNight = budgetPerNight;
        this.desiredRoomRank = desiredRoomRank;
    }

    public List<Client> getMembers() {
        return members;
    }

    public HotelVisitPurpose getHotelVisitPurpose() {
        return hotelVisitPurpose;
    }

    public int getNumberOfClients(){
        return members.size();
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
        this.room.setState(RoomState.OCCUPIED);
        this.checkInTime = checkInTime;
    }

    public RoomRank getDesiredRoomRank() {
        return desiredRoomRank;
    }

    public int getBudgetPerNight() {
        return budgetPerNight;
    }

    public void generateOpinions(){
        for(Client client: members){
            client.generateOpinion(this.room, this.budgetPerNight);
        }
    }

    @Override
    public String toString() {
        return "ClientGroup{" +

                "hotelVisitPurpose=" + hotelVisitPurpose +
                ", checkOutTime=" + checkOutTime +
                ", budgetPerNight=" + budgetPerNight +
                ", desiredRoomRank=" + desiredRoomRank +
                ", numberOfMembers=" + members.size() +
                '}';
    }

    public void generateOpinion(){
        double margin = budgetPerNight - room.getRentPrice().doubleValue();

        double val = Math.min(0.75 + margin/budgetPerNight, 1.);
        this.opinion = new Opinion(val);

        // zalezy tez od eventów
    }

    public Opinion getOpinion() {return this.opinion;}
}
