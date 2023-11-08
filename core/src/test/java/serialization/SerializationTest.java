package serialization;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.agh.edu.data.loader.JSONAdvertisementDataLoader;
import pl.agh.edu.data.type.BankData;
import pl.agh.edu.engine.advertisement.AdvertisementCampaign;
import pl.agh.edu.engine.advertisement.AdvertisementType;
import pl.agh.edu.engine.attraction.Attraction;
import pl.agh.edu.engine.attraction.AttractionSize;
import pl.agh.edu.engine.attraction.AttractionState;
import pl.agh.edu.engine.attraction.AttractionType;
import pl.agh.edu.engine.bank.BankAccountDetails;
import pl.agh.edu.engine.bank.Credit;
import pl.agh.edu.engine.bank.Transaction;
import pl.agh.edu.engine.bank.TransactionType;
import pl.agh.edu.engine.calendar.CalendarEvent;
import pl.agh.edu.engine.client.ClientGroup;
import pl.agh.edu.engine.client.report.data.ClientGroupReportData;
import pl.agh.edu.engine.generator.ClientGenerator;
import pl.agh.edu.engine.hotel.HotelVisitPurpose;
import pl.agh.edu.engine.opinion.Opinion;
import pl.agh.edu.engine.opinion.OpinionBuilder;
import pl.agh.edu.serialization.KryoConfig;
import pl.agh.edu.utils.LanguageString;
import pl.agh.edu.utils.Pair;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static pl.agh.edu.engine.advertisement.AdvertisementType.FLYERS;
import static pl.agh.edu.engine.attraction.AttractionSize.MEDIUM;
import static pl.agh.edu.engine.attraction.AttractionType.RESTAURANT;
import static pl.agh.edu.engine.bank.TransactionType.EXPENSE;
import static pl.agh.edu.engine.hotel.HotelVisitPurpose.BUSINESS_TRIP;

public class SerializationTest {

    Kryo kryo = new Kryo();
    KryoConfig kryoConfig = new KryoConfig(kryo);


    Output output;
    Input input;



    public SerializationTest() {
    }

    @BeforeEach
    public void setUp(){
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
                LocalDate.now().plusDays(2)
        );

        // When
        kryo.writeObject(output,advertisementCampaign);

        initInput();
        AdvertisementCampaign advertisementCampaign2 = kryo.readObject(input,AdvertisementCampaign.class);

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
        Attraction attraction2 = kryo.readObject(input,Attraction.class);

        // Then
        assertEquals(attraction.type,attraction2.type);
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
                LocalDate.now()
        );

        // When
        kryo.writeObject(output, credit);

        initInput();
        Credit credit2 = kryo.readObject(input,Credit.class);

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
        Transaction transaction = new Transaction(EXPENSE,new BigDecimal(222), LocalDateTime.MIN);

        // When
        kryo.writeObject(output, transaction);

        initInput();
        Transaction transaction2 = kryo.readObject(input,Transaction.class);

        // Then
        assertEquals(transaction,transaction2);

    }

    @Test
    public void bankDataTest() {
        // Given
        BankData bankData = new BankData(
                "bank1",
                new BankAccountDetails(
                        new BigDecimal("0.5"),
                        new BigDecimal("20")
                )
        );

        // When
        kryo.writeObject(output, bankData);

        initInput();
        BankData bankData2 = kryo.readObject(input, BankData.class);

        // Then
        assertEquals(bankData, bankData2);
    }

    @Test
    public void languageStringTest(){
        // Given
        List<Pair<String, String>> list = List.of(Pair.of("a","b"), Pair.of("c","d"));
        LanguageString languageString = new LanguageString("some.description", list);

        // When
        kryo.writeObject(output, languageString);
        initInput();
        LanguageString languageString2 = kryo.readObject(input, LanguageString.class);

        // Then
        assertEquals(languageString.path,languageString2.path);
        assertEquals(languageString.replacementsList,languageString2.replacementsList);

    }

    @Test
    public void calendarEventTest() {
        // Given
        List<Pair<String, String>> list = List.of(Pair.of("a","b"), Pair.of("c","d"));
        CalendarEvent calendarEvent = new CalendarEvent(
                LocalDate.now(),
                new LanguageString("some.title"),
                new LanguageString("some.description", list)
        );

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
    public void clientGroupTest() {
        // Give
        ClientGroup clientGroup = ClientGenerator.getInstance().generateClientGroupForGivenHotelVisitPurpose(BUSINESS_TRIP);

        // When
        kryo.writeObject(output,clientGroup);

        initInput();
        ClientGroup clientGroup2 = kryo.readObject(input,ClientGroup.class);

        // Then
        assertEquals(clientGroup.getHotelVisitPurpose(),clientGroup2.getHotelVisitPurpose());
        assertEquals(clientGroup.getMembers(),clientGroup2.getMembers());
        assertEquals(clientGroup.getDesiredPricePerNight(),clientGroup2.getDesiredPricePerNight());
        assertEquals(clientGroup.getDesiredRoomRank(),clientGroup2.getDesiredRoomRank());
        assertEquals(clientGroup.getMaxWaitingTime(),clientGroup2.getMaxWaitingTime());
        assertEquals(clientGroup.getNumberOfNights(),clientGroup2.getNumberOfNights());
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

        kryo.writeObject(output,opinion);

        initInput();
        Opinion opinion2 = kryo.readObject(input, Opinion.class);

        // Then
        assertEquals(opinion.roomCleaning,opinion2.roomCleaning);
        assertEquals(opinion.roomBreaking,opinion2.roomBreaking);
        assertEquals(opinion.roomPrice,opinion2.roomPrice);
        assertEquals(opinion.queueWaiting,opinion2.queueWaiting);
        assertEquals(opinion.employeesSatisfaction,opinion2.employeesSatisfaction);
        assertEquals(opinion.getStars(),opinion2.getStars());
        assertEquals(opinion.getComment(),opinion2.getComment());
    }


}
