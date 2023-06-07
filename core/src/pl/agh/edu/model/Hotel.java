package pl.agh.edu.model;

import org.json.simple.parser.ParseException;
import pl.agh.edu.enums.Role;
import pl.agh.edu.enums.RoomRank;
import pl.agh.edu.enums.RoomState;
import pl.agh.edu.generator.client_generator.JSONExtractor;
import pl.agh.edu.room_builder.Builder;

import javax.swing.plaf.RootPaneUI;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;


public class Hotel {
    private static Hotel instance;
    private static ArrayList<Employee> employees;
    private static HashMap<RoomRank, ArrayList<Room>> roomsByRank;
    private static HashMap<Integer, ArrayList<Room>> roomsByCapacity;
    private static ArrayList<Builder> builders = new ArrayList<>();
    private static ArrayList<Room> rooms;
    private static Time checkInTime;
    private static Time checkOutTime;
    private static Integer attractiveness = null;
    private static Integer competitiveness;

    public Hotel(ArrayList<Room> rooms, Time checkInTime, Time checkOutTime) throws IOException, ParseException {
        Hotel.rooms = rooms;
        Hotel.checkInTime = checkInTime;
        Hotel.checkOutTime = checkOutTime;

        HashMap<String, Long>  attractivenessConstants = JSONExtractor.getAttractivenessConstantsFromJSON();
        Hotel.attractiveness = (int)(attractivenessConstants.get("local_market") + attractivenessConstants.get("local_attractions"));

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

    public static Hotel getInstance() throws IOException, ParseException {
        if (instance == null){
            instance = new Hotel(new ArrayList<Room>(), new Time(15), new Time(12));
        }
        return instance;
    }

    public ArrayList<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(ArrayList<Employee> employees) {
        Hotel.employees = employees;
    }

    public HashMap<RoomRank, ArrayList<Room>> getRoomsByRank() {
        return roomsByRank;
    }

    public void setRoomsByRank(HashMap<RoomRank, ArrayList<Room>> roomsByRank) {
        Hotel.roomsByRank = roomsByRank;
    }

    public Time getCheckInTime() {
        return checkInTime;
    }

    public void setCheckInTime(Time checkInTime) {
        Hotel.checkInTime = checkInTime;
    }

    public Time getCheckOutTime() {
        return checkOutTime;
    }

    public void setCheckOutTime(Time checkOutTime) {
        Hotel.checkOutTime = checkOutTime;
    }

    public Integer getAttractiveness() {
        return attractiveness;
    }

    public Integer getCompetitiveness() {
        return competitiveness;
    }

    public void setCompetitiveness(Integer competitiveness) {
        Hotel.competitiveness = competitiveness;
    }

    public HashMap<Integer, ArrayList<Room>> getRoomsByCapacity() {
        return roomsByCapacity;
    }

    public void setRoomsByCapacity(HashMap<Integer, ArrayList<Room>> roomsByCapacity) {
        Hotel.roomsByCapacity = roomsByCapacity;
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

        for(Room room: rooms){
            avgRoomStandard.add(room.getStandard());
        }

        for(Employee employee: employees){
            avgWorkerHappiness.add(BigDecimal.valueOf(employee.getSatisfaction()));
        }

        avgRoomStandard.divide(BigDecimal.valueOf(rooms.size()));
        avgWorkerHappiness.divide(BigDecimal.valueOf(employees.size()));

        competitiveness = (Integer) avgRoomStandard.add(avgWorkerHappiness).divide(BigDecimal.valueOf(2)).intValue();
    }

    public void checkForMaintenance(){
        for(Room room: rooms){
            if(room.getState().equals(RoomState.DIRTY)){
                maintainRoom(room, Role.cleaner);
            }
            else if (room.getState().equals(RoomState.FAULT)){
                maintainRoom(room, Role.technician);
            }
        }
    }

    public void maintainRoom(Room room, Role role){
        for(Employee employee: employees){
            if(employee.getRole().equals(role) && !employee.isOccupied()){
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            employee.doRoomMaintenance(room);
                        } catch (InterruptedException | IOException | ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        }
    }

    public void upgradeRoom(Room room, int numUpgrades){
        RoomRank prev = room.getRank();

        for(Builder builder : builders){
            if(!builder.isOccupied()){
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            builder.upgradeRoom(room, numUpgrades);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        }

        roomsByRank.get(prev).remove(room);
        roomsByRank.get(room.getRank()).add(room);
    }
}
