package pl.agh.edu.engine.opinion.bucket;

import static java.math.BigDecimal.ZERO;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EmployeesSatisfactionOpinionBucket extends OpinionBucket {
	private final List<BigDecimal> satisfactions = new ArrayList<>();

	public EmployeesSatisfactionOpinionBucket(int weight) {
		super(weight);
	}

	public void addSatisfaction(BigDecimal satisfaction) {
		satisfactions.add(satisfaction);
	}

	@Override
	public double getValue() {
		if (satisfactions.isEmpty())
			return 0.;
		return satisfactions.stream().reduce(ZERO, BigDecimal::add).doubleValue() / satisfactions.size();
	}

	@Override
	public Optional<String> getComment() {
		double value = getValue();
		if (value > 0.8) {
			return Optional.of("opinionComment.employeesSatisfaction.good");
		}
		if (value < 0.3) {
			return Optional.of("opinionComment.employeesSatisfaction.bad");
		}
		return Optional.empty();
	}
}
