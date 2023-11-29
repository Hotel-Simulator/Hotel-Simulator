package pl.agh.edu.engine.generator;

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.github.javafaker.Faker;

import pl.agh.edu.data.loader.JSONEmployeeDataLoader;
import pl.agh.edu.engine.employee.EmployeePreferences;
import pl.agh.edu.engine.employee.Profession;
import pl.agh.edu.engine.employee.contract.TypeOfContract;
import pl.agh.edu.engine.employee.possible.PossibleEmployee;
import pl.agh.edu.utils.Pair;
import pl.agh.edu.utils.RandomUtils;

public class PossibleEmployeeGenerator {
	private static final Faker faker = new Faker();
	private static final int maxNameLength = 14;

	private PossibleEmployeeGenerator() {}

	public static PossibleEmployee generatePossibleEmployeeWithProfession(Profession profession) {
		BigDecimal skills = BigDecimal.valueOf(RandomUtils.randomInt(1, 101)).divide(BigDecimal.valueOf(100), 2, RoundingMode.CEILING);
		Pair<String, String> name = createRandomName();
		return new PossibleEmployee.PossibleEmployeeBuilder()
				.firstName(name.first())
				.lastName(name.second())
				.age(RandomUtils.randomInt(18, 60))
				.skills(skills)
				.preferences(new EmployeePreferences.Builder()
						.desiredShift(RandomUtils.randomKeyWithProbabilities(JSONEmployeeDataLoader.shiftProbabilities))
						.acceptableWage(generateAcceptableWage(skills))
						.desiredWage(generateDesiredWage(skills))
						.desiredTypeOfContract(TypeOfContract.PERMANENT)
						.build())
				.profession(profession)
				.acceptancePointsThreshold(RandomUtils.randomInt(1, 5))
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

	private static Pair<String, String> createRandomName() {
		String firstName = faker.name().firstName();
		String lastName = faker.name().lastName();
		while (firstName.length() + lastName.length() > maxNameLength) {
			firstName = faker.name().firstName();
			lastName = faker.name().lastName();
		}
		return new Pair<>(firstName, lastName);
	}
}
