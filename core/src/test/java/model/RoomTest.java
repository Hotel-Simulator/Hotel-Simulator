package model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import pl.agh.edu.enums.HotelVisitPurpose;
import pl.agh.edu.enums.RoomRank;
import pl.agh.edu.enums.Sex;
import pl.agh.edu.model.Client;
import pl.agh.edu.model.ClientGroup;
import pl.agh.edu.model.Room;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class RoomTest {

    @Test
    public void upgradeRankTest(){
        Room room = new Room(RoomRank.TWO, 5);
        room.upgradeRank();

        assertEquals(room.getRank(), RoomRank.THREE);
    }

    @Test
    public void upgradeRankManySuccessTest(){
        Room room = new Room(RoomRank.ONE, 5);
        room.upgradeRankMany(3);

        assertEquals(room.getRank(), RoomRank.FOUR);
        assertTrue(room.getRoomStates().isBeingUpgraded());
    }

    @Test
    public void upgradeRankManyFailTest(){
        Room room = new Room(RoomRank.ONE, 5);
        room.upgradeRankMany(6);

        assertEquals(room.getRank(), RoomRank.ONE);
        assertFalse(room.getRoomStates().isBeingUpgraded());
    }

    @Test
    public void checkInResidentsTest(){
        List<Client> clients = new ArrayList<>();
        clients.add(new Client(23, Sex.MALE, HotelVisitPurpose.BUSINESS_TRIP));

        ClientGroup group = new ClientGroup(
                HotelVisitPurpose.BUSINESS_TRIP,
                clients,
                LocalDateTime.now(),
                2000,
                RoomRank.THREE
        );

        Room room = new Room(RoomRank.THREE, 1);

        room.checkIn(group);

        assertTrue(room.getRoomStates().isOccupied());
    }

    @Test
    public void checkOutResidentsTest(){
        List<Client> clients = new ArrayList<>();
        clients.add(new Client(23, Sex.MALE, HotelVisitPurpose.BUSINESS_TRIP));

        ClientGroup group = new ClientGroup(
                HotelVisitPurpose.BUSINESS_TRIP,
                clients,
                LocalDateTime.now(),
                2000,
                RoomRank.THREE
        );

        Room room = new Room(RoomRank.THREE, 1);

        room.checkIn(group);
        room.checkOut();

        assertFalse(room.getRoomStates().isOccupied());
        assertTrue(room.getRoomStates().isDirty());
    }
}
