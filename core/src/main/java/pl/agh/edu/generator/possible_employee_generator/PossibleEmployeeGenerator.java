
package pl.agh.edu.generator.possible_employee_generator;

import java.math.BigDecimal;
import java.math.RoundingMode;

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
		BigDecimal skills = BigDecimal.valueOf(RandomUtils.randomInt(1, 101)).divide(BigDecimal.valueOf(100), 2, RoundingMode.CEILING);
		return new PossibleEmployee.Builder()
				.firstName(faker.name().firstName())
				.lastName(faker.name().lastName())
				.age(RandomUtils.randomInt(18, 60))
				.skills(skills)
				.preferences(new EmploymentPreferences.Builder()
						.desiredShift(RandomUtils.randomKeyWithProbabilities(JSONEmployeeDataLoader.shiftProbabilities))
						.acceptableWage(generateAcceptableWage(skills))
						.desiredWage(generateDesiredWage(skills))
						.desiredTypeOfContract(RandomUtils.randomKeyWithProbabilities(JSONEmployeeDataLoader.typeOfContractProbabilities))
						.build())
				.profession(profession)
				.build();

	}

	public static PossibleEmployee generatePossibleEmployee() {
		return generatePossibleEmployeeWithProfession(RandomUtils.randomKeyWithProbabilities(JSONEmployeeDataLoader.professionProbabilities));
	}

	private static BigDecimal generateDesiredWage(BigDecimal skills) {
		return BigDecimal.valueOf((int) (JSONEmployeeDataLoader.minWage.doubleValue() * (1 + 0.5 * (skills.doubleValue() + RandomUtils.randomDouble(0.3, 0.4)))) / 100 * 100);

	}

	private static BigDecimal generateAcceptableWage(BigDecimal skills) {
		return BigDecimal.valueOf((int) (JSONEmployeeDataLoader.minWage.doubleValue() * (1 + 0.5 * (skills.doubleValue() + RandomUtils.randomDouble(0.2)))) / 100 * 100);
	}

}
