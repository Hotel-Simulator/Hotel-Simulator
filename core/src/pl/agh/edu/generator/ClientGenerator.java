package pl.agh.edu.generator;

import org.json.simple.parser.ParseException;
import pl.agh.edu.enums.HotelVisitPurpose;
import pl.agh.edu.enums.RoomRank;
import pl.agh.edu.enums.Sex;
import pl.agh.edu.model.Client;
import pl.agh.edu.model.ClientGroup;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.IntStream;

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

    private final Random random;
    private HashMap<String, Long> attractivenessConstants;
    private List<HotelVisitPurpose> hotelVisitPurposeProbabilityList;
    private EnumMap<HotelVisitPurpose, List<Integer>> roomSizeProbabilityLists;
    private EnumMap<HotelVisitPurpose, List<RoomRank>> desiredRoomRankProbabilityLists;
    private EnumMap<HotelVisitPurpose, List<Integer>> numberOfNightsProbabilityLists;
    private EnumMap<RoomRank, Map<Integer, Integer>> averagePricesPerNight;


    private ClientGenerator() throws IOException, ParseException {
        random = new Random();

        attractivenessConstants = JSONExtractor.getAttractivenessConstantsFromJSON();
        averagePricesPerNight = JSONExtractor.getAveragePricesPerNightFromJSON();

        generateProbabilityLists();
    }

    private void generateProbabilityLists() throws IOException, ParseException {
        hotelVisitPurposeProbabilityList = getProbabilityList(JSONExtractor.getHotelVisitPurposeProbabilitiesFromJSON());

        roomSizeProbabilityLists = new EnumMap<>(HotelVisitPurpose.class);
        for(Map.Entry<HotelVisitPurpose, Map<Integer, Integer>> entry : JSONExtractor.getRoomSizeProbabilitiesFromJSON().entrySet()){
            roomSizeProbabilityLists.put(entry.getKey(),getProbabilityList(entry.getValue()));
        }
        desiredRoomRankProbabilityLists = new EnumMap<>(HotelVisitPurpose.class);
        for(Map.Entry<HotelVisitPurpose, EnumMap<RoomRank, Integer>> entry : JSONExtractor.getDesiredRoomRankProbabilitiesFromJSON().entrySet()){
            desiredRoomRankProbabilityLists.put(entry.getKey(),getProbabilityList(entry.getValue()));
        }
        numberOfNightsProbabilityLists = new EnumMap<>(HotelVisitPurpose.class);
        for(Map.Entry<HotelVisitPurpose, Map<Integer, Integer>> entry : JSONExtractor.getNumberOfNightsProbabilitiesFromJSON().entrySet()){
            numberOfNightsProbabilityLists.put(entry.getKey(),getProbabilityList(entry.getValue()));
        }
    }

    public static ClientGenerator getClientGeneratorInstance() throws IOException, ParseException {
        if(clientGeneratorInstance == null) clientGeneratorInstance = new ClientGenerator();
        return clientGeneratorInstance;
    }

    private <T> List<T>  getProbabilityList(Map<T,Integer> map){
        List<T> list = new ArrayList<>();
        for(Map.Entry<T,Integer> entry : map.entrySet()){
            for(int i=0;i<entry.getValue();i++) list.add(entry.getKey());
        }
        return list;
    }

    private <T> T getRandomValue(List<T> list){
        return list.get(random.nextInt(0,list.size()));
    }

    public ClientGroup generateClientGroup(LocalDateTime time){
        HotelVisitPurpose hotelVisitPurpose = getRandomValue(hotelVisitPurposeProbabilityList);

        int roomSize = getRandomValue(roomSizeProbabilityLists.get(hotelVisitPurpose));
        RoomRank desiredRoomRank  = getRandomValue(desiredRoomRankProbabilityLists.get(hotelVisitPurpose));
        int numberOfNight = getRandomValue(numberOfNightsProbabilityLists.get(hotelVisitPurpose));
        int desiredPricePerNight = averagePricesPerNight.get(desiredRoomRank).get(roomSize);
        List<Client> members = new ArrayList<>();
        for(int i=0;i<roomSize;i++){
            members.add(new Client(random.nextInt(1,99), Sex.values()[random.nextInt(0,3)], hotelVisitPurpose));
        }

        return new ClientGroup(hotelVisitPurpose,members,time.plusDays(numberOfNight),desiredPricePerNight,desiredRoomRank);
    }

    public static void main(String[] args) throws IOException, ParseException {
       ClientGenerator generator = getClientGeneratorInstance();
       for(int i=0;i<10;i++){
           System.out.println(generator.generateClientGroup(LocalDateTime.now()));
       }
    }


}
