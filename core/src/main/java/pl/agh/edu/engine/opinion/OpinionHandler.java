package pl.agh.edu.engine.opinion;

import java.util.ArrayList;
import java.util.List;

import pl.agh.edu.engine.time.Time;
import pl.agh.edu.utils.LanguageString;
import pl.agh.edu.utils.RandomUtils;

public class OpinionHandler {
	private static final List<OpinionData> opinions = new ArrayList<>();
	private static final Time time = Time.getInstance();

	public static void addOpinionWithProbability(Opinion opinion, double probability) {
		if (RandomUtils.randomBooleanWithProbability(probability)) {
			OpinionData opinionData = new OpinionData(time.getTime().toLocalDate(), opinion.getStars(), opinion.getComment().stream().map(LanguageString::new).toList());
			System.out.println(opinionData);
			opinions.add(opinionData);
		}

	}
}
