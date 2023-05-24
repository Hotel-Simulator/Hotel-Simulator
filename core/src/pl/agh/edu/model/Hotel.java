package pl.agh.edu.model;

import pl.agh.edu.enums.RoomRank;

import javax.swing.plaf.RootPaneUI;
import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;


// TODO: 10.05.2023 attrcativeness comeptity 
public class Hotel {
    private static Hotel instance;
    private static ArrayList<Integer> employees;
    private static HashMap<RoomRank, ArrayList<Room>> roomsByRank;
    private static HashMap<Integer, ArrayList<Room>> roomsByCapacity;
    private static ArrayList<Room> rooms;
    private static Time checkInTime;
    private static Time checkOutTime;
    private static Integer attractiveness = null;
    private static Integer competitiveness;

    public Hotel(ArrayList<Room> rooms, Time checkInTime, Time checkOutTime, Integer attractiveness) {
        Hotel.rooms = rooms;
        Hotel.checkInTime = checkInTime;
        Hotel.checkOutTime = checkOutTime;
        Hotel.attractiveness = attractiveness;

        for(RoomRank rank: RoomRank.values()){
            roomsByRank.put(rank, new ArrayList<>());
        }

        for(int i = 1; i < 6; i++){
            roomsByCapacity.put(i, new ArrayList<>());
        }

        for(Room room : rooms){
            roomsByRank.get(room.getRank()).add(room);
            roomsByCapacity.get(room.getCapacity()).add(room);
        }
    }

    public static Hotel getInstance(){
        if (instance == null){
            instance = new Hotel(new ArrayList<>(), new Time(15), new Time(12), 87);
        }
        return instance;
    }

    public ArrayList<Integer> getEmployees() {
        return employees;
    }

    public void setEmployees(ArrayList<Integer> employees) {
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

    public void setPrices(ArrayList<Room> roomsToSet, int newPrice){
        for(Room room : roomsToSet){
            room.setRentPrice(newPrice);
        }
    }

    public void upgradeRooms(ArrayList<Room> roomsToUpgrade){
        for(Room room : roomsToUpgrade){
            RoomRank prev = room.getRank();
            if(room.upgradeRank()){
                roomsByRank.get(prev).remove(room);
                roomsByRank.get(room.getRank()).add(room);
            }
        }
    }
}
