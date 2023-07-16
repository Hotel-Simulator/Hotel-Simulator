package pl.agh.edu.model.employee.repairman;

import pl.agh.edu.json.data_loader.JSONEmployeeDataLoader;
import pl.agh.edu.model.employee.Employee;
import pl.agh.edu.model.employee.Shift;

import java.math.BigDecimal;
import java.time.Duration;

// TODO: zmiana Technician
public class Technician extends Employee {
    private static final Duration baseRepairingTime = Duration.ofMinutes(JSONEmployeeDataLoader.maintenanceTimes.get("fix"));

    public Technician(String firstName, String lastName, int age, double skills, BigDecimal desiredWage, BigDecimal minimalAcceptedWage, Shift desiredShift) {
        super(firstName, lastName, age, skills, desiredWage, minimalAcceptedWage, desiredShift);
    }

    public Duration getRepairingTime(){
        return Duration.ofSeconds((long)(baseRepairingTime.getSeconds() * (1 - 0.5*( Math.min(skills, getSatisfaction())))));
    }

    @Override
    public String toString() {
        return "Repairman{" +
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
