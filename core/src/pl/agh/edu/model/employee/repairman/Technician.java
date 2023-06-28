package pl.agh.edu.model.employee.repairman;

import org.json.simple.parser.ParseException;
import pl.agh.edu.generator.client_generator.JSONExtractor;
import pl.agh.edu.model.employee.Employee;
import pl.agh.edu.model.employee.Shift;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.Duration;

// TODO: zmiana Technician
public class Technician extends Employee {
    private static final Duration baseRepairingTime;

    static {
        try {
            baseRepairingTime = Duration.ofMinutes(JSONExtractor.getMaintenanceTimesFromJSON().get("fix"));
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
    }

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
