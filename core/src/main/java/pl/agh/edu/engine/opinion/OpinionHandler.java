package pl.agh.edu.engine.opinion;

import java.util.ArrayList;
import java.util.List;

import pl.agh.edu.utils.RandomUtils;

public class OpinionHandler {
	private static final List<Opinion> opinions = new ArrayList<>();

	public static void addOpinionWithProbability(Opinion opinion, double probability) {
		if (RandomUtils.randomBooleanWithProbability(probability)) {
			opinions.add(opinion);
		}

	}
}
