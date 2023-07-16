package pl.agh.edu.model.employee.cleaner;

import pl.agh.edu.json.data_loader.JSONEmployeeDataLoader;
import pl.agh.edu.model.employee.Employee;
import pl.agh.edu.model.employee.Shift;

import java.math.BigDecimal;
import java.time.Duration;

public class Cleaner extends Employee {
    private static final Duration baseCleaningTime = Duration.ofMinutes(JSONEmployeeDataLoader.maintenanceTimes.get("clean"));

    public Cleaner(String firstName, String lastName, int age, double skills, BigDecimal desiredWage, BigDecimal minimalAcceptedWage, Shift desiredShift) {
        super(firstName, lastName, age, skills, desiredWage, minimalAcceptedWage, desiredShift);
    }

    public Duration getCleaningTime(){
        return Duration.ofSeconds((long)(baseCleaningTime.getSeconds() * (1 - 0.5*( Math.min(skills, getSatisfaction())))));
    }

    @Override
    public String toString() {
        return "Cleaner{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", age=" + age +
                ", desiredWage=" + desiredWage +
                ", minimalAcceptedWage=" + minimalAcceptedWage +
                ", skills=" + skills +
                ", desiredShift=" + desiredShift +
                '}';
    }
}
