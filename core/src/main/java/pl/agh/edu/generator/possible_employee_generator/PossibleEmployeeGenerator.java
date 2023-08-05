

package pl.agh.edu.generator.possible_employee_generator;

import com.github.javafaker.Faker;
import pl.agh.edu.enums.TypeOfContract;
import pl.agh.edu.generator.client_generator.ProbabilityListGenerator;
import pl.agh.edu.json.data_loader.JSONEmployeeDataLoader;
import pl.agh.edu.model.employee.EmploymentPreferences;
import pl.agh.edu.model.employee.PossibleEmployee;
import pl.agh.edu.model.employee.Profession;
import pl.agh.edu.model.employee.Shift;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

public class PossibleEmployeeGenerator {
    private static final Faker faker = new Faker();
    private static final Random random = new Random();

    private static final List<Shift> shiftList =
            ProbabilityListGenerator.getProbabilityList(JSONEmployeeDataLoader.shiftProbabilities);
    private static final BigDecimal minWage = JSONEmployeeDataLoader.minWage;
    private static final List<Profession> professionList =
            ProbabilityListGenerator.getProbabilityList(JSONEmployeeDataLoader.professionProbabilities);
    private static final List<TypeOfContract> typeOfContractList =
            ProbabilityListGenerator.getProbabilityList(JSONEmployeeDataLoader.typeOfContractProbabilities);

    public static PossibleEmployee generatePossibleEmployeeWithProfession(Profession profession){
        double skills = random.nextInt(100) / 100.;

        return new PossibleEmployee(
                faker.name().firstName(),
                faker.name().lastName(),
                random.nextInt(18,60),
                skills,
                new EmploymentPreferences(
                        generateDesiredShift(),
                        generateAcceptableWage(skills),
                        generateDesiredWage(skills),
                        generateDesiredTypeOfContract()),
                profession);

    }

    public static PossibleEmployee generatePossibleEmployee(){
        return generatePossibleEmployeeWithProfession(generateProfession());
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


    private static Profession generateProfession() {
        return professionList.get(random.nextInt(professionList.size()));
    }

    private static TypeOfContract generateDesiredTypeOfContract() {
        return typeOfContractList.get(random.nextInt(professionList.size()));
    }


    public static void main(String[] args) {
        Stream.iterate(0, i -> i<10, i -> i+1).forEach(
                i -> {
                    System.out.println(generatePossibleEmployee());
                }

        );
    }


}