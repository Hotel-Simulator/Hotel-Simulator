package pl.agh.edu.model.client;

import pl.agh.edu.enums.HotelVisitPurpose;
import pl.agh.edu.enums.RoomRank;
import pl.agh.edu.model.Opinion;
import pl.agh.edu.model.Room;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class ClientGroup {
    private final List<Client> members;
    private final HotelVisitPurpose hotelVisitPurpose;
    private final LocalDateTime checkOutTime;
//    private Opinion opinion;
    private final BigDecimal desiredPricePerNight;
    private final RoomRank desiredRoomRank;
    private final Duration maxWaitingTime;

    private ClientGroup(Builder builder) {
        this.hotelVisitPurpose = builder.hotelVisitPurpose;
        this.members = builder.members;
        this.checkOutTime = builder.checkOutTime;
        this.desiredPricePerNight = builder.desiredPricePerNight;
        this.desiredRoomRank = builder.desiredRoomRank;
        this.maxWaitingTime = builder.maxWaitingTime;
    }

    public LocalDateTime getCheckOutTime() {
        return checkOutTime;
    }

    public RoomRank getDesiredRoomRank() {
        return desiredRoomRank;
    }



//    public void generateOpinions(){
//        for(Client client: members){
//            client.generateOpinion(this.room, this.budgetPerNight);
//        }
//    }

    @Override
    public String toString() {
        return "ClientGroup{" +

                "hotelVisitPurpose=" + hotelVisitPurpose +
                ", checkOutTime=" + checkOutTime +
                ", desiredPricePerNight=" + desiredPricePerNight +
                ", desiredRoomRank=" + desiredRoomRank +
                ", numberOfMembers=" + members.size() +
                '}';
    }
    public Duration getMaxWaitingTime() {
        return maxWaitingTime;
    }

    public BigDecimal getDesiredPricePerNight() {
        return desiredPricePerNight;
    }

    public static class Builder {
        private HotelVisitPurpose hotelVisitPurpose;
        private List<Client> members;
        private LocalDateTime checkOutTime;
        private BigDecimal desiredPricePerNight;
        private RoomRank desiredRoomRank;
        private Duration maxWaitingTime;

        public Builder hotelVisitPurpose(HotelVisitPurpose hotelVisitPurpose) {
            this.hotelVisitPurpose = hotelVisitPurpose;
            return this;
        }

        public Builder members(List<Client> members) {
            this.members = members;
            return this;
        }

        public Builder checkOutTime(LocalDateTime checkOutTime) {
            this.checkOutTime = checkOutTime;
            return this;
        }

        public Builder desiredPricePerNight(BigDecimal desiredPricePerNight) {
            this.desiredPricePerNight = desiredPricePerNight;
            return this;
        }

        public Builder desiredRoomRank(RoomRank desiredRoomRank) {
            this.desiredRoomRank = desiredRoomRank;
            return this;
        }

        public Builder maxWaitingTime(Duration maxWaitingTime) {
            this.maxWaitingTime = maxWaitingTime;
            return this;
        }

        public ClientGroup build() {
            return new ClientGroup(this);
        }
    }
}
