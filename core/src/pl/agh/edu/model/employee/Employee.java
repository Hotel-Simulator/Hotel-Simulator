package pl.agh.edu.model.employee;

import pl.agh.edu.enums.TypeOfContract;
import pl.agh.edu.model.Time;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalTime;

public abstract class Employee {
    protected final String firstName;
    protected final String lastName;
    protected final int age;
    protected final BigDecimal desiredWage;
    protected final BigDecimal minimalAcceptedWage;
    protected BigDecimal wage;
    protected boolean isOccupied = false;
    protected TypeOfContract typeOfContract;
    protected final double skills;
    protected Shift shift;
    protected final Shift desiredShift;

    protected BigDecimal bonusForThisMonth;


    public Employee(String firstName, String lastName, int age, double skills, BigDecimal desiredWage, BigDecimal minimalAcceptedWage, Shift desiredShift) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.skills = skills;
        this.desiredWage = desiredWage;
        this.minimalAcceptedWage = minimalAcceptedWage;
        this.desiredShift = desiredShift;
        this.bonusForThisMonth = BigDecimal.ZERO;
    }

    public String getFirstName() {
        return firstName;
    }


    public String getLastName() {
        return lastName;
    }


    public boolean isOccupied() {
        return isOccupied;
    }

    public double getSatisfaction() {
        return Math.min(1.,wage.add(bonusForThisMonth).divide(desiredWage,2, RoundingMode.CEILING).doubleValue()) ;
    }

    public void setOccupied(boolean occupied) {
        isOccupied = occupied;
    }

    public int getAge() {
        return age;
    }


    public double getSkills() {
        return skills;
    }

    public JobOfferResponse offerJob(Shift shift,BigDecimal offeredWage,TypeOfContract typeOfContract){
        if(desiredShift == shift &&  offeredWage.doubleValue() >= minimalAcceptedWage.doubleValue() ||
        offeredWage.doubleValue() * 2 >= minimalAcceptedWage.doubleValue() + desiredWage.doubleValue()){
            this.shift = shift;
            this.wage = offeredWage;
            this.typeOfContract = typeOfContract;
            return JobOfferResponse.POSITIVE;
        }
        return JobOfferResponse.NEGATIVE;
    }


    public TypeOfContract getTypeOfContract() {
        return typeOfContract;
    }

    public void setTypeOfContract(TypeOfContract typeOfContract) {
        this.typeOfContract = typeOfContract;
    }

    public boolean isAtWork(LocalTime time){
        return time.isBefore(shift.getEndTime()) && ! time.isBefore(shift.getStartTime());
    }

    public void giveBonus(BigDecimal bonus){
         bonusForThisMonth = bonusForThisMonth.add(bonus);
    }

    public void update(){
        bonusForThisMonth = BigDecimal.ZERO;
    }
    public Shift getShift() {
        return shift;
    }
//    public void doRoomMaintenance(Room room) throws IOException, ParseException {
//        this.isOccupied = true;
//        this.maintainingRoom = room;
//        HashMap<String, Long> times = JSONExtractor.getMaintenanceTimesFromJSON();
//
//        if(role.equals(Role.cleaner) && room.getState() == RoomState.DIRTY){
//            room.setState(RoomState.MAINTENANCE);
//            this.endMaintenance = Time.getInstance().getTime().plusMinutes(times.get("clean"));
//        }
//        else if(role.equals(Role.technician) && room.getState() == RoomState.FAULT){
//            room.setState(RoomState.MAINTENANCE);
//            this.endMaintenance = Time.getInstance().getTime().plusMinutes(times.get("fix"));
//        }
//    }
//
//    public boolean finishMaintenance(){
//
//        if(endMaintenance.isAfter(Time.getInstance().getTime())){
//            if(role.equals(Role.cleaner)){
//                maintainingRoom.clean();
//            }
//            else if(role.equals(Role.technician)){
//                maintainingRoom.fix();
//            }
//
//            isOccupied = false;
//            return true;
//        }
//        return false;
//
//    }

    public static void main(String[] args) {
        System.out.println(Math.min(1.,BigDecimal.valueOf(2000).add(BigDecimal.valueOf(500)).divide(BigDecimal.valueOf(4200),3, RoundingMode.CEILING).doubleValue()));
    }
}
