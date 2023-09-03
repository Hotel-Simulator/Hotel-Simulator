
package pl.agh.edu.generator.possible_employee_generator;

import java.math.BigDecimal;

import com.github.javafaker.Faker;

import pl.agh.edu.json.data_loader.JSONEmployeeDataLoader;
import pl.agh.edu.model.employee.EmploymentPreferences;
import pl.agh.edu.model.employee.PossibleEmployee;
import pl.agh.edu.model.employee.Profession;
import pl.agh.edu.utils.RandomUtils;

public class PossibleEmployeeGenerator {
	private static final Faker faker = new Faker();

	private PossibleEmployeeGenerator() {}

	public static PossibleEmployee generatePossibleEmployeeWithProfession(Profession profession) {
		double skills = RandomUtils.randomInt(100) / 100.;

		return new PossibleEmployee(
				faker.name().firstName(),
				faker.name().lastName(),
				RandomUtils.randomInt(18, 60),
				skills,
				new EmploymentPreferences(
						RandomUtils.randomKeyWithProbabilities(JSONEmployeeDataLoader.shiftProbabilities),
						generateAcceptableWage(skills),
						generateDesiredWage(skills),
						RandomUtils.randomKeyWithProbabilities(JSONEmployeeDataLoader.typeOfContractProbabilities)),
				profession);

	}

	public static PossibleEmployee generatePossibleEmployee() {
		return generatePossibleEmployeeWithProfession(RandomUtils.randomKeyWithProbabilities(JSONEmployeeDataLoader.professionProbabilities));
	}

	private static BigDecimal generateDesiredWage(double skills) {
		return BigDecimal.valueOf((int) (JSONEmployeeDataLoader.minWage.doubleValue() * (1 + 0.5 * (skills + RandomUtils.randomDouble(0.3, 0.4)))) / 100 * 100);

	}

	private static BigDecimal generateAcceptableWage(double skills) {
		return BigDecimal.valueOf((int) (JSONEmployeeDataLoader.minWage.doubleValue() * (1 + 0.5 * (skills + RandomUtils.randomDouble(0.2)))) / 100 * 100);
	}

}
