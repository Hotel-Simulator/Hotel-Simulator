package serialization;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static pl.agh.edu.engine.advertisement.AdvertisementType.FLYERS;
import static pl.agh.edu.engine.attraction.AttractionSize.MEDIUM;
import static pl.agh.edu.engine.attraction.AttractionType.RESTAURANT;
import static pl.agh.edu.engine.bank.TransactionType.EXPENSE;
import static pl.agh.edu.engine.employee.Profession.CLEANER;
import static pl.agh.edu.engine.employee.Shift.MORNING;
import static pl.agh.edu.engine.employee.contract.TypeOfContract.PERMANENT;
import static pl.agh.edu.engine.hotel.HotelVisitPurpose.BUSINESS_TRIP;
import static pl.agh.edu.engine.room.RoomRank.ECONOMIC;
import static pl.agh.edu.engine.room.RoomSize.DOUBLE;
import static pl.agh.edu.engine.time.Frequency.EVERY_DAY;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import pl.agh.edu.data.loader.JSONAdvertisementDataLoader;
import pl.agh.edu.data.loader.JSONEventDataLoader;
import pl.agh.edu.data.type.BankData;
import pl.agh.edu.engine.advertisement.AdvertisementCampaign;
import pl.agh.edu.engine.attraction.Attraction;
import pl.agh.edu.engine.attraction.AttractionState;
import pl.agh.edu.engine.bank.BankAccountDetails;
import pl.agh.edu.engine.bank.Credit;
import pl.agh.edu.engine.bank.Transaction;
import pl.agh.edu.engine.calendar.CalendarEvent;
import pl.agh.edu.engine.client.Arrival;
import pl.agh.edu.engine.client.ClientGroup;
import pl.agh.edu.engine.employee.Employee;
import pl.agh.edu.engine.employee.EmployeeStatus;
import pl.agh.edu.engine.employee.EmploymentPreferences;
import pl.agh.edu.engine.employee.PossibleEmployee;
import pl.agh.edu.engine.employee.contract.Offer;
import pl.agh.edu.engine.event.permanent.BuildingCostModificationPermanentEvent;
import pl.agh.edu.engine.event.temporary.ClientNumberModificationTemporaryEvent;
import pl.agh.edu.engine.event.temporary.TemporaryEvent;
import pl.agh.edu.engine.generator.ClientGenerator;
import pl.agh.edu.engine.opinion.Opinion;
import pl.agh.edu.engine.room.Room;
import pl.agh.edu.engine.room.RoomState;
import pl.agh.edu.engine.time.command.NRepeatingTimeCommand;
import pl.agh.edu.engine.time.command.RepeatingTimeCommand;
import pl.agh.edu.engine.time.command.SerializableRunnable;
import pl.agh.edu.engine.time.command.TimeCommand;
import pl.agh.edu.serialization.KryoConfig;
import pl.agh.edu.utils.LanguageString;
import pl.agh.edu.utils.Pair;

public class SerializationTest {

	private final Kryo kryo = KryoConfig.kryo;

	private Output output;
	private Input input;

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
		assertEquals(languageString.path, languageString2.path);
		assertEquals(languageString.replacementsList, languageString2.replacementsList);

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
		assertEquals(calendarEvent.title().path, calendarEvent.title().path);
		assertEquals(calendarEvent.title().replacementsList, calendarEvent.title().replacementsList);
		assertEquals(calendarEvent.description().path, calendarEvent.description().path);
		assertEquals(calendarEvent.description().replacementsList, calendarEvent.description().replacementsList);

	}

	@Test
	public void clientArrivalTest() {
		// Give
		ClientGroup clientGroup = ClientGenerator.getInstance().generateClientGroupForGivenHotelVisitPurpose(BUSINESS_TRIP);
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
		Opinion opinion = ClientGenerator.getInstance().generateClientGroupForGivenHotelVisitPurpose(BUSINESS_TRIP).opinion;

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
		PossibleEmployee possibleEmployee = new PossibleEmployee.Builder()
				.firstName("Jan")
				.lastName("Kowal")
				.age(18)
				.skills(new BigDecimal("0.45"))
				.preferences(new EmploymentPreferences.Builder()
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
		PossibleEmployee possibleEmployee = new PossibleEmployee.Builder()
				.firstName("")
				.lastName("")
				.age(18)
				.skills(new BigDecimal("0.45"))
				.preferences(new EmploymentPreferences.Builder()
						.desiredShift(MORNING)
						.acceptableWage(BigDecimal.valueOf(5000))
						.desiredWage(BigDecimal.valueOf(6000))
						.desiredTypeOfContract(PERMANENT)
						.build())
				.profession(CLEANER)
				.build();

		Offer contractOffer = new Offer(MORNING, BigDecimal.valueOf(5000), PERMANENT);
		Employee employee = new Employee(possibleEmployee, contractOffer);

		// When
		employee.setStatus(EmployeeStatus.HIRED_WORKING);
		employee.setOccupied(true);
		employee.addBonus(BigDecimal.valueOf(1000));

		kryo.writeObject(output, employee);

		initInput();
		Employee employee2 = kryo.readObject(input, Employee.class);

		assertEquals(employee.firstName, employee2.firstName);
		assertEquals(employee.lastName, employee2.lastName);
		assertEquals(employee.age, employee2.age);
		assertEquals(employee.skills, employee2.skills);
		assertEquals(employee.preferences.desiredShift, employee2.preferences.desiredShift);
		assertEquals(employee.preferences.acceptableWage, employee2.preferences.acceptableWage);
		assertEquals(employee.preferences.desiredWage, employee2.preferences.desiredWage);
		assertEquals(employee.preferences.desiredTypeOfContract, employee2.preferences.desiredTypeOfContract);
		assertEquals(employee.profession, employee2.profession);

		assertEquals(employee.shift, employee2.shift);
		assertEquals(employee.wage, employee2.wage);
		assertEquals(employee.typeOfContract, employee2.typeOfContract);

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
		assertEquals(event.title.path, event2.title.path);
		assertEquals(event.title.replacementsList, event2.title.replacementsList);
		assertEquals(event.eventAppearancePopupDescription.path, event2.eventAppearancePopupDescription.path);
		assertEquals(event.eventAppearancePopupDescription.replacementsList, event2.eventAppearancePopupDescription.replacementsList);
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
		assertEquals(event.title.path, event2.title.path);
		assertEquals(event.title.replacementsList, event2.title.replacementsList);
		assertEquals(event.eventAppearancePopupDescription.path, event2.eventAppearancePopupDescription.path);
		assertEquals(event.eventAppearancePopupDescription.replacementsList, event2.eventAppearancePopupDescription.replacementsList);
		assertEquals(event.eventStartPopupDescription.path, event2.eventStartPopupDescription.path);
		assertEquals(event.eventStartPopupDescription.replacementsList, event2.eventStartPopupDescription.replacementsList);
		assertEquals(event.calendarDescription.path, event2.calendarDescription.path);
		assertEquals(event.calendarDescription.replacementsList, event2.calendarDescription.replacementsList);
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
		assertEquals(event.title.path, event2.title.path);
		assertEquals(event.title.replacementsList, event2.title.replacementsList);
		assertEquals(event.eventAppearancePopupDescription.path, event2.eventAppearancePopupDescription.path);
		assertEquals(event.eventAppearancePopupDescription.replacementsList, event2.eventAppearancePopupDescription.replacementsList);
		assertEquals(event.eventStartPopupDescription.path, event2.eventStartPopupDescription.path);
		assertEquals(event.eventStartPopupDescription.replacementsList, event2.eventStartPopupDescription.replacementsList);
		assertEquals(event.calendarDescription.path, event2.calendarDescription.path);
		assertEquals(event.calendarDescription.replacementsList, event2.calendarDescription.replacementsList);
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
		ClientGroup clientGroup = ClientGenerator.getInstance().generateClientGroupForGivenHotelVisitPurpose(BUSINESS_TRIP);
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
		RepeatingTimeCommand repeatingTimeCommand = new RepeatingTimeCommand(EVERY_DAY, (SerializableRunnable) () -> System.out.println("hi"), LocalDateTime.now());

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
		NRepeatingTimeCommand nRepeatingTimeCommand = new NRepeatingTimeCommand(EVERY_DAY, (SerializableRunnable) () -> System.out.println("hi"), LocalDateTime.now(), 3);

		// When
		kryo.writeObject(output, nRepeatingTimeCommand);

		initInput();
		NRepeatingTimeCommand nRepeatingTimeCommand2 = kryo.readObject(input, NRepeatingTimeCommand.class);

		// Then

		assertEquals(0, nRepeatingTimeCommand.compareTo(nRepeatingTimeCommand2));
		assertEquals(nRepeatingTimeCommand.getDueDateTime(), nRepeatingTimeCommand.getDueDateTime());
		assertEquals(nRepeatingTimeCommand.getCounter(), nRepeatingTimeCommand.getCounter());

	}
}
