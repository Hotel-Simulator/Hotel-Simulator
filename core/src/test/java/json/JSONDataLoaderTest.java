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

import pl.agh.edu.data.loader.JSONAdvertisementDataLoader;
import pl.agh.edu.data.loader.JSONAttractionDataLoader;
import pl.agh.edu.data.loader.JSONBankDataLoader;
import pl.agh.edu.data.loader.JSONClientDataLoader;
import pl.agh.edu.data.loader.JSONEmployeeDataLoader;
import pl.agh.edu.data.loader.JSONEventDataLoader;
import pl.agh.edu.data.loader.JSONGameDataLoader;
import pl.agh.edu.data.loader.JSONHotelDataLoader;
import pl.agh.edu.data.loader.JSONHotelScenariosDataLoader;
import pl.agh.edu.data.loader.JSONOpinionDataLoader;
import pl.agh.edu.data.loader.JSONRoomDataLoader;
import pl.agh.edu.data.type.AdvertisementData;
import pl.agh.edu.data.type.AttractivenessConstantsData;
import pl.agh.edu.data.type.BankData;
import pl.agh.edu.data.type.ClientNumberModificationRandomEventData;
import pl.agh.edu.data.type.CyclicEventData;
import pl.agh.edu.data.type.RandomBuildingCostModificationPermanentEventData;
import pl.agh.edu.engine.advertisement.AdvertisementType;
import pl.agh.edu.engine.attraction.AttractionSize;
import pl.agh.edu.engine.attraction.AttractionType;
import pl.agh.edu.engine.employee.Profession;
import pl.agh.edu.engine.employee.Shift;
import pl.agh.edu.engine.hotel.HotelType;
import pl.agh.edu.engine.hotel.HotelVisitPurpose;
import pl.agh.edu.engine.room.Room;
import pl.agh.edu.engine.room.RoomRank;
import pl.agh.edu.engine.room.RoomSize;
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
			double possibleEmployeeRemovalProbability = JSONGameDataLoader.everyDayPossibleEmployeeRemovalProbability;
			double afterNegativeResponsePossibleEmployeeRemovalProbability = JSONGameDataLoader.afterNegativeResponsePossibleEmployeeRemovalProbability;
			double roomFaultProbability = JSONGameDataLoader.roomFaultProbability;
		});
	}

	@Test
	@SuppressWarnings("unused")
	public void jSONEventDataLoaderTest() {
		assertDoesNotThrow(() -> {
			List<CyclicEventData> cyclicEventData = JSONEventDataLoader.cyclicEventData;
			List<ClientNumberModificationRandomEventData> clientNumberModificationRandomEventData = JSONEventDataLoader.clientNumberModificationRandomEventData;
			RandomBuildingCostModificationPermanentEventData randomPermanentEventData = JSONEventDataLoader.randomPermanentEventData;
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
			EnumMap<HotelType, EnumMap<HotelVisitPurpose, BigDecimal>> hotelTypeVisitProbabilities = JSONHotelScenariosDataLoader.hotelTypeVisitProbabilities;
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
			Duration opinionHoldingDuration = JSONOpinionDataLoader.opinionHoldingDuration;
			BigDecimal opinionChangeMultiplier = JSONOpinionDataLoader.opinionChangeMultiplier;
			double desiredPriceModifier = JSONOpinionDataLoader.desiredPriceModifier;
			double maxWaitingTimeModifier = JSONOpinionDataLoader.maxWaitingTimeModifier;
		});
	}

	@Test
	@SuppressWarnings("unused")
	public void jSONAttractionDataLoaderTest() {
		assertDoesNotThrow(() -> {
			EnumMap<AttractionSize, Integer> dailyCapacity = JSONAttractionDataLoader.dailyCapacity;
			EnumMap<AttractionSize, BigDecimal> dailyExpenses = JSONAttractionDataLoader.dailyExpenses;
			BigDecimal incomePerClient = JSONAttractionDataLoader.incomePerClient;
			EnumMap<AttractionSize, BigDecimal> buildCost = JSONAttractionDataLoader.buildCost;
			EnumMap<AttractionSize, Duration> buildDuration = JSONAttractionDataLoader.buildDuration;
			Map<Pair<AttractionType, HotelVisitPurpose>, Double> chancesOfVisit = JSONAttractionDataLoader.chancesOfVisit;
			Map<Pair<AttractionType, AttractionSize>, EnumMap<HotelVisitPurpose, BigDecimal>> modifier = JSONAttractionDataLoader.modifier;
		});
	}
}
