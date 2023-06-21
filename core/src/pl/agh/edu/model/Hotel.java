package pl.agh.edu.model;

import org.json.simple.parser.ParseException;
import pl.agh.edu.enums.Role;
import pl.agh.edu.enums.RoomRank;
import pl.agh.edu.enums.RoomState;
import pl.agh.edu.generator.client_generator.JSONExtractor;
import pl.agh.edu.logo.RandomLogoCreator;
import pl.agh.edu.room_builder.Builder;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

// TODO: popularity by customers reviews
// TODO: checkout handler leave opinions
// TODO: observery zamiast wątków
public class Hotel {

    private String hotelName;
    private Long hotelId;
    private ArrayList<Opinion> opinions = new ArrayList<>();
    private ArrayList<Employee> employees;
    private HashMap<RoomRank, ArrayList<Room>> roomsByRank = new HashMap<>();
    private HashMap<Integer, ArrayList<Room>> roomsByCapacity = new HashMap<>();
    private ArrayList<Builder> builders = new ArrayList<>();
    private ArrayList<Room> rooms;
    private LocalTime checkInTime;
    private LocalTime checkOutTime;
    private Integer attractiveness = null;
    private Double competitiveness;
    private RandomLogoCreator logo;

    public Hotel(ArrayList<Room> rooms, LocalTime checkInTime, LocalTime checkOutTime) throws IOException, ParseException {
        this.rooms = rooms;
        this.checkInTime = checkInTime;
        this.checkOutTime = checkOutTime;


        HashMap<String, Long>  attractivenessConstants = JSONExtractor.getAttractivenessConstantsFromJSON();
        this.attractiveness = (int)(attractivenessConstants.get("local_market") + attractivenessConstants.get("local_attractions"));

        for(RoomRank rank: RoomRank.values()){
            roomsByRank.put(rank, new ArrayList<Room>());
        }

        // config do wielkości
        for(int i = 1; i < JSONExtractor.getMaxRoomSize(); i++){
            roomsByCapacity.put(i, new ArrayList<Room>());
        }

        for(Room room : rooms){
            roomsByRank.get(room.getRank()).add(room);
            roomsByCapacity.get(room.getCapacity()).add(room);
        }

        builders.add(new Builder());
    }

    public ArrayList<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(ArrayList<Employee> employees) {
        this.employees = employees;
    }

    public HashMap<RoomRank, ArrayList<Room>> getRoomsByRank() {
        return roomsByRank;
    }

    public void setRoomsByRank(HashMap<RoomRank, ArrayList<Room>> roomsByRank) {
        this.roomsByRank = roomsByRank;
    }

    public LocalTime getCheckInTime() {
        return checkInTime;
    }


    public void setCheckInTime(LocalTime checkInTime) {
        this.checkInTime = checkInTime;
    }

    public LocalTime getCheckOutTime() {
        return checkOutTime;
    }

    public void setCheckOutTime(LocalTime checkOutTime) {
        this.checkOutTime = checkOutTime;
    }

    public Integer getAttractiveness() {
        return attractiveness;
    }

    public Double getCompetitiveness() {
        return competitiveness;
    }

    public void setCompetitiveness(Double competitiveness) {
        this.competitiveness = competitiveness;
    }

    public HashMap<Integer, ArrayList<Room>> getRoomsByCapacity() {
        return roomsByCapacity;
    }

    public void setRoomsByCapacity(HashMap<Integer, ArrayList<Room>> roomsByCapacity) {
        this.roomsByCapacity = roomsByCapacity;
    }

    public void setPrices(ArrayList<Room> roomsToSet, BigDecimal newPrice){
        for(Room room : roomsToSet){
            room.setRentPrice(newPrice);
        }
    }

//    public void upgradeRooms(ArrayList<Room> roomsToUpgrade){
//        for(Room room : roomsToUpgrade){
//            RoomRank prev = room.getRank();
//            if(room.upgradeRank()){
//                roomsByRank.get(prev).remove(room);
//                roomsByRank.get(room.getRank()).add(room);
//            }
//        }
//    }

    public void addBuilder() throws IOException, ParseException {
        builders.add(new Builder());
    }

    public void updateCompetitveness(){
        BigDecimal avgRoomStandard = BigDecimal.valueOf(0);
        BigDecimal avgWorkerHappiness = BigDecimal.valueOf(0);
        Double avgOpinionValue = 0.;

        for(Room room: rooms){
            avgRoomStandard.add(room.getStandard());
        }

        for(Employee employee: employees){
            avgWorkerHappiness.add(BigDecimal.valueOf(employee.getSatisfaction()));
            // TODO: też na podstawie opinii kielntów + jaki zapas od wymagań klienta spełniamy plus eventy pokoje i w hotelu ogólnie - dynamiczny współczynnik u klienta
        }

        for(Opinion opinion: opinions){
            avgOpinionValue += opinion.getValue();
        }

        avgRoomStandard.divide(BigDecimal.valueOf(rooms.size()));
        avgWorkerHappiness.divide(BigDecimal.valueOf(employees.size()));
        avgOpinionValue /= opinions.size();

        competitiveness =  (avgOpinionValue + avgRoomStandard.doubleValue() + avgWorkerHappiness.doubleValue())/3;
    }

    public void checkForMaintenance() throws IOException, ParseException {
        for(Room room: rooms){
            if(room.getState().equals(RoomState.DIRTY)){
                maintainRoom(room, Role.cleaner);
            }
            else if (room.getState().equals(RoomState.FAULT)){
                maintainRoom(room, Role.technician);
            }
        }
    }


    public void maintainRoom(Room room, Role role) throws IOException, ParseException {
        for(Employee employee: employees){
            if(employee.getRole().equals(role) && !employee.isOccupied()){
                employee.doRoomMaintenance(room);

                int intervalMinutes = 5;

                Timer timer = new Timer();
                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        if(employee.finishMaintenance())
                            timer.cancel();
                    }
                };

                // Schedule the task to run repeatedly at the specified interval
                timer.schedule(task, 0, intervalMinutes * 60 * 1000);
                return;
            }
        }
    }

    public void upgradeRoom(Room room, int numUpgrades) throws IOException, ParseException {
        RoomRank prev = room.getRank();

        for(Builder builder : builders){
            if(!builder.isOccupied()){
                builder.upgradeRoom(room, numUpgrades);
                int intervalMinutes = 5;

                Timer timer = new Timer();
                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        if(builder.finishUpgrade())
                            timer.cancel();
                    }
                };

                // Schedule the task to run repeatedly at the specified interval
                timer.schedule(task, 0, intervalMinutes * 60 * 1000);
                break;
            }
        }

        roomsByRank.get(prev).remove(room);
        roomsByRank.get(room.getRank()).add(room);
    }

    public Room findRoomForClientGroup(ClientGroup group){
        for(Room room : roomsByRank.get(group.getDesiredRoomRank())){
            if(room.getState().equals(RoomState.EMPTY) && room.getRentPrice().compareTo(BigDecimal.valueOf(group.getBudgetPerNight())) < 1){
                return room;
            }
        }
        return null;
    }

    public int getEmployeesNumber(){
        return this.employees.size();
    }

    public void checkOutGuest(ClientGroup group){
        group.generateOpinion();
        Opinion opinion =  group.getOpinion();

        group.getRoom().checkOut();
        this.opinions.add(opinion);
    }
}
