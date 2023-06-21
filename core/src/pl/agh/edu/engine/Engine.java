package pl.agh.edu.engine;

import org.json.simple.parser.ParseException;
import pl.agh.edu.generator.client_generator.ClientGenerator;
import pl.agh.edu.model.Hotel;
import pl.agh.edu.model.Time;
import pl.agh.edu.model.advertisement.AdvertisementHandler;
import pl.agh.edu.time_command.ClientArrivalTimeCommand;
import pl.agh.edu.time_command.TimeCommandExecutor;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

public class Engine {
    private final Time time;
    private final ClientGenerator clientGenerator;
    private final Hotel hotel;
    private final TimeCommandExecutor timeCommandExecutor;
    private final AdvertisementHandler advertisementHandler;

    public Engine() throws IOException, ParseException {
        this.time = Time.getInstance();
        this.clientGenerator = ClientGenerator.getInstance();
        this.hotel = new Hotel(new ArrayList<>(), LocalTime.of(15,0),LocalTime.of(12,0));;
        this.timeCommandExecutor = TimeCommandExecutor.getInstance();
        this.advertisementHandler = AdvertisementHandler.getInstance();


        System.out.println(clientGenerator.generateArrivalsForDay(hotel.getCheckInTime(),hotel.getCheckOutTime()));
    }

    private void generateClientArrivals() throws IOException, ParseException{
        clientGenerator.generateArrivalsForDay(hotel.getCheckInTime(),hotel.getCheckOutTime())
                .forEach(arrival -> {
                    try {
                        timeCommandExecutor.addCommand(LocalDateTime.of(time.getTime().toLocalDate(),arrival.time()),new ClientArrivalTimeCommand(arrival.clientGroup()));
                    } catch (IOException | ParseException e) {
                        throw new RuntimeException(e);
                    }
                });

    }

    private void updateAdvertisements(){
        advertisementHandler.update();
    }

    public void everyDayUpdate() throws IOException, ParseException {
        generateClientArrivals();
        updateAdvertisements();
    }

    public void everyYearUpdate() {

    }


    public static void main(String[] args) throws IOException, ParseException {
        Engine engine = new Engine();
    }



}
