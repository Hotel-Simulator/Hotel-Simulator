package model;

import org.junit.jupiter.api.Test;
import pl.agh.edu.enums.HotelVisitPurpose;
import pl.agh.edu.enums.RoomRank;
import pl.agh.edu.enums.Sex;
import pl.agh.edu.model.Client;
import pl.agh.edu.model.ClientGroup;
import pl.agh.edu.model.Hotel;
import pl.agh.edu.model.Room;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class HotelTest {

    @Test
    public void findRoomForClientGroupSuccessTest(){
        List<Client> clients = new ArrayList<>();
        clients.add(new Client(23, Sex.MALE, HotelVisitPurpose.BUSINESS_TRIP));
        clients.add(new Client(22, Sex.FEMALE, HotelVisitPurpose.BUSINESS_TRIP));

        ClientGroup group = new ClientGroup(
                HotelVisitPurpose.BUSINESS_TRIP,
                clients,
                LocalDateTime.now(),
                2000,
                RoomRank.THREE
        );

        Hotel hotel = new Hotel(LocalTime.of(15, 0), LocalTime.of(12, 0));
        Room room = new Room(RoomRank.THREE, 2);
        room.setRentPrice(BigDecimal.valueOf(1000L));

        hotel.addRoomByRank(room);

        assertEquals(hotel.findRoomForClientGroup(group).get(), room);
    }
}
