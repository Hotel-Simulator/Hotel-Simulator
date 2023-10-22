package pl.agh.edu.model.opinion.bucket;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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
		return satisfactions.stream().reduce(BigDecimal.ZERO, BigDecimal::add).doubleValue() / satisfactions.size();
	}
}
