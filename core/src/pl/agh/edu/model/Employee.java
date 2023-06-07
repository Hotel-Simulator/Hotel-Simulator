package pl.agh.edu.model;


import org.json.simple.parser.ParseException;
import pl.agh.edu.enums.Role;
import pl.agh.edu.enums.RoomState;
import pl.agh.edu.enums.TypeOfContract;
import pl.agh.edu.generator.client_generator.JSONExtractor;
import pl.agh.edu.time.Time;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Employee {

    static List<Employee> hiredEmployees = new ArrayList<>();
    private String firstName;
    private String lastName;
    private int age;
    private int expectedWage;
    private int wage;
    private int satisfaction;
    private boolean isOccupied = false;
    private Room maintainingRoom;
    private LocalDateTime endMaintenance;
    private TypeOfContract typeOfContract;
    private Role role;
    private double skills;
    private boolean hired;

    public Employee(String firstName, String lastName, int age,double skills,Role role, int expectedWage) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.skills = skills;
        this.role = role;
        this.hired = false;
        this.expectedWage = expectedWage;

    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public boolean isOccupied() {
        return isOccupied;
    }

    public int getSatisfaction() {
        return satisfaction;
    }

    public void setOccupied(boolean occupied) {
        isOccupied = occupied;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public double getSkills() {
        return skills;
    }

    public void setSkills(int skills) {
        this.skills = skills;
    }

    public boolean isHired() {
        return hired;
    }

    public void setHired(boolean hired) {
        this.hired = hired;
        if(hired){
            if(!Employee.hiredEmployees.contains(this))
                Employee.hiredEmployees.add(this);
        }
        else{
            if(Employee.hiredEmployees.contains(this))
                Employee.hiredEmployees.remove(this);
        }
    }


    public TypeOfContract getTypeOfContract() {
        return typeOfContract;
    }

    public void setTypeOfContract(TypeOfContract typeOfContract) {
        this.typeOfContract = typeOfContract;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void doRoomMaintenance(Room room) throws IOException, ParseException {
        this.isOccupied = true;
        this.maintainingRoom = room;
        HashMap<String, Long> times = JSONExtractor.getMaintenanceTimesFromJSON();

        if(role.equals(Role.cleaner) && room.getState() == RoomState.DIRTY){
            room.setState(RoomState.MAINTENANCE);
            this.endMaintenance = Time.getInstance().getTime().plusMinutes(times.get("clean"));
        }
        else if(role.equals(Role.technician) && room.getState() == RoomState.FAULT){
            room.setState(RoomState.MAINTENANCE);
            this.endMaintenance = Time.getInstance().getTime().plusMinutes(times.get("fix"));
        }
    }

    public void finishMaintenance(){

        if(endMaintenance.isAfter(Time.getInstance().getTime())){
            if(role.equals(Role.cleaner)){
                maintainingRoom.clean();
            }
            else if(role.equals(Role.technician)){
                maintainingRoom.fix();
            }

            isOccupied = false;
        }

    }
}
