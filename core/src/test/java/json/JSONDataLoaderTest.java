package json;

import org.junit.jupiter.api.Test;
import pl.agh.edu.json.data_extractor.JSONFilePath;
import pl.agh.edu.json.data_loader.*;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class JSONDataLoaderTest {

    static {
        try {
            changeJSONPath();
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }


    @Test
    public void jSONRoomDataLoaderTest() {
        assertDoesNotThrow(() -> {
            System.out.printf("Room max size: %d%n", JSONRoomDataLoader.maxSize);
            System.out.printf("Room max size: %s%n",JSONRoomDataLoader.upgradeTimes);
        });
    }

    @Test
    public void jSONHotelDataLoaderTest() {
        assertDoesNotThrow(() -> {
            System.out.printf("Hotel initial data: %s%n", JSONHotelDataLoader.initialData);
            System.out.printf("Check in & check out time: %s%n", JSONHotelDataLoader.checkInAndOutTime);
            System.out.printf("AttractivenessConstants: %s%n", JSONHotelDataLoader.attractivenessConstants);
        });
    }

    @Test
    public void jSONGameDataLoaderTest() {
        assertDoesNotThrow(() -> {
            System.out.printf("Game start data: %s%n", JSONGameDataLoader.startDate);
            System.out.printf("Game end data: %s%n", JSONGameDataLoader.endDate);
            System.out.printf("EmployeesToHireList size: %s%n", JSONGameDataLoader.employeesToHireListSize);

        });
    }

    @Test
    public void jSONEventDataLoaderTest() {
        assertDoesNotThrow(() -> {
            System.out.printf("Cyclic event temporary data: %s%n", JSONEventDataLoader.clientNumberModificationCyclicTemporaryEventData);
            System.out.printf("Random event temporary data: %s%n", JSONEventDataLoader.clientNumberModificationRandomTemporaryEventData);
        });
    }

    @Test
    public void jSONEmployeeDataLoaderTest() {
        assertDoesNotThrow(() -> {
            System.out.printf("Min wage: %s%n", JSONEmployeeDataLoader.minWage);
            System.out.printf("Notice period in months: %s%n", JSONEmployeeDataLoader.noticePeriodInMonths);
            System.out.printf("Shift probabilities: %s%n", JSONEmployeeDataLoader.shiftProbabilities);
            System.out.printf("Maintenance: %s%n", JSONEmployeeDataLoader.maintenanceTimes);


        });
    }
    @Test
    public void jSONBankDataLoaderTest() {
        assertDoesNotThrow(() -> {
            System.out.printf("Bank scenarios: %s%n", JSONBankDataLoader.scenarios);
        });
    }

    @Test
    public void jSONAdvertisementDataLoaderTest() {
        assertDoesNotThrow(() -> {
            System.out.printf("Advertisement multiplier : %s%n", JSONAdvertisementDataLoader.multiplier);
            System.out.printf("SingleAdvertisement data : %s%n", JSONAdvertisementDataLoader.singleAdvertisementData);
            System.out.printf("ConstantAdvertisement data : %s%n", JSONAdvertisementDataLoader.constantAdvertisementData);
        });
    }

    @Test
    public void jSONClientDataLoaderTest() {
        assertDoesNotThrow(() -> {
            System.out.printf("HotelVisitPurpose probabilities : %s%n", JSONClientDataLoader.hotelVisitPurposeProbabilities);
            System.out.printf("Desired RoomRank probabilities : %s%n", JSONClientDataLoader.desiredRankProbabilities);
            System.out.printf("Number of nights probabilities : %s%n", JSONClientDataLoader.numberOfNightsProbabilities);
            System.out.printf("Room size probabilities : %s%n", JSONClientDataLoader.roomSizeProbabilities);
            System.out.printf("Average prices per night : %s%n", JSONClientDataLoader.averagePricesPerNight);
        });
    }


    private static void changeJSONPath()
            throws ReflectiveOperationException {

        Field field = JSONFilePath.class.getDeclaredField("PATH");
        field.setAccessible(true);
        field.set(null, "../assets/jsons/%s.json");
    }

}