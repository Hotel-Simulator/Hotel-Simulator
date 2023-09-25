package json;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import pl.agh.edu.enums.HotelVisitPurpose;
import pl.agh.edu.enums.RoomRank;
import pl.agh.edu.enums.RoomSize;
import pl.agh.edu.enums.TypeOfContract;
import pl.agh.edu.json.data.*;
import pl.agh.edu.json.data_extractor.JSONFilePath;
import pl.agh.edu.json.data_loader.*;
import pl.agh.edu.model.Room;
import pl.agh.edu.model.advertisement.ConstantAdvertisementType;
import pl.agh.edu.model.advertisement.SingleAdvertisementType;
import pl.agh.edu.model.employee.Profession;
import pl.agh.edu.model.employee.Shift;
import pl.agh.edu.utils.Pair;

public class JSONDataLoaderTest {

	static {
		try {
			changeJSONPath();
		} catch (ReflectiveOperationException e) {
			throw new RuntimeException(e);
		}
	}

	@Test
	@SuppressWarnings("unused")
	public void jSONRoomDataLoaderTest() {
		assertDoesNotThrow(() -> {
			Duration roomRankChangeDuration = JSONRoomDataLoader.roomRankChangeDuration;
			Duration roomBuildingDuration = JSONRoomDataLoader.roomBuildingDuration;
			Map<Pair<RoomRank, RoomSize>, BigDecimal> roomBuildingCosts = JSONRoomDataLoader.roomBuildingCosts;
			System.out.println(roomBuildingCosts);
		});
	}

	@Test
	@SuppressWarnings("unused")
	public void jSONHotelDataLoaderTest() {
		assertDoesNotThrow(() -> {
			Map<String, Integer> initialData = JSONHotelDataLoader.initialData;
			Map<String, Long> attractivenessConstants = JSONHotelDataLoader.attractivenessConstants;
			Map<String, LocalTime> checkInAndOutTime = JSONHotelDataLoader.checkInAndOutTime;
			List<Room> initialRooms = JSONHotelDataLoader.initialRooms;
		});
	}

	@Test
	@SuppressWarnings("unused")
	public void jSONGameDataLoaderTest() {
		assertDoesNotThrow(() -> {
			LocalDate startDate = JSONGameDataLoader.startDate;
			LocalDate endDate = JSONGameDataLoader.endDate;
			int employeesToHireListSize = JSONGameDataLoader.employeesToHireListSize;
			double possibleEmployeeRemovalProbability = JSONGameDataLoader.possibleEmployeeRemovalProbability;
			double roomFaultProbability = JSONGameDataLoader.roomFaultProbability;
		});
	}

	@Test
	@SuppressWarnings("unused")
	public void jSONEventDataLoaderTest() {
		assertDoesNotThrow(() -> {
			List<ClientNumberModificationCyclicTemporaryEventData> clientNumberModificationCyclicTemporaryEventData = JSONEventDataLoader.clientNumberModificationCyclicTemporaryEventData;
			List<ClientNumberModificationRandomTemporaryEventData> clientNumberModificationRandomTemporaryEventData = JSONEventDataLoader.clientNumberModificationRandomTemporaryEventData;
		});
	}

	@Test
	@SuppressWarnings("unused")
	public void jSONEmployeeDataLoaderTest() {
		assertDoesNotThrow(() -> {
			BigDecimal minWage = JSONEmployeeDataLoader.minWage;
			int noticePeriodInMonths = JSONEmployeeDataLoader.noticePeriodInMonths;
			EnumMap<Shift, Integer> shiftProbabilities = JSONEmployeeDataLoader.shiftProbabilities;
			EnumMap<Profession, Duration> basicServiceExecutionTimes = JSONEmployeeDataLoader.basicServiceExecutionTimes;
			EnumMap<Profession, Integer> professionProbabilities = JSONEmployeeDataLoader.professionProbabilities;
			EnumMap<TypeOfContract, Integer> typeOfContractProbabilities = JSONEmployeeDataLoader.typeOfContractProbabilities;
			int payDay = JSONEmployeeDataLoader.payDayOfMonth;
		});
	}

	@Test
	@SuppressWarnings("unused")
	public void jSONBankDataLoaderTest() {
		assertDoesNotThrow(() -> {
			List<BankData> scenarios = JSONBankDataLoader.scenarios;
			int chargeAccountFeeDayOfMonth = JSONBankDataLoader.chargeAccountFeeDayOfMonth;
			BigDecimal initialBalance = JSONBankDataLoader.initialBalance;
			BigDecimal basicCreditValue = JSONBankDataLoader.minCreditValue;
		});
	}

	@Test
	@SuppressWarnings("unused")
	public void jSONAdvertisementDataLoaderTest() {
		assertDoesNotThrow(() -> {
			double multiplier = JSONAdvertisementDataLoader.multiplier;
			EnumMap<SingleAdvertisementType, SingleAdvertisementData> singleAdvertisementData = JSONAdvertisementDataLoader.singleAdvertisementData;
			EnumMap<ConstantAdvertisementType, ConstantAdvertisementData> constantAdvertisementData = JSONAdvertisementDataLoader.constantAdvertisementData;
		});
	}

	@Test
	@SuppressWarnings("unused")
	public void jSONClientDataLoaderTest() {
		assertDoesNotThrow(() -> {
			EnumMap<HotelVisitPurpose, EnumMap<RoomRank, Integer>> desiredRankProbabilities = JSONClientDataLoader.desiredRankProbabilities;
			EnumMap<HotelVisitPurpose, Map<Integer, Integer>> numberOfNightsProbabilities = JSONClientDataLoader.numberOfNightsProbabilities;
			EnumMap<HotelVisitPurpose, Map<Integer, Integer>> clientGroupSizeProbabilities = JSONClientDataLoader.clientGroupSizeProbabilities;
			EnumMap<RoomRank, EnumMap<RoomSize, BigDecimal>> averagePricesPerNight = JSONClientDataLoader.averagePricesPerNight;
			Duration basicMaxWaitingTime = JSONClientDataLoader.basicMaxWaitingTime;
			int waitingTimeVariation = JSONClientDataLoader.waitingTimeVariation;
		});
	}

	private static void changeJSONPath()
			throws ReflectiveOperationException {

		Field field = JSONFilePath.class.getDeclaredField("PATH");
		field.setAccessible(true);
		field.set(null, "../assets/jsons/%s.json");
	}

}
