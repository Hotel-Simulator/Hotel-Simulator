package pl.agh.edu.model;

import pl.agh.edu.enums.RoomRank;

import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;

public class Hotel {
    private ArrayList<Integer> employees;
    private HashMap<RoomRank, Room> rooms;
    private Time checkInTime;
    private Time checkOutTime;
    private final Integer attractiveness;
    private Integer competitiveness;

    public Hotel(HashMap<RoomRank, Room> rooms, Time checkInTime, Time checkOutTime, Integer attractiveness) {
        this.rooms = rooms;
        this.checkInTime = checkInTime;
        this.checkOutTime = checkOutTime;
        this.attractiveness = attractiveness;
    }

    public ArrayList<Integer> getEmployees() {
        return employees;
    }

    public void setEmployees(ArrayList<Integer> employees) {
        this.employees = employees;
    }

    public HashMap<RoomRank, Room> getRooms() {
        return rooms;
    }

    public void setRooms(HashMap<RoomRank, Room> rooms) {
        this.rooms = rooms;
    }

    public Time getCheckInTime() {
        return checkInTime;
    }

    public void setCheckInTime(Time checkInTime) {
        this.checkInTime = checkInTime;
    }

    public Time getCheckOutTime() {
        return checkOutTime;
    }

    public void setCheckOutTime(Time checkOutTime) {
        this.checkOutTime = checkOutTime;
    }

    public Integer getAttractiveness() {
        return attractiveness;
    }

    public Integer getCompetitiveness() {
        return competitiveness;
    }

    public void setCompetitiveness(Integer competitiveness) {
        this.competitiveness = competitiveness;
    }
}
