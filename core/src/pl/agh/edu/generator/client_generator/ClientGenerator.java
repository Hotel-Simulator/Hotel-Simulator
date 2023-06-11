package pl.agh.edu.generator.client_generator;

import org.json.simple.parser.ParseException;
import pl.agh.edu.enums.HotelVisitPurpose;
import pl.agh.edu.enums.RoomRank;
import pl.agh.edu.enums.Sex;
import pl.agh.edu.model.Client;
import pl.agh.edu.model.ClientGroup;
import pl.agh.edu.model.advertisement.AdvertisementHandler;
import pl.agh.edu.model.advertisement.SingleAdvertisementType;
import pl.agh.edu.model.advertisement.report.AdvertisementReportData;
import pl.agh.edu.model.advertisement.report.AdvertisementReportHandler;
import pl.agh.edu.model.event.temporary.ClientNumberModificationTemporaryEvent;
import pl.agh.edu.model.event.temporary.ClientNumberModificationTemporaryEventHandler;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class ClientGenerator {

    /*
    WIZJA
    mamy plik, który mowi jaki procent klientow jest danego typu np: turysci: 80%, biznesmeni 15%, kuracjusze 5%
    każdy rodzaj klienta ma preferowaną rangę, cenę, dlugosc pobytu i ilosc osob w pokoju np: biznesmen: ranga 4, ilosc osob: 1 cena x, turysta: ranga 2, ilosc osob 5: cena y
    preferowana cena,ranga,ilosc osob,ilosc nocy bedzie losowana z rozkladow normalnych ( zazwyczaj biznesmen bedzie chciał range 4, rzadzij 3, bardzo zadko 2

    pomysł: typ klienta bedzie też miał szanse zespawnowania w danym dniu na podstawie rozkładu normalnego, np: biznesmen zazwczycaj sie pojawi na pocz tygodnia, turysci w piątek, kuracjusze roznie

    w zaleznosci od popularnosci hotelu, clientGenerator bedzie w wypluwał wiecej lub mniej klientow
    clientGenerator bedzie wypluwał wszystkich mozliwych klintow a nie tylko tych co mozemy przyjąć: klient chce pokoj 5 osobowy a my mamy tylko 3 osobowe itd
    clientGenerator bedzie wypluwał o północy wszystkich klientow na dany dzien razem z godziną przybycia

    pomysł: klienci będą się ustawiać w wirtualnej kolejce i będa mieli atrybut mowiący jak długo mogą czekać (jak klienci beda wolno obslugiwani to trzeba zatrudnic dddatkową recepcjoniste, recepcjonistka moze miec atrybut jak  szybko obsluguje klintow)
    mozemy ustalic ze recepcja jest czynna od 8 do 24 wiec potrzeba zatrudnic przynajmniej na dwie zmiany!

    pomysł: na podstawie standardu miejsca (pokoj i recepcja) klient wystawia ilosc gwiazdek, ktora wpływa na popularność (razem z reklamą)
    pomysł: w wakacje i ferie bedzie bonus do turystow
     */

    private static ClientGenerator clientGeneratorInstance;

    private final Random random = new Random();
    private final HashMap<String, Long> attractivenessConstants;
    private EnumMap<HotelVisitPurpose,Double> hotelVisitPurposeProbability;
    private EnumMap<HotelVisitPurpose, List<Integer>> roomSizeProbabilityLists;
    private EnumMap<HotelVisitPurpose, List<RoomRank>> desiredRoomRankProbabilityLists;
    private EnumMap<HotelVisitPurpose, List<Integer>> numberOfNightsProbabilityLists;
    private EnumMap<RoomRank, Map<Integer, Integer>> averagePricesPerNight;
    private final AdvertisementHandler advertisementHandler;
    private final ClientNumberModificationTemporaryEventHandler clientNumberModificationTemporaryEventHandler;


    private ClientGenerator() throws IOException, ParseException {

        attractivenessConstants = JSONExtractor.getAttractivenessConstantsFromJSON();
        averagePricesPerNight = JSONExtractor.getAveragePricesPerNightFromJSON();
        hotelVisitPurposeProbability = JSONExtractor.getHotelVisitPurposeProbabilitiesFromJSON();
        roomSizeProbabilityLists = ProbabilityListGenerator.getMapOfProbabilityLists(JSONExtractor.getRoomSizeProbabilitiesFromJSON(),HotelVisitPurpose.class);
        desiredRoomRankProbabilityLists = ProbabilityListGenerator.getMapOfProbabilityLists(JSONExtractor.getDesiredRoomRankProbabilitiesFromJSON(),HotelVisitPurpose.class);
        numberOfNightsProbabilityLists = ProbabilityListGenerator.getMapOfProbabilityLists(JSONExtractor.getNumberOfNightsProbabilitiesFromJSON(),HotelVisitPurpose.class);
        advertisementHandler = AdvertisementHandler.getInstance();
        clientNumberModificationTemporaryEventHandler = ClientNumberModificationTemporaryEventHandler.getInstance();
    }


    public static ClientGenerator getInstance() throws IOException, ParseException {
        if(clientGeneratorInstance == null) clientGeneratorInstance = new ClientGenerator();
        return clientGeneratorInstance;
    }


    private <T> T getRandomValue(List<T> list){
        return list.get(random.nextInt(0,list.size()));
    }

    private LocalTime getRandomLocalTime(LocalTime min, LocalTime max){

        int randomTimeInMinutes =  random.nextInt(min.getHour()*60 + min.getMinute(),max.getHour()*60 + max.getMinute());
        return LocalTime.of(randomTimeInMinutes / 60,randomTimeInMinutes % 60);
    }


    private LocalDateTime getCheckOutTime(LocalDate date, int numberOfNight,LocalTime checkOutMaxTime) {

        return LocalDateTime.of(date.plusDays(numberOfNight),getRandomLocalTime(LocalTime.of(6,0),checkOutMaxTime));
    }


    private int getDesiredPricePerNight(RoomRank desiredRoomRank, int roomSize) {
        return averagePricesPerNight.get(desiredRoomRank).get(roomSize) + (int) Math.round(random.nextGaussian() * 0.2 * averagePricesPerNight.get(desiredRoomRank).get(roomSize));
    }

    private List<Client> getMembers(HotelVisitPurpose hotelVisitPurpose, int roomSize) {
        return IntStream.range(0,roomSize)
                .mapToObj(it->new Client(
                        random.nextInt(1,99),
                        Sex.values()[random.nextInt(0,3)],
                        hotelVisitPurpose))
                .collect(Collectors.toList());
    }

//    private ClientGroup generateClientGroup(LocalDate date, LocalTime checkoutMaxTime){
//
//        HotelVisitPurpose hotelVisitPurpose = getRandomValue(hotelVisitPurposeProbabilityList);
//        return generateClientGroupForGivenHotelVisitPurpose(date,checkoutMaxTime,hotelVisitPurpose);
//    }

    private ClientGroup generateClientGroupForGivenHotelVisitPurpose(LocalDate date, LocalTime checkoutMaxTime,HotelVisitPurpose hotelVisitPurpose){
        RoomRank desiredRoomRank  = getRandomValue(desiredRoomRankProbabilityLists.get(hotelVisitPurpose));
        int numberOfNight = getRandomValue(numberOfNightsProbabilityLists.get(hotelVisitPurpose));
        int roomSize = getRandomValue(roomSizeProbabilityLists.get(hotelVisitPurpose));
        List<Client> members = getMembers(hotelVisitPurpose, roomSize);
        int desiredPricePerNight = getDesiredPricePerNight(desiredRoomRank, roomSize);
        LocalDateTime checkOutTime = getCheckOutTime(date, numberOfNight,checkoutMaxTime);
        return new ClientGroup(hotelVisitPurpose,members,checkOutTime,desiredPricePerNight,desiredRoomRank);
    }


    private EnumMap<HotelVisitPurpose,Integer> getNumberOfClientGroupsFromAdvertisement(LocalDate date, EnumMap<HotelVisitPurpose,Integer> noClientsWithoutAdvertisements){
        return advertisementHandler.getCumulatedModifier(date).entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e ->  (int)Math.round(e.getValue() * noClientsWithoutAdvertisements.get(e.getKey())),
                        (a, b) ->  b,
                        () -> new EnumMap<>(HotelVisitPurpose.class)));
    }


    private EnumMap<HotelVisitPurpose,Integer> getNumberOfClientGroups(){
        double popularityModifier = 0.1;
        int basicNumberOfClients = (int)Math.round(((attractivenessConstants.get("local_market") + attractivenessConstants.get("local_attractions"))) * popularityModifier);
        return Stream.of(HotelVisitPurpose.values()).collect(Collectors.toMap(
                e -> e,
                e -> (int)Math.round(basicNumberOfClients * hotelVisitPurposeProbability.get(e) * Math.abs(1 + random.nextGaussian()/3) * (clientNumberModificationTemporaryEventHandler.getClientNumberModifier().get(e) + 1)),
                (a, b) ->b,
                () -> new EnumMap<>(HotelVisitPurpose.class)
        ));
    }

    public List<Arrival> generateArrivalsForDay(LocalDate date, LocalTime checkInMinTime, LocalTime checkOutMaxTime){
        EnumMap<HotelVisitPurpose,Integer> numberOfClientGroups = getNumberOfClientGroups();
        EnumMap<HotelVisitPurpose,Integer> numberOfClientGroupsFromAdvertisements = getNumberOfClientGroupsFromAdvertisement(date,numberOfClientGroups);
        AdvertisementReportHandler.collectData(
                new AdvertisementReportData(
                        date,
                        numberOfClientGroups,
                        numberOfClientGroupsFromAdvertisements
                )
        );
//        return Stream.concat(
//                IntStream
//                        .range(0,numberOfClientGroups)
//                        .mapToObj(it -> new Arrival(
//                                getRandomLocalTime(checkInMinTime,LocalTime.MAX),
//                                generateClientGroup(date,checkOutMaxTime))),
//                numberOfClientGroupsFromAdvertisements.entrySet().stream().flatMap(
//                        entry -> IntStream.range(0,entry.getValue()).mapToObj(it -> new Arrival(
//                                getRandomLocalTime(checkInMinTime,LocalTime.MAX),
//                                generateClientGroupForGivenHotelVisitPurpose(date,checkOutMaxTime,entry.getKey())))
//                )
//
//        )
        return Stream.of(HotelVisitPurpose.values())
                .flatMap(e -> IntStream.range(0,numberOfClientGroups.get(e) + numberOfClientGroupsFromAdvertisements.get(e))
                        .mapToObj(it -> new Arrival(
                                getRandomLocalTime(checkInMinTime,LocalTime.MAX),
                                generateClientGroupForGivenHotelVisitPurpose(date,checkOutMaxTime,e)))
                )
                .sorted(Arrival::compareTo)
                .collect(Collectors.toList());
    }





    public static void main(String[] args) throws IOException, ParseException {

       ClientGenerator generator = getInstance();
       AdvertisementHandler advertisementHandler = AdvertisementHandler.getInstance();
       advertisementHandler.create(SingleAdvertisementType.INTERNET_ADVERTISEMENT,List.of(LocalDate.now()));

        ClientNumberModificationTemporaryEventHandler clientNumberModificationTemporaryEventHandler = ClientNumberModificationTemporaryEventHandler.getInstance();
        var m = new EnumMap<HotelVisitPurpose,Double>(HotelVisitPurpose.class);
        m.put(HotelVisitPurpose.REHABILITATION,5.);
        m.put(HotelVisitPurpose.VACATION,0.);
        m.put(HotelVisitPurpose.BUSINESS_TRIP,0.);
        clientNumberModificationTemporaryEventHandler.add(new ClientNumberModificationTemporaryEvent(LocalDate.now(),LocalDate.now().plusDays(2),m));
        clientNumberModificationTemporaryEventHandler.update(LocalDate.now());

        System.out.println(advertisementHandler.getAdvertisements());
        System.out.println(generator.generateArrivalsForDay(LocalDate.now(),LocalTime.of(15,0),LocalTime.of(12,0)));
        System.out.println(AdvertisementReportHandler.getData());
    }

}

