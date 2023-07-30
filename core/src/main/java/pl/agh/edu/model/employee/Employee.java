package pl.agh.edu.model.employee;

import pl.agh.edu.enums.TypeOfContract;
import pl.agh.edu.json.data_loader.JSONEmployeeDataLoader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalTime;

public class Employee {
    private final PossibleEmployee possibleEmployee;
    private BigDecimal wage;
    private TypeOfContract typeOfContract;
    private Shift shift;
    private boolean isOccupied;
    private BigDecimal bonusForThisMonth;

    private final Duration basicServiceExecutionTime;


    public Employee(PossibleEmployee possibleEmployee, JobOffer jobOffer) {
        this.possibleEmployee = possibleEmployee;
        this.wage = jobOffer.offeredWage();
        this.typeOfContract = jobOffer.typeOfContract();
        this.shift = jobOffer.shift();
        this.isOccupied = false;
        this.bonusForThisMonth = BigDecimal.ZERO;
        //todo czy da sie lepiej? w senise to moglby byc static
        this.basicServiceExecutionTime = JSONEmployeeDataLoader.basicServiceExecutionTimes.get(possibleEmployee.profession());
    }


    public double getSatisfaction() {
        return Math.min(1.,wage.add(bonusForThisMonth).divide(possibleEmployee.desiredWage(),2, RoundingMode.CEILING).doubleValue()) ;
    }

    public boolean isAtWork(LocalTime time){
        return time.isBefore(shift.getEndTime()) && ! time.isBefore(shift.getStartTime());
    }
    public Duration getServiceExecutionTime(){
        return Duration.ofSeconds(
                (long)(basicServiceExecutionTime.getSeconds() *
                        (1 - 0.5*Math.min(possibleEmployee.skills(), getSatisfaction())))
        );
    }

    public void giveBonus(BigDecimal bonus){
        bonusForThisMonth = bonusForThisMonth.add(bonus);
    }

    public void update(){
        bonusForThisMonth = BigDecimal.ZERO;
    }

    public boolean isOccupied() {
        return isOccupied;
    }

    public void setOccupied(boolean occupied) {
        isOccupied = occupied;
    }

    public TypeOfContract getTypeOfContract() {
        return typeOfContract;
    }

    public void setTypeOfContract(TypeOfContract typeOfContract) {
        this.typeOfContract = typeOfContract;
    }

    public Shift getShift() {
        return shift;
    }
    public String getFirstName() {
        return possibleEmployee.firstName();
    }
    public String getLastName() {
        return possibleEmployee.lastName();
    }
    public Profession getProfession(){return possibleEmployee.profession();}
    public int getAge() {
        return possibleEmployee.age();
    }
    public double getSkills() {
        return possibleEmployee.skills();
    }
}
