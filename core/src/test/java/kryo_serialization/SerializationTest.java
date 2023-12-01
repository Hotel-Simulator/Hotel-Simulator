package kryo_serialization;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static pl.agh.edu.engine.advertisement.AdvertisementType.FLYERS;
import static pl.agh.edu.engine.attraction.AttractionSize.MEDIUM;
import static pl.agh.edu.engine.attraction.AttractionType.RESTAURANT;
import static pl.agh.edu.engine.attraction.AttractionType.SPA;
import static pl.agh.edu.engine.attraction.AttractionType.SWIMMING_POOL;
import static pl.agh.edu.engine.bank.TransactionType.EXPENSE;
import static pl.agh.edu.engine.employee.Profession.CLEANER;
import static pl.agh.edu.engine.employee.Shift.MORNING;
import static pl.agh.edu.engine.employee.contract.TypeOfContract.PERMANENT;
import static pl.agh.edu.engine.hotel.HotelType.CITY;
import static pl.agh.edu.engine.hotel.HotelVisitPurpose.BUSINESS_TRIP;
import static pl.agh.edu.engine.hotel.dificulty.DifficultyLevel.EASY;
import static pl.agh.edu.engine.opinion.OpinionStars.FIVE;
import static pl.agh.edu.engine.room.RoomRank.ECONOMIC;
import static pl.agh.edu.engine.room.RoomSize.DOUBLE;
import static pl.agh.edu.engine.time.Frequency.EVERY_DAY;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.EnumMapSerializer;

import pl.agh.edu.data.loader.JSONAdvertisementDataLoader;
import pl.agh.edu.data.loader.JSONEventDataLoader;
import pl.agh.edu.data.type.BankData;
import pl.agh.edu.engine.Engine;
import pl.agh.edu.engine.advertisement.AdvertisementCampaign;
import pl.agh.edu.engine.attraction.Attraction;
import pl.agh.edu.engine.attraction.AttractionSize;
import pl.agh.edu.engine.attraction.AttractionState;
import pl.agh.edu.engine.attraction.AttractionType;
import pl.agh.edu.engine.bank.BankAccountDetails;
import pl.agh.edu.engine.bank.Credit;
import pl.agh.edu.engine.bank.Transaction;
import pl.agh.edu.engine.calendar.CalendarEvent;
import pl.agh.edu.engine.client.Arrival;
import pl.agh.edu.engine.client.ClientGroup;
import pl.agh.edu.engine.client.report.util.DateTrie;
import pl.agh.edu.engine.employee.EmployeeContractStatus;
import pl.agh.edu.engine.employee.EmployeePreferences;
import pl.agh.edu.engine.employee.contract.EmployeeOffer;
import pl.agh.edu.engine.employee.hired.HiredEmployee;
import pl.agh.edu.engine.employee.possible.PossibleEmployee;
import pl.agh.edu.engine.event.permanent.BuildingCostModificationPermanentEvent;
import pl.agh.edu.engine.event.temporary.ClientNumberModificationTemporaryEvent;
import pl.agh.edu.engine.event.temporary.TemporaryEvent;
import pl.agh.edu.engine.generator.ClientGenerator;
import pl.agh.edu.engine.hotel.dificulty.DifficultyLevel;
import pl.agh.edu.engine.hotel.dificulty.GameDifficultyManager;
import pl.agh.edu.engine.opinion.Opinion;
import pl.agh.edu.engine.opinion.OpinionData;
import pl.agh.edu.engine.opinion.OpinionHandler;
import pl.agh.edu.engine.room.Room;
import pl.agh.edu.engine.room.RoomState;
import pl.agh.edu.engine.time.command.NRepeatingTimeCommand;
import pl.agh.edu.engine.time.command.RepeatingTimeCommand;
import pl.agh.edu.engine.time.command.TimeCommand;
import pl.agh.edu.serialization.KryoConfig;
import pl.agh.edu.utils.LanguageString;
import pl.agh.edu.utils.Pair;

public class SerializationTest {

	private final Kryo kryo = KryoConfig.kryo;

	private Output output;
	private Input input;
	private final ClientGenerator clientGenerator = new ClientGenerator(new GameDifficultyManager(EASY), new OpinionHandler());

	public SerializationTest() {
		kryo.register(SerializationTest.class);
	}

	@BeforeEach
	public void setUp() {
		output = new Output(1024, -1);
	}

	private void initInput() {
		input = new Input(output.getBuffer(), 0, output.position());
	}

	@Test
	public void advertisementCampaignTest() {

		// Given
		AdvertisementCampaign advertisementCampaign = new AdvertisementCampaign(
				JSONAdvertisementDataLoader.advertisementData.get(FLYERS),
				LocalDate.now(),
				LocalDate.now().plusDays(2));

		// When
		kryo.writeObject(output, advertisementCampaign);

		initInput();
		AdvertisementCampaign advertisementCampaign2 = kryo.readObject(input, AdvertisementCampaign.class);

		// Then
		assertEquals(advertisementCampaign, advertisementCampaign2);
	}

	@Test
	public void attractionTest() {
		// Given
		Attraction attraction = new Attraction(RESTAURANT, MEDIUM);
		attraction.setState(AttractionState.OPENING);

		// When
		kryo.writeObject(output, attraction);

		initInput();
		Attraction attraction2 = kryo.readObject(input, Attraction.class);

		// Then
		assertEquals(attraction.type, attraction2.type);
		assertEquals(attraction.getSize(), attraction2.getSize());
		assertEquals(attraction.getState(), attraction2.getState());
	}

	@Test
	public void creditTest() {
		// Given
		Credit credit = new Credit(
				new BigDecimal("100"),
				12,
				new BigDecimal("0.5"),
				LocalDate.now());

		// When
		kryo.writeObject(output, credit);

		initInput();
		Credit credit2 = kryo.readObject(input, Credit.class);

		assertEquals(credit.value, credit2.value);
		assertEquals(credit.lengthInMonths, credit2.lengthInMonths);
		assertEquals(credit.interestRate, credit2.interestRate);
		assertEquals(credit.takeOutDate, credit2.takeOutDate);
		assertEquals(credit.monthlyPayment, credit2.monthlyPayment);
		assertEquals(credit.valueWithInterest, credit2.valueWithInterest);

	}

	@Test
	public void transactionTest() {
		// Given
		Transaction transaction = new Transaction(EXPENSE, new BigDecimal(222), LocalDateTime.MIN);

		// When
		kryo.writeObject(output, transaction);

		initInput();
		Transaction transaction2 = kryo.readObject(input, Transaction.class);

		// Then
		assertEquals(transaction, transaction2);

	}

	@Test
	public void bankDataTest() {
		// Given
		BankData bankData = new BankData(
				"bank1",
				new BankAccountDetails(
						new BigDecimal("0.5"),
						new BigDecimal("20")));

		// When
		kryo.writeObject(output, bankData);

		initInput();
		BankData bankData2 = kryo.readObject(input, BankData.class);

		// Then
		assertEquals(bankData, bankData2);
	}

	@Test
	public void languageStringTest() {
		// Given
		List<Pair<String, String>> list = List.of(Pair.of("a", "b"), Pair.of("c", "d"));
		LanguageString languageString = new LanguageString("some.description", list);

		// When
		kryo.writeObject(output, languageString);
		initInput();
		LanguageString languageString2 = kryo.readObject(input, LanguageString.class);

		// Then
		assertEquals(languageString, languageString2);

	}

	@Test
	public void calendarEventTest() {
		// Given
		List<Pair<String, String>> list = List.of(Pair.of("a", "b"), Pair.of("c", "d"));
		CalendarEvent calendarEvent = new CalendarEvent(
				LocalDate.now(),
				new LanguageString("some.title"),
				new LanguageString("some.description", list));

		// When
		kryo.writeObject(output, calendarEvent);

		initInput();
		CalendarEvent calendarEvent2 = kryo.readObject(input, CalendarEvent.class);

		// Then
		assertEquals(calendarEvent.date(), calendarEvent2.date());
		assertEquals(calendarEvent.title(), calendarEvent.title());
		assertEquals(calendarEvent.description(), calendarEvent.description());

	}

	@Test
	public void clientArrivalTest() {
		// Give
		ClientGroup clientGroup = clientGenerator.generateClientGroupForGivenHotelVisitPurpose(BUSINESS_TRIP);
		Arrival arrival = new Arrival(LocalTime.NOON, clientGroup);
		Opinion opinion = clientGroup.opinion;
		// When

		opinion.roomCleaning.setGotCleanRoom(true);
		opinion.roomCleaning.setRoomCleaned();
		opinion.roomBreaking.setGotBrokenRoom(true);
		opinion.roomBreaking.roomRepaired();
		opinion.roomPrice.setPrices(new BigDecimal("20"));
		opinion.queueWaiting.setStartDate(LocalDateTime.now());
		opinion.queueWaiting.setEndDate(LocalDateTime.now().plusMinutes(20));
		opinion.employeesSatisfaction.addSatisfaction(new BigDecimal("0.5"));
		opinion.employeesSatisfaction.addSatisfaction(new BigDecimal("0.8"));
		opinion.setClientGroupGotRoom();
		opinion.setClientSteppedOutOfQueue();
		kryo.writeObject(output, arrival);

		initInput();
		Arrival arrival2 = kryo.readObject(input, Arrival.class);
		ClientGroup clientGroup2 = arrival2.clientGroup();
		Opinion opinion2 = clientGroup2.opinion;

		// Then
		assertEquals(arrival.time(), arrival2.time());

		assertEquals(clientGroup.getHotelVisitPurpose(), clientGroup2.getHotelVisitPurpose());
		assertEquals(clientGroup.getMembers(), clientGroup2.getMembers());
		assertEquals(clientGroup.getDesiredPricePerNight(), clientGroup2.getDesiredPricePerNight());
		assertEquals(clientGroup.getDesiredRoomRank(), clientGroup2.getDesiredRoomRank());
		assertEquals(clientGroup.getMaxWaitingTime(), clientGroup2.getMaxWaitingTime());
		assertEquals(clientGroup.getNumberOfNights(), clientGroup2.getNumberOfNights());

		assertEquals(opinion.roomCleaning, opinion2.roomCleaning);
		assertEquals(opinion.roomBreaking, opinion2.roomBreaking);
		assertEquals(opinion.roomPrice, opinion2.roomPrice);
		assertEquals(opinion.queueWaiting, opinion2.queueWaiting);
		assertEquals(opinion.employeesSatisfaction, opinion2.employeesSatisfaction);
		assertEquals(opinion.getStars(), opinion2.getStars());
		assertEquals(opinion.getComment(), opinion2.getComment());
	}

	@Test
	public void opinionTest() {
		// Give
		Opinion opinion = clientGenerator.generateClientGroupForGivenHotelVisitPurpose(BUSINESS_TRIP).opinion;

		// When
		opinion.roomCleaning.setGotCleanRoom(true);
		opinion.roomCleaning.setRoomCleaned();
		opinion.roomBreaking.setGotBrokenRoom(true);
		opinion.roomBreaking.roomRepaired();
		opinion.roomPrice.setPrices(new BigDecimal("20"));
		opinion.queueWaiting.setStartDate(LocalDateTime.now());
		opinion.queueWaiting.setEndDate(LocalDateTime.now().plusMinutes(20));
		opinion.employeesSatisfaction.addSatisfaction(new BigDecimal("0.5"));
		opinion.employeesSatisfaction.addSatisfaction(new BigDecimal("0.8"));
		opinion.setClientGroupGotRoom();
		opinion.setClientSteppedOutOfQueue();

		kryo.writeObject(output, opinion);

		initInput();
		Opinion opinion2 = kryo.readObject(input, Opinion.class);

		// Then
		assertEquals(opinion.roomCleaning, opinion2.roomCleaning);
		assertEquals(opinion.roomBreaking, opinion2.roomBreaking);
		assertEquals(opinion.roomPrice, opinion2.roomPrice);
		assertEquals(opinion.queueWaiting, opinion2.queueWaiting);
		assertEquals(opinion.employeesSatisfaction, opinion2.employeesSatisfaction);
		assertEquals(opinion.getStars(), opinion2.getStars());
		assertEquals(opinion.getComment(), opinion2.getComment());
	}

	@Test
	public void possibleEmployeeTest() {
		// Given
		PossibleEmployee possibleEmployee = new PossibleEmployee.PossibleEmployeeBuilder()
				.firstName("Jan")
				.lastName("Kowal")
				.age(18)
				.skills(new BigDecimal("0.45"))
				.preferences(new EmployeePreferences.Builder()
						.desiredShift(MORNING)
						.acceptableWage(BigDecimal.valueOf(5000))
						.desiredWage(BigDecimal.valueOf(6000))
						.desiredTypeOfContract(PERMANENT)
						.build())
				.profession(CLEANER)
				.build();

		// When
		kryo.writeObject(output, possibleEmployee);

		initInput();
		PossibleEmployee possibleEmployee2 = kryo.readObject(input, PossibleEmployee.class);

		// Then

		assertEquals(possibleEmployee.firstName, possibleEmployee2.firstName);
		assertEquals(possibleEmployee.lastName, possibleEmployee2.lastName);
		assertEquals(possibleEmployee.age, possibleEmployee2.age);
		assertEquals(possibleEmployee.skills, possibleEmployee2.skills);
		assertEquals(possibleEmployee.preferences.desiredShift, possibleEmployee2.preferences.desiredShift);
		assertEquals(possibleEmployee.preferences.acceptableWage, possibleEmployee2.preferences.acceptableWage);
		assertEquals(possibleEmployee.preferences.desiredWage, possibleEmployee2.preferences.desiredWage);
		assertEquals(possibleEmployee.preferences.desiredTypeOfContract, possibleEmployee2.preferences.desiredTypeOfContract);
		assertEquals(possibleEmployee.profession, possibleEmployee2.profession);

	}

	@Test
	public void employeeTest() {
		// Given
		PossibleEmployee possibleEmployee = new PossibleEmployee.PossibleEmployeeBuilder()
				.firstName("")
				.lastName("")
				.age(18)
				.skills(new BigDecimal("0.45"))
				.preferences(new EmployeePreferences.Builder()
						.desiredShift(MORNING)
						.acceptableWage(BigDecimal.valueOf(5000))
						.desiredWage(BigDecimal.valueOf(6000))
						.desiredTypeOfContract(PERMANENT)
						.build())
				.profession(CLEANER)
				.build();

		EmployeeOffer contractOffer = new EmployeeOffer(MORNING, BigDecimal.valueOf(5000), PERMANENT);
		HiredEmployee employee = new HiredEmployee(possibleEmployee, contractOffer);

		// When
		employee.setStatus(EmployeeContractStatus.ACTIVE);
		employee.setOccupied(true);
		employee.addBonus(BigDecimal.valueOf(1000));

		kryo.writeObject(output, employee);

		initInput();
		HiredEmployee employee2 = kryo.readObject(input, HiredEmployee.class);

		assertEquals(employee.firstName, employee2.firstName);
		assertEquals(employee.lastName, employee2.lastName);
		assertEquals(employee.age, employee2.age);
		assertEquals(employee.skills, employee2.skills);
		assertEquals(employee.preferences.desiredShift, employee2.preferences.desiredShift);
		assertEquals(employee.preferences.acceptableWage, employee2.preferences.acceptableWage);
		assertEquals(employee.preferences.desiredWage, employee2.preferences.desiredWage);
		assertEquals(employee.preferences.desiredTypeOfContract, employee2.preferences.desiredTypeOfContract);
		assertEquals(employee.profession, employee2.profession);

		assertEquals(employee.getShift(), employee2.getShift());
		assertEquals(employee.getWage(), employee2.getWage());
		assertEquals(employee.getTypeOfContract(), employee2.getTypeOfContract());

		assertEquals(employee.isOccupied(), employee2.isOccupied());
		assertEquals(employee.getStatus(), employee2.getStatus());
		assertEquals(employee.getSatisfaction(), employee2.getSatisfaction());
		assertEquals(employee.getServiceExecutionTime(), employee2.getServiceExecutionTime());
	}

	@Test
	public void buildingCostModificationPermanentEventTest() {
		// Given
		BuildingCostModificationPermanentEvent event = new BuildingCostModificationPermanentEvent(
				new LanguageString("some.title"),
				new LanguageString("some.description"),
				LocalDate.now(),
				10,
				"imagePath");

		// When
		kryo.writeObject(output, event);

		initInput();
		BuildingCostModificationPermanentEvent event2 = kryo.readObject(input, BuildingCostModificationPermanentEvent.class);

		// Then
		assertEquals(event.title, event2.title);
		assertEquals(event.eventAppearancePopupDescription, event2.eventAppearancePopupDescription);
		assertEquals(event.appearanceDate, event2.appearanceDate);
		assertEquals(event.modifierValueInPercent, event2.modifierValueInPercent);
		assertEquals(event.imagePath, event2.imagePath);
	}

	@Test
	public void temporaryEventTest() {
		// Given
		TemporaryEvent event = new TemporaryEvent(
				new LanguageString("some.title"),
				new LanguageString("some.eventAppearancePopupDescription"),
				new LanguageString("some.eventStartPopupDescription"),
				new LanguageString("some.calendarDescription"),
				"imagePath",
				LocalDate.now(),
				LocalDate.now().plusMonths(1));

		// When
		kryo.writeObject(output, event);

		initInput();
		TemporaryEvent event2 = kryo.readObject(input, TemporaryEvent.class);

		// Then
		assertEquals(event.title, event2.title);
		assertEquals(event.eventAppearancePopupDescription, event2.eventAppearancePopupDescription);
		assertEquals(event.eventStartPopupDescription, event2.eventStartPopupDescription);
		assertEquals(event.calendarDescription, event2.calendarDescription);
		assertEquals(event.imagePath, event2.imagePath);
		assertEquals(event.appearanceDate, event2.appearanceDate);
		assertEquals(event.startDate, event2.startDate);

	}

	@Test
	public void clientNumberModificationTemporaryEventTest() {
		// Given

		ClientNumberModificationTemporaryEvent event = new ClientNumberModificationTemporaryEvent(
				new LanguageString("some.title"),
				new LanguageString("some.eventAppearancePopupDescription"),
				new LanguageString("some.eventStartPopupDescription"),
				new LanguageString("some.calendarDescription"),
				"imagePath",
				LocalDate.now(),
				LocalDate.now().plusMonths(1),
				12,
				JSONEventDataLoader.clientNumberModificationRandomEventData.get(0).modifier()

		);

		// When
		kryo.writeObject(output, event);

		initInput();
		ClientNumberModificationTemporaryEvent event2 = kryo.readObject(input, ClientNumberModificationTemporaryEvent.class);

		// Then
		assertEquals(event.title, event2.title);
		assertEquals(event.eventAppearancePopupDescription, event2.eventAppearancePopupDescription);
		assertEquals(event.eventStartPopupDescription, event2.eventStartPopupDescription);
		assertEquals(event.calendarDescription, event2.calendarDescription);
		assertEquals(event.imagePath, event2.imagePath);
		assertEquals(event.appearanceDate, event2.appearanceDate);
		assertEquals(event.startDate, event2.startDate);
		assertEquals(event.durationInDays, event2.durationInDays);
		assertEquals(event.modifier.getValue(), event2.modifier.getValue());
	}

	@Test
	public void roomStateTest() {
		// Given
		RoomState roomState = new RoomState();

		// When
		roomState.setOccupied(true);
		roomState.setFaulty(true);
		roomState.setBeingBuild(true);

		kryo.writeObject(output, roomState);

		initInput();
		RoomState roomState2 = kryo.readObject(input, RoomState.class);

		// Then
		assertEquals(roomState.isOccupied(), roomState2.isOccupied());
		assertEquals(roomState.isDirty(), roomState2.isDirty());
		assertEquals(roomState.isFaulty(), roomState2.isFaulty());
		assertEquals(roomState.isUnderRankChange(), roomState2.isUnderRankChange());
		assertEquals(roomState.isBeingBuild(), roomState2.isBeingBuild());

	}

	@Test
	public void roomWithoutResidentsTest() {
		// Given
		Room room = new Room(ECONOMIC, DOUBLE);

		// When
		room.roomState.setOccupied(true);
		room.roomState.setFaulty(true);
		room.roomState.setBeingBuild(true);

		kryo.writeObject(output, room);

		initInput();
		Room room2 = kryo.readObject(input, Room.class);

		// Then
		assertEquals(room.getRank(), room2.getRank());
		assertEquals(room.size, room2.size);
		assertEquals(room.roomState.isOccupied(), room2.roomState.isOccupied());
		assertEquals(room.roomState.isDirty(), room2.roomState.isDirty());
		assertEquals(room.roomState.isFaulty(), room2.roomState.isFaulty());
		assertEquals(room.roomState.isUnderRankChange(), room2.roomState.isUnderRankChange());
		assertEquals(room.roomState.isBeingBuild(), room2.roomState.isBeingBuild());
		assertEquals(room.getResidents(), room2.getResidents());
	}

	@Test
	public void roomWithResidentsTest() {
		// Given
		Room room = new Room(ECONOMIC, DOUBLE);
		ClientGroup clientGroup = clientGenerator.generateClientGroupForGivenHotelVisitPurpose(BUSINESS_TRIP);
		room.checkIn(clientGroup);
		// When
		room.roomState.setOccupied(true);
		room.roomState.setFaulty(true);
		room.roomState.setBeingBuild(true);

		kryo.writeObject(output, room);

		initInput();
		Room room2 = kryo.readObject(input, Room.class);

		// Then
		assertEquals(room.getRank(), room2.getRank());
		assertEquals(room.size, room2.size);
		assertEquals(room.roomState.isOccupied(), room2.roomState.isOccupied());
		assertEquals(room.roomState.isDirty(), room2.roomState.isDirty());
		assertEquals(room.roomState.isFaulty(), room2.roomState.isFaulty());
		assertEquals(room.roomState.isUnderRankChange(), room2.roomState.isUnderRankChange());
		assertEquals(room.roomState.isBeingBuild(), room2.roomState.isBeingBuild());

		assertEquals(clientGroup.getHotelVisitPurpose(), room2.getResidents().getHotelVisitPurpose());
		assertEquals(clientGroup.getMembers(), room2.getResidents().getMembers());
		assertEquals(clientGroup.getDesiredPricePerNight(), room2.getResidents().getDesiredPricePerNight());
		assertEquals(clientGroup.getDesiredRoomRank(), room2.getResidents().getDesiredRoomRank());
		assertEquals(clientGroup.getMaxWaitingTime(), room2.getResidents().getMaxWaitingTime());
		assertEquals(clientGroup.getNumberOfNights(), room2.getResidents().getNumberOfNights());
	}

	@Test
	public void timeCommandTest() {
		// Given
		TimeCommand timeCommand = new TimeCommand(() -> System.out.println("hi"), LocalDateTime.now());

		// When
		kryo.writeObject(output, timeCommand);

		initInput();
		TimeCommand timeCommand2 = kryo.readObject(input, TimeCommand.class);

		// Then

		assertEquals(0, timeCommand.compareTo(timeCommand2));
		assertEquals(timeCommand.getDueDateTime(), timeCommand2.getDueDateTime());
	}

	@Test
	public void repeatingTimeCommandTest() {
		// Given
		RepeatingTimeCommand repeatingTimeCommand = new RepeatingTimeCommand(EVERY_DAY, () -> System.out.println("hi"), LocalDateTime.now());

		// When
		kryo.writeObject(output, repeatingTimeCommand);

		initInput();
		RepeatingTimeCommand repeatingTimeCommand2 = kryo.readObject(input, RepeatingTimeCommand.class);

		// Then

		assertEquals(0, repeatingTimeCommand.compareTo(repeatingTimeCommand2));
		assertEquals(repeatingTimeCommand.getDueDateTime(), repeatingTimeCommand2.getDueDateTime());
	}

	@Test
	public void nRepeatingTimeCommandTest() {
		// Given
		NRepeatingTimeCommand nRepeatingTimeCommand = new NRepeatingTimeCommand(EVERY_DAY, () -> System.out.println("hi"), LocalDateTime.now(), 3L);

		// When
		kryo.writeObject(output, nRepeatingTimeCommand);

		initInput();
		NRepeatingTimeCommand nRepeatingTimeCommand2 = kryo.readObject(input, NRepeatingTimeCommand.class);

		// Then

		assertEquals(0, nRepeatingTimeCommand.compareTo(nRepeatingTimeCommand2));
		assertEquals(nRepeatingTimeCommand.getDueDateTime(), nRepeatingTimeCommand.getDueDateTime());
		assertEquals(nRepeatingTimeCommand.getCounter(), nRepeatingTimeCommand.getCounter());

	}

	@Test
	public void mapTest() {
		// Given
		Map<AttractionType, Pair<AttractionSize, LocalDateTime>> map = new TreeMap<>();
		map.put(RESTAURANT, Pair.of(MEDIUM, LocalDateTime.now()));
		map.put(SPA, Pair.of(MEDIUM, LocalDateTime.now()));
		map.put(SWIMMING_POOL, null);

		// When
		kryo.writeObject(output, map, KryoConfig.mapSerializer(AttractionType.class, Pair.class));

		initInput();
		Map<AttractionType, Pair<AttractionSize, LocalDateTime>> map2 = kryo.readObject(input, Map.class, KryoConfig.mapSerializer(AttractionType.class, Pair.class));

		// Then
		assertEquals(map, map2);
	}

	@Test
	public void enumMapTest() {
		// Given
		EnumMap<AttractionType, Pair<AttractionSize, LocalDateTime>> map = new EnumMap<>(AttractionType.class);
		map.put(RESTAURANT, Pair.of(MEDIUM, LocalDateTime.now()));
		map.put(SPA, Pair.of(MEDIUM, LocalDateTime.now()));
		map.put(SWIMMING_POOL, null);

		// When
		kryo.writeObject(output, map, new EnumMapSerializer(AttractionType.class));

		initInput();
		EnumMap<AttractionType, Pair<AttractionSize, LocalDateTime>> map2 = kryo.readObject(input, EnumMap.class, new EnumMapSerializer(AttractionType.class));

		// Then
		assertEquals(map, map2);
	}

	@Test
	public void dateTrieTest() {
		// Given
		DateTrie dateTrie = new DateTrie();

		// When
		dateTrie.insert(LocalDate.of(2020, 1, 1), 10);
		dateTrie.insert(LocalDate.of(2020, 1, 2), 20);
		dateTrie.insert(LocalDate.of(2020, 1, 3), 30);

		dateTrie.insert(LocalDate.of(2020, 2, 1), 40);
		dateTrie.insert(LocalDate.of(2020, 2, 2), 50);

		dateTrie.insert(LocalDate.of(2021, 1, 1), 60);

		kryo.writeObject(output, dateTrie);

		initInput();
		DateTrie dateTrie2 = kryo.readObject(input, DateTrie.class);

		// Then
		assertEquals(dateTrie.getDailyData(), dateTrie2.getDailyData());
		assertEquals(dateTrie.getMonthlyData(), dateTrie2.getMonthlyData());
		assertEquals(dateTrie.getYearlyData(), dateTrie2.getYearlyData());
	}

	@Test
	public void opinionDataTest() {
		// Given
		OpinionData opinionData = new OpinionData(
				"John",
				LocalDate.of(2020, 11, 1),
				FIVE,
				Set.of(
						new LanguageString("some.path", List.of(Pair.of("a", "b"), Pair.of("c", "d"))),
						new LanguageString("some.other.path")));

		// When
		kryo.writeObject(output, opinionData);

		initInput();
		OpinionData opinionData2 = kryo.readObject(input, OpinionData.class);

		// Then
		assertEquals(opinionData.guest(), opinionData2.guest());
		assertEquals(opinionData.date(), opinionData2.date());
		assertEquals(opinionData.stars(), opinionData2.stars());
		assertEquals(opinionData.comments(), opinionData2.comments());

	}

	@Test
	public void engineTest() {
		// Given
		Engine engine = new Engine(CITY, DifficultyLevel.MEDIUM);

		// When
		kryo.writeObject(output, engine);

		initInput();
		kryo.readObject(input, Engine.class);
	}

}
