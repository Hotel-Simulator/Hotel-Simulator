package json;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import pl.agh.edu.enums.HotelType;
import pl.agh.edu.enums.HotelVisitPurpose;
import pl.agh.edu.enums.RoomRank;
import pl.agh.edu.enums.RoomSize;
import pl.agh.edu.json.data.AdvertisementData;
import pl.agh.edu.json.data.AttractivenessConstantsData;
import pl.agh.edu.json.data.BankData;
import pl.agh.edu.json.data.ClientNumberModificationRandomEventData;
import pl.agh.edu.json.data.CyclicEventData;
import pl.agh.edu.json.data_loader.JSONAdvertisementDataLoader;
import pl.agh.edu.json.data_loader.JSONBankDataLoader;
import pl.agh.edu.json.data_loader.JSONClientDataLoader;
import pl.agh.edu.json.data_loader.JSONEmployeeDataLoader;
import pl.agh.edu.json.data_loader.JSONEventDataLoader;
import pl.agh.edu.json.data_loader.JSONGameDataLoader;
import pl.agh.edu.json.data_loader.JSONHotelDataLoader;
import pl.agh.edu.json.data_loader.JSONHotelScenariosDataLoader;
import pl.agh.edu.json.data_loader.JSONOpinionDataLoader;
import pl.agh.edu.json.data_loader.JSONRoomDataLoader;
import pl.agh.edu.model.Room;
import pl.agh.edu.model.advertisement.AdvertisementType;
import pl.agh.edu.model.employee.Profession;
import pl.agh.edu.model.employee.Shift;
import pl.agh.edu.utils.Pair;

public class JSONDataLoaderTest {
	@Test
	@SuppressWarnings("unused")
	public void jSONRoomDataLoaderTest() {
		assertDoesNotThrow(() -> {
			Duration roomRankChangeDuration = JSONRoomDataLoader.roomRankChangeDuration;
			Duration roomBuildingDuration = JSONRoomDataLoader.roomBuildingDuration;
			Map<Pair<RoomRank, RoomSize>, BigDecimal> roomBuildingCosts = JSONRoomDataLoader.roomBuildingCosts;
		});
	}

	@Test
	@SuppressWarnings("unused")
	public void jSONHotelDataLoaderTest() {
		assertDoesNotThrow(() -> {
			Map<String, Integer> initialData = JSONHotelDataLoader.initialData;
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
			List<CyclicEventData> cyclicEventData = JSONEventDataLoader.cyclicEventData;
			List<ClientNumberModificationRandomEventData> clientNumberModificationRandomEventData = JSONEventDataLoader.clientNumberModificationRandomEventData;
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
			BigDecimal multiplier = JSONAdvertisementDataLoader.multiplier;
			EnumMap<AdvertisementType, AdvertisementData> advertisementData = JSONAdvertisementDataLoader.advertisementData;
		});
	}

	@Test
	@SuppressWarnings("unused")
	public void jSONClientDataLoaderTest() {
		assertDoesNotThrow(() -> {
			EnumMap<HotelVisitPurpose, EnumMap<RoomRank, Integer>> desiredRankProbabilities = JSONClientDataLoader.desiredRankProbabilities;
			EnumMap<HotelVisitPurpose, Map<Integer, Integer>> numberOfNightsProbabilities = JSONClientDataLoader.numberOfNightsProbabilities;
			EnumMap<HotelVisitPurpose, Map<Integer, Integer>> clientGroupSizeProbabilities = JSONClientDataLoader.clientGroupSizeProbabilities;
			Map<Pair<RoomRank, RoomSize>, BigDecimal> averagePricesPerNight = JSONClientDataLoader.averagePricesPerNight;
			Duration basicMaxWaitingTime = JSONClientDataLoader.basicMaxWaitingTime;
			int waitingTimeVariation = JSONClientDataLoader.waitingTimeVariation;
		});
	}

	@Test
	@SuppressWarnings("unused")
	public void jSONHotelScenariosDataLoaderTest() {
		assertDoesNotThrow(() -> {
			EnumMap<HotelType, EnumMap<HotelVisitPurpose, Double>> hotelTypeVisitProbabilities = JSONHotelScenariosDataLoader.hotelTypeVisitProbabilities;
			Map<HotelType, Map<Integer, Double>> vacationPopularity = JSONHotelScenariosDataLoader.vacationPopularity;
			EnumMap<HotelType, AttractivenessConstantsData> attractivenessConstants = JSONHotelScenariosDataLoader.attractivenessConstants;
		});
	}

	@Test
	@SuppressWarnings("unused")
	public void jSONOpinionDataLoaderTest() {
		assertDoesNotThrow(() -> {
			double opinionProbabilityForClientWhoGotRoom = JSONOpinionDataLoader.opinionProbabilityForClientWhoGotRoom;
			double opinionProbabilityForClientWhoDidNotGetRoom = JSONOpinionDataLoader.opinionProbabilityForClientWhoDidNotGetRoom;
			double opinionProbabilityForClientWhoSteppedOutOfQueue = JSONOpinionDataLoader.opinionProbabilityForClientWhoSteppedOutOfQueue;
		});
	}
}
