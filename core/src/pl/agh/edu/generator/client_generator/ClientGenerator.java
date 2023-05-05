package pl.agh.edu.generator.client_generator;

import org.json.simple.parser.ParseException;
import pl.agh.edu.enums.HotelVisitPurpose;
import pl.agh.edu.enums.RoomRank;
import pl.agh.edu.enums.Sex;
import pl.agh.edu.model.Client;
import pl.agh.edu.model.ClientGroup;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

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
    private List<HotelVisitPurpose> hotelVisitPurposeProbabilityList;
    private EnumMap<HotelVisitPurpose, List<Integer>> roomSizeProbabilityLists;
    private EnumMap<HotelVisitPurpose, List<RoomRank>> desiredRoomRankProbabilityLists;
    private EnumMap<HotelVisitPurpose, List<Integer>> numberOfNightsProbabilityLists;
    private EnumMap<RoomRank, Map<Integer, Integer>> averagePricesPerNight;


    private ClientGenerator() throws IOException, ParseException {

        attractivenessConstants = JSONExtractor.getAttractivenessConstantsFromJSON();
        averagePricesPerNight = JSONExtractor.getAveragePricesPerNightFromJSON();
        hotelVisitPurposeProbabilityList = ProbabilityListGenerator.getProbabilityList(JSONExtractor.getHotelVisitPurposeProbabilitiesFromJSON());
        roomSizeProbabilityLists = ProbabilityListGenerator.getMapOfProbabilityLists(JSONExtractor.getRoomSizeProbabilitiesFromJSON(),HotelVisitPurpose.class);
        desiredRoomRankProbabilityLists = ProbabilityListGenerator.getMapOfProbabilityLists(JSONExtractor.getDesiredRoomRankProbabilitiesFromJSON(),HotelVisitPurpose.class);
        numberOfNightsProbabilityLists = ProbabilityListGenerator.getMapOfProbabilityLists(JSONExtractor.getNumberOfNightsProbabilitiesFromJSON(),HotelVisitPurpose.class);
    }


    public static ClientGenerator getClientGeneratorInstance() throws IOException, ParseException {
        if(clientGeneratorInstance == null) clientGeneratorInstance = new ClientGenerator();
        return clientGeneratorInstance;
    }


    private <T> T getRandomValue(List<T> list){
        return list.get(random.nextInt(0,list.size()));
    }

    private LocalTime getRandomLocalTime(){
        int randomTimeInMinutes = 840 + (int)Math.round(random.nextGaussian()*120);
        return LocalTime.of(randomTimeInMinutes / 60,randomTimeInMinutes % 60);
    }

    private LocalDateTime getCheckOutTime(LocalDate date, int numberOfNight) {

        return LocalDateTime.of(date.plusDays(numberOfNight),getRandomLocalTime());
    }


    private int getDesiredPricePerNight(RoomRank desiredRoomRank, int roomSize) {
        return averagePricesPerNight.get(desiredRoomRank).get(roomSize) + (int) Math.round(random.nextGaussian() * 0.2 * averagePricesPerNight.get(desiredRoomRank).get(roomSize));
    }

    private List<Client> getMembers(HotelVisitPurpose hotelVisitPurpose, int roomSize) {
        List<Client> members = new ArrayList<>();
        for(int i = 0; i< roomSize; i++){
            members.add(new Client(random.nextInt(1,99), Sex.values()[random.nextInt(0,3)], hotelVisitPurpose));
        }
        return members;
    }

    private ClientGroup generateClientGroup(LocalDate date){

        HotelVisitPurpose hotelVisitPurpose = getRandomValue(hotelVisitPurposeProbabilityList);
        RoomRank desiredRoomRank  = getRandomValue(desiredRoomRankProbabilityLists.get(hotelVisitPurpose));
        int numberOfNight = getRandomValue(numberOfNightsProbabilityLists.get(hotelVisitPurpose));
        int roomSize = getRandomValue(roomSizeProbabilityLists.get(hotelVisitPurpose));
        List<Client> members = getMembers(hotelVisitPurpose, roomSize);
        int desiredPricePerNight = getDesiredPricePerNight(desiredRoomRank, roomSize);
        LocalDateTime checkOutTime = getCheckOutTime(date, numberOfNight);
        return new ClientGroup(hotelVisitPurpose,members,checkOutTime,desiredPricePerNight,desiredRoomRank);
    }

    private int getNumberOfClientGroups(List<Double> clientGroupNumberModifiers){
        int numberOfClients =  (int)(attractivenessConstants.get("local_market") + attractivenessConstants.get("local_attractions"));
        for(Double modifier: clientGroupNumberModifiers) numberOfClients*=modifier;
        return numberOfClients;
    }

    public List<Arrival> generateArrivalsForDay(LocalDate date, List<Double> clientGroupNumberModifiers){
        List<Arrival> arrivals = new LinkedList<>();
        int numberOfClients = getNumberOfClientGroups(clientGroupNumberModifiers);
        for(int i = 0;i<numberOfClients;i++){
            arrivals.add(new Arrival(getRandomLocalTime(),generateClientGroup(date)));
        }
        Collections.sort(arrivals);
        return arrivals;
    }



    public static void main(String[] args) throws IOException, ParseException {
       ClientGenerator generator = getClientGeneratorInstance();
        System.out.println(generator.generateArrivalsForDay(LocalDate.now(),new ArrayList<>(Arrays.asList(0.1,1.1))));
    }


}

