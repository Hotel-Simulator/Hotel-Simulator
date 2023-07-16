

package pl.agh.edu.generator.employee_generator;

import com.github.javafaker.Faker;
import pl.agh.edu.generator.client_generator.ProbabilityListGenerator;
import pl.agh.edu.json.data_loader.JSONEmployeeDataLoader;
import pl.agh.edu.model.employee.Employee;
import pl.agh.edu.model.employee.Shift;
import pl.agh.edu.model.employee.cleaner.Cleaner;
import pl.agh.edu.model.employee.repairman.Technician;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

public class EmployeeGenerator {
    private static final Faker faker = new Faker();
    private static final Random random = new Random();

    private static final List<Shift> shiftList = ProbabilityListGenerator.getProbabilityList(JSONEmployeeDataLoader.shiftProbabilities);
    private static final BigDecimal minWage = JSONEmployeeDataLoader.minWage;


    public static Employee generateCleaner(){
        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();
        int age = random.nextInt(18,60); // TODO: 24.06.2023   ustalic co robimy z emeryturami
        double skills = random.nextInt(100) / 100.;
        BigDecimal acceptableWage = generateAcceptableWage(skills);
        BigDecimal desiredWage = generateDesiredWage(skills);
        Shift desiredShift = generateDesiredShift();
        return new Cleaner(firstName,lastName,age,skills,desiredWage,acceptableWage,desiredShift);
    }

    public static Employee tmpGenerateRepairman(){
        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();
        int age = random.nextInt(18,60); // TODO: 24.06.2023   ustalic co robimy z emeryturami
        double skills = random.nextInt(100) / 100.;
        BigDecimal acceptableWage = generateAcceptableWage(skills);
        BigDecimal desiredWage = generateDesiredWage(skills);
        Shift desiredShift = generateDesiredShift();
        return new Technician(firstName,lastName,age,skills,desiredWage,acceptableWage,desiredShift);
    }

    private static BigDecimal generateDesiredWage(double skills) {
        return BigDecimal.valueOf((int)(minWage.doubleValue() * (1 + 0.5 * (skills + random.nextDouble(0.3,0.4)))) / 100 * 100) ;

    }

    private static BigDecimal generateAcceptableWage(double skills) {
        return BigDecimal.valueOf((int)(minWage.doubleValue() * (1 + 0.5 * (skills + random.nextDouble(0.2)))) / 100 * 100) ;
    }

    private static Shift generateDesiredShift(){
        return shiftList.get(random.nextInt(shiftList.size()));
    }


    public static void main(String[] args) {
        Stream.iterate(0, i -> i<10, i -> i+1).forEach(
                i -> {
                    System.out.println(generateCleaner());
                }

        );
    }


}